package assembler4;
public class TextSection {
    private String text;
    private int start, end;
    private String[] instructions;
    
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

    public void setInstructions(String[] instructions) {
        this.instructions = instructions;
    }

    public String[] getInstructions() {
        return instructions;
    }

    @Override
    public String toString() {
        return text;
    }
}
