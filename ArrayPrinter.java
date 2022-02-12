import java.util.Arrays;

public class ArrayPrinter {
    
    public static void printArray(Object[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            sb.append(i == 0 ? arr[i] : " " + arr[i]);
        }
        System.out.println(sb.toString());
    }
        
    public static void main(String[] args) {
        int[] arr = new int[] {1, 2, 3, 4, 5, 6};
        Integer[] objs = Arrays.stream(arr).boxed().toArray(Integer[]::new);
        printArray(objs);
    }
}
