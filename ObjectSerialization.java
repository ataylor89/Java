import java.io.*;
public class ObjectSerialization implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name="Puzzle1";
	private String solution = 	"020006009\n" +
						"200000001\n" +
						"300000910\n" +
						"050080000\n" +
						"007090004\n" +
						"000010040\n" +
						"000020050\n" +
						"000004000\n" +
						"900000008\n";
	private String problem = 	"123456789\n" +
						"234567891\n" +
						"345678912\n" +
						"456789123\n" +
						"567891234\n" +
						"678912345\n" +
						"789123456\n" +
						"891234567\n" +
						"912345678\n";

	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setSolution(String solution) {
		this.solution = solution;
	}
	public String getSolution() {
		return solution;
	}	
	public void setProblem(String problem) {
		this.problem = problem;
	}
	public String getProblem() {
		return problem;
	}

	public static void main(String[] args) {
		ObjectSerialization os = new ObjectSerialization();
		try {
			ObjectOutput out = new ObjectOutputStream(System.out);
			out.writeObject(os);
		} catch (IOException e) {
			System.err.println(e);
		}
	}
}					