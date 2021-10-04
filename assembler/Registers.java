package assembler;
import java.util.Map;
import java.util.HashMap;
public class Registers {
	public static final Register RAX = new Register("rax", new byte[] {(byte) 0x48, (byte) 0xb8});
	public static final Register EAX = new Register("eax", new byte[] {(byte) 0xb8});
	public static final Register RCX = new Register("rcx", new byte[] {(byte) 0x48, (byte) 0xb9});
	public static final Register ECX = new Register("ecx", new byte[] {(byte) 0xb9});
	public static final Register RDX = new Register("rdx", new byte[] {(byte) 0x48, (byte) 0xba});
	public static final Register EDX = new Register("edx", new byte[] {(byte) 0xba});
	public static final Register RSI = new Register("rsi", new byte[] {(byte) 0x48, (byte) 0xbe});
	public static final Register ESI = new Register("esi", new byte[] {(byte) 0xbe});
	public static final Register RDI = new Register("rdi", new byte[] {(byte) 0x48, (byte) 0xbf});
	public static final Register EDI = new Register("edi", new byte[] {(byte) 0xbf});

	private static Map<String, Register> map;

	static {
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

	public static Register getRegister(String register) {
		return map.get(register);
	}

    public static boolean isRegister(String register) {
        return map.containsKey(register);
    }
}
