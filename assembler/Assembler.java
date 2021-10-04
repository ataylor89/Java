package assembler;
import java.io.*;
import java.util.*;
import java.util.stream.*;
public class Assembler {

	public Assembler() {}

	private static String parseArg(String arg) {
		if (arg.startsWith("~"))
			return System.getProperty("user.home") + arg.substring(1);
		return arg;
	}


	public String getCode(File file) {
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			return reader.lines().collect(Collectors.joining("\n"));
		} catch (IOException e) {
			System.err.println(e);
		}
		return null;
	}

	public Instruction parseInstruction(String text) {
		String[] tokens = text.trim().split("\\s");
		Instruction instruction = new Instruction();
        Opcode opcode = null;
        Operand[] operands = new Operand[tokens.length-1];
		if (tokens.length >= 1) 
			opcode = Opcode.valueOf(tokens[0].trim().toUpperCase());
		if (tokens.length >= 2) 
			operands[0] = Operands.getOperand(tokens[1].trim());
		if (tokens.length >= 3) 
			operands[1] = Operands.getOperand(tokens[2].trim());
		instruction.setText(text);
		instruction.setOpcode(opcode);
		instruction.setOperands(operands);
		return instruction;
	}

    public List<String> parseGlobals(String code) {
        List<String> globals = new ArrayList<>();
        int directiveStart = code.indexOf("global");
        int directiveEnd = code.indexOf("\n", directiveStart);
        while (directiveStart > 0 && directiveEnd > 0) {
            String directive = code.substring(directiveStart, directiveEnd);
            String[] tokens = directive.split(" ");
            String global = tokens[1].trim();
            globals.add(global);
            directiveStart = code.indexOf("global", directiveEnd);
            directiveEnd = code.indexOf("\n", directiveStart);
        }
        return globals;
    }

    public List<String> parseExterns(String code) {
        List<String> externs = new ArrayList<>();
        int directiveStart = code.indexOf("extern");
        int directiveEnd = code.indexOf("\n", directiveStart);
        while (directiveStart > 0 && directiveEnd > 0) {
            String directive = code.substring(directiveStart, directiveEnd);
            String[] tokens = directive.split(" ");
            String extern = tokens[1].trim();
            externs.add(extern);
            directiveStart = code.indexOf("extern", directiveEnd);
            directiveEnd = code.indexOf("\n", directiveStart);
        }
        return externs;
    }

    public List<Instruction> parseInstructions(String code) {
		int textIndex = code.indexOf("section .text");
        int dataIndex = code.indexOf("section .data");
        String textSection = code.substring(textIndex, dataIndex-1);
        List<Instruction> instructions = new ArrayList<>();
        int instructionStart = textSection.indexOf("\n", textIndex) + 1;
		int instructionEnd = textSection.indexOf("\n", instructionStart);
		while (instructionStart > 0 && instructionEnd > 0) {
			String line = textSection.substring(instructionStart, instructionEnd);
			Instruction instruction = parseInstruction(line);
			instructions.add(instruction);
			instructionStart = instructionEnd + 1;
			instructionEnd = textSection.indexOf("\n", instructionStart);
		}
        return instructions;
    }

	public AssemblyFile parse(File file) {
		AssemblyFile assemblyFile = new AssemblyFile();
		assemblyFile.setFile(file);
		String code = getCode(file);
		assemblyFile.setCode(code);
		int textIndex = code.indexOf("section .text");
        int dataIndex = code.indexOf("section .data");
        String textSection = code.substring(textIndex, dataIndex-1);
		String dataSection = code.substring(dataIndex);
		assemblyFile.setTextSection(textSection);
		assemblyFile.setDataSection(dataSection);
		String[] tokens = code.split("\\s");
		assemblyFile.setTokens(tokens);
		List<String> globals = parseGlobals(code);
        assemblyFile.setGlobals(globals);
        List<String> externs = parseExterns(code);
        assemblyFile.setExterns(externs);
		List<Instruction> instructions = parseInstructions(code);
        assemblyFile.setInstructions(instructions);
        return assemblyFile;
		
	}

	public ObjectFile assemble(File sourceFile) {
		ObjectFile objectFile = new ObjectFile();
		AssemblyFile assemblyFile = parse(sourceFile);
		List<Instruction> instructions = assemblyFile.getInstructions();
		for (Instruction instruction : instructions) {
			Opcode opcode = instruction.getOpcode();
			Operand[] operands = instruction.getOperands();
			switch (opcode) {
				case MOV:	
					Operand dest = operands[0];
					Operand src = operands[1];
                    if (dest != null && src != null) {
						objectFile.addBytes(dest.getBytes());
						objectFile.addBytes(src.getBytes());
					}	
					break;
				case AND:
				case OR:
				case XOR:
				case ADD:
				case SUB:
				case MUL:
				case IMUL:
				case INC:
				case DEC:
				case CMP:
				case JMP:
				case JE:
				case JGE:
				case JLE:
		   }		
		}
        return objectFile;
	}

	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println("Usage: java Assembler <assemblyfile> <objectfile>");
			return;
		}
		String sourcePath = parseArg(args[0]);
		String outPath = parseArg(args[1]);
		File sourceFile = new File(sourcePath);
		File outFile = new File(outPath);
		Assembler assembler = new Assembler();
		AssemblyFile assemblyFile = assembler.parse(sourceFile);
		System.out.println(assemblyFile);
		ObjectFile objectFile = assembler.assemble(sourceFile);
		System.out.println(objectFile);
	}
}
