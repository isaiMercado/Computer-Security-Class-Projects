/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cipher;

import Properties.AESByte;
import Properties.AESState;
import Properties.AESWord;
import java.util.ArrayList;

/**
 *
 * @author rental
 */
public class AESCipher {
    private String text = new String();
    private ArrayList<AESByte> textByteChunks = new ArrayList<AESByte>();
    private ArrayList<AESWord> textWordChunks = new ArrayList<AESWord>();
    private ArrayList<AESState> textStateChunks = new ArrayList<AESState>();
    
    private String key;
    private ArrayList<AESByte> keyByteChunks = new ArrayList<AESByte>();
    private ArrayList<AESWord> keyWordChunks = new ArrayList<AESWord>();
    private AESState keyInitialState = new AESState();
    
    
    public AESCipher(ArrayList<AESState> states, String key) throws Exception {
        if (key.getBytes().length == 16) {
            this.key = key;
            
            byte[] keyBytes = key.getBytes();
            for (int i = 0; i < keyBytes.length; i++) {
                this.keyByteChunks.add(new AESByte(keyBytes[i]));
            }
            
            for (int i = 0; i < this.keyByteChunks.size() - 3; i+=4) {
                this.keyWordChunks.add(new AESWord(this.keyByteChunks.get(i), this.keyByteChunks.get(i+1), this.keyByteChunks.get(i+2), this.keyByteChunks.get(i+3)));
            }
            
            this.keyInitialState = new AESState(this.keyWordChunks.get(0), this.keyWordChunks.get(1), this.keyWordChunks.get(2), this.keyWordChunks.get(3));
            
            this.textStateChunks = states;
        } else {
            throw new Exception();
        }
    }
            
    public AESCipher(String text, String key) throws Exception {
        if (key.getBytes().length == 16) {
            this.text = text;
            this.key = key;
            
            byte[] keyBytes = key.getBytes();
            for (int i = 0; i < keyBytes.length; i++) {
                this.keyByteChunks.add(new AESByte(keyBytes[i]));
            }
            
            for (int i = 0; i < this.keyByteChunks.size() - 3; i+=4) {
                this.keyWordChunks.add(new AESWord(this.keyByteChunks.get(i), this.keyByteChunks.get(i+1), this.keyByteChunks.get(i+2), this.keyByteChunks.get(i+3)));
            }
            
            this.keyInitialState = new AESState(this.keyWordChunks.get(0), this.keyWordChunks.get(1), this.keyWordChunks.get(2), this.keyWordChunks.get(3));

            byte[] textBytes = text.getBytes();
            for (int i = 0; i < textBytes.length; i++) {
                this.textByteChunks.add(new AESByte(textBytes[i]));
            }

            while((this.textByteChunks.size() % 16) != 0) {
                this.textByteChunks.add(new AESByte());
            }

            for (int i = 0; i < this.textByteChunks.size() - 3; i+=4) {
                this.textWordChunks.add(new AESWord(this.textByteChunks.get(i), this.textByteChunks.get(i+1), this.textByteChunks.get(i+2), this.textByteChunks.get(i+3)));
            }

            for (int i = 0; i < this.textWordChunks.size() - 3; i+=4) {
                this.textStateChunks.add(new AESState(this.textWordChunks.get(i),this.textWordChunks.get(i+1), this.textWordChunks.get(i+2), this.textWordChunks.get(i+3)));
            }
        } else {
            throw new Exception("key length is incorrect");
        }
    }
    
    public ArrayList<AESState> encrypt() {
        AESKeySchedule keySchedule = new AESKeySchedule(keyInitialState);
        AESTextEncryptor textEncryptor = new AESTextEncryptor(textStateChunks, keySchedule);
        return textEncryptor.encrypt();
    }
    
        public ArrayList<AESState> decrypt() {
        AESKeySchedule keySchedule = new AESKeySchedule(keyInitialState);
        AESTextDecryptor textDecryptor = new AESTextDecryptor(textStateChunks, keySchedule);
        return textDecryptor.decrypt();
    }
    

}
