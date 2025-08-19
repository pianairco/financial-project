package ir.piana.financial.commons.utilities;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;

public class DESedeUtility {
    private static String BOUNCY_CASTLE_PROVIDER = "BC";

    enum ALGORITHM {
        DESEDE_ECB_NOPADDING("DESede/ECB/Nopadding"),
        DESEDE_CBC_NOPADDING("DESede/CBC/Nopadding"),
        ;

        private final String algorithm;


        ALGORITHM(String algorithm)  {
            this.algorithm = algorithm;
        }

        public byte[] encrypt(byte[] input, byte[] key) {
            try {
                SecretKey keySpec = new SecretKeySpec(key, algorithm);
                Cipher cipher = Cipher.getInstance(algorithm, BOUNCY_CASTLE_PROVIDER);
                cipher.init(Cipher.ENCRYPT_MODE, keySpec);
                return cipher.doFinal(input);
            } catch (Throwable e) {
                throw e instanceof RuntimeException ? (RuntimeException) e : new RuntimeException(e);
            }
        }

        public byte[] decrypt(byte[] input, byte[] key) {
            try {
                SecretKey keySpec = new SecretKeySpec(key, algorithm);
                Cipher cipher = Cipher.getInstance(algorithm, BOUNCY_CASTLE_PROVIDER);
                cipher.init(Cipher.DECRYPT_MODE, keySpec);
                return cipher.doFinal(input);
            } catch (Throwable e) {
                throw e instanceof RuntimeException ? (RuntimeException) e : new RuntimeException(e);
            }
        }
    }

    private static String DESede_ECB_NOPADDING = "DESede/ECB/Nopadding";
    private static String DESede_CBC_NOPADDING = "DESede/CBC/Nopadding";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public byte[] encode(ALGORITHM algorithm, byte[] key, byte[] input) {
        return algorithm.encrypt(input, key);
    }

}
