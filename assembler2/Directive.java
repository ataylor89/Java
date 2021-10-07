package assembler2;
import java.util.Map;
import java.util.HashMap;
public enum Directive {
    private static Map<String, Directive> map;

    static {
        directives = new HashMap<>();
        directives.put("db", Directive.DB);
        directives.put("dw", Directive.DW);
        directives.put("dd", Directive.DD);
        directives.put("dq", Directive.DQ);
        directives.put("resb", Directive.RESB);
        directives.put("resw", Directive.RESW);
        directives.put("resd", Directive.RESD);
        directives.put("resq", Directive.RESQ);
        directives.put("equ", Directive.EQU);
    }

    DB, DW, DD, DQ, RESB, RESW, RESD, RESQ, EQU;

    public static Map<String, Directive> map() {
        return map;
    }
}
