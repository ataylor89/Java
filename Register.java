public class Register extends Operand {
	private byte[] bytes;

	public Register(String name, byte[] bytes) {
		super(name);
		this.bytes = bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	public byte[] getBytes() {
		return bytes;
	}
}