package rsa;

import java.math.BigInteger;

public class RSA {

    public static BigInteger getPhiN(BigInteger p_firstPrimeNumber, BigInteger q_secondPrimeNumber) {
        BigInteger ONE = BigInteger.ONE;
        BigInteger output = p_firstPrimeNumber.subtract(ONE).multiply(q_secondPrimeNumber.subtract(ONE));
        return output;
    }
    
    public static BigInteger getPublicModulus(BigInteger p_firstPrimeNumber, BigInteger q_secondPrimeNumber) {
        BigInteger output = p_firstPrimeNumber.multiply(q_secondPrimeNumber);
        return output;
    }
    
    public static BigInteger getPrivateKey(BigInteger phiN, BigInteger e_publicKey) {
        Utilities.ExtendedEuclideanResult euclidResult_phiN_e = Utilities.extendedEuclidean(phiN, e_publicKey);
        BigInteger output = euclidResult_phiN_e.coefficientSecondNumber;
        return output;
    }
    
    public static BigInteger encrypt(BigInteger m_message, BigInteger e_publicKey, BigInteger n_publicModulus) {
        BigInteger c_cipherNumber = Utilities.modularExponentiation(m_message, e_publicKey, n_publicModulus);
        return c_cipherNumber;
    }
    
    public static BigInteger decrypt(BigInteger c_cipherText, BigInteger d_privateKey, BigInteger n_publicModulus) {
        BigInteger m_decipherNumber = Utilities.modularExponentiation(c_cipherText, d_privateKey, n_publicModulus);
        return m_decipherNumber;
    }
    
    public static Boolean isCoprime(BigInteger biggerNumber, BigInteger smallerNumber) {
        Utilities.ExtendedEuclideanResult result = Utilities.extendedEuclidean(biggerNumber, smallerNumber);
        Boolean output = result.greatestCommonFactor.equals(BigInteger.ONE);
        return output;
    }

}