package assembler3;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
public class SymbolTable {
    private Map<String, Symbol> map;
    private List<Symbol> list;

    public SymbolTable() {
        map = new HashMap<>();
        list = new ArrayList<>();
    }

    public void setMap(Map<String, Symbol> map) {
        this.map = map;
    }

    public void setList(List<Symbol> list) {
        this.list = list;
    }

    public Map<String, Symbol> getMap() {
        return map;
    }

    public List<Symbol> getList() {
        return list;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Symbol table\n");
        for (int i = 0; i < list.size(); i++) 
            sb.append(list.get(i));
        return sb.toString();
    }
}
