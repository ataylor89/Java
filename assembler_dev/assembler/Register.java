package assembler;
public class Register extends Operand {

	public Register() {
		super(REGISTER);
	}

	public Register(String name, byte[] bytes) {
		super(name, bytes, REGISTER);
	}
}
