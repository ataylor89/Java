package assembler4;
public class BssSection {   
    private String bss;
    private int start, end;
    private String[] directives;
    
    public BssSection() {}

    public void setBss(String bss) {
        this.bss = bss;
    }

    public String getBss() {
        return bss;
    }
    
    public void setStart(int start) {
        this.start = start;
    }

    public int getStart() {
        return start;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getEnd() {
        return end;
    }

    public void setDirectives(String[] directives) {
        this.directives = directives;
    }

    public String[] getDirectives() {
        return directives;
    }
    
    @Override
    public String toString() {
        return bss;
    }
}
