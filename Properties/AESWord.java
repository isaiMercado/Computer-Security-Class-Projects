/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Properties;

/**
 *
 * @author rental
 */
public class AESWord {
    
    public static AESWord xor(AESWord firstWord, AESWord secondWord) {
        AESWord output = new AESWord();
        for (int i = 0; i < 4; i++) {
            output.getBytes()[i] = AESByte.xor(firstWord.getBytes()[i], secondWord.getBytes()[i]);
        }
        return output;
    }
    
    private int WORD_LENGTH = 4;
    private AESByte[] bytes = new AESByte[4];
    
    public AESWord(AESByte[] bytes) throws Exception {
        if (bytes.length == this.WORD_LENGTH) {
            this.bytes = bytes;
        } else {
            throw new Exception("more bytes than a word can contain");
        }
    }
    
    public AESWord(AESByte byte0, AESByte byte1, AESByte byte2, AESByte byte3) {
        this.bytes[0] = byte0;
        this.bytes[1] = byte1;
        this.bytes[2] = byte2;
        this.bytes[3] = byte3;
    }
    
        public AESWord() {
        this.bytes[0] = new AESByte();
        this.bytes[1] = new AESByte();
        this.bytes[2] = new AESByte();
        this.bytes[3] = new AESByte();
    }

    /**
     * @return the bytes
     */
    public AESByte[] getBytes() {
        return bytes;
    }

    /**
     * @param bytes the bytes to set
     */
    public void setBytes(AESByte[] bytes) {
        this.bytes = bytes;
    }
}
