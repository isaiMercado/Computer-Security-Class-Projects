package AES;


public class AES_Main {

	public static void main(String[] args) {
		String textToEncrypt = "00112233445566778899aabbccddeeff";
		System.out.println("textToEncrypt " + textToEncrypt + "\n");
		
		//String key = "000102030405060708090a0b0c0d0e0f";
		//String key = "000102030405060708090a0b0c0d0e0f1011121314151617";
		String key = "000102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f";
		System.out.println("key " + key + "\n");
		
		Cipher cipher = new Cipher();
		String textEncrypted = cipher.Encryption(textToEncrypt, key);
		System.out.println("textEncrypted " + textEncrypted + "\n");
		
		InverseCipher inverseCipher = new InverseCipher();
		String textDecrypted = inverseCipher.Decryption(textEncrypted, key);
		System.out.println("textDecrypted " + textDecrypted + "\n");
	} 
	
}
