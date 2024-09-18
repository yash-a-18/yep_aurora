import type { Model } from '../language/generated/ast.js';
import chalk from 'chalk';
import { Command } from 'commander';
import { MiniLogoLanguageMetaData } from '../language/generated/module.js';
import { createMiniLogoServices } from '../language/mini-logo-module.js';
import { extractAstNode } from './cli-util.js';
import { generateCommands, generateJSON } from './generator.js';
import { NodeFileSystem } from 'langium/node';
import * as url from 'node:url';
import * as fs from 'node:fs/promises';
import * as path from 'node:path';
const __dirname = url.fileURLToPath(new URL('.', import.meta.url));

const packagePath = path.resolve(__dirname, '..', '..', 'package.json');
const packageContent = await fs.readFile(packagePath, 'utf-8');

export const cliGenerateAction = async (fileName: string, opts: GenerateOptions): Promise<void> => {
    const services = createMiniLogoServices(NodeFileSystem).miniLogoService;
    const model = await extractAstNode<Model>(fileName, services);
    const generatedResult = generateJSON(model);
    console.log(chalk.green(`Generated successfully: ${generatedResult}`));
};

/**
 * Generate MiniLogo drawing commands
 * 
 * @param fileName Name of MiniLogo program commands from
 */
export const cliGenerateCmds = async (fileName: string): Promise<void> => {
    const services = createMiniLogoServices(NodeFileSystem).miniLogoService;
    console.log(generateCommands(await extractAstNode<Model>(fileName, services)));
};

export type GenerateOptions = {
    destination?: string;
}

export default function(): void {
    const program = new Command();

    program.version(JSON.parse(packageContent).version);

    const fileExtensions = MiniLogoLanguageMetaData.fileExtensions.join(', ');
    program
        .command('generateJSON')
        .argument('<file>', `source file (possible file extensions: ${fileExtensions})`)
        .option('-d, --destination <dir>', 'destination directory of generating')
        .description('generates JSON that prints')
        .action(cliGenerateAction);

    program
        .command('generateCmds')
        .argument('<file>', `source file (possible file extensions: ${fileExtensions})`)
        .option('-d, --destination <dir>', 'destination directory of generating')
        .description('generates the sequence of commands from the source file')
        .action(cliGenerateCmds);

    program.parse(process.argv);
}
