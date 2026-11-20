package RSA;
import java.math.BigInteger;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;

/**
 * Simple RSA encryption helper.
 * <p>
 * NOTE: This implementation operates on single characters and applies
 * textbook RSA (m^e mod n) to each character value. It does NOT use
 * any padding (e.g. OAEP) and therefore is insecure for production use.
 * It is intended for educational purposes or small experiments.
 */
public class RSAEncrypt {
    public static String encryptCmnd(String message, PublicKey publicKey) {
        // Encrypt the message into an array of BigInteger values
        StringBuilder ciphertextS = new StringBuilder();
        BigInteger[] ciphertextBI = encrypt(message, publicKey);

        // Join the BigInteger decimal representations with semicolons for simple transport
        for (int i = 0; i < ciphertextBI.length; i++) {
            ciphertextS.append(ciphertextBI[i].toString()).append(";");
        }

        // Remove trailing separator if present
        if (ciphertextS.length() > 0 && ciphertextS.charAt(ciphertextS.length() - 1) == ';') {
            ciphertextS.setLength(ciphertextS.length() - 1);
        }

        return ciphertextS.toString();
    }

    private static BigInteger[] encrypt(String message, PublicKey publicKey) {
        // Convert message to character codes (simple 1-byte per char mapping)
        char[] chars = message.toCharArray();
        int[] ascii = new int[chars.length];
        for (int i = 0; i < chars.length; i++) {
            ascii[i] = (int) chars[i];
        }

        // Prepare result array
        BigInteger[] encrypted = new BigInteger[ascii.length];

        // Extract public exponent and modulus from the provided RSAPublicKey
        BigInteger exponent = ((RSAPublicKey) publicKey).getPublicExponent();
        BigInteger modulus = ((RSAPublicKey) publicKey).getModulus();

        // Encrypt each character value: c = m^e mod n
        for (int i = 0; i < ascii.length; i++) {
            encrypted[i] = BigInteger.valueOf(ascii[i]).modPow(exponent, modulus);
        }

        return encrypted;
    }

}
