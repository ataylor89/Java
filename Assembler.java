import java.io.*;
import java.util.*;
import java.util.stream.*;
public class Assembler {

	public Assembler() {}

	public static String parseArg(String arg) {
		if (arg.startsWith("~"))
			return System.getProperty("user.home") + arg.substring(1);
		return arg;
	}

	private String toString(File file) {
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			return reader.lines().collect(Collectors.joining("\n"));
		} catch (IOException e) {
			System.err.println(e);
		}
		return null;
	}

	public AssemblyFile parse(File file) {
		AssemblyFile assemblyFile = new AssemblyFile();
		assemblyFile.setFile(file);
		String code = toString(file);
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
			String instruction = textSection.substring(instructionStart, instructionEnd);
			assemblyFile.addInstruction(instruction);
			instructionStart = instructionEnd + 1;
			instructionEnd = textSection.indexOf("\n", instructionStart);
		}
		return assemblyFile;
		
	}

	public ObjectFile assemble(File sourceFile, File outputFile) {
		AssemblyFile assemblyFile = parse(sourceFile);
		List<String> instructions = assemblyFile.getInstructions();
		for (String instruction : instructions) {
			String[] tokens = instruction.split("\\s");
			String mnemonic = tokens[0];
			int opcode = Opcodes.getOpcode(mnemonic);
			switch (opcode) {
				case Opcodes.MOV:
				case Opcodes.AND:
				case Opcodes.OR:
				case Opcodes.XOR:
				case Opcodes.ADD:
				case Opcodes.SUB:
				case Opcodes.MUL:
				case Opcodes.IMUL:
				case Opcodes.INC:
				case Opcodes.DEC:
				case Opcodes.CMP:
				case Opcodes.JMP:
				case Opcodes.JE:
				case Opcodes.JGE:
				case Opcodes.JLE:
			}		
		}
	}

	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println("Usage: java Assembler <assemblyfile> <objectfile>");
			return;
		}
		String sourcePath = parseArg(args[0]);
		File sourceFile = new File(sourcePath);
		Assembler assembler = new Assembler();
		AssemblyFile assemblyFile = assembler.parse(sourceFile);
		System.out.println(assemblyFile);
	}
}