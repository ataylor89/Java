package assembler;
public class Instruction {
	private String text;
	private Opcode opcode;
	private Operand[] operands;

	public Instruction() {}

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}		

	public void setOpcode(Opcode opcode) {
		this.opcode = opcode;
	}

	public Opcode getOpcode() {
		return opcode;
	}

	public void setOperands(Operand[] operands) {
		this.operands = operands;
	}

	public Operand[] getOperands() {
		return operands;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Instruction: " + text);
		sb.append("\n");
		sb.append("Opcode: " + opcode);
		for (Operand operand : operands) {
			sb.append("\n");
			sb.append(operand);
		}
		return sb.toString();
	}
}
