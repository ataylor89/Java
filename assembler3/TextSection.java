package assembler3;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
public class TextSection {
    private String text;
    private int start, end;
    private List<Instruction> instructions;

    public TextSection() {}

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
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

    public void setInstructions(List<Instruction> instructions) {
        this.instructions = instructions;
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }

    @Override 
    public String toString() }

    }
}
