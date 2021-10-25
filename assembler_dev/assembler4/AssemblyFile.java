package assembler4;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Stream;
import java.util.stream.Collectors;
public class AssemblyFile {
    private String code;
    private List<String> globals;
    private List<String> externs;
    private TextSection textSection;
    private DataSection dataSection;
    private BssSection bssSection;

    public AssemblyFile() {}

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setGlobals(List<String> globals) {
        this.globals = globals;
    }

    public List<String> getGlobals() {
        return globals;
    }

    public void setExterns(List<String> externs) {
        this.externs = externs;
    }

    public List<String> getExterns() {
        return externs;
    }

    public void setTextSection(TextSection textSection) {
        this.textSection = textSection;
    }

    public TextSection getTextSection() {
        return textSection;
    }

    public void setDataSection(DataSection dataSection) {
        this.dataSection = dataSection;
    }

    public DataSection getDataSection() {
        return dataSection;
    }

    public void setBssSection(BssSection bssSection) {
        this.bssSection = bssSection;
    }

    public BssSection getBssSection() {
        return bssSection;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Assembly file\n");
        sb.append("Globals: " + globals.stream().collect(Collectors.joining(" ")));
        sb.append("\n");
        sb.append("Externs: " + externs.stream().collect(Collectors.joining(" ")));
        sb.append("\n");
        sb.append("Text Section\n");
        sb.append(textSection);
        sb.append("\n");
        sb.append("Data Section\n");
        sb.append(dataSection);
        sb.append("\n");
        sb.append("Bss Section\n");
        sb.append(bssSection);
        return sb.toString();
    }
}
