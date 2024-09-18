import type { ValidationAcceptor, ValidationChecks } from 'langium';
import type { Def, MiniLogoAstType, Model } from './generated/ast.js';
import type { MiniLogoServices } from './mini-logo-module.js';

/**
 * Register custom validation checks.
 */

//Below method allows us to register, validate and checks on types in AST
export function registerValidationChecks(services: MiniLogoServices) {
    const registry = services.validation.ValidationRegistry;
    const validator = services.validation.MiniLogoValidator;
    const checks: ValidationChecks<MiniLogoAstType> = {
        Model: validator.checkModel,
        Def: validator.checkDef
    };
    registry.register(checks, validator);
}

/**
 * Implementation of custom validations.
 */

//The actual checks are typically implemented in this class (can keep any class name ofc)
export class MiniLogoValidator {
    /**
     * Check Model for duplicate definitions
     * 
     * @param model 
     * @param accept 
     */
    checkModel(model: Model, accept: ValidationAcceptor): void {
        //check for duplicate definition names
        const defs = model.defs;
        const previousNames = new Set<string>();
        for (const def of defs){
            if(previousNames.has(def.name.toLowerCase())){
                accept('error', 'Definition cannot redefine an existing definition name', { node: def, property: 'name' });
            }
            else{
                previousNames.add(def.name.toLowerCase());
            }
        }
    }

    /**
     * Checks definition for duplicate parameters
     * 
     * @param def 
     * @param accept 
     */
    checkDef(def: Def, accept: ValidationAcceptor): void {
        const params = def.params;
        const previousNames = new Set<string>();
        for (const param of params){
            if(previousNames.has(param.name.toLowerCase())){
                accept('error', `Duplicate Parameter name not allowed: '${param.name}'`, { node: param, property: 'name' });
            }
            else{
                previousNames.add(param.name.toLowerCase());
            }
        }
    }

}
