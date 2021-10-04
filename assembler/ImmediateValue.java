package assembler;
public class ImmediateValue extends Operand {
	private long value;	

	public ImmediateValue() {
		super(IMMEDIATE_VALUE);
	}

	public void setValue(long value) {
		this.value = value;
	}

	public long getValue() {
		return value;
	}
}
