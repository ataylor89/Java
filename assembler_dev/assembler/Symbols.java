package assembler;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
public class Symbols {
    private static Map<String, Symbol> map = new HashMap<>();
    private static List<Symbol> list = new ArrayList<>();

    public static Map<String, Symbol> getMap() {
        return map;
    }

    public static List<Symbol> getList() {
        return list;
    }
     
    public static String view() {
        StringBuilder sb = new StringBuilder();
        sb.append("Symbols table");
        Iterator<String> it = map.keySet().iterator();
        while (it.hasNext()) {
            String name = it.next();
            sb.append("Symbol: " + name);
            sb.append("\n");
            sb.append(map.get(name));
            if (it.hasNext())
                sb.append("\n");
        }
        return sb.toString();
    }
}
