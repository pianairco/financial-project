package ir.piana.financial.commons.utilities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class SymmetricCryptoBuilderTest {
    @Test
    void CryptoBuilderDESTest() throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        SymmetricCryptoBuilder.CipherFinalizer CipherFinalizer = SymmetricCryptoBuilder.withAlgorithm(SymmetricCryptoBuilder.CipherAlgorithm.DES)
                .withMode(SymmetricCryptoBuilder.CipherMode.CBC)
                .withPadding(SymmetricCryptoBuilder.CipherPadding.NoPadding)
                .genKey()
                .withDefaultIv()
                .build();
        byte[] encryptedBytes = CipherFinalizer.doFinal(
                SymmetricCryptoBuilder.CipherFunctionality.Encrypt, "12345678".getBytes(StandardCharsets.ISO_8859_1));

        byte[] decryptedBytes = CipherFinalizer.doFinal(
                SymmetricCryptoBuilder.CipherFunctionality.Decrypt, encryptedBytes);

        Assertions.assertEquals("12345678", new String(decryptedBytes));
    }

    @Test
    void CryptoBuilderDESedeTest() throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        SymmetricCryptoBuilder.CipherFinalizer CipherFinalizer = SymmetricCryptoBuilder
                .withAlgorithm(SymmetricCryptoBuilder.CipherAlgorithm.DESede)
                .withMode(SymmetricCryptoBuilder.CipherMode.CBC)
                .withPadding(SymmetricCryptoBuilder.CipherPadding.PKCS5Padding)
                .genKey()
                .withDefaultIv()
                .build();
        byte[] encryptedBytes = CipherFinalizer.doFinal(
                SymmetricCryptoBuilder.CipherFunctionality.Encrypt, "ali".getBytes(StandardCharsets.ISO_8859_1));

        byte[] decryptedBytes = CipherFinalizer.doFinal(
                SymmetricCryptoBuilder.CipherFunctionality.Decrypt, encryptedBytes);

        Assertions.assertEquals("ali", new String(decryptedBytes));
    }

    @Test
    void CryptoBuilderDESedeProvided16KeyTest() throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        SymmetricCryptoBuilder.CipherFinalizer CipherFinalizer = SymmetricCryptoBuilder
                .withAlgorithm(SymmetricCryptoBuilder.CipherAlgorithm.DESede)
                .withMode(SymmetricCryptoBuilder.CipherMode.CBC)
                .withPadding(SymmetricCryptoBuilder.CipherPadding.NoPadding)
                .withKey(new byte[]{
                        0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF,
                        0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF})
                .withDefaultIv()
                .build();
        byte[] encryptedBytes = CipherFinalizer.doFinal(
                SymmetricCryptoBuilder.CipherFunctionality.Encrypt, "12345678".getBytes(StandardCharsets.ISO_8859_1));

        byte[] decryptedBytes = CipherFinalizer.doFinal(
                SymmetricCryptoBuilder.CipherFunctionality.Decrypt, encryptedBytes);

        Assertions.assertEquals("12345678", new String(decryptedBytes));
    }

    @Test
    void CryptoBuilderDESedeProvided24KeyTest() throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        SymmetricCryptoBuilder.CipherFinalizer CipherFinalizer = SymmetricCryptoBuilder
                .withAlgorithm(SymmetricCryptoBuilder.CipherAlgorithm.DESede)
                .withMode(SymmetricCryptoBuilder.CipherMode.CBC)
                .withPadding(SymmetricCryptoBuilder.CipherPadding.NoPadding)
                .withKey(new byte[]{
                        0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF,
                        0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF,
                        0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF})
                .withDefaultIv()
                .build();
        byte[] encryptedBytes = CipherFinalizer.doFinal(
                SymmetricCryptoBuilder.CipherFunctionality.Encrypt, "12345678".getBytes(StandardCharsets.ISO_8859_1));

        byte[] decryptedBytes = CipherFinalizer.doFinal(
                SymmetricCryptoBuilder.CipherFunctionality.Decrypt, encryptedBytes);

        Assertions.assertEquals("12345678", new String(decryptedBytes));
    }

    @Test
    void CryptoBuilderAES_256Test() throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        SymmetricCryptoBuilder.CipherFinalizer CipherFinalizer = SymmetricCryptoBuilder
                .withAlgorithm(SymmetricCryptoBuilder.CipherAlgorithm.AES_256)
                .withMode(SymmetricCryptoBuilder.CipherMode.CBC)
                .withPadding(SymmetricCryptoBuilder.CipherPadding.PKCS5Padding)
                .genKey()
                .withRandomIv()
                .build();
        byte[] encryptedBytes = CipherFinalizer.doFinal(
                SymmetricCryptoBuilder.CipherFunctionality.Encrypt, "ali".getBytes(StandardCharsets.ISO_8859_1));

        byte[] decryptedBytes = CipherFinalizer.doFinal(
                SymmetricCryptoBuilder.CipherFunctionality.Decrypt, encryptedBytes);

        Assertions.assertEquals("ali", new String(decryptedBytes));
    }

    @Test
    void AES_128_CBC_PKCS7PaddingWithKeyTest() throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        SymmetricCryptoBuilder.CipherFinalizer CipherFinalizer = SymmetricCryptoBuilder
                .withAlgorithm(SymmetricCryptoBuilder.CipherAlgorithm.AES_128)
                .withMode(SymmetricCryptoBuilder.CipherMode.CBC)
                .withPadding(SymmetricCryptoBuilder.CipherPadding.PKCS5Padding)
                .withKey(new byte[]{0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF,
                        0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF})
                .withDefaultIv()
                .build();
        byte[] encryptedBytes = CipherFinalizer.doFinal(
                SymmetricCryptoBuilder.CipherFunctionality.Encrypt, "ali".getBytes(StandardCharsets.ISO_8859_1));

        byte[] decryptedBytes = CipherFinalizer.doFinal(
                SymmetricCryptoBuilder.CipherFunctionality.Decrypt, encryptedBytes);

        Assertions.assertEquals("ali", new String(decryptedBytes));
    }

    @Test
    void AES_192_GCM_NoPaddingTest() throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        SymmetricCryptoBuilder.CipherFinalizer CipherFinalizer = SymmetricCryptoBuilder
                .withAlgorithm(SymmetricCryptoBuilder.CipherAlgorithm.AES_192)
                .withMode(SymmetricCryptoBuilder.CipherMode.GCM)
                .withPadding(SymmetricCryptoBuilder.CipherPadding.NoPadding)
                .genKey()
                .withDefaultIv()
                .build();
        byte[] encryptedBytes = CipherFinalizer.doFinal(
                SymmetricCryptoBuilder.CipherFunctionality.Encrypt, "ali".getBytes(StandardCharsets.ISO_8859_1));

        byte[] decryptedBytes = CipherFinalizer.doFinal(
                SymmetricCryptoBuilder.CipherFunctionality.Decrypt, encryptedBytes);

        Assertions.assertEquals("ali", new String(decryptedBytes));
    }
}
