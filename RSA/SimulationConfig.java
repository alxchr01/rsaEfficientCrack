package RSA;

/**
 * Configuration data for a controlled RSA brute-force simulation.
 *
 * IMPORTANT: This class is intended for *safe* simulations and experiments
 * on toy keys or locally generated test material ONLY. Never use simulation
 * code against systems you do not own or have explicit permission to test.
 */
public class SimulationConfig {
    /** Key size in bits to generate for the simulation (e.g. 512) */
    public final int keySizeBits;

    /** Plaintext message to encrypt for the simulation */
    public final String plaintext;

    /** Maximum number of candidate attempts the simulation should perform */
    public final long maxAttempts;

    /** Whether to use tiny toy keys (very insecure) to enable factorization */
    public final boolean useToyKeys;

    public SimulationConfig(int keySizeBits, String plaintext, long maxAttempts, boolean useToyKeys) {
        this.keySizeBits = keySizeBits;
        this.plaintext = plaintext;
        this.maxAttempts = maxAttempts;
        this.useToyKeys = useToyKeys;
    }
}
