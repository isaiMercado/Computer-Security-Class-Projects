package MacAttack;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

	public static void main(String[] args) {
		
        // this is what we need for a length extension attack
        String text_interceptedMessage = "No one has completed lab 2 so give them all a 0"; // This was intercepted
        String hexString_interceptedMac = "f4b645e89faaec2ff8e443c595009c16dbdfba4b"; // This was intercepted 
        String hashAlgorithm = "SHA-1"; // The algorithm is guessed by looking at the initalMac's length. In this case the length is 20 bytes which usually come from SHA-1
        int keyBitsLength = 128; // The key length was obtained by some other means
        String text_extensionMessage = "P.S. Except for Isai. He deserves 100% on all Projects"; // We make this extension text
           
        
        
        // preparing variables for attack
        String text_keyPlaceHolder = "333333333333333?"; 
        String text_keyPlaceHolderAndInterceptedMessage = text_keyPlaceHolder + text_interceptedMessage;
        int keyPlaceHolderAndInterceptedMessageBitsLength = text_keyPlaceHolderAndInterceptedMessage.length() * HexUtility.BITS_PER_TEXT;
        int[] hashInitialState = HexUtility.hexStringToIntArray(hexString_interceptedMac);
        
        
        
        // attack
        String hexString_modifiedMessage = Sha_1_Attacker.getHexStringModifiedMessage(text_keyPlaceHolderAndInterceptedMessage, keyPlaceHolderAndInterceptedMessageBitsLength, text_extensionMessage); 
        int modifiedMessageBitsLength = hexString_modifiedMessage.length() * HexUtility.BITS_PER_HEXSTRING;
        String hexString_modifiedMac = Sha_1_Attacker.getHexStringModifiedMac(text_extensionMessage, modifiedMessageBitsLength,hashInitialState);
        
        
        
        // print results
        System.out.println("hexString modifiedMessage " + hexString_modifiedMessage);
        System.out.println("hexString modifiedMac " + hexString_modifiedMac);
        System.out.println("\n");  
        
	} 
}
