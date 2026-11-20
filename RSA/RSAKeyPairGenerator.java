package RSA;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

/**
 * Utility to generate an RSA key pair using the JCA KeyPairGenerator.
 * <p>
 * This class wraps the standard KeyPairGenerator and retains references
 * to the generated public and private keys for later use by encrypt/decrypt
 * utilities in this project.
 */
public class RSAKeyPairGenerator {
    private PublicKey publicKey;
    private PrivateKey privateKey;

    /**
     * Generate an RSA key pair of the given size.
     *
     * @param keySize the key size in bits (e.g. 2048)
     * @throws Exception when the RSA algorithm is not available or generation fails
     */
    public RSAKeyPairGenerator(int keySize) throws Exception {
        // Create a KeyPairGenerator for RSA and initialize with secure randomness
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(keySize, new SecureRandom());

        // Generate the key pair and store the public/private keys for later retrieval
        KeyPair pair = keyGen.generateKeyPair();
        this.publicKey = pair.getPublic();
        this.privateKey = pair.getPrivate();
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }
}