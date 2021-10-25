package assembler;
public class Operands {

	public static Operand getOperand(String text) {
		if (text.endsWith(","))
			text = text.substring(0, text.length()-1);
		if (Registers.isRegister(text))
			return Registers.getRegister(text);
		if (ImmediateValues.isImmediateValue(text))
			return ImmediateValues.getImmediateValue(text);
		return null;
	}
}
