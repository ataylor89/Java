package assembler5;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
public class ObjectFile {
    private ByteArray codeSection;
    private ByteArray dataSection;
    private Map<String, Object> symbols;

    public ObjectFile() {
        codeSection = new ByteArray();
        dataSection = new ByteArray();
        symbols = new HashMap<>();
    }

    public void setCodeSection(ByteArray codeSection) {
        this.codeSection = codeSection;
    }

    public ByteArray getCodeSection() {
        return codeSection;
    }

    public void setDataSection(ByteArray dataSection) {
        this.dataSection = dataSection;
    }

    public ByteArray getDataSection() {
        return dataSection;
    }

    public void setSymbols(Map<String, Object> symbols) {
        this.symbols = symbols;
    }

    public Map<String, Object> getSymbols() {
        return symbols;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Object file\n");
        sb.append(String.format("Code section (%02x bytes)\n", codeSection.getBytes().length));
        sb.append(Bytes.hexstring(codeSection.getBytes()));
        sb.append("\n");
        sb.append(String.format("Data section (%02x bytes)\n", dataSection.getBytes().length));
        sb.append(Bytes.hexstring(dataSection.getBytes()));
        sb.append("\n");
        sb.append("Symbol table\n");
        Iterator<String> it = symbols.keySet().iterator();
        while (it.hasNext()) {
            String symbol = it.next();
            sb.append(String.format("Symbol name: %s value: %s", symbol, symbols.get(symbol)));
            if (it.hasNext())
                sb.append("\n");
        }
        return sb.toString();
    }
}
