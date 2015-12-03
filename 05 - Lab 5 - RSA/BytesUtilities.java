package rsa;

import java.math.BigInteger;
import java.nio.charset.Charset;

public class BytesUtilities {
    
    
    public static final int BITS_PER_HEXSTRING = 4;
    public static final int BITS_PER_TEXT = 8;
	
    
    // IntArray functions
    public static int[] textToIntArray(String text) {
        byte[] bytes = textToByteArray(text);
        assert(text.length() % 4 == 0);
        int outputLength = text.length() / 4;
        int[] intArray = new int[outputLength];
        for (int i = 0; i < intArray.length; i=i+4) {
        	intArray[i] = bytesToInt(bytes[i], bytes[i+1], bytes[i+2], bytes[i+3]);
        }
        return intArray;
    }
    
    
    public static String IntArrayToText(int[] intArray) {
    	StringBuilder stringBuilder = new StringBuilder();
    	for (int i = 0; i < intArray.length; i++) {
    		byte[] byteArray = intToBytes(intArray[i]);
    		String text = byteArrayToText(byteArray);
    		stringBuilder.append(text);
    	}
    	return stringBuilder.toString();
    }
    
    
    public static int[] hexStringToIntArray(String hexString) {
        if ((hexString.length() % 8) != 0) {
        	System.err.println("hexStringToIntArray is not multiple of 4");
        }
        byte[] bytes = hexStringToByteArray(hexString);
        int outputLength = hexString.length() / 8;
        int[] intArray = new int[outputLength];
        //System.out.println("hexString " + hexString);
        int byteIndex = 0;
        for (int intIndex = 0; intIndex < intArray.length; intIndex++, byteIndex = byteIndex + 4) {
        	intArray[intIndex] = bytesToInt(bytes[byteIndex], bytes[byteIndex+1], bytes[byteIndex+2], bytes[byteIndex+3]);
        	//System.out.println("intArray at " + intIndex + " is " + intToHexString(intArray[intIndex]));
        }
        return intArray;
    }
    
    
    public static String IntArrayToHexString(int[] intArray) {
    	StringBuilder stringBuilder = new StringBuilder();
    	for (int i = 0; i < intArray.length; i++) {
    		byte[] byteArray = intToBytes(intArray[i]);
    		String hexString = byteArrayToHexString(byteArray);
    		stringBuilder.append(hexString);
    	}
    	return stringBuilder.toString();
    }
    
    
    // ByteArray functions
    public static byte[] textToByteArray(String text) {
            return text.getBytes(Charset.forName("UTF8"));//text.getBytes();
    }


    public static String byteArrayToText(byte[] bytes) {
            return new String(bytes);
    }

    
    public static String byteToText(byte inputByte) {
        byte[] array = new byte[1];
        array[0] = inputByte;
        String output = new String(array);
        return output;
    }
     

    public static String byteArrayToHexString(byte[] inputArray) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int index = 0; index < inputArray.length; index++) {
                    String stringByte = byteToHexString(inputArray[index]);
                    stringBuilder.append(stringByte);
            }
            String output = stringBuilder.toString().toUpperCase();
            return output;
    }


    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                 + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }


    // Secondary Functions
    public static String textToHexString(String text) {
            byte[] bytes = textToByteArray(text);
            String hexString = byteArrayToHexString(bytes);
            return hexString;
    }


    public static String byteToHexString(byte inputByte){
            String output = "";

            int unsignedNumber = (inputByte + 256) % 256;
            if((inputByte >= 0) && (inputByte < 16)) {
                    output = output + "0";
            }

            output = output + Integer.toString(unsignedNumber,16);
            return output;
    }


    public static String intToHexString(int integer){
            byte[] bytes = intToBytes(integer);
            String hexString = byteArrayToHexString(bytes);
            return hexString;
    }

	
    public static int bytesToInt(byte byte0, byte byte1, byte byte2, byte byte3) {
        int chunk0 = (byte0 << 24) & 0xff000000;
        int chunk1 = (byte1 << 16) & 0x00ff0000;
        int chunk2 = (byte2 <<  8) & 0x0000ff00;
        int chunk3 = (byte3 <<  0) & 0x000000ff;
        int word = chunk0 | chunk1 | chunk2 | chunk3;
        return word;
    }
    
    
    public static byte[] intToBytes(int integer) {
    	byte[] byteArray = new byte[4];
    	byteArray[0] = (byte)(integer >>> 24);
    	byteArray[1] = (byte)(integer >>> 16);
    	byteArray[2] = (byte)(integer >>>  8);
    	byteArray[3] = (byte)(integer >>>  0);
        return byteArray;
    }
    
    
    // Decimal Strings
    public static String textToBinString(String text) {
        String binString = new BigInteger(text.getBytes()).toString(2);
        return binString;
    }

    
    public static String binStringToText(String binString) {
        String text = new String(new BigInteger(binString, 2).toByteArray());
        return text;
    }
    
    
    public static String textToDecString(String text) {
        String binString = new BigInteger(text.getBytes()).toString(10);
        return binString;
    }

    
    public static String decStringToText(String binString) {
        String text = new String(new BigInteger(binString, 10).toByteArray());
        return text;
    }
    
    
}