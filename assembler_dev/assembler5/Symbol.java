package assembler5;
public class Symbol {
    private String name;
    private Object value;
    private byte[] bytes;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
