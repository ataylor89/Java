package assembler;

/**
 *
 * @author andrewtaylor
 */
public class Expression {
    
    private String expression;
    private Object value;
    private static SymbolTable symbolTable;
     
    public Expression(String expression) {
        if (expression != null) {
            this.expression = expression;
            if (symbolTable.isSymbol(expression)) {
                Symbol symbol = symbolTable.getMap().get(expression);
                if (symbol.getType() == SymbolType.ABSOLUTE)
                    value = (int) symbol.getValue();
                else
                    value = symbol.getValue();
            }
            else if (Register.isRegister(expression))
                value = Register.parse(expression);
            else if (StringConstant.isStringConstant(expression))
                value = new StringConstant(expression).getValue();
            else if (expression.startsWith("$-")) 
                value = symbolTable.getMap().get(expression.substring(2, expression.length())).getSize();
            else {
                try {
                    Long l = Long.decode(expression);
                    if (l > Integer.MAX_VALUE)
                        value = l;
                    else
                        value = Integer.decode(expression);
                } catch (NumberFormatException e) {
                    System.err.println(e);
                }
            }
        }
    }
    
    /**
     * @return the expression
     */
    public String getExpression() {
        return expression;
    }

    /**
     * @param expression the expression to set
     */
    public void setExpression(String expression) {
        this.expression = expression;
    }

    /**
     * @return the value
     */
    public Object getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(Object value) {
        this.value = value;
    }
    
    public static void setSymbolTable(SymbolTable symbolTable) {
        Expression.symbolTable = symbolTable;
    }
}
