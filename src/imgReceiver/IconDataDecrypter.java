package imgReceiver;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class IconDataDecrypter
{
    private byte[] encryptedData;
    private byte[][] DecryptionData = new byte[5][];
    private int keyIndex;

    public IconDataDecrypter(byte[] encryptedData, int keyIndex)
    {
        this.encryptedData = encryptedData;
        this.keyIndex = keyIndex;

        this.DecryptionData[0] = new byte[] { -92, 105, -121, -82, 71, -40, 43, -76, -6, -118, -68, 4, 80, 40, 95, -92 };

        this.DecryptionData[1] = new byte[] { 74, -71, -92, 14, 20, 105, 117, -88, 75, -79, -76, -13, -20, -17, -60, 123 };

        this.DecryptionData[2] = new byte[] { -112, -96, -69, 30, 14, -122, 74, -24, 125, 19, -90, -96, 61, 40, -55, -72 };

        this.DecryptionData[3] = new byte[] { Byte.MAX_VALUE, -69, 87, -63, 78, -104, -20, 105, 117, -77, -124, -4, -12, 7, -122, -75 };

        this.DecryptionData[4] = new byte[] { Byte.MIN_VALUE, -110, 55, -103, -76, 31, 54, -90, -89, 95, -72, -76, -116, -107, -10, 111 };
    }

    public byte[] decryptData() throws Exception
    {

        Cipher decryptCipher = Cipher.getInstance("AES/CBC/NoPadding");
        SecretKey aesKey = new SecretKeySpec(this.DecryptionData[(this.keyIndex + 1)], "AES");
        decryptCipher.init(2, aesKey, new IvParameterSpec(this.DecryptionData[0]));

        return decryptCipher.doFinal(this.encryptedData);
    }
}
