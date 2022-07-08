import java.util.ArrayList;
import java.util.List;

public class Part3 {

    /**
     * Wrapper method to start the recursion for a fibonnaci series.
     *
     * @param size - the depth of recursion.
     *
     * @return - the string result of the recursion
     */
    public static String recurseFibonnaci(int size) {
        // initialize the starting fibonnaci list for it to function
        List<Integer> startList = new ArrayList<>();
        startList.add(0);
        startList.add(1);

        if (size > 2) {
            return fibonnaci(startList, size - 2).toString();
        } else {
            return "Sorry, enter a larger number. :) ";
        }
    }

    public static List<Integer> fibonnaci(List<Integer> c, int size) {
        if (size == 0) return c; // break the recursion

        List<Integer> newList = new ArrayList<>(c);

        // recurse further by getting the last integers and making the next integer
        Integer n1 = c.get(c.size()-1);
        Integer n2 = c.get(c.size()-2);
        newList.add(n1 + n2);

        return fibonnaci(newList, size-1);
    }

}
