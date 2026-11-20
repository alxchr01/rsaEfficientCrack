package RSA;

import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Safe simulation utilities for experimenting with RSA operations and a
 * controlled (toy) brute-force simulation loop.
 *
 * The provided code performs only harmless operations: it verifies that
 * encryption and decryption are consistent on local, freshly-generated key
 * pairs and gathers information for a simulation from the user. The
 * "brute-force" routine below is a toy demonstration that iterates
 * candidate guesses and does not include any actionable attack code.
 */
public class Simulation {

    /**
     * (Removed) previous verification method. Use the toy brute-force demo
     * below instead which generates its own small key and demonstrates a
     * simple trial-division brute-force loop on the modulus.
     */

    /**
     * Interactively collect parameters required for running a controlled
     * simulation or toy brute-force experiment.
     *
     * The method uses the supplied Scanner to prompt the user for values.
     * Callers may pass `new Scanner(System.in)` for interactive use.
     *
     * @param sc Scanner to read user input from
     * @return a SimulationConfig containing the collected information
     */
    public static SimulationConfig getSimulationInfo(Scanner sc) {
        System.out.println("--- Simulation configuration ---");

        System.out.print("Key size in bits (suggest 256, 512 for toy tests): ");
        int keySize = Integer.parseInt(sc.nextLine().trim());

        System.out.print("Plaintext to encrypt (short text recommended): ");
        String plaintext = sc.nextLine();

        System.out.print("Max candidate attempts for the simulation (e.g. 1000): ");
        long maxAttempts = Long.parseLong(sc.nextLine().trim());

        System.out.print("Use tiny toy keys (yes/no)? [yes]: ");
        String toy = sc.nextLine().trim();
        boolean useToyKeys = toy.isEmpty() || toy.equalsIgnoreCase("yes") || toy.equalsIgnoreCase("y");

        System.out.println("Configuration collected. NOTE: Use toy keys only for simulations.");
        return new SimulationConfig(keySize, plaintext, maxAttempts, useToyKeys);
    }

    /**
     * Generate a tiny RSA keypair, encrypt a hard-coded plaintext, and
     * attempt to "brute force" the modulus by simple trial division.
     *
     * This demonstration is strictly for educational and local experiments
     * using *very small* keys that you generated just now. The routine is
     * intentionally naive (trial division) and will not be effective on
     * real RSA moduli used in practice.
     */
    public static void runBruteForceDemo() {
        System.out.println("\n--- Running toy brute-force demo (safe, local) ---");

        // Choose small prime sizes so factorization by trial division completes quickly
        int primeBits = 50; // primes ~ 2^16 => modulus ~ 32 bits

        // Generate two distinct small primes
        BigInteger p = BigInteger.probablePrime(primeBits, new java.util.Random());
        BigInteger q;
        do {
            q = BigInteger.probablePrime(primeBits, new java.util.Random());
        } while (q.equals(p));

        BigInteger n = p.multiply(q);
        BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

        // Choose public exponent e that is coprime to phi
        BigInteger e = BigInteger.valueOf(65537);
        if (!e.gcd(phi).equals(BigInteger.ONE)) {
            // fallback to a small odd exponent
            e = BigInteger.valueOf(17);
            if (!e.gcd(phi).equals(BigInteger.ONE)) e = BigInteger.valueOf(3);
        }

        // Compute private exponent d
        BigInteger d = e.modInverse(phi);

        // Hard-coded short plaintext (friendly demo string)
        String plaintext = "password";

        // Encrypt plaintext with textbook RSA per-character
        BigInteger[] ciphertext = new BigInteger[plaintext.length()];
        for (int i = 0; i < plaintext.length(); i++) {
            ciphertext[i] = BigInteger.valueOf((int) plaintext.charAt(i)).modPow(e, n);
        }

        System.out.println("\nGenerated tiny RSA key (toy): n=" + n + " p=" + p + " q=" + q + " e=" + e + "\n");

        // --- Brute-force trial division of n ---
        long attempts = 0;
        BigInteger foundP = null;

        BigInteger candidate = BigInteger.TWO;
        long start = System.nanoTime();
        while (candidate.multiply(candidate).compareTo(n) <= 0) {
           attempts++;
            if (n.mod(candidate).equals(BigInteger.ZERO)) {
                foundP = candidate;
                break;
            }
            candidate = candidate.add(BigInteger.ONE);
            if(attempts % 10000000 == 0) {
                System.out.println("Attempts: " + attempts + "/" + (p.min(q)));
            }
            
        if (candidate.compareTo(p) > 0) { break;}

        } 
        long durationMs = (System.nanoTime() - start) / 1_000_000;

        if (foundP == null) {
            System.out.println("No factor found (unexpected for tiny keys). Attempts: " + attempts + " time=" + durationMs + "ms" + "  candidate reached: " + candidate);
            return;
        }

        BigInteger foundQ = n.divide(foundP);
        System.out.println("Factorization succeeded after " + attempts + " attempts in " + durationMs + "ms: p='" + foundP + "' q='" + foundQ + "'\n");

        // Reconstruct private key and try to decrypt
        BigInteger recoveredPhi = foundP.subtract(BigInteger.ONE).multiply(foundQ.subtract(BigInteger.ONE));
        BigInteger recoveredD = e.modInverse(recoveredPhi);

        StringBuilder recovered = new StringBuilder();
        for (BigInteger c : ciphertext) {
            int val = c.modPow(recoveredD, n).intValue();
            recovered.append((char) val);
        }

        System.out.println("Original plaintext: '" + plaintext + "' \nRecovered plaintext: '" + recovered + "'\n");
    }

    /**
     * A simple, safe "toy" brute-force loop that demonstrates iteration
     * and progress reporting without performing any real cryptanalysis.
     *
     * This method is intentionally non-actionable: it simply counts attempts
     * and periodically prints progress. Use it as a template for controlled
     * experiments on tiny keys that you own.
     *
     * @param cfg the simulation configuration
     */
    public static void runToySimulation(SimulationConfig cfg) {
        System.out.println("Starting toy simulation: will perform up to " + cfg.maxAttempts + " attempts");

        long reportInterval = Math.max(1, cfg.maxAttempts / 10);
        for (long attempt = 1; attempt <= cfg.maxAttempts; attempt++) {
            // Simulate work (replace this with safe, non-sensitive logic if needed)
            if (attempt % reportInterval == 0 || attempt == cfg.maxAttempts) {
                System.out.printf("[SIM] attempt %d/%d (%.1f%%)%n", attempt, cfg.maxAttempts, attempt * 100.0 / cfg.maxAttempts);
            }
            // Respectful single-threaded pacing so simulations don't overload a system
            try { Thread.sleep(1); } catch (InterruptedException ignored) {}
        }

        System.out.println("Toy simulation finished.");
    }
}
