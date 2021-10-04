package assembler;
public class Operand {
	
	private String name;
    private int type;
	private byte[] bytes;

    public static final int REGISTER = 0;
	public static final int IMMEDIATE_VALUE = 1;
	public static final int EFFECTIVE_ADDRESS = 2;
	public static final int SYMBOL = 3;

	public Operand() {}

	public Operand(int type) {
		this.type = type;
	}

    public Operand(String name, byte[] bytes, int type) {
        this.name = name;
        this.bytes = bytes;
        this.type = type;
    }

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public int getType() {
		return type;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	public byte[] getBytes() {
		if (bytes == null)
			return new byte[] {};
		return bytes;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
        sb.append("Name: " + name);
        sb.append("\n");
        sb.append("Type: " + type);
        sb.append("\n");
        sb.append("Bytes: " + Bytes.hexstring(bytes));
        sb.append("\n");
        return sb.toString();
	}
}
