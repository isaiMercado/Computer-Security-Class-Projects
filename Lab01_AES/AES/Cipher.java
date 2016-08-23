package AES;


public class Cipher extends AESBase {
	
	
	public void ShiftRows(){
		for(int row = 0; row < 4; row++){
			if(row != 0) {  
				for(int shiftTimes = row; shiftTimes > 0; shiftTimes--){
					byte temp = state[row][0];
					for(int col = 1; col < 4; col++) {
						state[row][col - 1] = state[row][col];
					}
					state[row][3] = temp;
				}
			}
		}
	}
	
	
	public String Encryption(String inputText, String keyString) {
		init(inputText, keyString);
		
        AddRoundKey(keySchedule.nextEncryptKey(), 0);
        
        for(int round = 1; round < Nr_roundsInKeySchedule; round++){
        	SubBytes(SboxTable);
        	ShiftRows();
        	MixColumns(MixColumnTable);
        	AddRoundKey(keySchedule.nextEncryptKey(), round);
        }
        
        SubBytes(SboxTable);
    	ShiftRows();
    	AddRoundKey(keySchedule.nextEncryptKey(), Nr_roundsInKeySchedule);
    	
    	return MatrixToString(state, Nb_wordsInBlock);
	}
	
	
	
}
