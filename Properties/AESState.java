/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Properties;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 *
 * @author rental
 */
public class AESState {
    
    public static String printStates(ArrayList<AESState> encryption) {
        String output = new String();
        for (int i = 0 ; i < encryption.size(); i++) {
            output = output + encryption.get(i).toString();
        }
        return output;
    }
    
    public static AESState multiply(AESState currentState, AESState mixColumn) {
        int matrixLength = 4;
        
        AESState result = new AESState();
        AESByte sum = new AESByte();
        for (int i = 0 ; i < matrixLength ; i++) {
            for (int j = 0 ; j < matrixLength; j++) {
                for (int k = 0 ; k < matrixLength; k++) {
                    sum = AESByte.sum(sum, AESByte.multiply(currentState.getMatrix()[i][k], mixColumn.getMatrix()[k][j]));
                }
                result.setValue(i, j, sum);
                sum = new AESByte();
            }
        }
        return result;
    }
    
    public static AESState xor (AESState firstState, AESState secondState) {
        AESState outputState = new AESState();
        
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                AESByte result = AESByte.xor(firstState.getMatrix()[i][j], secondState.getMatrix()[i][j]);
                outputState.setValue(i, j, result);
            }
        }
        return outputState;
    }
        
    private final int MATRIX_LENGTH = 4;
    private AESByte[][] matrix = new AESByte[4][4];
    
    public AESState() {
        for (int i = 0; i < this.MATRIX_LENGTH; i++) {
            for (int j = 0; j < this.MATRIX_LENGTH; j++) {
                matrix[i][j] = new AESByte();
            }
        }
    }
    
    public AESState(AESByte[] bytes) throws Exception {
        if (bytes.length == 16) {
            int index = 0;
            for (int col = 0; col < this.MATRIX_LENGTH; col++) {
                for (int row = 0; row < this.MATRIX_LENGTH; row++) {
                    matrix[row][col] = bytes[index];
                    index++;
                }
            }
        } else {
            throw new Exception("bytes too long for state");
        }
    }
    
    public AESState(AESWord[] words) throws Exception {
        if (words.length == 4) {
            for (int col = 0; col < this.MATRIX_LENGTH; col++) {
                for (int row = 0; row < this.MATRIX_LENGTH; row++) {
                    matrix[row][col] = words[col].getBytes()[row];
                }
            }
        } else {
            throw new Exception("words too long for state");
        }
    }
    
    public AESState(AESWord word0, AESWord word1, AESWord word2, AESWord word3) {
        ArrayList<AESWord> words = new ArrayList<AESWord>();
        words.add(word0);
        words.add(word1);
        words.add(word2);
        words.add(word3);
        for (int col = 0; col < words.size(); col++) {
            for (int row = 0; row < words.size(); row++) {
                matrix[row][col] = words.get(col).getBytes()[row];
            }
        }
    }
        
    public AESState(AESState state) throws Exception {
        for (int col = 0; col < this.MATRIX_LENGTH; col++) {
            for (int row = 0; row < this.MATRIX_LENGTH; row++) {
                matrix[row][col] = state.getMatrix()[col][row];
            }
        }
    }
    
    public AESState(AESByte[][] bytes) {
        this.matrix = bytes;
    }

    /**
     * @return the matrix
     */
    public AESByte[][] getMatrix() {
        return matrix;
    }

    /**
     * @param matrix the matrix to set
     */
    public void setMatrix(AESByte[][] matrix) {
        this.matrix = matrix;
    }
    
    public void setValue(int row, int col, AESByte value) {
        this.matrix[row][col] = value;
    }

    public AESWord getColumn(int col) throws Exception {
        AESWord column = new AESWord();
        if (col >= 0 && col < 4) {
            for (int row = 0; row < 4; row++) {
                column.getBytes()[row] = this.matrix[row][col];
            }
            return column;
        } else {
            throw new Exception("index of column out of Bounds");
        }
    }
    
    public void setColumn(int col, AESWord colValues) throws Exception {
        if (col >= 0 && col < 4) {
            for (int row = 0; row < 4; row++) {
                this.matrix[row][col] = colValues.getBytes()[row];
            }
        } else {
            throw new Exception("index of column out of Bounds");
        }
    }
    
    @Override
    public String toString() {
        
        byte[] bytes = new byte[16]; 
        for (int col = 0; col < 4; col++) {
            for(int row = 0; row < 4; row++) {
                bytes[col + row] = this.matrix[row][col].getByte();
            }
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
