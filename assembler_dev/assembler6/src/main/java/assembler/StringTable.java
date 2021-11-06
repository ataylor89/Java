package assembler;

/**
 *
 * @author andrewtaylor
 */
public class StringTable {
    private String contents;
    
    public StringTable(SymbolTable symbolTable) {
        StringBuilder sb = new StringBuilder();
        sb.append('\0');
        for (Symbol symbol : symbolTable.getList()) {
            sb.append(symbol.getName());
            sb.append('\0');
        }
        contents = sb.toString();
    }

    @Override
    public String toString() {
        return contents;
    }
}
