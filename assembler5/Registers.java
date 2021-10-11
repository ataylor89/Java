package assembler5;
import java.util.Map;
import java.util.HashMap;
public class Registers {
    private static final Map<String, Register> registers;

    static {
        registers = new HashMap<>();
        registers.put("rax", Register.RAX);
        registers.put("rdi", Register.RDI);
        registers.put("rsi", Register.RSI);
        registers.put("rdx", Register.RDX);
    }

    public static Map<String, Register> map() {
        return registers;
    }
}
