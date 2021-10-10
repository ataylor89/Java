package assembler5;
import java.io.File;
public class Assembler {
    
    private Parser parser;

    public Assembler() {
        parser = new Parser();
    }

    public ObjectFile compile(File file) {
        ObjectFile objectFile = new ObjectFile();
        AssemblyFile assemblyFile = parser.parse(file);
        String[] dataDirectives = assemblyFile.getDataDirectives();
        compileDataSection(dataDirectives, objectFile);
        return objectFile;
    }

    public void compileDataSection(String[] directives, ObjectFile objectFile) {
        for (int i = 0; i < directives.length; i++) {
            String[] tokens = directives[i].split("\\s+", 3);
            String label = tokens[0];
            if (label.endsWith(":"))
                label = label.substring(0, label.length()-1);
            String type = tokens[1];
            String data = tokens[2];
            Directive directive = null;
            try {
                directive = Directive.valueOf(type.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.err.println(e);
                continue;
            }
            int index = objectFile.getDataSection().getIndex();
            switch (directive) {
                case DB:
                    String[] constants = data.split(",");
                    for (String constant : constants) {
                        if (constant.startsWith("'") && constant.endsWith("'")) {
                            constant = constant.substring(1, constant.length()-1);
                            objectFile.getDataSection().addBytes(constant.getBytes());
                            objectFile.getSymbols().put(label, index);
                        }
                        else if (constant.startsWith("\"") && constant.endsWith("\"")) {
                            constant = constant.substring(1, constant.length()-1);
                            objectFile.getDataSection().addBytes(constant.getBytes());
                            objectFile.getSymbols().put(label, index);
                        }
                        else {
                            try {
                                Long num = Long.decode(constant);
                                int size = Bytes.size(num);
                                byte[] bytes = Bytes.littleendian(num, size);
                                objectFile.getDataSection().addBytes(bytes);
                                objectFile.getSymbols().put(label, index);
                            } catch (NumberFormatException e) {
                                System.err.println(e);
                            }    
                        }   
                    } 
                    break;
                case EQU:
                    if (data.startsWith("$-")) {
                        Integer start = (Integer) objectFile.getSymbols().get(data.substring(2));
                        byte[] dataSection = objectFile.getDataSection().getBytes();
                        Integer end = start;
                        while (end < dataSection.length - 1 && dataSection[end] != 0x00)
                            end++;
                        Long value = (long) (end - start + 1);
                        objectFile.getSymbols().put(label, value);
                    }
                    else {
                        try {
                            Long value = Long.decode(data);
                            objectFile.getSymbols().put(label, value);
                        } catch (NumberFormatException e) {
                            System.err.println(e);
                        }   
                    }                  
                    break;
            }       
        }
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java assembler5.Assembler <sourcefile.asm> <objectfile.o>");
            return;
        }
        Assembler assembler = new Assembler();
        File src = new File(args[0]);
        ObjectFile objectFile = assembler.compile(src);
        System.out.println(objectFile);
    }
}
