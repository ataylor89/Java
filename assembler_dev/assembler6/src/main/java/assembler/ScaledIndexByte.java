package assembler;

/**
 *
 * @author andrewtaylor
 */
public class ScaledIndexByte {
    
    public static final byte[][] SIB8;
    public static final byte[][] SIB16;
    public static final byte[][] SIB32;
    public static final byte[][] SIB64;
    
    static {
        SIB8 = new byte[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                SIB8[i][j] = (byte) (j + 8 * i);
            }
        }
        SIB16 = new byte[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                SIB16[i][j] = (byte) (SIB8[7][7] + 1 + j + 8 * i);
            }
        }
        SIB32 = new byte[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                SIB32[i][j] = (byte) (SIB16[7][7] + 1 + j + 8 * i);
            }
        }
        SIB64 = new byte[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                SIB64[i][j] = (byte) (SIB32[7][7] + 1 + j + 8 * i);
            }
        }
    }
    
    public static byte lookup(Register reg1, Register reg2) {
        switch (reg1.size) {
            case 64:
                return SIB64[reg1.index][reg2.index];
            case 32:
                return SIB32[reg1.index][reg2.index];
            case 16:
                return SIB16[reg1.index][reg2.index];
            case 8:
                return SIB8[reg1.index][reg2.index];
            default: 
                return (byte) 0;
        }
    }
}
