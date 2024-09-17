import type { Model } from '../language/generated/ast.js';

export function generate(model: Model): string {
    const length = model.stmts;
    return length.toString()
}