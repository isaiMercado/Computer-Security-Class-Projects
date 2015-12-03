package rsa;

import java.math.BigInteger;

public class Utilities {

    
    static ExtendedEuclideanResult extendedEuclidean(BigInteger firstNumber, BigInteger secondNumber) {
        ExtendedEuclideanResult output = new ExtendedEuclideanResult();
        BigInteger ZERO = BigInteger.ZERO;
        BigInteger ONE = BigInteger.ONE;
        
        if (secondNumber.equals(ZERO)) {
            output.greatestCommonFactor = firstNumber;
            output.coefficientFirstNumber = ONE;
            output.coefficientSecondNumber = ZERO;
        } else {
            ExtendedEuclideanResult values = extendedEuclidean(secondNumber, Utilities.mod(firstNumber, secondNumber));
            output.greatestCommonFactor = values.greatestCommonFactor;
            output.coefficientFirstNumber = values.coefficientSecondNumber;
            output.coefficientSecondNumber = values.coefficientFirstNumber.subtract(firstNumber.divide(secondNumber).multiply(values.coefficientSecondNumber));
        }
        
        return output;
    }
      
    
    private static BigInteger mod(BigInteger numberToMod, BigInteger modulo)
    {
        BigInteger modularResult = numberToMod.mod(modulo);
        Boolean isModularResultNegative = (modularResult.compareTo(BigInteger.ZERO) == -1);

        if (isModularResultNegative) 
        {
        	modularResult = modularResult.add(modulo);
        }
        
        return modularResult;
    }


    public static BigInteger modularExponentiation(BigInteger base, BigInteger exponent, BigInteger modulo)
    {
    	BigInteger TWO = new BigInteger("2");
    	BigInteger ZERO = BigInteger.ZERO;
    	BigInteger ONE = BigInteger.ONE;
    	
    	Boolean exponentIsZero = (exponent.compareTo(ZERO) == 0);
    	Boolean exponentIsEven = (mod(exponent, TWO) == ZERO);
    	
        if (exponentIsZero) 
        {
        	BigInteger nextBase = ONE;
            return nextBase;
        }
        else if (exponentIsEven)
        {
            BigInteger exponentHalf = exponent.divide(TWO);
            BigInteger nextBaseRaw = modularExponentiation(base, exponentHalf, modulo);
            BigInteger nextBaseSquared = nextBaseRaw.pow(2);
            BigInteger nextBaseMod = mod(nextBaseSquared , modulo);
            BigInteger nextBase = nextBaseMod;
            return nextBase;
        }
        else
        {
            BigInteger exponentMinusOne = exponent.subtract(ONE);
            BigInteger nextBaseSolvablePart = mod(base, modulo);
            BigInteger nextBaseUnsolvablePart = modularExponentiation(base, exponentMinusOne, modulo);
            BigInteger nextBaseMod = mod(nextBaseSolvablePart.multiply(nextBaseUnsolvablePart), modulo);
            BigInteger nextBase = nextBaseMod;
            return nextBase;
        }
    }
    
    
    public static class ExtendedEuclideanResult {
        public BigInteger greatestCommonFactor;
        public BigInteger coefficientFirstNumber;
        public BigInteger coefficientSecondNumber;
    }
}