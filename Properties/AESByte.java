/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Properties;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author rental
 */
public class AESByte {
    
    public static AESByte sum(AESByte firstByte, AESByte secondByte) {
        return new AESByte((byte)(firstByte.getByte() + secondByte.getByte()));
    }
    
    public static AESByte xor(AESByte firstByte, AESByte secondByte) {
        return new AESByte((byte)(firstByte.getByte() ^ secondByte.getByte())); 
    }
    
    public static AESByte multiply(AESByte firstByte, AESByte secondByte) {
        int numberOfBits = 8;
        ArrayList<AESByte> xtimeResults = new ArrayList<AESByte>();
        AESByte nextXtimeResult = secondByte;
        
        for (int index = 0; index < numberOfBits; index++) {
            xtimeResults.add(nextXtimeResult);
            nextXtimeResult = AESByte.xtime(nextXtimeResult);
        }
        
        AESByte output = new AESByte();
        
        try {
            int bitPosition = 0;
            for (int i = numberOfBits - 1; i >=0; i--) {
                if (secondByte.getBit(bitPosition) == true) {
                    output = AESByte.xor(xtimeResults.get(bitPosition), output);
                }
                bitPosition++;
            }

            
        } catch (Exception ex) {
            Logger.getLogger(AESByte.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return output;
    }
    
    private static AESByte xtime(AESByte input) {
        byte output = 0;
        byte shifted = (byte)(input.getByte() << 1);
        byte limit = (byte)0x80;
        if (shifted < limit) {
            output = shifted;
        } else {
            byte mod = (byte)0x1B;
            output = (byte)(shifted % mod);
        }
        return new AESByte(output);
    }
    
    private byte aByte;
    
    public AESByte(byte aByte) {
        this.aByte = aByte;
    }
    
    public AESByte() {
        this.aByte = 0;
    }
    
    public boolean getBit(int index) throws Exception {
        if (index >= 0 && index < 8) {
            byte mask = (byte)(1 << index);
            byte maskedNumber = (byte)(this.aByte & mask);
            return mask == maskedNumber;
        } else {
            throw new Exception("bit index out of bounds");
        }
    }

    /**
     * @return the aByte
     */
    public byte getByte() {
        return aByte;
    }

    /**
     * @param aByte the aByte to set
     */
    public void setByte(byte aByte) {
        this.aByte = aByte;
    }
    
   
}
