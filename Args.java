public class Args {
	public static void main(String[] args) {
		System.out.println("Args length: " + args.length);
		for (int i = 0; i < args.length; i++) {
			System.out.printf("args[%d]: %s\n", i, args[i]);
		}
	}
}