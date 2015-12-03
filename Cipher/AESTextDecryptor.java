/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cipher;

import Properties.AESByte;
import Properties.AESState;
import Properties.AESTables;
import java.util.ArrayList;

/**
 *
 * @author rental
 */
public class AESTextDecryptor {
    
    private ArrayList<AESState> states;
    private AESKeySchedule keyEncryptor;
    
    public AESTextDecryptor(ArrayList<AESState> states, AESKeySchedule keyEncryptor) {
        this.states = states;
        this.keyEncryptor = keyEncryptor;
    }
    
    public ArrayList<AESState> decrypt() {
        ArrayList<AESState> decryptedStates = new ArrayList<AESState>();
        for(int stateIndex = 0; stateIndex < this.states.size(); stateIndex++) {
            decryptedStates.add(decryptState(stateIndex));
        }
        return decryptedStates;
    }

    private AESState decryptState(int stateIndex) {
        
        AESState currentState = this.states.get(stateIndex);
        currentState = addRoundKey(currentState, this.keyEncryptor.nextKey());
        
        for (int i = 0; i < 9; i++) {
            currentState = invShiftRows(currentState);
            currentState = invSubBytes(currentState);
            currentState = addRoundKey(currentState, this.keyEncryptor.nextKey());
            currentState = invMixColumns(currentState);
        }
        
        currentState = invShiftRows(currentState);
        currentState = invSubBytes(currentState);
        currentState = addRoundKey(currentState, this.keyEncryptor.nextKey());
        
        return currentState;
    }
    
    private AESState addRoundKey(AESState currentState, AESState currentKey) {
        return AESState.xor(currentState, currentKey);
    }

    private AESState invShiftRows(AESState currentState) {
        int matrixLength = 4;
        
        for (int row = 0; row < matrixLength; row++) {
            currentState = invShiftRow(currentState, row);
        }
        return currentState;
    }

    private AESState invShiftRow(AESState currentState, int row) {
        int matrixLength = 4;
        int lastSquareIndex = 3;
        
        for (int time = 0; time < row; time++) {
            AESByte temporal = new AESByte();
            for (int square = matrixLength - 1; square >= 0 ; square--) {
                int rightSquare = square + 1;
                if (square == lastSquareIndex) {
                    temporal = currentState.getMatrix()[row][square];
                } else if (square == 0) { 
                    currentState.setValue(row, rightSquare, temporal);
                } else {
                    currentState.setValue(row, rightSquare, currentState.getMatrix()[row][square]);
                }
            }
            
        }
        
        return currentState;
    }

    private AESState invSubBytes(AESState currentState) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                AESByte value = AESTables.findInvSboxValue(currentState.getMatrix()[i][j]);
                currentState.setValue(i, j, value);
            }
        }
        return currentState;
    }

    private AESState invMixColumns(AESState currentState) {
        return AESState.multiply(currentState, new AESState(AESTables.invMixColumns));
    }
}
