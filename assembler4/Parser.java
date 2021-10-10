package assembler4;
import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.util.stream.Collectors;
public class Parser {
    
    public AssemblyFile parse(File file) {
        AssemblyFile assemblyFile = new AssemblyFile();
        String code = read(file);
        assemblyFile.setCode(code);
        assemblyFile.setGlobals(parseGlobals(code));
        assemblyFile.setExterns(parseExterns(code));
        assemblyFile.setTextSection(parseTextSection(code));
        assemblyFile.setDataSection(parseDataSection(code));
        assemblyFile.setBssSection(parseBssSection(code));
        return assemblyFile;
    }

    public List<String> parseGlobals(String code) {
        List<String> globals = new ArrayList<>();
        int start = code.indexOf("global");
        int end = code.indexOf("\n", start);
        while (start > 0 && end > start) {
            String global = code.substring(start, end);
            globals.add(global);
            start = code.indexOf("global", end);
            end = code.indexOf("\n", start);
        }
        return globals;
    }

    public List<String> parseExterns(String code) {
        List<String> externs = new ArrayList<>();
        int start = code.indexOf("extern");
        int end = code.indexOf("\n", start);
        while (start > 0 && end > start) {
            String extern = code.substring(start, end);
            externs.add(extern);
            start = code.indexOf("extern", end);
            end = code.indexOf("\n", start);
        }
        return externs;
    }

    public TextSection parseTextSection(String code) {
        TextSection textSection = new TextSection();
        int sectionStart = code.indexOf("section .text");
        int sectionEnd = code.indexOf("section", sectionStart+13);
        if (sectionEnd < 0)
            sectionEnd = code.length();
        String text = code.substring(sectionStart, sectionEnd).trim();
        textSection.setText(text);
        textSection.setStart(sectionStart);
        textSection.setEnd(sectionEnd);
        String[] lines = text.split("\n");
        List<String> instructions = new ArrayList<>();
        for (int i = 1; i < lines.length; i++) {
            if (lines[i].endsWith(":") && i < lines.length-2) 
                instructions.add(lines[i] + " " + lines[++i]);
            else
                instructions.add(lines[i]);
        }
        textSection.setInstructions(instructions.toArray(new String[instructions.size()]));
        return textSection;
    }
    
    public DataSection parseDataSection(String code) {
        DataSection dataSection = new DataSection();
        int sectionStart = code.indexOf("section .data");
        if (sectionStart < 0)
            return null;
        int sectionEnd = code.indexOf("section", sectionStart+13);
        if (sectionEnd < 0)
            sectionEnd = code.length();       
        String data = code.substring(sectionStart, sectionEnd).trim();
        dataSection.setData(data);
        dataSection.setStart(sectionStart);
        dataSection.setEnd(sectionEnd);
        String[] lines = data.split("\n");
        List<String> directives = new ArrayList<>();
        for (int i = 1; i < lines.length; i++) {
            if (lines[i].endsWith(":") && i < lines.length-2)
                directives.add(lines[i] + " " + lines[++i]);
            else
                directives.add(lines[i]);
        }
        dataSection.setDirectives(directives.toArray(new String[directives.size()]));
        return dataSection;
    }

    public BssSection parseBssSection(String code) {
        BssSection bssSection = new BssSection();
        int sectionStart = code.indexOf("section .bss");
        if (sectionStart < 0)
            return null;
        int sectionEnd = code.indexOf("section", sectionStart+12);
        if (sectionEnd < 0)
            sectionEnd = code.length();
        String bss = code.substring(sectionStart, sectionEnd).trim();
        bssSection.setBss(bss);
        bssSection.setStart(sectionStart);
        bssSection.setEnd(sectionEnd);
        String[] lines = bss.split("\n");
        List<String> directives = new ArrayList<>();
        for (int i = 1; i < lines.length; i++) {
            if (lines[i].endsWith(":") && i < lines.length-2)
                directives.add(lines[i] + " " + lines[++i]);
            else
                directives.add(lines[i]);
        } 
        bssSection.setDirectives(directives.toArray(new String[directives.size()]));      
        return bssSection;
    }

    public String read(File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            System.err.println(e);
        }
        return null;
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java assembler4.Parser <sourcefile.asm>");
            return;
        }
        Parser parser = new Parser();
        File file = new File(args[0]);
        AssemblyFile assemblyFile = parser.parse(file);
        System.out.println(assemblyFile);
    }
}
