import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.io.File;
import java.util.stream.Stream;
import java.util.stream.Collectors;
public class AssemblyFile {
	private List<String> globals;
	private List<String> externs;
	private File file;
	private String code;
	private String textSection, dataSection, bssSection;
	private String[] tokens;	
	private List<String> instructions;

	public AssemblyFile() {
		globals = new ArrayList<>();
		externs = new ArrayList<>();
		instructions = new ArrayList<>();
	}

	public void addGlobal(String global) {
		globals.add(global);
	}

	public void addExtern(String extern) {
		externs.add(extern);
	}

	public List<String> getGlobals() {
		return globals;
	}
	
	public List<String> getExterns() {
		return externs;
	}

	public void setTokens(String[] tokens) {
		this.tokens = tokens;
	}

	public String[] getTokens() {
		return tokens;
	}

	public void setFile(File file) {
		this.file = file;
	}
	
	public File getFile() {
		return file;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setTextSection(String textSection) {
		this.textSection = textSection;
	}

	public String getTextSection() {
		return textSection;
	}

	public void setDataSection(String dataSection) {
		this.dataSection = dataSection;
	}

	public String getDataSection() {
		return dataSection;
	}

	public void setBssSection(String bssSection) {
		this.bssSection = bssSection;
	}

	public String getBssSection() {
		return bssSection;
	}

	public void addInstruction(String instruction) {
		instructions.add(instruction);
	}

	public List<String> getInstructions() {
		return instructions;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("Globals: " + globals.stream().collect(Collectors.joining(" ")));
		stringBuilder.append("\n");
		stringBuilder.append("External object files: " + externs.stream().collect(Collectors.joining(" ")));
		stringBuilder.append("\n");
		stringBuilder.append("Text section:\n" + textSection);
		stringBuilder.append("Data section:\n" + dataSection);
		stringBuilder.append("\n");
		stringBuilder.append("Instructions:\n" + instructions.stream().collect(Collectors.joining("\n")));

		return stringBuilder.toString();
	}
}