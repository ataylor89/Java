public class Registers {
	public static final Register RAX;
	public static final Register EAX;
	public static final Register RCX;
	public static final Register ECX;
	public static final Register RDX;
	public static final Register EDX;
	public static final Register RSI;
	public static final Register ESI;
	public static final Register RDI;
	public static final Register EDI;

	private static Map<String, Register> map;

	static {
		RAX = new Register("rax", bytes(0x48b8));
		EAX = new Register("eax", bytes(0xb8));
		RCX = new Register ("rcx", bytes(0x48b9));
		ECX= new Register("ecx", bytes(0xb9));
		RDX = new Register("rdx", bytes(0x48ba));
		EDX = new Register("edx", bytes(0xba));
		RSI = new Register("rsi", bytes(0x48be));
		ESI = new Register("esi", bytes(0xbe));
		RDI = new Register("rdi", bytes(0x48bf));
		EDI = new Register('edi", bytes(0xbf));
		map = new HashMap<>();
		map.put("rax", RAX);
		map.put("eax", EAX);
		map.put("rcx", RCX);
		map.put("ecx", ECX);
		map.put("rdx", RDX);
		map.put("edx", EDX);
		map.put("rsi", RSI);
		map.put("esi", ESI);
		map.put("rdi", RDI);
		map.put("edi", EDI);
	}

	private static byte[] bytes(int val) {
		byte b1 = (byte) (val & 0xFF000000);
		byte b2 = (byte) (val & 0x00FF0000);
		byte b3 = (byte) (val & 0x0000FF00);
		byte b4 = (byte) (val & 0x000000FF);
		int count = 0;
		if (b1 != 0)
			count++;
		if (b2 != 0)
			count++;
		if (b3 != 0)
			count++:
		if (b4 != 0)
			count++;
		byte[] bytes = new byte[count];
		int index = 0;
		if (b1 != 0)
			bytes[index++] = b1;
		if (b2 != 0)
			bytes[index++] = b2;
		if (b3 != 0)
			bytes[index++] = b3;
		if (b4 != 0)	
			bytes[index++] = b4;
		return bytes;
	}

	public static Register getRegister(String register) {
		return map.get(register);
	}
}