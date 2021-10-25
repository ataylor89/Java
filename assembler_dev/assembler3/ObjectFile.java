package assembler3;
import java.util.List;
public class ObjectFile {
    private SymbolTable symbolTable;
    private TextSection textSection;
    private DataSection dataSection;
    private BssSection bssSection;
    private List<String> globals;
    private List<String> externs;

    public void setSymbolTable(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
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
}
