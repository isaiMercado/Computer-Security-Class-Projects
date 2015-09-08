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
public class AESTextEncryptor {
   
    private ArrayList<AESState> states;
    private AESKeySchedule keySchedule;
    
    public AESTextEncryptor(ArrayList<AESState> states, AESKeySchedule keySchedule) {
        this.states = states;
        this.keySchedule = keySchedule;
    }
    
    public ArrayList<AESState> encrypt() {
        ArrayList<AESState> encryptedStates = new ArrayList<AESState>();
        for(int stateIndex = 0; stateIndex < this.states.size(); stateIndex++) {
            encryptedStates.add(encryptState(stateIndex));
        }
        return encryptedStates;
    }

    private AESState encryptState(int stateIndex) {
        AESState currentState = this.states.get(stateIndex);
        //addRoundKey
        currentState = addRoundKey(currentState, keySchedule.nextKey());
        
        for (int i = 0; i < 9; i++) {
            //SubBytes
            currentState = subBytes(currentState);
            //ShiftRows
            currentState = shiftRows(currentState);
            // Mix Columns
            currentState = mixColumns(currentState);
            // Add Round Key
            currentState = addRoundKey(currentState, keySchedule.nextKey());
        }
        
        //SubBytes
        currentState = subBytes(currentState);
        //ShiftRows
        currentState = shiftRows(currentState);
        // Addround key
        currentState = addRoundKey(currentState, keySchedule.nextKey());
        
        return currentState;
    }

    private AESState addRoundKey(AESState currentState, AESState currentKey) {
        return AESState.xor(currentState, currentKey);
    }

    private AESState subBytes(AESState currentState) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                AESByte value = AESTables.findSboxValue(currentState.getMatrix()[i][j]);
                currentState.setValue(i, j, value);
            }
        }
        return currentState;
    }



    private AESState shiftRows(AESState currentState) {
        int matrixLength = 4;
        
        for (int row = 0; row < matrixLength; row++) {
            currentState = shiftRow(currentState, row);
        }
        return currentState;
    }

    private AESState shiftRow(AESState currentState, int row) {
        int matrixLength = 4;
        int lastSquareIndex = 3;
        
        for (int time = 0; time < row; time++) {
            AESByte temporal = new AESByte();
            for (int square = 0; square < matrixLength ; square++) {
                int leftSquare = square - 1;
                if (square == 0) {
                    temporal = currentState.getMatrix()[row][square];
                } else if (square == lastSquareIndex) { 
                    currentState.setValue(row, leftSquare, temporal);
                } else {
                    currentState.setValue(row, leftSquare, currentState.getMatrix()[row][square]);
                }
            }
            
        }
        
        return currentState;
    }

    private AESState mixColumns(AESState currentState) {
        return AESState.multiply(currentState, new AESState(AESTables.mixColumns));
    }




}
