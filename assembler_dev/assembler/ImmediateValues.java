package assembler;
public class ImmediateValues {

	public static boolean isImmediateValue(String text) {
		try {
			Integer num = Integer.decode(text);
		} catch (NumberFormatException e) {
			System.err.println(e);
            return false;
		}
        return true;
	}

	public static ImmediateValue getImmediateValue(String text) {
		ImmediateValue immediateValue = new ImmediateValue();
	    immediateValue.setName(text);
        Integer value = null;
        try {
            value = Integer.decode(text);
			immediateValue.setValue(value);
		} catch (NumberFormatException e) {
			System.err.println(e);
		}   
		byte[] bytes = Bytes.littleendian(value);
		immediateValue.setBytes(bytes);
        return immediateValue;
	}
}
