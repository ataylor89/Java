package assembler2;
import java.io.*;
import java.util.List;
public class Assembler {
    
    private Parser parser;

    public Assembler() {
        parser = new Parser();
    }

    public ObjectFile assemble(File src, File dest) {
        ObjectFile objectFile = new ObjectFile(dest);
        AssemblyFile assemblyFile = parser.parse(src);
        System.out.println(assemblyFile);
        SymbolTable symbolTable = assemblyFile.getSymbolTable();
        for (String instruction : assemblyFile.getInstructions()) {
            byte[] bytes = parser.parseInstruction(instruction, symbolTable);
            objectFile.getByteArray().addBytes(bytes);
        }
        List<Symbol> symbols = symbolTable.getList();
        for (int i = 0; i < symbols.size(); i++) {
            Symbol symbol = symbols.get(i);
            byte[] bytes = symbol.getBytes();
            objectFile.getByteArray().addBytes(symbol.getBytes());
        }
        return objectFile;
    }

    public void writeToFile(ObjectFile objectFile) {
        File dest = objectFile.getFile();
        try {
            PrintWriter out = new PrintWriter(new FileWriter(dest), true);
            byte[] bytes = objectFile.getByteArray().getBytes();
            out.print(bytes);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java assembler2.Assembler <sourcefile.asm> <outputfile.o>");
            return;
        }
        File src = new File(args[0]);
        File dest = new File(args[1]);
        Assembler assembler = new Assembler();
        ObjectFile objectFile = assembler.assemble(src, dest);
        assembler.writeToFile(objectFile);
        System.out.println(objectFile);
    }
}
