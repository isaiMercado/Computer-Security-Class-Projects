package HashAttack;

import java.security.MessageDigest;
import java.util.Random;

public class CollisionTester {
	
	
	private String currentPasswordHash;
	private Random randomGenerator;
	
	
	public String convertByteToString(byte inputByte){
		String output = "";
		
		int unsignedNumber = (inputByte + 256) % 256;
		if((inputByte >= 0) && (inputByte < 16)) {
			output = output + "0";
		}
		
		output = output + Integer.toString(unsignedNumber,16);
		return output;
	}
	
	
	public String convertBytesToString(byte[] inputArray) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int index = 0; index < inputArray.length; index++) {
			String stringByte = convertByteToString(inputArray[index]);
			stringBuilder.append(stringByte);
		}
		String output = "0x" + stringBuilder.toString().toUpperCase();
		return output;
	}
	
	
	public String generateRandomString() {
		byte[] randomBytes = new byte[Main.STRING_MAX_BYTE_LENGHT];
		randomGenerator.nextBytes(randomBytes);
		String randomString = convertBytesToString(randomBytes);
		return randomString;
	}
	
	
	public String getHash(String inputString) {
		String stringHash = null;
		try {
		    MessageDigest hashEncrypter = MessageDigest.getInstance("SHA-1");
		    hashEncrypter.reset();
		    hashEncrypter.update(inputString.getBytes());
		    byte[] largeHash = hashEncrypter.digest();
		    // cutting hash to a more testing length
		    byte[] smallHash = new byte[Main.HASH_MAX_BYTE_LENGTH];
		    if (largeHash.length > Main.HASH_MAX_BYTE_LENGTH) {
			    for (int i = 0; i < Main.HASH_MAX_BYTE_LENGTH; i++) {
			    	smallHash[i] = largeHash[i];
			    }
		    } else {
		    	smallHash = largeHash;
		    }
		    // transforming hash to String for logging
		    stringHash = convertBytesToString(smallHash);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stringHash;
	}
	
	
	public CollisionTester() {
		currentPasswordHash = new String();
		randomGenerator = new Random();
	}
	
	
	public Boolean checkForCollision(String otherHash) {
		Boolean collision = currentPasswordHash.equals(otherHash);
		return collision;
	}
	
	
	public void addPasswordHash(String passwordHash) {
		currentPasswordHash = passwordHash;
	}
	
	
}
