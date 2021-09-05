import java.util.Arrays;
import java.util.stream.Stream;
public class MultiplesOfThree {
	public static void main(String[] args) {
		Integer[] nums = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21};
		Arrays.stream(nums).filter(i -> i %3 == 0).forEach(System.out::println);
	}
}