package HashAttack;

import java.math.BigDecimal;

public class Main {

	
	public static int MAX_PASSWORD_TESTS = 10;
	public static int MAX_COLLISION_TESTS = 10000000;
	
	public static int STRING_MAX_BYTE_LENGHT = 10;
	public static int HASH_MAX_BYTE_LENGTH = 3; // 3 * 8 = 24 bits
	
	
	public static void main(String[] args) {
		Logger logger = new Logger();
		
		// collision attack testing
		logger.startTotalTimer();
		int collisionsCounter = 0;
		double collisionSeconds = 0.0;
		for (int test = 1; test <= MAX_PASSWORD_TESTS; test++) {
			logger.startTestTimer();
			CollisionTester collisionTester = new CollisionTester();
			String password = collisionTester.generateRandomString();
			String passwordHash = collisionTester.getHash(password);
			collisionTester.addPasswordHash(passwordHash);
			for (int collisionTry = 1; collisionTry <= MAX_COLLISION_TESTS; collisionTry++) {
				String randomWord = collisionTester.generateRandomString();
				String collisionHash = collisionTester.getHash(randomWord); 
				Boolean isCollision = collisionTester.checkForCollision(collisionHash);
				if (isCollision == true) {
					logger.stopTestTimer();
					collisionsCounter++;
					collisionSeconds = collisionSeconds + logger.getTestSeconds();
					String log = 
						"There was a collision on test " + test + " out of " + MAX_PASSWORD_TESTS + " tests\n" +
						"after " + collisionTry + " collision tries\n" +
						"User Password: " + password + "\n" +
						"Random Word: " + randomWord + "\n" +
						"Collision Hash: " + collisionHash + "\n" +
						"Time: " + logger.getTestSeconds() + " Seconds\n";
					logger.write(log);
					logger.startTestTimer();
				}
			}
		}
		
		logger.stopTotalTimer();
		BigDecimal allTests = new BigDecimal(MAX_PASSWORD_TESTS * MAX_COLLISION_TESTS);
		BigDecimal successfulCollisions = new BigDecimal(collisionsCounter);
		BigDecimal oneHundred = new BigDecimal(100);
		BigDecimal  probability = oneHundred.multiply(successfulCollisions.divide(allTests));
		String logCollision = "Collision tests finished after " + logger.getTotalSeconds() + " seconds\n" +
		"What is the probability of having two words with the same hash value when that a value has " + (HASH_MAX_BYTE_LENGTH * 8) + " bits\n" +
		"after " + MAX_COLLISION_TESTS + " random Words?\n" +
		"Probability: " + probability.toPlainString() + "%\n" +
		"What is the avarage time to find a collision per word?\n" +
		"Avarage time: " + (collisionSeconds/collisionsCounter) + " Seconds";
		logger.write(logCollision);
		
		
		
		// pre-image attack testing
		logger.startTotalTimer();
		collisionsCounter = 0;
		for (int test = 1; test <= MAX_PASSWORD_TESTS; test++) {
			logger.startTestTimer();
			CollisionTester collisionTester = new CollisionTester();
			String unknownPasswordHash = collisionTester.getHash(collisionTester.generateRandomString());
			collisionTester.addPasswordHash(unknownPasswordHash);
			for (int collisionTry = 1; collisionTry <= MAX_COLLISION_TESTS; collisionTry++) {
				String possiblePreImagePassword = collisionTester.generateRandomString();
				String possibleCollisionHash = collisionTester.getHash(possiblePreImagePassword); 
				Boolean isCollision = collisionTester.checkForCollision(possibleCollisionHash);
				if (isCollision == true) {
					logger.stopTestTimer();
					collisionsCounter++;
					String log = 
						"There was a collision on test " + test + " out of " + MAX_PASSWORD_TESTS + " tests\n" +
						"after " + collisionTry + " collision tries\n" +
						"Unknown Password Hash: " + unknownPasswordHash + "\n" +
						"PreImage Password: " + possiblePreImagePassword + "\n" +
						"Time: " + logger.getTestSeconds() + " Seconds\n";
					logger.write(log);
					logger.startTestTimer();
				}
			}
		}
		
		logger.stopTotalTimer();
		successfulCollisions = new BigDecimal(collisionsCounter);
		probability = oneHundred.multiply(successfulCollisions.divide(allTests));
		String logPreImage = "PreImage tests finished after " + logger.getTotalSeconds() + " seconds\n" +
		"What is the probability of finding a preImage password on an intercepted hash of " + (HASH_MAX_BYTE_LENGTH * 8) + " bits\n" +
		"after " + MAX_COLLISION_TESTS + " random Words?\n" +
		"Probability: " + probability.toPlainString() + "%\n";
		logger.write(logPreImage);
		
	}
}
