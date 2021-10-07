package assembler2;
import java.io.File;
public AssemblyFile {
 
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

    public String[] setInstructions(String[] instructions) {
        this.instructions = instructions;
    }

    public String[] getInstructions() {
        return instructions;
    }

    public String[] setDataDirectives(String[] dataDirectives) {
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
}
