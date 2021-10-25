package assembler;
public class DbDirectives {
    
    public static DbDirective parse(String text) {
        DbDirective directive = new DbDirective(text);
        String[] tokens = text.split("\\s+", 3);
        String label = tokens[0];
        if (label.endsWith(":"))
            label = label.substring(0, label.length()-1);
        directive.setLabel(label);
        String operand = tokens[2];
        directive.setOperand(operand);
        String[] constants = operand.split(",");
        byte[] bytes = new byte[1000];
        int index = 0;
        for (String constant : constants) {
            constant = constant.trim();
            int n = constant.length();
            if (constant.startsWith("'") && constant.endsWith("'")) {
                String charconstant = constant.substring(1, n-1);
                byte[] b = charconstant.getBytes();
                System.arraycopy(b, 0, bytes, index, b.length);
                index += b.length;
            }
            else if (constant.startsWith("\"") && constant.endsWith("\"")) {
                String stringconstant = constant.substring(1, n-1);
                byte[] b = stringconstant.getBytes();
                System.arraycopy(b, 0, bytes, index, b.length);
                index += b.length;
            }
            else {
                try {
                    int num = Integer.decode(constant);
                    bytes[index] = (byte) num;
                    index++;           
                } catch (NumberFormatException e) {
                    System.err.println(e);
                }
            }
        }
        byte[] arr = new byte[index];
        System.arraycopy(bytes, 0, arr, 0, index);
        directive.setBytes(arr);
        return directive;
    }
    
    public static void main(String[] args) {
        DbDirective dir = parse("message: db 'hello world'");
        System.out.println(dir);
        DbDirective dir2 = parse("message: db 'hello world', 10, 0");
        System.out.println(dir2);
        DbDirective dir3 = parse("message: db \"hello world\", 10, 0");
        System.out.println(dir3);
    }
}    
