package assembler;
public class Directives {
    public static Directive parse(String text) {
        String[] tokens = text.split("\\s");
        if (tokens[1].equals("db"))
            return DbDirectives.parse(text);
        return null;
    }
}
