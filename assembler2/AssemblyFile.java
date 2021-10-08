package assembler2;
import java.io.File;
import java.util.stream.Stream;
import java.util.stream.Collectors;
public class AssemblyFile {
 
    private File file;
    private String code;
    private String text;
    private String data;
    private String bss;
    private String[] instructions;
    private String[] dataDirectives;
    private String[] bssDirectives;
    private String[] globals;
    private String[] externs;   
    private SymbolTable symbolTable;
    
    public void setFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setBss(String bss) {
        this.bss = bss;
    }
    
    public String getBss() {
        return bss;
    }

    public void setInstructions(String[] instructions) {
        this.instructions = instructions;
    }

    public String[] getInstructions() {
        return instructions;
    }

    public void setDataDirectives(String[] dataDirectives) {
        this.dataDirectives = dataDirectives;
    }

    public String[] getDataDirectives() {
        return dataDirectives;
    }

    public void setBssDirectives(String[] bssDirectives) {
        this.bssDirectives = bssDirectives;
    }

    public String[] getBssDirectives() {
        return bssDirectives;
    }

    public void setGlobals(String[] globals) {
        this.globals = globals;
    }

    public String[] getGlobals() {
        return globals;
    }
    
    public void setExterns(String[] externs) {
        this.externs = externs;
    }

    public String[] getExterns() {
        return externs;
    }

    public void setSymbolTable(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }
    
    public SymbolTable getSymbolTable() {
        return symbolTable;
    }
    
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
    
        sb.append("Assembly file\n");
		sb.append("Globals: " + Stream.of(globals).collect(Collectors.joining(" ")));
		sb.append("\n");
		sb.append("Externs: " + Stream.of(externs).collect(Collectors.joining(" ")));
		sb.append("\n");
		sb.append("Text section:\n" + text);
		sb.append("Data section:\n" + data);
		sb.append("\n");
		sb.append("Instructions:\n" + Stream.of(instructions).collect(Collectors.joining("\n")));
        sb.append("\n");
        if (dataDirectives != null && dataDirectives.length > 0) {
            sb.append("Data directives:\n" + Stream.of(dataDirectives).collect(Collectors.joining("\n")));
            sb.append("\n");
        }
        if (bssDirectives != null && bssDirectives.length > 0) {
            sb.append("BSS directives:\n" + Stream.of(bssDirectives).collect(Collectors.joining("\n")));
            sb.append("\n");
        }
        if (symbolTable != null && symbolTable.getList().size() > 0)
            sb.append(symbolTable);
		
        return sb.toString();
	}
}
