public class StringTokenizer {
    
    public static void tokenize(String s) {
        String[] tokens = s.split("\\s+", 3);
        for (int i = 0; i < tokens.length; i++) {   
            System.out.printf("tokens[%d] = %s\n", i, hexstring(tokens[i].getBytes()));
        }
    }

    public static String hexstring(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(String.format("%x", bytes[i]));
            if (i < bytes.length - 1)
                sb.append(" ");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        tokenize("message:  db  'hello world',10,0");
        // tokenize("len   equ     $-message");
    }
}
