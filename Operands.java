public class Operands {
	public static final int REGISTER = 0;
	public static final int EFFECTIVE_ADDRESS = 1;
	public static final int IMMEDIATE_VALUE = 2;

	static {
		registers = new HashMap<>();
		registers.put("rax", 0x48b8);
		registers.put("eax", 0xb8);
		registers.put("rcx", 0x48b9);
		registers.put("ecx", 0xb9);
		registers.put("rdx", 0x48ba);
		registers.put('edx", 0xba);
		registers.put("rsi", 0x48be);
		registers.put("esi", 0xbe);
		registers.put("rdi", 0x48bf);
		registers.put("edi", 0xbf);
	}

	public static int getOperandType(String operand) {
		if (registers.containsKey(operand))
			return REGISTER;
		if (operand.charAt(0) == '[' && operand.charAt(operand.length() - 1) == ']')
			return EFFECTIVE_ADDRESS;
		try {
			Integer val = new Integer(operand);
			return IMMEDIATE_VALUE;
		} catch (NumberFormatException e) {
			System.err.println(e);
		}
		return -1;
	}

	public static int getRegister(String operand) {
		if (registers.containsKey(operand))
			return registers.get(operand);
		return -1;
	}
	
	public static int getImmediateValue(String operand) {
		try {
			return new Integer(operand);
		} catch (NumberFormatException e) {
			System.err.println(e);
		}
		return -1;
	}
}