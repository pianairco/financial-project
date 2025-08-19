package ir.piana.financial.commons.utilities;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.InvalidKeySpecException;

public class SymmetricCryptoBuilder {
    private final static SecureRandom secureRandom = new SecureRandom();

    private static final byte[] DEFAULT_8_BYTES_IV = new byte[]{
            0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF};
    private static final byte[] DEFAULT_16_BYTES_IV = new byte[]{
            0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF,
            0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF};

    private static final KeyGenerator desKeyGenerator;
    private static final SecretKeyFactory desSecretKeyFactory;

    private static final KeyGenerator desedeKeyGenerator;
    private static final SecretKeyFactory desedeSecretKeyFactory;

    private static final KeyGenerator aes128KeyGenerator;
    private static final KeyGenerator aes192KeyGenerator;
    private static final KeyGenerator aes256KeyGenerator;

    static {
        Security.addProvider(new BouncyCastleProvider());
        try {
            desKeyGenerator = KeyGenerator.getInstance("DES", "BC");
            desKeyGenerator.init(56);
            desSecretKeyFactory = SecretKeyFactory.getInstance("DES", "BC");

            desedeKeyGenerator = KeyGenerator.getInstance("DESede");
            desedeKeyGenerator.init(168);
            desedeSecretKeyFactory = SecretKeyFactory.getInstance("DESede", "BC");

            aes128KeyGenerator = KeyGenerator.getInstance("AES");
            aes128KeyGenerator.init(128);

            aes192KeyGenerator = KeyGenerator.getInstance("AES");
            aes192KeyGenerator.init(192);

            aes256KeyGenerator = KeyGenerator.getInstance("AES");
            aes256KeyGenerator.init(256);
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
    }

    public static class CryptoSpecs {
        CipherAlgorithm algorithm;
        CipherMode mode;
        CipherPadding padding;
        /*CipherFunctionality functionality;*/
        SecretKey key;
        IvParameterSpec ivSpec;

        public CryptoSpecs(CipherAlgorithm algorithm) {
            this.algorithm = algorithm;
        }
    }

    public enum CipherAlgorithm {
        DES("DES", 8, 8, 8),
        DESede("DESede", 8, 24, 8),
        AES_128("AES", 16, 16, 16),
        AES_192("AES", 16, 24, 16),
        AES_256("AES", 16, 32, 16),
        HmacSHA1("HmacSHA1", 16, 16, 16),
        HmacSHA256("HmacSHA256", 16, 16, 16),
        ;

        private final String algName;
        private final int blockByteLen;
        private final int keyByteLen;
        private final int ivByteLen;

        CipherAlgorithm(String algName, int blockByteLen, int keyByteLen, int ivByteLen) {
            this.algName = algName;
            this.blockByteLen = blockByteLen;
            this.keyByteLen = keyByteLen;
            this.ivByteLen = ivByteLen;
        }

        public String getAlgName() {
            return algName;
        }

        public int getBlockByteLen() {
            return blockByteLen;
        }

        public int getKeyByteLen() {
            return keyByteLen;
        }

        public int getIvByteLen() {
            return ivByteLen;
        }
    }

    public enum CipherMode {
        CBC,// required iv for decryption functionality
        ECB,
        GCM,
        CFB8,
        OFB,
        ;
    }

    public enum CipherPadding {
        PKCS1PAdding,
        PKCS5Padding,
        PKCS7Padding,
        /**
         * DES, DESede => requires input length to be a multiple of 8 bytes
         * AES => requires input length to be a multiple of 16 bytes
         */
        NoPadding, //
        ;
    }

    public enum CipherFunctionality {
        Encrypt(1),
        Decrypt(2),
        ;

        private final int value;

        CipherFunctionality(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public static CipherModeSelector withAlgorithm(CipherAlgorithm algorithm) {
        return new CipherModeSelector(new CryptoSpecs(algorithm));
    }

    public static class CipherModeSelector {
        CryptoSpecs cryptoSpecs;

        public CipherModeSelector(CryptoSpecs cryptoSpecs) {
            this.cryptoSpecs = cryptoSpecs;
        }

        public CipherPaddingSelector withMode(CipherMode mode) {
            this.cryptoSpecs.mode = mode;
            return new CipherPaddingSelector(cryptoSpecs);
        }
    }

    public static class CipherPaddingSelector {
        CryptoSpecs cryptoSpecs;

        public CipherPaddingSelector(CryptoSpecs cryptoSpecs) {
            this.cryptoSpecs = cryptoSpecs;
        }

        /*public CipherFunctionalitySelector withPadding(CipherPadding padding) {
            this.cryptoSpecs.padding = padding;
            return new CipherFunctionalitySelector(cryptoSpecs);
        }*/

        public CipherKeySetter withPadding(CipherPadding padding) {
            this.cryptoSpecs.padding = padding;
            return new CipherKeySetter(cryptoSpecs);
        }
    }

    public static class CipherKeySetter {
        CryptoSpecs cryptoSpecs;

        public CipherKeySetter(CryptoSpecs cryptoSpecs) {
            this.cryptoSpecs = cryptoSpecs;
        }

        public CipherIVSetter withKey(byte[] key) throws InvalidKeyException, InvalidKeySpecException {
            if (cryptoSpecs.algorithm == CipherAlgorithm.DES) {
//                this.cryptoSpecs.key = new SecretKeySpec(key, this.cryptoSpecs.algorithm.algName());
                DESKeySpec desKeySpec = new DESKeySpec(key);
                this.cryptoSpecs.key = desSecretKeyFactory.generateSecret(desKeySpec);
            } else if (cryptoSpecs.algorithm == CipherAlgorithm.DESede) {
                byte[] key24;
                if (key.length == 16) {
                    key24 = new byte[24];
                    System.arraycopy(key, 0, key24, 0, 16);
                    System.arraycopy(key, 0, key24, 16, 8);
                } else if (key.length == 24) {
                    key24 = key;
                } else {
                    throw new InvalidKeyException("Invalid key length");
                }
//                this.cryptoSpecs.key = new SecretKeySpec(key24, "DESede");
                DESedeKeySpec desedeKeySpec = new DESedeKeySpec(key24);
                this.cryptoSpecs.key = desedeSecretKeyFactory.generateSecret(desedeKeySpec);
            } else if (cryptoSpecs.algorithm.algName.equals(CipherAlgorithm.AES_128.algName)) {
                if (key.length != cryptoSpecs.algorithm.keyByteLen) {
                    throw new InvalidKeyException(String.format("Invalid key length, %s has %s bytes key",
                            cryptoSpecs.algorithm.algName, cryptoSpecs.algorithm.keyByteLen));
                }
                this.cryptoSpecs.key = new SecretKeySpec(key, "AES");
            }
            return new CipherIVSetter(cryptoSpecs);
        }

        public CipherIVSetter genKey() {
            if (cryptoSpecs.algorithm == CipherAlgorithm.DES)
                this.cryptoSpecs.key = desKeyGenerator.generateKey();
            else if (cryptoSpecs.algorithm == CipherAlgorithm.DESede)
                this.cryptoSpecs.key = desedeKeyGenerator.generateKey();
            else if (cryptoSpecs.algorithm == CipherAlgorithm.AES_128) {
                this.cryptoSpecs.key = aes128KeyGenerator.generateKey();
            } else if (cryptoSpecs.algorithm == CipherAlgorithm.AES_192) {
                this.cryptoSpecs.key = aes192KeyGenerator.generateKey();
            } else if (cryptoSpecs.algorithm == CipherAlgorithm.AES_256) {
                this.cryptoSpecs.key = aes256KeyGenerator.generateKey();
            }
            return new CipherIVSetter(cryptoSpecs);
        }
    }

    public static class CipherIVSetter {
        CryptoSpecs cryptoSpecs;

        public CipherIVSetter(CryptoSpecs cryptoSpecs) {
            this.cryptoSpecs = cryptoSpecs;
        }

        public CipherBuilder withIv(byte[] iv) {
            if (iv == null || iv.length != cryptoSpecs.algorithm.ivByteLen) {
                throw new IllegalArgumentException("Invalid IV length, should be 8 bytes");
            }
            /*if ((cryptoSpecs.algorithm == CipherAlgorithm.DES || cryptoSpecs.algorithm == CipherAlgorithm.DESede) &&
                iv.length != 8) {
                throw new IllegalArgumentException("Invalid IV length, should be 8 bytes");
            }
            if ((cryptoSpecs.algorithm.name().equals(CipherAlgorithm.AES_256.name())) &&
                    iv.length != 16) {
                throw new IllegalArgumentException("Invalid IV length, should be 16 bytes");
            }*/
            this.cryptoSpecs.ivSpec = new IvParameterSpec(iv);
            return new CipherBuilder(cryptoSpecs);
        }

        public CipherBuilder skipIv() {
            return new CipherBuilder(cryptoSpecs);
        }

        public CipherBuilder withDefaultIv() {
            if (cryptoSpecs.algorithm.ivByteLen == 8) {
                this.cryptoSpecs.ivSpec = new IvParameterSpec(DEFAULT_8_BYTES_IV);
            } else if (cryptoSpecs.algorithm.ivByteLen == 16) {
                this.cryptoSpecs.ivSpec = new IvParameterSpec(DEFAULT_16_BYTES_IV);
            }
            /*if (cryptoSpecs.algorithm == CipherAlgorithm.DES || cryptoSpecs.algorithm == CipherAlgorithm.DESede) {
                this.cryptoSpecs.ivSpec = new IvParameterSpec(DEFAULT_8_BYTES_IV);
            } else if (cryptoSpecs.algorithm.algName.equals(CipherAlgorithm.AES_128.algName)) {
                this.cryptoSpecs.ivSpec = new IvParameterSpec(DEFAULT_16_BYTES_IV);
            }*/
            return new CipherBuilder(cryptoSpecs);
        }

        public CipherBuilder withRandomIv() {
            if (cryptoSpecs.algorithm.ivByteLen == 8) {
                byte[] ivBytes = new byte[8];
                secureRandom.nextBytes(ivBytes);
                this.cryptoSpecs.ivSpec = new IvParameterSpec(ivBytes);
            } else if (cryptoSpecs.algorithm.ivByteLen == 16) {
                byte[] ivBytes = new byte[16];
                secureRandom.nextBytes(ivBytes);
                this.cryptoSpecs.ivSpec = new IvParameterSpec(ivBytes);
            }
            /*if (cryptoSpecs.algorithm == CipherAlgorithm.DES || cryptoSpecs.algorithm == CipherAlgorithm.DESede) {
                byte[] ivBytes = new byte[8];
                secureRandom.nextBytes(ivBytes);
                this.cryptoSpecs.ivSpec = new IvParameterSpec(ivBytes);
            } else if (cryptoSpecs.algorithm == CipherAlgorithm.AES_256) {
                byte[] ivBytes = new byte[16];
                secureRandom.nextBytes(ivBytes);
                this.cryptoSpecs.ivSpec = new IvParameterSpec(ivBytes);
            }*/

            return new CipherBuilder(cryptoSpecs);
        }
    }

    public static class CipherBuilder {
        CryptoSpecs cryptoSpecs;

        public CipherBuilder(CryptoSpecs cryptoSpecs) {
            this.cryptoSpecs = cryptoSpecs;
        }

        public CipherFinalizer build() throws NoSuchPaddingException, NoSuchAlgorithmException {
            Cipher cipher = Cipher.getInstance(cryptoSpecs.algorithm.algName.concat("/")
                    .concat(cryptoSpecs.mode.name()).concat("/")
                    .concat(cryptoSpecs.padding.name()));
            return new CipherFinalizer(cryptoSpecs, cipher);
        }
    }

    public static class CipherFinalizer {
        CryptoSpecs cryptoSpecs;
        Cipher cipher;

        public CipherFinalizer(CryptoSpecs cryptoSpecs, Cipher cipher) {
            this.cryptoSpecs = cryptoSpecs;
            this.cipher = cipher;
        }

        public byte[] doFinal(CipherFunctionality cipherFunctionality, byte[] target) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
            if (cryptoSpecs.ivSpec != null) {
                cipher.init(cipherFunctionality.value, cryptoSpecs.key, cryptoSpecs.ivSpec);
            } else {
                cipher.init(cipherFunctionality.value, cryptoSpecs.key);
            }

            return cipher.doFinal(target);
        }
    }
}
