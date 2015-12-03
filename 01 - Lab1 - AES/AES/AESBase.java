package AES;

public abstract class AESBase implements AESTables {

	
	protected KeySchedule keySchedule;
	
	protected byte[][] state;
	protected byte[][] key;
	
	protected int Nb_wordsInBlock;
	protected int Nk_wordsInKey;
	protected int Nr_roundsInKeySchedule;
		
	
	protected byte ffAdd(byte firstByte, byte secondByte){
		return (byte)(firstByte ^ secondByte);
	}
	
	
	private static byte xtime(byte inputByte){
		byte outputByte = (byte)(inputByte << 1);
		
		if(inputByte < 0) {
			return (byte)(outputByte ^ (byte)0x1b);
		} else {
			return outputByte;
		}	
	}
	
	
	protected byte ffMultiply(byte firstByte, byte secondByte){
		
		byte xTimeResult = firstByte;
		byte outputByte = 0;
		
		for(int bitIndex = 0; bitIndex < 8; bitIndex++)
		{
			if((secondByte & 0b00000001) == 0b00000001)  
			{
				outputByte = (byte)(outputByte ^ xTimeResult);
			}
			
			xTimeResult = xtime(xTimeResult);
			secondByte = (byte)(secondByte >> 1); // shift one bit to the left
		}
		
		return outputByte;
	}
	
	
	public void AddRoundKey(byte[] keyWord, int round){
		int roundIndex = round * 16;
		for(int col = 0; col < 4; col++) {
			for(int row = 0; row < 4; row++){
				state[row][col] = ffAdd(state[row][col], keyWord[roundIndex]);
				roundIndex++;
			}
		}
	}
	
	
	public void SubBytes(int[][] table){
		
		int left = 0;
		int right = 0;
		
		for(int i = 0; i<4; i++)
			for(int j = 0; j<4; j++){
				int temp = 0;
				if(state[j][i]<0)
					temp = state[j][i] + 256;
				else
					temp = state[j][i];
				
				right = temp%16;
				left = (temp/16)%16;
				
				//state[j][i] = (byte)InvSbox[left][right];		
				state[j][i] = (byte)table[left][right];	
			}
	}
	
	
	public void MixColumns(int[][] table){		
		byte[][] mixColumnsMatrix = new byte[4][4];
		
		// matrix addition
		for(int i = 0; i < 4; i++){    
			for(int j = 0; j < 4; j++){
				byte sum = 0;
				for(int k = 0; k < 4; k++){	
					sum = ffAdd(sum, ffMultiply(state[k][i], (byte)table[j][k]));
				}			
				mixColumnsMatrix[j][i] = sum;
			}
		}
	
		//replace state by new values
		for(int col = 0; col < 4; col++) {
			for(int row = 0; row < 4; row++) {
				state[col][row] = mixColumnsMatrix[col][row];
			}
		}
	}
	
	
	public void init(String inputText, String keyString) {
		
		Nb_wordsInBlock = 4;
		
		Nk_wordsInKey = keyString.length()/8;  
		
		if (Nk_wordsInKey == 4) {
			Nr_roundsInKeySchedule = 10;
		} else if(Nk_wordsInKey == 6) {
			Nr_roundsInKeySchedule = 12;
		} else if (Nk_wordsInKey == 8) {
			Nr_roundsInKeySchedule = 14;
		}
		
		this.state = TextToMatrix(inputText);
		this.key = KeyToMatrix(keyString); 
		
		this.keySchedule = new KeySchedule(key, Nb_wordsInBlock, Nk_wordsInKey, Nr_roundsInKeySchedule);
	}
	
	
	protected void PrintMatrix(byte[][] state, int columns){
		System.out.print(MatrixToString(state, columns));
	}
	
	
	protected String MatrixToString(byte[][] state, int Columns) {
		StringBuilder stringBuilder = new StringBuilder("");
		for(int col = 0; col < Columns; col++){
			for(int row = 0; row < 4; row++) {
				stringBuilder.append(ConvertHexNumberToString(state[row][col]));
			}
		}
		return stringBuilder.toString();
	}
	
	
	protected String ConvertHexNumberToString(byte inputByte){
		String output = "";
		
		int unsignedNumber = (inputByte+ 256)%256;
		if((inputByte >= 0) && (inputByte < 16)) {
			output = output + "0";
		}
		
		output = output + Integer.toString(unsignedNumber,16);
		return output;
	}
	
	
	private byte[][] TextToMatrix(String inputText){
		byte[][] bigMatrix = new byte[4][Nk_wordsInKey];
		
		for(int col = 0; col < Nk_wordsInKey; col++)
		{
			int textIndex = col*8;
			for(int row = 0; row < 4; row++)
			{
				int hexNumber=0; 
				
				if(textIndex < inputText.length()) 
				{
					String hexString = inputText.substring(textIndex, textIndex+2);
					hexNumber = Integer.parseInt(hexString, 16); 
				}
				
				bigMatrix[row][col] = (byte)hexNumber;
				textIndex = textIndex + 2;
			}
		}
		
		return bigMatrix;
	}
	
	
	private byte[][] KeyToMatrix(String keyString)
	{
		// appending two chars to make a byte
		char[] keyCharArray = keyString.toCharArray();
		String[] keyStringArray = new String[keyCharArray.length/2];
		int stringArrayIndex = 0;
		for(int charArrayIndex = 0; charArrayIndex < keyCharArray.length; charArrayIndex++)
		{
			char a = keyCharArray[charArrayIndex];
			charArrayIndex++;
			char b = keyCharArray[charArrayIndex];
			keyStringArray[stringArrayIndex] = "" + a + b;
			stringArrayIndex++;
		}
		
		
		byte[][] outputMatrix = new byte[4][Nk_wordsInKey];
		stringArrayIndex = 0;
		for(int col = 0; col < Nk_wordsInKey; col++)
		{
			for(int row = 0; row < 4; row++)
			{
				int hexadecimal = 16;
				outputMatrix[row][col] = (byte)Integer.parseInt(keyStringArray[stringArrayIndex],hexadecimal);
				stringArrayIndex++;
			}
		}
		
		return outputMatrix;
	}
	
	
}
