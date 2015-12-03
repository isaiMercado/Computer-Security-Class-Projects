
package rsa;

import java.math.BigInteger;

public class Main {
    
    public static Boolean DEBUG = true; 
    
    public static void main(String[] args) {

        BigInteger p_firstPrimeNumber = new BigInteger("6997040548236306032270028142697844577679802860009390483825615834396766817911865846408328563633463004219619254639348141491179371158391819359706834377936017");
        BigInteger q_secondPrimeNumber = new BigInteger("8593701979078313474945043728666349994614714557320430419781846880808258697302068260916571720507008823663836468130075453764283766701743551377885890781118909");
        BigInteger phiN = RSA.getPhiN(p_firstPrimeNumber, q_secondPrimeNumber);
        BigInteger n_publicModulus = RSA.getPublicModulus(p_firstPrimeNumber, q_secondPrimeNumber);
        BigInteger e_publicKey = new BigInteger("65537");
        BigInteger d_privateKey = RSA.getPrivateKey(phiN, e_publicKey);
        
        if (RSA.isCoprime(phiN, e_publicKey)) {
            
            System.out.println("p: " + p_firstPrimeNumber);
            System.out.println("q: " + q_secondPrimeNumber);
            System.out.println("phiN: " + phiN);
            System.out.println("n: " + n_publicModulus);
            System.out.println("e: " + e_publicKey);
            System.out.println("d: " + d_privateKey);
            
            String m_messageText = "isai deserves A+ in this class!!!";
            BigInteger m_messageNumber = new BigInteger(BytesUtilities.textToDecString(m_messageText));
            
            System.out.println("m_messageText " + m_messageText);
            System.out.println("m_messageNumber " + m_messageNumber);
            
            BigInteger c_cipherNumber = RSA.encrypt(m_messageNumber, e_publicKey, n_publicModulus);
            
            System.out.println("c_cipherNumber " + c_cipherNumber);
            
            BigInteger m_decryptedNumber = RSA.decrypt(c_cipherNumber, d_privateKey, n_publicModulus);
            String m_decryptedText = BytesUtilities.decStringToText(m_decryptedNumber.toString());
            
            System.out.println("m_decryptedNumber " + m_decryptedNumber);
            System.out.println("m_decryptedText " + m_decryptedText);
            
            System.out.println("\n");
            
            // Autograder
            BigInteger toEncrypt = new BigInteger("212788291171290661843158364587637197333531097895143818988755059744299426205080018037256906030614828346200464605058471107535343001238694433356829682872");
            System.out.println("toEncrypt " + toEncrypt);
            BigInteger encrypted = RSA.encrypt(toEncrypt, e_publicKey, n_publicModulus);
            System.out.println("encrypted " + encrypted);
            
            BigInteger toDecrypt = new BigInteger("38836004958973333808444474744049950597152340928568277009922464771958929445481001650323472151575854567252425265205811145901606099326089029141609035935574695159638546746822086067633268843681771212436983924976927634925742602911873891599802616821582340073810951502143926279012766864221984005313249745229167595735");
            System.out.println("toDecrypt " + toDecrypt);
            BigInteger decrypted = RSA.decrypt(toDecrypt, d_privateKey, n_publicModulus);
            System.out.println("decrypted " + decrypted);
            
            System.out.println("\n");
            
        } else {
            
            System.err.println("try other p and q prime numbers");
            
        }

    }
    
}
