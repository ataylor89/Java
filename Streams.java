import java.util.stream.Stream;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
public class Streams {

    public static void main(String[] args) {
        Integer[] arr = new Integer[] {1,2,3,4,5,6,7,8,9};
        List<Integer> lst = Arrays.asList(arr);  
        lst.stream().filter(i -> i > 4).forEach(i -> System.out.println(i));
        System.out.println(lst.stream().filter(i -> i > 4).mapToInt(i -> i.intValue()).sum());
        String ls = "ls";
        String[] words = ls.split(" ");
        System.out.println(words[0]);
    }
    
}
