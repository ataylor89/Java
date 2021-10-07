package assembler2;
import java.util.List;
import java.util.ArrayList;
public class Parser {
    
    public AssemblyFile parse(File file) {
        AssemblyFile assemblyFile = new AssemblyFile();
        assemblyFile.setFile(file);
        String code = readFile(file);
        assemblyFile.setCode(code);
        String text = parseText(code);
        assemblyFile.setText(text);
        String data = parseData(code);
        assemblyFile.setData(data);
        String bss = parseBss(code);
        assemblyFile.setBss(bss);
        String[] globals = parseGlobals(code);
        assemblyFile.setGlobals(globals);
        String[] externs = parseExterns(code);
        assemblyFile.setExterns(externs);
        String[] instructions = parseInstructions(code);
        assemblyFile.setInstructions(instructions);
        String[] dataDirectives = parseDataDirectives(code);
        assemblyFile.setDataDirectives(dataDirectives);
        String[] bssDirectives = parseBssDirectives(code);
        assemblyFile.setBssDirectives();
        String[] globals = parseGlobals(code);
        assemblyFile.setGlobals(globals);
        String[] externs = parseExterns(code);
        assemblyFile.setExterns(externs);
    }

    public String parseText(String code) {
        int start = code.indexOf("section .text");
        int end = code.indexOf("section", start+13);
        if (end < 0)
            end = code.length();
        return code.substring(start, end);
    }

    public String parseData(String code) {
        int start = code.indexOf("section .data");
        int end = code.indexOf("section", start+13);
        if (end < 0)
            end = code.length();
        return code.substring(start, end);
    }

    public String parseBss(String code) {
        int start = code.indexOf("section .bss");
        int end = code.indexOf("section", start+13);
        if (end < 0)
            end = code.length();
        return code.substring(start, end);
    }

    public String[] parseGlobals(String code) {
        List<String> lst = new ArrayList<>();
        int start = code.indexOf("global");
        int end = code.indexOf("\n", start);
        while (start > 0 && end > 0) {
            String global = code.substring(start, end);
            lst.add(global);
            start = code.indexOf("global", end);
            end = code.indexOf("\n", start);
        }
        String[] globals = new String[lst.size()];
        for (int i = 0; i < lst.size(); i++)
            globals[i] = lst.get(i);
        return globals;
    }

    public String[] parseExterns(String code) {
        List<String> lst = new ArrayList<>();
        int start = code.indexOf("extern");
        int end = code.indexOf("\n", start);
        while (start > 0 && end > 0) {
            String extern = code.substring(start, end);
            lst.add(extern);
            start = code.indexOf("extern", end);
            end = code.indexof("\n", start);
        }
        String[] externs = new String[lst.size()];
        for (int i = 0; i < lst.size(); i++)
            externs[i] = lst.get(i);
        return externs;
    }
 
    public String[] parseInstructions(String code) {
        List<String> lst = new ArrayList<>();
        String text = parseText(code);
        int start = text.indexOf("\n") + 1;
        int end = text.IndexOf("\n", start);
        if (end < 0)
            end = text.length();
        while (start > 0 && end > start) {
            String instruction = text.substring(start, end);
            lst.add(instruction);
            start = end + 1;
            end = text.IndexOf("\n", start);
            if (end < 0)
                end = text.length();
        }
        String[] instructions = new String[lst.size()];
        for (int i = 0; i < instructions.length; i++)
            instructions[i] = lst.get(i);
        return instructions;
    }

    public String[] parseDataDirectives(String code) {
        List<String> lst = new ArrayList<>();
        String data = parseData(code);
        int start = data.indexOf("\n") + 1;
        int end = data.indexOf("\n", start);
        if (end < 0)
            end = data.length();
        while (start > 0 && end > start) {
            String dataDirective = data.substring(start, end);
            lst.add(dataDirective);
            start = end + 1;
            end = data.indexOf("\n", start);
            if (end < 0)
                end = data.length();
        }
        String[] dataDirectives = new String[lst.size()];
        for (int i = 0; i < dataDirectives.length; i++)
            dataDirectives[i] = lst.get(i);
        return dataDirectives;
    }

    public String[] parseBssDirectives(String code) {
        List<String> lst = new ArrayList<>();
        String bss = parseBss(code);
        int start = bss.indexOf("\n") + 1;
        int end = bss.indexOf("\n", end);
        if (end < 0)
            end = bss.length();
        while (start > 0 && end > start) {
            String bssDirective = bss.substring(start, end);
            lst.add(bssDirective);
            start = end + 1;
            end = bss.indexOf("\n", start);
            if (end < 0)
                end = bss.length();
        }
        String[] bssDirectives = new String[lst.size()];
        for (int i = 0; i < bssDirectives.length; i++)
            bssDirectives[i] = lst.get(i);
        return bssDirectives;
    }
   
    public String readFile(File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            System.err.println(e);
        }
        return null;
    }

}
