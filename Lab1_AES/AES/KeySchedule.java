package AES;


public class KeySchedule extends AESBase  {

	
	private final int Nb_wordsInBlock;
	private final int Nk_wordsInKey;
	private final int Nr_roundsInKeySchedule;
	private byte[] encryptKey;
	private byte[] decryptKey; 
	
	
	public KeySchedule(byte[][] keyMatrix, int Nb, int Nk, int Nr){
		this.Nb_wordsInBlock = Nb;
		this.Nk_wordsInKey = Nk;
		this.Nr_roundsInKeySchedule = Nr;
		
		encryptKey = new byte[4*Nb_wordsInBlock*(Nr_roundsInKeySchedule+1)];
		decryptKey = new byte[4*Nb_wordsInBlock*(Nr_roundsInKeySchedule+1)];
		keyMatrixToKeyArray(keyMatrix);
	}
	
	
	private void keyMatrixToKeyArray(byte[][] keyMatrix){
		int wordIndex = 0;
		
		while(wordIndex < Nk_wordsInKey) { 
			for(int row = 0; row<4; row++) {
				encryptKey[wordIndex*4 + row] = keyMatrix[row][wordIndex];	
			}
			wordIndex++;
		}
	}
	
	
	private byte[] RotWord(byte[] word){
		byte temp = word[0];
		for(int byteIndex = 1; byteIndex < 4; byteIndex++){
			word[byteIndex - 1] = word[byteIndex];
			
			if(byteIndex == 3) {
				word[byteIndex] = temp;
			}
		}
		
		return word;
	}
	
	
	private byte[] SubWord(byte[] word){
		int leftNumber = 0;
		int rightNumber = 0;
		
		for(int byteIndex = 0; byteIndex < 4; byteIndex++){
			int positiveByte = 0;
			
			if(word[byteIndex] < 0) {
				positiveByte = word[byteIndex] + 256;
			} else {
				positiveByte = word[byteIndex];
			}
			
			leftNumber = (positiveByte / 16 ) % 16;
			rightNumber = positiveByte % 16;
			word[byteIndex] = (byte)SboxTable[leftNumber][rightNumber];	
		}
		
		return word;
	}
	
	
	private void GenerateEncryptKey(){
		
		byte[] temp = new byte[4];
		int i = Nk_wordsInKey;
		
		while(i < Nb_wordsInBlock * (Nr_roundsInKeySchedule + 1)){
			
			// filling up temp
			for(int j =0; j < 4; j++){
				temp[j] = encryptKey[(i - 1) * 4 + j];
			}
			
			if (i % Nk_wordsInKey == 0) {

				RotWord(temp);
				SubWord(temp);
				temp[0] = (byte)(temp[0] ^ (byte)(RconTable[i / Nk_wordsInKey]));
				
			} else if ((Nk_wordsInKey > 6) && (i % Nk_wordsInKey == 4)) {
				SubWord(temp);
			}
			
			int p = i * 4;
			for(int j =0; j < 4; j++){
				encryptKey[p] = (byte)(encryptKey[p - Nk_wordsInKey*4] ^ temp[j]);
				p++;
			}
			
			
			i++;			
		}
	}
	
	
	private void GenerateDecryptKey(){
		
		byte[] temp = new byte[4];
		int i = Nk_wordsInKey;
		
		while(i < Nb_wordsInBlock * (Nr_roundsInKeySchedule + 1)){
			
			for(int j =0; j < 4; j++){
				temp[j] = encryptKey[(i - 1) * 4 + j];
			}
			
			if(i % Nk_wordsInKey == 0){

				RotWord(temp);
				SubWord(temp);
				temp[0] = (byte)(temp[0] ^ (byte)(RconTable[i / Nk_wordsInKey]));
				
			}else if((Nk_wordsInKey > 6) && ( i % Nk_wordsInKey == 4))
				SubWord(temp);
			
			int p = i * 4;
			for(int j =0; j < 4; j++){
				encryptKey[p] = (byte)(encryptKey[p - Nk_wordsInKey*4] ^ temp[j]);
				p++;
			}
			
			
			// decrypting part
			for(int byteIndex = 0; byteIndex < (Nr_roundsInKeySchedule + 1) * Nb_wordsInBlock * 4; byteIndex++) {
				decryptKey[byteIndex] = encryptKey[byteIndex];
			}
			
			for(int round = 1; round < Nr_roundsInKeySchedule; round++) {
				InvMixColumns(round);
			}
			i++;			
		}
	}
	
	private void InvMixColumns(int round){		
		byte[][] partialkeyMatrix = new byte[4][4];
		
		// get partial key as a matrix
		int x = round * 16;
		for(int i = 0; i<4; i++){
			for(int j = 0; j<4; j++){
				partialkeyMatrix[j][i] = decryptKey[x];
				x++;
			}
		}
			
		// mix column
		int y = round * 16;
		for(int i = 0; i<4; i++){
			for(int j = 0; j<4; j++){
				byte sum = 0;
				
				for(int k = 0; k<4; k++){
					sum = (byte)(sum ^ ffMultiply(partialkeyMatrix[k][i], (byte)InvMixColumnTable[j][k]));					
				}
				decryptKey[y] = sum;
				y++;
			}
		}
	}
	
	
	public byte[] nextEncryptKey(){
		GenerateEncryptKey();
		return encryptKey;			
	}
	
	
	public byte[] nextDecryptKey(){
		GenerateDecryptKey();
		return decryptKey;
	}

	
}
