/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cipher;

import Properties.AESByte;
import Properties.AESByte;
import Properties.AESState;
import Properties.AESState;
import Properties.AESTables;
import Properties.AESTables;
import Properties.AESWord;
import Properties.AESWord;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rental
 */
public class AESKeySchedule {
    
    private AESState currentState;
    private int RconIndex;
    
     public AESKeySchedule(AESState initialState) {
         this.currentState = initialState;
         this.RconIndex = 1;
     }
     
     public AESState nextKey() {
         AESState nextState = new AESState();
         try {
            int indexLastColumn = 3;
            AESWord RconColumn = new AESWord(new AESByte((byte)AESTables.Rcon[RconIndex]), new AESByte(), new AESByte(), new AESByte());
            RconIndex++;
            
            AESWord lastColumnValues = currentState.getColumn(indexLastColumn); 
            lastColumnValues = shiftContent(lastColumnValues);
            lastColumnValues = switchSboxValue(lastColumnValues);
            lastColumnValues = AESWord.xor(lastColumnValues, RconColumn);
            
            nextState.setColumn(0, lastColumnValues);
            
            for (int col = 0; col < 3; col++) {
                AESWord currentStateColumn = this.currentState.getColumn(col + 1);
                AESWord nextStateColumn = nextState.getColumn(col);
                AESWord xorResult = AESWord.xor(currentStateColumn, nextStateColumn);
                nextState.setColumn(col + 1, xorResult);
            }
            
         } catch (Exception ex) {
             Logger.getLogger(AESWord.class.getName()).log(Level.SEVERE, null, ex);
         }
         
         this.currentState = nextState;
         return nextState;
     }

    private AESWord shiftContent(AESWord lastColumn) {
        AESByte temporal = new AESByte();
        int numberOfBytesInWord = 4;
        for (int index = 0; index < numberOfBytesInWord; index++) {
            if (index == 0) {
                temporal = lastColumn.getBytes()[index];
            } else if (index == numberOfBytesInWord - 1) {
                lastColumn.getBytes()[index] = temporal;
            } else {
                lastColumn.getBytes()[index - 1] = lastColumn.getBytes()[index];
            }
        }
        return lastColumn;
    }

    private AESWord switchSboxValue(AESWord lastColumn) {
        int maxNumberOfBytesInWord = 4;
        for (int i = 0; i < maxNumberOfBytesInWord; i++) {
            lastColumn.getBytes()[i] = AESTables.findSboxValue(lastColumn.getBytes()[i]);
        }
        return lastColumn;
    }

    
}
