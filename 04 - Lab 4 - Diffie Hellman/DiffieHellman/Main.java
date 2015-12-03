package DiffieHellman;

import java.math.BigInteger;

public class Main {

	public static void main(String[] args) {
		
		// initial information
	    String text_publicPrimeModulus = "2074722246773485207821695222107608587480996474721117292752992589912196684750549658310084416732550077";
	    String text_publicGenerator = "5";
	    String text_mySecretExponent = "2367495770217142995264827948666809233066409497699870112003149352380375124855230068487109373226251983";
	    
	    // making bigIntegers
	    BigInteger p_publicPrimeModulus = new BigInteger(text_publicPrimeModulus);
	    BigInteger g_publicGenerator = new BigInteger(text_publicGenerator);
	    BigInteger s_mySecretExponent = new BigInteger(text_mySecretExponent);
	    
	    // computing my public result
	    BigInteger myPublicResult = Math.modularExponentiation(g_publicGenerator, s_mySecretExponent, p_publicPrimeModulus);
	    String text_myPublicResult = "104765995999613403064665193352387978633637738110646613084625844911787945600687702766524975555408041";
	    System.out.println("myPublicResult " + myPublicResult.toString());
	    System.out.println("\n");

	    // receiving their public result
	    String text_theirPublicResult = "1514706283146814272451639352196777135539145014616719805707766252371138133446835163331837698701137985";//"1514706283146814272451639352196777135539145014616719805707766252371138133446835163331837698701137985";
	    BigInteger theirPublicResult = new BigInteger(text_theirPublicResult);
	    
	    // computing secret key
	    BigInteger secretKey = Math.modularExponentiation( theirPublicResult, s_mySecretExponent, p_publicPrimeModulus);
	    System.out.println("secret Key " + secretKey.toString());
	    System.out.println("\n");
		
	}
}
