package assembler2;
public enum Opcode {

    private static Map<String, Opcode> opcodes;
    
    static {
        opcodes = new HashMap<>();
        opcodes.put("mov", Opcode.MOV);
        opcodes.put("and", Opcode.AND);
        opcodes.put("or", Opcode.OR);
        opcodes.put("xor", Opcode.XOR);
        opcodes.put("syscall", Opcode.SYSCALL);
    }

    MOV, AND, OR, XOR, SYSCALL;

    public static Map<String, Opcode> map() {
        return opcodes;
    }
}
