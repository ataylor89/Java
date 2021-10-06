package assembler;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
public class Symbols {
    private static Map<String, Symbol> map = new HashMap<>();
    private static List<Symbol> list = new ArrayList<>();

    public static Map<String, Symbol> getMap() {
        return map;
    }

    public static List<Symbol> getList() {
        return list;
    }
}
