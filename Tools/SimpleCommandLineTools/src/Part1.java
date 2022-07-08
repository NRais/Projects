public class Part1 {

    /**
     * Simple function to check if a given integer is prime using the modulus function.
     *
     * A prime number is a number only divisible by itself and 1. So check if any other number can be divided with no remainder.
     *
     * @param number - input
     *
     * @return - T/F
     */
    public static boolean intIsPrime(int number) {
        if (number < 0) return false; // negative integers can not be prime.

        boolean check = true;

        // iterate through the numbers to see if any work
        // we start with smallest to speed up the process and once we have checked 2 we know nothing greater than "number/2" will work, which shortens our loop
        for (int i = 2; i <= number/2; i++) {
            if (number % i == 0) {
                check = false;
                break;
            }
        }

        return check;
    }


}
