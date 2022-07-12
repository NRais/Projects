public class Part2 {


    /**
     * Simple function to reverse the order of letters in a string.
     *
     * @param input - input String
     *
     * @return - reversed String
     */
    public static String reverseString(String input) {
        if (input == null) return "";

        String output = "";

        // iterate backwards through the string, constructing a new string as we go
        for (int i = input.length()-1; i >= 0; i--) {
            output += input.charAt(i);
        }

        return output;
    }
}
