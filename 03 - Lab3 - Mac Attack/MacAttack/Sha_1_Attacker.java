package MacAttack;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.util.ArrayList;

public class Sha_1_Attacker {
	
	
    static String getHexStringModifiedMessage(String text_keyPlaceHolderAndInterceptedMessage,int keyPlaceHolderAndInterceptedMessageBitsLength, String text_extensionMessage) {
        Sha_1 sha1 = new Sha_1();
        int[][] blocks_initialMessage = sha1.parseMessage(text_keyPlaceHolderAndInterceptedMessage, keyPlaceHolderAndInterceptedMessageBitsLength);
        
        String hexString_keyPlaceHolderAndInterceptedMessageWithPadding = sha1.blocksToString(blocks_initialMessage);
        String hexString_extensionMessage = HexUtility.textToHexString(text_extensionMessage);
        
        String hexString_modifiedMessage = hexString_keyPlaceHolderAndInterceptedMessageWithPadding + hexString_extensionMessage;
        return hexString_modifiedMessage;
    }


	public static String getHexStringModifiedMac(String text_extensionMessage, int modifiedMessageBitsLength, int[] hashInitialState) {
		Sha_1 sha1 = new Sha_1();
		String hexString_modifiedMac = sha1.hash(text_extensionMessage, modifiedMessageBitsLength, hashInitialState);
		return hexString_modifiedMac;
	}


}
