package MacAttack;

import java.util.ArrayList;

public class Sha_1 {
    
	
    public static final int BITS_PER_BYTE = 8;
    
    public static final int BITS_PER_WORD = 32; 
    public static final int BYTES_PER_WORD = 4;
    
    public static final int WORDS_PER_BLOCK = 16;
    public static final int MESSAGE_SCHEDULE_CAPACITY = 80;
    
    
    public int[][] parseMessage(String message, int messageBitsLenght) {
        int l_messageWordsLength;
        int N_numberOfBlocks;
        int[][] M_Blocks; 
        
        int messageBytesLength = message.length();
        l_messageWordsLength = messageBytesLength / BYTES_PER_WORD + 2;
        N_numberOfBlocks = (int)Math.ceil((double)l_messageWordsLength/(double)WORDS_PER_BLOCK);
        M_Blocks = new int[N_numberOfBlocks][WORDS_PER_BLOCK];  

        for (int i_blockIndex = 0; i_blockIndex < N_numberOfBlocks; i_blockIndex++) {
            for (int t_wordIndex = 0; t_wordIndex < WORDS_PER_BLOCK; t_wordIndex++) { 
            	M_Blocks[i_blockIndex][t_wordIndex] = getMessageWord(message, i_blockIndex, t_wordIndex);
            } 
        }

        //M_Blocks[N_numberOfBlocks - 1][WORDS_PER_BLOCK - 2] = 0; 
        M_Blocks[N_numberOfBlocks - 1][WORDS_PER_BLOCK - 1] = messageBitsLenght;
        
        return M_Blocks;
    }
   
    
    public String hashBlocks(int[][] blocks, int[] initialHashes) {
    	
        int[] K_Constants = { 
                0x5a827999, 
                0x6ed9eba1, 
                0x8f1bbcdc, 
                0xca62c1d6 
            };

    	int[][] M_Blocks = blocks;
    	int N_numberOfBlocks = M_Blocks.length;
            
        int H0_previousHash = initialHashes[0];
        int H1_previousHash = initialHashes[1];
        int H2_previousHash = initialHashes[2];
        int H3_previousHash = initialHashes[3];
        int H4_previousHash = initialHashes[4];
            
        int[] W_messageSchedule = new int[MESSAGE_SCHEDULE_CAPACITY];

    	
        for (int i_blockIndex = 0; i_blockIndex < N_numberOfBlocks; i_blockIndex++) {

            // 1 - prepare message schedule 'W'
            for (int t_wordIndex = 0; t_wordIndex < WORDS_PER_BLOCK; t_wordIndex++) { 
                    W_messageSchedule[t_wordIndex] = M_Blocks[i_blockIndex][t_wordIndex];
            }
            for (int t_wordIndex = WORDS_PER_BLOCK; t_wordIndex < MESSAGE_SCHEDULE_CAPACITY; t_wordIndex++) {
                    W_messageSchedule[t_wordIndex] = ROTL_circularLeftShift(W_messageSchedule[t_wordIndex-3] ^ W_messageSchedule[t_wordIndex-8] ^ W_messageSchedule[t_wordIndex-14] ^ W_messageSchedule[t_wordIndex-16], 1);
            }

            // 2 - initialise five working variables a, b, c, d, e with previous hash value
            int a_currentHash = H0_previousHash; 
            int b_currentHash = H1_previousHash; 
            int c_currentHash = H2_previousHash; 
            int d_currentHash = H3_previousHash; 
            int e_currentHash = H4_previousHash;

            // 3 - main loop
            for (int t_wordIndex = 0; t_wordIndex < MESSAGE_SCHEDULE_CAPACITY; t_wordIndex++) {
                int s_operationIndex = (int)Math.floor(t_wordIndex/20); // seq for blocks of 'f' functions and 'K' constants
                int T_temporaryHash = (ROTL_circularLeftShift(a_currentHash,5) + functions(s_operationIndex,b_currentHash,c_currentHash,d_currentHash) + e_currentHash + K_Constants[s_operationIndex] + W_messageSchedule[t_wordIndex]) & 0xffffffff;
                e_currentHash = d_currentHash;
                d_currentHash = c_currentHash;
                c_currentHash = ROTL_circularLeftShift(b_currentHash, 30);
                b_currentHash = a_currentHash;
                a_currentHash = T_temporaryHash;
            }

            // 4 - compute the new intermediate hash value (note 'addition modulo 2^32')
            H0_previousHash = (H0_previousHash + a_currentHash);// & 0xffffffff;
            H1_previousHash = (H1_previousHash + b_currentHash);// & 0xffffffff;
            H2_previousHash = (H2_previousHash + c_currentHash);// & 0xffffffff;
            H3_previousHash = (H3_previousHash + d_currentHash);// & 0xffffffff;
            H4_previousHash = (H4_previousHash + e_currentHash);// & 0xffffffff;
        }
        
        String hexString_H0 = HexUtility.intToHexString(H0_previousHash);
        String hexString_H1 = HexUtility.intToHexString(H1_previousHash);
        String hexString_H2 = HexUtility.intToHexString(H2_previousHash);
        String hexString_H3 = HexUtility.intToHexString(H3_previousHash);
        String hexString_H4 = HexUtility.intToHexString(H4_previousHash);
        String hexString_All = hexString_H0 + hexString_H1 + hexString_H2 + hexString_H3 + hexString_H4;
        
        return hexString_All;
    }
    
    
    public String hash(String message, int messageLenght) {
        int[] initialHashes = {
        		0x67452301, 
        		0xefcdab89,
        		0x98badcfe,
        		0x10325476,
        		0xc3d2e1f0
        		};
        
        int[][] M_Blocks = parseMessage(message, messageLenght);
        String hashValue = hashBlocks(M_Blocks, initialHashes);
        
        return hashValue; 
    }

    
    public String hash(String message, int messageLenght, int[] initialState) {
    	if (initialState.length != 5) {
    		System.err.println("lenght is not 5");  
    	}
    	
    	int[][] M_Blocks = parseMessage(message, messageLenght);
        String hashValue = hashBlocks(M_Blocks, initialState);
        
        return hashValue; 
    }

    
    public int functions(int functionIndex, int b_currentHash, int c_currentHash, int d_currentHash) {
        switch (functionIndex) {
            case 0: return (b_currentHash & c_currentHash) ^ (~b_currentHash & d_currentHash);                                   // Ch()
            case 1: return  b_currentHash ^ c_currentHash  ^  d_currentHash;                                                     // Parity()
            case 2: return (b_currentHash & c_currentHash) ^ (b_currentHash & d_currentHash) ^ (c_currentHash & d_currentHash);  // Maj()
            case 3: return  b_currentHash ^ c_currentHash  ^  d_currentHash;                                                     // Parity()
        }
        return 0;
    }


    public int ROTL_circularLeftShift(int value, int shift) {
        return (value << shift) | (value >>> (32 - shift));
    }

    
    private int getMessageWord(String message, int i_blockIndex, int t_wordIndex) {
        int nextBlock = BYTES_PER_WORD * WORDS_PER_BLOCK;
        int nextWord = BYTES_PER_WORD;
        int messageIndex = i_blockIndex * nextBlock + t_wordIndex * nextWord;
        int finalWord = 0;
        
        int[] wordChunks = new int[BYTES_PER_WORD];
        int shiftMultiple = BYTES_PER_WORD - 1;
        for (int byteIndex = 0; byteIndex < BYTES_PER_WORD; byteIndex++, shiftMultiple--) {
            if (messageIndex + byteIndex < message.length()) {
                int wordChunk = message.charAt(messageIndex + byteIndex) << (8 * shiftMultiple);
                wordChunks[byteIndex] = wordChunk;
            } else if (messageIndex + byteIndex == message.length()) {
                int wordChunk = 0x80 << (8 * shiftMultiple);
                wordChunks[byteIndex] = wordChunk;
            } else {
                int wordChunk = 0;
                wordChunks[byteIndex] = wordChunk;
            }
        }
        
        finalWord = wordChunks[0] | wordChunks[1] | wordChunks[2] | wordChunks[3];
        return finalWord;
    }


    public String blocksToString(int[][] M_Blocks) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int blockIndex = 0; blockIndex < M_Blocks.length; blockIndex++) {
            for (int wordIndex = 0; wordIndex < WORDS_PER_BLOCK; wordIndex++) {
            	String hexString = HexUtility.intToHexString(M_Blocks[blockIndex][wordIndex]);
            	stringBuilder.append(hexString);
            }
        }
        return stringBuilder.toString();
    }


    
}
