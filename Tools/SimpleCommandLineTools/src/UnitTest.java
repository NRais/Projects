import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UnitTest {

    // PART 1 //
    @Test
    public void P1test_non_prime() {
        assertFalse(Part1.intIsPrime(10));
        assertFalse(Part1.intIsPrime(12));
        assertFalse(Part1.intIsPrime(14));
        assertFalse(Part1.intIsPrime(15));
        assertFalse(Part1.intIsPrime(16));
    }
    @Test
    public void P1test_prime() {
        assertTrue(Part1.intIsPrime(5));
        assertTrue(Part1.intIsPrime(7));
        assertTrue(Part1.intIsPrime(11));
        assertTrue(Part1.intIsPrime(13));
        assertTrue(Part1.intIsPrime(17));
    }
    @Test
    public void P1test_boundary() {
        assertFalse(Part1.intIsPrime(-123214));
        assertFalse(Part1.intIsPrime(1232232314));
    }

    // PART 2 //
    @Test
    public void P2test_true() {
        assertTrue(Part2.reverseString("Go").equals("oG"));
        assertTrue(Part2.reverseString("Hello").equals("olleH"));
        assertTrue(Part2.reverseString("Characterisation").equals("noitasiretcarahC"));
    }
    @Test
    public void P2test_false() {
        assertFalse(Part2.reverseString("Characterisation").equals("noitasirearahC"));
        assertFalse(Part2.reverseString("Gooo").equals("ooog"));
        assertFalse(Part2.reverseString("Hello").equals("olleh"));
    }
    @Test
    public void P2test_boundary() {
        assertTrue(Part2.reverseString(null).equals(""));
        assertTrue(Part2.reverseString("").equals(""));
    }

    // PART 3 //
    @Test
    public void P3test_boundary() {
        assertTrue(Part3.recurseFibonnaci(-23).equals("Sorry, enter a larger number. :) "));
    }
}
