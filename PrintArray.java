import java.util.Arrays;

public class PrintArray {
    
    public static void printArray(Object[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            if (i == 0) 
                sb.append(arr[i]);
            else
                sb.append(" " + arr[i]);
        }
        System.out.println(sb.toString());
    }
        
    public static void main(String[] args) {
        int[] arr = new int[] {1, 2, 3, 4, 5, 6};
        Integer[] objs = Arrays.stream(arr).boxed().toArray(Integer[]::new);
        printArray(objs);
    }
}
