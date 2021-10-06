package assembler;
public class DbDirectives {
    
    public static DbDirective parse(String text) {
        DbDirective directive = new DbDirective(text);
        String[] tokens = text.split("\\s", 3);
        directive.setLabel(tokens[0]);
        directive.setOperand(tokens[2]);
        String[] constants = tokens[2].split(",");
        byte[] bytes = new byte[1000];
        int index = 0;
        for (String constant : constants) {
            constant = constant.trim();
            // System.out.println("Constant: " + constant);
            int n = constant.length();
            if (constant.startsWith("'") && constant.endsWith("'")) {
                String charconstant = constant.substring(1, n-1);
                // System.out.println("Char constant: " + charconstant);
                byte[] b = charconstant.getBytes();
                // System.out.println("Char constant bytes: " + Bytes.hexstring(b));
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
    
    /*
    public static DbDirective parse(String text) {
        DbDirective directive = new DbDirective();
        String[] tokens = text.split("\gs");
        String label = tokena
        String[] tokens = text.split(",");
        for (String token : tokens) {
            System.out.println("Token: " + token);
            int n = token.length();
            if (token.startsWith("'") && token.endsWith("'")) {
                String charConstant = token.substring(1, n-1);
                addBytes(charConstant.getBytes());
            }
            else if (token.startsWith("\"") && token.endsWith("\"")) {
                String stringConstant = token.substring(1, n-1);
                addBytes(stringConstant.getBytes());
            }
            else {
                try {
                    long num = Long.decode(token);
                    long bitmask = 0xff << 8*7;
                    for (int i = 7; i >= 0; i++) {
                        byte b = (byte) ((bitmask & num) >> (8*i));
                        addByte(b);
                    }
                } catch (NumberFormatException e) {
                    System.err.println(e);
                }
            }
        }
    }
    */

    public static void main(String[] args) {
        DbDirective dir = parse("message: db 'hello world'");
        System.out.println(dir);
        DbDirective dir2 = parse("message: db 'hello world', 10, 0");
        System.out.println(dir2);
        DbDirective dir3 = parse("message: db \"hello world\", 10, 0");
        System.out.println(dir3);
        /*
        byte[] b = dir.getBytes();
        String s = new String(b);
        System.out.println(s);
        String hs = Bytes.hexstring(b);
        System.out.println(hs);
        */
    }
}    
