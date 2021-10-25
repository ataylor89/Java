package assembler;
public enum Opcode {
    MOV ("mov"), 
    AND ("and"), 
    OR ("or"), 
    XOR ("xor"), 
    ADD("add"), 
    SUB("sub"), 
    MUL("mul"), 
    IMUL("imul"),
    INC ("inc"),
    DEC ("dec"),
    CMP ("cmp"),
    JMP ("jmp"),
    JE ("je"),
    JNE ("jne"),
    JGE ("jge"),
    JLE ("jle"),
    SYSCALL ("syscall");

    private String name;

    Opcode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    } 
}
