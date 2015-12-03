/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab1;

import Cipher.AESCipher;
import Properties.AESState;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rental
 */
public class Lab1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            String key = "januaryboyPerris";
            System.out.println("key bytes " + key.getBytes().length);
            String text = "isai es guapo como un camote";
        
            AESCipher cipher = new AESCipher(text, key);
            ArrayList<AESState> encryption = cipher.encrypt();
            String cipherText = AESState.printStates(encryption);
            System.out.println("cipherText - " + cipherText);
            
            
            cipher = new AESCipher(encryption, key);
            ArrayList<AESState> decryption = cipher.decrypt();
            String decryptedText = AESState.printStates(decryption);
            System.out.println("decryptedText - " + decryptedText);
            int isai = 0;
        } catch (Exception ex) {
            Logger.getLogger(Lab1.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
