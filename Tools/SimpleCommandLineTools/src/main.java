import java.util.Scanner;

public class main {

    /**
     * initializer allowing modules to be tested
     * @param args
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // PART 1 //
        System.out.print("Enter a number: ");

        System.out.println("Is it a prime? " + Part1.intIsPrime(sc.nextInt()));

        // PART 2 //
        System.out.print("Enter a string: ");

        System.out.println("Reversed: " + Part2.reverseString(sc.next()));


        // PART 3 //
        System.out.println("Enter a length for fibonnaci: ");

        System.out.println("Fibonnaci: " + Part3.recurseFibonnaci(sc.nextInt()));

    }
}
