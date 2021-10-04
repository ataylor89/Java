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
		Instruction instruction = new Instruction();
		Opcode opcode = null;
		String[] tokens = text.trim().split("\\s");
		Operand[] operands = new Operand[tokens.length-1];
           // IntStream.range(0, tokens.length).mapToObj(i -> String.format("%d: %s", i, tokens[i])).forEach(System.out::println);
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
		for (int i = 0; i < tokens.length; i++) {
			String token = tokens[i];
			if (token.equals("global"))
				assemblyFile.addGlobal(tokens[i+1]);
			else if (token.equals("extern"))
				assemblyFile.addExtern(tokens[i+1]);
		}
		int instructionStart = textSection.indexOf("\n", textIndex) + 1;
		int instructionEnd = textSection.indexOf("\n", instructionStart);
		while (instructionStart > 0 && instructionEnd > 0) {
			String text = textSection.substring(instructionStart, instructionEnd);
            	// System.out.printf("Instruction: %s\n", text);
			Instruction instruction = parseInstruction(text);
			assemblyFile.addInstruction(instruction);
			instructionStart = instructionEnd + 1;
			instructionEnd = textSection.indexOf("\n", instructionStart);
		}
		return assemblyFile;
		
	}

	public ObjectFile assemble(File sourceFile) {
		ObjectFile objectFile = new ObjectFile();
		AssemblyFile assemblyFile = parse(sourceFile);
		List<Instruction> instructions = assemblyFile.getInstructions();
		for (Instruction instruction : instructions) {
			// System.out.println(instruction);
			Opcode opcode = instruction.getOpcode();
			Operand[] operands = instruction.getOperands();
			switch (opcode) {
				case MOV:	
					Operand dest = operands[0];
					Operand src = operands[1];
                    System.out.println("Dest operand: " + dest);
                    System.out.println("Src operand: " + src);
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
