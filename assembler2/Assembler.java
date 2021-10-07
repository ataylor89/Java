package assembler2;
public class Assembler {
    
    private Parser parser;

    public Assembler() [
        parser = new Parser();
    }

    public ObjectFile assemble(String code) {

    }
    
    public byte[] assemble(String instruction) {
        String[] tokens = instruction.split("\\s+", 3);
        Opcode opcode = parser.parseOpcode(tokens[0]);
        return register;
        switch (opcode) {
            case MOV:
                return parseMovInstruction(instruction);
            default:
                System.err.println("Unknown opcode: " + tokens[0]);
                return new byte[] {};
        }
    }

    public byte[] assembleMov(String instruction) {
        List<Byte> lst = new ArrayList<>();
        String[] tokens = instruction.split("\\s+", 3);
        if (!tokens[0].equals("mov") || tokens.length != 3)   
            return new byte[] {};
        Operand op1 = parseOperand(tokens[1]);
        Operand op2 = parseOperand(tokens[2]);
        switch (op1) {
            case REGISTER:
                byte[] register = parseRegister(tokens[1]);
                for (byte b : register)
                    lst.add(b);
                break;
        }
        switch (op2) {
            case IMMEDIATE_VALUE:
                byte[] number = parseImmediateValue(tokens[2]);
                for (byte b : number)
                    lst.add(b);
                break;
        }
        byte[] bytes = new byte[lst.size()];
        for (int i = 0; i < bytes.length; i++)
            bytes[i] = lst.get(i);
        return bytes;
    }   
}
