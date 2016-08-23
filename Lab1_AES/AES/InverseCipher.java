package AES;


public class InverseCipher extends AESBase {
	
	
	public void ShiftRows(){
		for(int row = 0; row < 4; row++){
			if(row != 0){
				for(int shiftTimes = row; shiftTimes > 0; shiftTimes--){
					byte temp = state[row][3];
					for(int col = 3; col > 0; col--) {
						state[row][col] = state[row][col - 1];
					}
					state[row][0] = temp;
				}
			}
		}
	}
	
	
	public String Decryption(String inputText, String keyString) {
		init(inputText, keyString);
		
    	AddRoundKey(keySchedule.nextDecryptKey(), Nr_roundsInKeySchedule);
    	
    	for(int round = Nr_roundsInKeySchedule - 1; round > 0; round--){
    		SubBytes(InvSboxTable);
    		ShiftRows();
    		MixColumns(InvMixColumnTable);
    		AddRoundKey(keySchedule.nextDecryptKey(), round);
    	}
    	
    	SubBytes(InvSboxTable);
    	ShiftRows();
    	AddRoundKey(keySchedule.nextDecryptKey(), 0);
    	
    	return MatrixToString(state, Nb_wordsInBlock);
	}
	
	
}
