package assembler;
public class Directives {
    public static Directive parse(String text) {
        // System.out.printf("Directives.parse(%s)\n", text);
        String[] tokens = text.split("\\s+", 3);
        // System.out.println("tokens.length == " + tokens.length);
        String type = tokens[1].trim();
        // System.out.println("Type: " + type);
        if (type.equals("db"))
            return DbDirectives.parse(text);
        if (type.equals("equ"))
            return EquDirectives.parse(text);
        return null;
    }
}
