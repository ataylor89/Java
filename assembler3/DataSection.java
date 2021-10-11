package assembler3;
import java.util.List;
import java.util.ArrayList;
public class DataSection {
    private String data;
    private int start, end;
    private String[] directives;

    public DataSection() {}

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
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
        return null;
    }
}
