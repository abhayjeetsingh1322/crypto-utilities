
import components.naturalnumber.NaturalNumber;
import components.naturalnumber.NaturalNumber2;
import components.random.Random;
import components.random.Random1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * Utilities that could be used with RSA cryptosystems.
 *
 * @author A. Singh
 *
 */
public final class CryptoUtilities {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private CryptoUtilities() {
    }

    /**
     * Useful constant, not a magic number: 3.
     */
    private static final int THREE = 3;

    /**
     * Pseudo-random number generator.
     */
    private static final Random GENERATOR = new Random1L();

    /**
     * Returns a random number uniformly distributed in the interval [0, n].
     *
     * @param n
     *            top end of interval
     * @return random number in interval
     * @requires n > 0
     * @ensures <pre>
     * randomNumber = [a random number uniformly distributed in [0, n]]
     * </pre>
     */
    public static NaturalNumber randomNumber(NaturalNumber n) {
        assert !n.isZero() : "Violation of: n > 0";
        final int base = 10;
        NaturalNumber result;
        int d = n.divideBy10();
        if (n.isZero()) {
            /*
             * Incoming n has only one digit and it is d, so generate a random
             * number uniformly distributed in [0, d]
             */
            int x = (int) ((d + 1) * GENERATOR.nextDouble());
            result = new NaturalNumber2(x);
            n.multiplyBy10(d);
        } else {
            /*
             * Incoming n has more than one digit, so generate a random number
             * (NaturalNumber) uniformly distributed in [0, n], and another
             * (int) uniformly distributed in [0, 9] (i.e., a random digit)
             */
            result = randomNumber(n);
            int lastDigit = (int) (base * GENERATOR.nextDouble());
            result.multiplyBy10(lastDigit);
            n.multiplyBy10(d);
            if (result.compareTo(n) > 0) {
                /*
                 * In this case, we need to try again because generated number
                 * is greater than n; the recursive call's argument is not
                 * "smaller" than the incoming value of n, but this recursive
                 * call has no more than a 90% chance of being made (and for
                 * large n, far less than that), so the probability of
                 * termination is 1
                 */
                result = randomNumber(n);
            }
        }
        return result;
    }

    /**
     * Finds the greatest common divisor of n and m.
     *
     * @param n
     *            one number
     * @param m
     *            the other number
     * @updates n
     * @clears m
     * @ensures n = [greatest common divisor of #n and #m]
     */
    public static void reduceToGCD(NaturalNumber n, NaturalNumber m) {

        //Checking if m is not zero
        if (!m.isZero()) {

            //Storing the remainder in NaturalNumber
            NaturalNumber rem = n.divide(m);

            //Recursively calling the reduceToGCD method on m and remainder
            reduceToGCD(m, rem);

            //Transferring m to n, this also clears m
            n.transferFrom(m);
        }

    }

    /**
     * Reports whether n is even.
     *
     * @param n
     *            the number to be checked
     * @return true iff n is even
     * @ensures isEven = (n mod 2 = 0)
     */
    public static boolean isEven(NaturalNumber n) {

        //Declaring a boolean variable initializing it to true
        boolean even = true;

        //Taking off last digit of n
        int lastDigit = n.divideBy10();

        //Checking if n is odd
        if (lastDigit % 2 != 0) {
            //Assigning the variable to false
            even = false;
        }

        //Appending the last digit
        n.multiplyBy10(lastDigit);

        //Returning boolean variable
        return even;
    }

    /**
     * Updates n to its p-th power modulo m.
     *
     * @param n
     *            number to be raised to a power
     * @param p
     *            the power
     * @param m
     *            the modulus
     * @updates n
     * @requires m > 1
     * @ensures n = #n ^ (p) mod m
     */
    public static void powerMod(NaturalNumber n, NaturalNumber p,
            NaturalNumber m) {
        assert m.compareTo(new NaturalNumber2(1)) > 0 : "Violation of: m > 1";

        //Declared NaturalNumbers: two, original p
        NaturalNumber divisorTwo = new NaturalNumber2(2);
        NaturalNumber pOriginal = p.newInstance();
        pOriginal.copyFrom(p);

        //Checking if p is zero
        if (p.isZero()) {

            //Declaring a new NaturalNumber one
            NaturalNumber one = n.newInstance();
            one.increment();

            //Assigning the value of one to n
            n.transferFrom(one);

            //Checking if p divided by 2 is equal to 0
        } else if (p.divide(divisorTwo).isZero()) {

            //Recursively calling the method, p already divided by 2
            powerMod(n, p, m);

            //Storing the value of n in temporary variable
            NaturalNumber temp = new NaturalNumber2(n);

            //Squaring n
            n.multiply(temp);

            //Storing the remainder of n % m and transferring to n
            NaturalNumber rem = n.divide(m);
            n.transferFrom(rem);

        } else {

            //Storing the original value of n in NaturalNumber variable
            NaturalNumber org = new NaturalNumber2(n);

            //Recursively calling the method, p already divided by 2
            powerMod(n, p, m);

            //Storing the current value of n in temporary NaturalNumber variable
            NaturalNumber temp = new NaturalNumber2(n);

            //Squaring n
            n.multiply(temp);

            //Multiplying to the original
            n.multiply(org);

            //Storing the remainder of n % m and transferring to n
            NaturalNumber rem = n.divide(m);
            n.transferFrom(rem);
        }

        //Restoring the value of p
        p.transferFrom(pOriginal);
    }

    /**
     * Reports whether w is a "witness" that n is composite, in the sense that
     * either it is a square root of 1 (mod n), or it fails to satisfy the
     * criterion for primality from Fermat's theorem.
     *
     * @param w
     *            witness candidate
     * @param n
     *            number being checked
     * @return true iff w is a "witness" that n is composite
     * @requires n > 2 and 1 < w < n - 1
     * @ensures <pre>
     * isWitnessToCompositeness =
     *     (w ^ 2 mod n = 1)  or  (w ^ (n-1) mod n /= 1)
     * </pre>
     */
    public static boolean isWitnessToCompositeness(NaturalNumber w,
            NaturalNumber n) {
        assert n.compareTo(new NaturalNumber2(2)) > 0 : "Violation of: n > 2";
        assert (new NaturalNumber2(1)).compareTo(w) < 0 : "Violation of: 1 < w";
        n.decrement();
        assert w.compareTo(n) < 0 : "Violation of: w < n - 1";
        n.increment();

        //Declaring and initializing a boolean variable for the witness
        boolean finalWitness = false;

        //Declared NatualNumbers
        NaturalNumber powerTwo = new NaturalNumber2(2);
        NaturalNumber answerOne = new NaturalNumber2(1);
        NaturalNumber wTemporary1 = new NaturalNumber2(w);
        NaturalNumber wTemporary2 = new NaturalNumber2(w);
        NaturalNumber nMinusOne = new NaturalNumber2(n);
        nMinusOne.decrement();

        //Calling powerMod on both ensures clauses
        powerMod(wTemporary1, powerTwo, n);
        powerMod(wTemporary2, nMinusOne, n);

        //If statement to check if either one is true
        if (wTemporary1.equals(answerOne) || !wTemporary2.equals(answerOne)) {
            //Assigning the value of true to variable, means the witness exists
            finalWitness = true;
        }

        //Returning variable
        return finalWitness;
    }

    /**
     * Reports whether n is a prime; may be wrong with "low" probability.
     *
     * @param n
     *            number to be checked
     * @return true means n is very likely prime; false means n is definitely
     *         composite
     * @requires n > 1
     * @ensures <pre>
     * isPrime1 = [n is a prime number, with small probability of error
     *         if it is reported to be prime, and no chance of error if it is
     *         reported to be composite]
     * </pre>
     */
    public static boolean isPrime1(NaturalNumber n) {
        assert n.compareTo(new NaturalNumber2(1)) > 0 : "Violation of: n > 1";
        boolean isPrime;
        if (n.compareTo(new NaturalNumber2(THREE)) <= 0) {
            /*
             * 2 and 3 are primes
             */
            isPrime = true;
        } else if (isEven(n)) {
            /*
             * evens are composite
             */
            isPrime = false;
        } else {
            /*
             * odd n >= 5: simply check whether 2 is a witness that n is
             * composite (which works surprisingly well :-)
             */
            isPrime = !isWitnessToCompositeness(new NaturalNumber2(2), n);
        }
        return isPrime;
    }

    /**
     * Reports whether n is a prime; may be wrong with "low" probability.
     *
     * @param n
     *            number to be checked
     * @return true means n is very likely prime; false means n is definitely
     *         composite
     * @requires n > 1
     * @ensures <pre>
     * isPrime2 = [n is a prime number, with small probability of error
     *         if it is reported to be prime, and no chance of error if it is
     *         reported to be composite]
     * </pre>
     */
    public static boolean isPrime2(NaturalNumber n) {
        assert n.compareTo(new NaturalNumber2(1)) > 0 : "Violation of: n > 1";

        //Declaring and initializing boolean variable for the method
        boolean isPrime = true;

        //Copying the value of n
        NaturalNumber nCopy = n.newInstance();
        nCopy.copyFrom(n);

        //Checking to see if n is greater than 3
        int isGreater = nCopy.compareTo(new NaturalNumber2(THREE));

        //Checking if n > 3, note 2 & 3 automatically be returned as true
        if (isGreater > 0) {

            //Checking if n is even, note 4 is checked by this method
            if (isEven(nCopy)) {
                isPrime = false;

                //Checking if there are witnesses
            } else {

                //Since 2, 3, 4, are already checked ranged can be decremented
                //by 4 which means [0, n-4] range for the random number generator
                nCopy.decrement();
                nCopy.decrement();
                nCopy.decrement();
                nCopy.decrement();

                //Declaring and initializing variables for the loop
                int count = 0;
                final int maxWitness = 50;

                //Entering while loop checking for both max witnesses and prime
                while (count < maxWitness && isPrime) {

                    //Declaring a variable to hold the randomly generated numbers
                    NaturalNumber candidate = randomNumber(nCopy);

                    //Incrementing variable twice, note the range not becomes
                    //[2, n-2], which is valid
                    candidate.increment();
                    candidate.increment();

                    //Checking if the candidate is not a witness of n
                    if (isWitnessToCompositeness(candidate, n)) {
                        //Assigning boolean variable as false
                        isPrime = false;
                    }

                    //Incrementing count
                    count++;
                }
            }
        }

        //Returning the boolean variable
        return isPrime;
    }

    /**
     * Generates a likely prime number at least as large as some given number.
     *
     * @param n
     *            minimum value of likely prime
     * @updates n
     * @requires n > 1
     * @ensures n >= #n and [n is very likely a prime number]
     */
    public static void generateNextLikelyPrime(NaturalNumber n) {
        assert n.compareTo(new NaturalNumber2(1)) > 0 : "Violation of: n > 1";

        //Declaring a boolean variable & calling isPrime2 on n
        boolean primeFound = isPrime2(n);

        //Entering loop body if variable is false, meaning n is not already prime
        while (!primeFound) {

            //Checking if n is even
            if (isEven(n)) {

                //Adding one making it odd
                n.increment();

            } else {

                //Adding two to keep it odd
                n.increment();
                n.increment();

            }

            //Calling isPrime2 again to check if it is prime
            primeFound = isPrime2(n);
        }
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();

        /*
         * Sanity check of randomNumber method -- just so everyone can see how
         * it might be "tested"
         */
        final int testValue = 17;
        final int testSamples = 100000;
        NaturalNumber test = new NaturalNumber2(testValue);
        int[] count = new int[testValue + 1];
        for (int i = 0; i < count.length; i++) {
            count[i] = 0;
        }
        for (int i = 0; i < testSamples; i++) {
            NaturalNumber rn = randomNumber(test);
            assert rn.compareTo(test) <= 0 : "Help!";
            count[rn.toInt()]++;
        }
        for (int i = 0; i < count.length; i++) {
            out.println("count[" + i + "] = " + count[i]);
        }
        out.println("  expected value = "
                + (double) testSamples / (double) (testValue + 1));

        /*
         * Check user-supplied numbers for primality, and if a number is not
         * prime, find the next likely prime after it
         */
        while (true) {
            out.print("n = ");
            NaturalNumber n = new NaturalNumber2(in.nextLine());
            if (n.compareTo(new NaturalNumber2(2)) < 0) {
                out.println("Bye!");
                break;
            } else {
                if (isPrime1(n)) {
                    out.println(n + " is probably a prime number"
                            + " according to isPrime1.");
                } else {
                    out.println(n + " is a composite number"
                            + " according to isPrime1.");
                }
                if (isPrime2(n)) {
                    out.println(n + " is probably a prime number"
                            + " according to isPrime2.");
                } else {
                    out.println(n + " is a composite number"
                            + " according to isPrime2.");
                    generateNextLikelyPrime(n);
                    out.println("  next likely prime is " + n);
                }
            }
        }

        /*
         * Close input and output streams
         */
        in.close();
        out.close();
    }

}