import java.io.*;
import java.util.*;
import java.util.stream.*;
public class Assembler {
	
	private Map<String, Integer> opcodes;
	private Map<String, Integer> directives;
	private Map<String, Integer> registers;
	private Map<String, Integer> symbols;
	// Instructions
	private final int MOV = 0;
	private final int ADD = 1;
	private final int SUB = 2;
	private final int MUL = 3;
	private final int IMUL = 4;
	private final int INC = 5;
	private final int DEC = 6;
	private final int CMP = 7;
	private final int JMP = 8;
	private final int JE = 9;
	private final int JNE = 10;
	// Directives
	private final int GLOBAL = 100;
	private final int SECTION = 101;
	private final int SECTION_TEXT = 102;
	private final int SECTION_DATA = 103;
	private final int SECTION_BSS = 104;
	private final int LABEL = 105;
	private final int DB = 200;
	private final int DW = 201;
	private final int DD = 202;
	private final int DQ = 203;
	private final int RESB = 204;
	private final int RESW = 205;
	private final int RESD = 206;
	private final int RESQ = 207;
	// Registers
	private final int RAX = 0x48b8;
	private final int EAX = 0xb8;
	private final int RDX = 0x48ba;
	private final int EDX = 0xba;
	private final int RDI = 0x48bf;
	private final int EDI = 0xbf;
	private final int RSI = 0x48be;
	private final int ESI = 0xbe;

	public Assembler() {
		initMaps();
	}

	public static String parseArg(String arg) {
		if (arg.startsWith("~"))
			return System.getProperty("user.home") + arg.substring(1);
		return arg;
	}

	private void initMaps() {
		opcodes = new HashMap<>();
		directives = new HashMap<>();
		registers = new HashMap<>();
		symbols = new HashMap<>();
		opcodes.put("mov", MOV);
		opcodes.put("add", ADD);
		opcodes.put("sub", SUB);
		opcodes.put("mul", MUL);
		opcodes.put("imul", IMUL);
		opcodes.put("inc", INC);
		opcodes.put("dec", DEC);
		opcodes.put("cmp", CMP);
		opcodes.put("jmp", JMP);
		opcodes.put("je", JE);
		opcodes.put("jne", JNE);
		directives.put("global", GLOBAL);
		directives.put("section", SECTION);
		directives.put(".text", SECTION_TEXT);
		directives.put(".data", SECTION_DATA);
		directives.put(".bss", SECTION_BSS);
		registers.put("rax", RAX);
		registers.put("rax,", RAX);
		registers.put("eax", EAX);
		registers.put("eax,", EAX);
		registers.put("rdx", RDX);
		registers.put("rdx,", RDX);
		registers.put("edx", EDX);
		registers.put("edx,", EDX);
		registers.put("rdi", RDI);
		registers.put("rdi,", RDI);
		registers.put("edi", EDI);
		registers.put("edi,", EDI);
		registers.put("rsi", RSI);
		registers.put("rsi,", RSI);
		registers.put("esi", ESI);
		registers.put("esi,", ESI);
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

	public boolean isKeyword(String token) {
		return opcodes.containsKey(token) || directives.containsKey(token);
	}

	public String hexString(String s) {
		String hexString = "";
		byte[] bytes = s.getBytes();
		for (int i = 0; i < bytes.length; i++) {
			hexString += Integer.toHexString((int) bytes[i]);
			if (i < bytes.length - 1)
				hexString += " ";
		}
		return hexString;
	}

	public void printTokens(File file) {
		String code = toString(file);
		String[] tokens = code.split("\\s");
		for (int i = 0; i < tokens.length; i++) {
			String token = tokens[i].trim();
			if (token.length() > 0) {
				String s = String.format("Token: %s\t%sBytes:%s", token, isKeyword(token)?"\tKeyword":"", hexString(token));
				System.out.println(s);
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