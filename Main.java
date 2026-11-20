import RSA.Simulation;
import RSA.SimulationConfig;
import java.util.Scanner;

/**
 * Small demo program demonstrating the RSA helpers and the safe simulation
 * scaffolding. This program:
 *  1) verifies encryption/decryption works locally
 *  2) interactively collects simulation parameters
 *  3) runs a safe toy simulation loop
 */
public class Main {
	public static void main(String[] args) {
		// Run the simplified toy brute-force demo (generates its own tiny key)
		Simulation.runBruteForceDemo();
	}
}
