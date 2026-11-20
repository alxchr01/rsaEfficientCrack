package RSA;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;

/**
 * Simple RSA decryption helper matching the behavior of {@link RSAEncrypt}.
 * <p>
 * This class performs textbook RSA decryption using the private exponent
 * and modulus to recover character values that were encrypted individually.
 * Because no padding is used, this should only be used for learning or testing.
 */
public class RSADecrypt {

    public static String decryptCmnd(BigInteger[] message, PrivateKey privateKey) {
        // Decrypt to a char array and build a Java String from it
        String ciphertextS = "";
        char[] ciphertextCh = decrypt(message, privateKey);

        // Append characters (for small messages this simple concatenation is fine)
        for (char ch: ciphertextCh) {
            ciphertextS += ch;
        }

        return ciphertextS;
    }

    private static char[] decrypt(BigInteger[] ciphertext, PrivateKey privateKey) {
        // Extract the private exponent and modulus from the RSAPrivateKey
        BigInteger exponent = ((RSAPrivateKey) privateKey).getPrivateExponent();
        BigInteger modulus = ((RSAPrivateKey) privateKey).getModulus();

        // Prepare array that will hold the decrypted characters
        char[] decrypted = new char[ciphertext.length];

        // For each BigInteger block, compute m = c^d mod n and convert to char
        for (int i = 0; i < ciphertext.length; i++) {
            decrypted[i] = (char) ciphertext[i].modPow(exponent, modulus).intValue();
        }

        return decrypted;
    }
}
