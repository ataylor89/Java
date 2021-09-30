public class Opcodes {
	public static final int MOV = 0;
	public static final int AND = 1;
	public static final int OR = 2;
	public static final int XOR = 3;
	public static final int ADD = 4;
	public static final int SUB = 5;
	public static final int MUL = 6;
	public static final int IMUL = 7;
	public static final int INC = 8;
	public static final int DEC = 9;
	public static final int CMP = 10;
	public static final int JMP = 11;
	public static final int JE = 12;
	public static final int JNE = 13;
	public static final int JGE = 14;
	public static final int JLE = 15;

	public static int getOpcode(String mnemonic) {
		if (mnemonic.equals("mov"))
			return MOV;
		if (mnemonic.equals("and"))
			return AND;
		if (mnemonic.equals("or"))
			return OR;
		if (mnemonic.equals("xor"))
			return XOR;
		else if (mnemonic.equals("add"))
			return ADD;
		else if (mnemonic.equals("sub"))
			return SUB;
		else if (mnemonic.equals("mul"))
			return MUL;
		else if (mnemonic.equals("imul"))
			return IMUL;
		else if (mnemonic.equals("inc"))
			return INC;
		else if (mnemonic.equals("dec"))
			return DEC;
		else if (mnemonic.equals("cmp"))
			return CMP;
		else if (mnemonic.equals("jmp"))
			return JMP;
		else if (mnemonic.equals("je"))
			return JE;
		else if (mnemonic.equals("jne"))
			return JNE;
		else if (mnemonic.equals("jge"))
			return JGE;
		else if (mnemonic.equals("jle"))
			return JLE;
	}
}