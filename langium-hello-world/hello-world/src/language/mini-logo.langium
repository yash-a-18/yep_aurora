grammar MiniLogo//Keyword for representing the name of the grammar

entry Model: (stmts+=Stmt | defs+=Def)*; //array of statements or array of definitions, "*" means zero or more, += means in any sequence

Stmt: Cmd | Macro; // Standalone Rule
Cmd: Pen | Move | Color | For;
//Cross-reference to definition, syntax with square brackets, ? means optional
// giving array of arguments with list separated by comma, basically the list of array of arguments
//Expr means Expression
Macro: def=[Def:ID] '(' (args+=Expr (',' args+=Expr)*)? ')';

//name is the name of the definition
//Block is referred as Fragment
Def: 'def' name=ID '(' (params+=Param (',' params+=Param)*)? ')' Block;

//Fragment is the special kind of instance that can be added in the body of the rule
fragment Block: '{' body+=Stmt '}';

Param: name=ID;

//starts with pen keyword has a single argument mode
Pen: 'pen' '(' mode=('up' | 'down') ')';
Move: 'move' '(' ex=Expr ',' ey=Expr ')';
//Syntax is: for x = 0 to 10 {...}
//notice we have used a var here, defining a variable, can also be used in the definition body
For: 'for' var=Param '=' e1=Expr 'to' e2=Expr Block;
Color: 'color' '(' ((r=Expr ',' b=Expr ',' g=Expr) | (color=ID) | (color=HEX)) ')';

//Chevrotain Parser
//Top-down parser
//Has problem with recursion
//That's why we cannot do Expr: Expr + Expr;
Expr: Add;
//`infers` means this parser rule will produce an object of type expression
// It will not produce an object of type Add
// AST will infer the name because it is an exception, just like the exceptions we have in organic chemistry :)
Add infers Expr:
    Mult ({infer BinExpr.e1=current} op=('+'|'-') e2=Mult)*;
//^^^ the bit in the curly braces is an action
//`infer` means in the case we parse the multiplication the parser rule succeeds
// if we have zero or more of +,- followed by multiplication
// then we have a binary expression, then we are going to change the body
// for this case, we change the object to the binary expression and 
// we are going to add the property e1 to this binary expression object and set to what we have currently parsed to be the Mult
Mult infers Expr:
    PrimExpr ({infer BinExpr.e1=current} op=('*'|'/') e2=PrimExpr)*;
//PrimExpr: Primary Expression
PrimExpr:Lit | Ref | Group | NegExpr;

//Literal int
Lit: val=INT;
//cross-reference
Ref: val=[Param:ID];
//grouped expression with parenthesis
Group: '(' ge=Expr ')';
//negated Expression
NegExpr: '-' ne=Expr;

// return type are correspond to the types present in the Typescript
//The sequence in the terminal matters, it is first come first serve
//order represents the precedence
// Something to do with the '#' sign as well
terminal HEX returns string: /#(\d|[a-fA-F])+/;
terminal ID returns string: /[_a-zA-Z][\w_]*/;
terminal INT returns number: /-?[0-9]+/;

// Need some hidden terminals as we want our language to be white-space insensitive
// 1+1 is same as 1 +       1
hidden terminal WS: /\s+/;
//multi line comment and nesting not allowed
hidden terminal ML_COMMENT: /\/\*[\s\S]*?\*\//;
//C style comments
hidden terminal SL_COMMENT: /\/\/[^\n\r]*/;