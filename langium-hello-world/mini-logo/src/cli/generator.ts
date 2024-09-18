import { NodeFileSystem } from 'langium/node';
import { Def, Expr, isBinExpr, isColor, isFor, isGroup, isLit, isMacro, isMove, isNegExpr, isPen, isRef, type Model, type Stmt } from '../language/generated/ast.js';
import { createMiniLogoServices } from '../language/mini-logo-module.js';

export function generateJSON(model: Model): string {
    //create and get access to mini logo services
    const services = createMiniLogoServices(NodeFileSystem).miniLogoService;

    //invoke the serializer
    const json = services.serializer.JsonSerializer.serialize(model);

    return json;
    // console.dir(model);
    // const stmtsLength: number = model.stmts.length;
    // return stmtsLength.toString();
}

type GenerateCommand = Pen | Move | Color;

type Pen = {
    cmd: 'penUp' | 'penDown'
};

type Move = {
    cmd: 'move'
    x: number
    y: number
};

type Color = {cmd: 'color'} & (ColorString | ColorRGB);

type ColorString = {
    color: string
};

type ColorRGB = {
    r: number
    g: number
    b: number
};

export function generateCommands(model: Model): GenerateCommand[] {
    /**
    [
        { cmd: 'penDown' },
        { cmd: 'move', x: 10, y: 10},
        { cmd: 'penUp' }
    ]
    */
   //minilogo evaluation env
   const env: MiniLogoGenEnv = new Map<string, number>();
   const result: GenerateCommand[] = generateStatements(model.stmts, env);
   return result;
}

//map of names to values
type MiniLogoGenEnv = Map<string, number>;

function generateStatements(stmts: Stmt[], env: MiniLogoGenEnv): GenerateCommand[] {
    // commands generated so for
    const generatedCmds: GenerateCommand[] = [];
    for (const stmt of stmts){
        if(isPen(stmt)){
            generatedCmds.push({
                cmd: stmt.mode === 'up' ? 'penUp' : 'penDown'
            });
        }else if(isMove(stmt)){
            generatedCmds.push({
                cmd: 'move',
                x: evalExprWithEnv(stmt.ex, env),
                y: evalExprWithEnv(stmt.ey, env)
            });
        }else if(isMacro(stmt)){
            // get the cross ref
            const macro: Def = stmt.def.ref as Def;

            //copied env
            let macroEnv = new Map(env);

            //produce pairs of string & exprs, using a tmp env
            // this is important to avoid mixing of params that are only present in the tmp env with our actual env
            let tmpEnv = new Map<string, number>();

            //evaluate args independently, staying out of the environment
            macro.params.map((elm, idx: number) => tmpEnv.set(elm.name, evalExprWithEnv(stmt.args[idx], macroEnv)));
            // add new params into our copied env
            tmpEnv.forEach((v,k) => macroEnv.set(k,v));

            //evaluate all statements under this macro
            generatedCmds.push(... generateStatements(macro.body, env));
        }else if(isFor(stmt)){
            //compute for loop bounds
            // start
            let vi = evalExprWithEnv(stmt.e1, env);
            //end
            let ve = evalExprWithEnv(stmt.e2, env);

            let results: GenerateCommand[] = [];

            //perform loop
            const loopEnv = new Map(env);
            while(vi < ve){
                loopEnv.set(stmt.var.name, vi++);
                results = results.concat(... generateStatements(stmt.body, new Map(loopEnv)));
            }
            generatedCmds.push(...results);
        }else if(isColor(stmt)){
            if(stmt.color){
                //literal color text or hex
                generatedCmds.push({cmd:'color', color:stmt.color});
            }
            else{
                //color as rgb
                const r = evalExprWithEnv(stmt.r!, env);
                const g = evalExprWithEnv(stmt.g!, env);
                const b = evalExprWithEnv(stmt.b!, env);
                generatedCmds.push({cmd:'color', r, g, b});
            }
        }else{
            throw new Error("Missing handling for statement");
        }
    }
    return generatedCmds;
}

/**
 * Evaluates expr in the context of an env
 * 
 * @param e Expr to evaluate
 * @param env Env to perform evaluation within
 * @returns Resulting value from the expr
 */
function evalExprWithEnv(e: Expr, env: MiniLogoGenEnv): number{
    if(isLit(e)){
        //literal value
        return e.val
    }else if(isRef(e)){
        // reference, resolve it from the env
        const v = env.get(e.val.ref?.name ?? '');
        if(v!=undefined){
            return v;
        } else{
            throw new Error('Unable to resolve reference to undefined symbol: ' + e.val.$refText);
        }
    }else if(isBinExpr(e)){
        // evaluate the binary expression, based on the operator
        let opval = e.op;
        let v1 = evalExprWithEnv(e.e1, env);
        let v2 = evalExprWithEnv(e.e2, env);

        switch(opval){
            case '+': return v1 + v2;
            case '-': return v1 - v2;
            case '*': return v1 * v2;
            case '/': return v1 / v2;
            default: throw new Error(`Unrecognized binary operation passed: ${opval}`);
        }

    }else if(isNegExpr(e)){
        // negate the result of the expression
        return -1 * evalExprWithEnv(e.ne, env);
    }else if(isGroup(e)){
        //grouped expression has its contents evaluated
        return evalExprWithEnv(e.ge, env);
    }else{
        //sanity check
        throw new Error('Unrecognized expression!');
    }
}