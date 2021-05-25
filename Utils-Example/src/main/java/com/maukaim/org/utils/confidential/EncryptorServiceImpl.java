package com.maukaim.org.utils.confidential;


import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.AlgorithmParameters;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

@Service
@Slf4j
//@ConfigurationProperties(prefix= "fix-engine")
public class EncryptorServiceImpl implements EncryptorService {
    private final String STRING_ENCODING_MODE = "UTF-8";
    private  final String GEN_KEY="Ruthos";
    private final String SECRET_KEY_FACTORY = "PBKDF2WithHmacSHA1";
    private final String ALGORITHM = "AES";
    private final String MODE = "CBC";
    private final String PADDING_SCHEME = "PKCS5Padding";
    private final String CIPHER_INSTANCE = String.format("%s/%s/%s", ALGORITHM, MODE, PADDING_SCHEME);
    private final int SALT_SIZE = 36;


    @Override
    public String encrypt(String word) throws Exception {
        byte[] ivBytes;
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[SALT_SIZE];
        random.nextBytes(bytes);
        byte[] saltBytes = bytes;

        SecretKeySpec secret = this.getSecret(saltBytes);

        //Encrypting the word
        Cipher cipher = Cipher.getInstance(CIPHER_INSTANCE);
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        AlgorithmParameters params = cipher.getParameters();
        ivBytes = params.getParameterSpec(IvParameterSpec.class).getIV();
        byte[] encryptedTextBytes = cipher.doFinal(word.getBytes(STRING_ENCODING_MODE));

        //prepend salt and vi
        byte[] buffer = new byte[saltBytes.length + ivBytes.length + encryptedTextBytes.length];
        System.arraycopy(saltBytes, 0, buffer, 0, saltBytes.length);
        System.arraycopy(ivBytes, 0, buffer, saltBytes.length, ivBytes.length);
        System.arraycopy(encryptedTextBytes, 0, buffer, saltBytes.length +ivBytes.length, encryptedTextBytes.length);
        return new Base64().encodeToString(buffer);
    }



    @Override
    public String decrypt(String encryptedValue) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_INSTANCE);

        // Strip off the salt and iv
        ByteBuffer buffer = ByteBuffer.wrap(new Base64().decode(encryptedValue));
        byte[] saltBytes = new byte[SALT_SIZE];
        buffer.get(saltBytes, 0, saltBytes.length);
        byte[] ivBytes1 = new byte[cipher.getBlockSize()];

        buffer.get(ivBytes1, 0, ivBytes1.length);
        byte[] encryptedTextBytes = new byte[buffer.capacity() - saltBytes.length - ivBytes1.length];
        buffer.get(encryptedTextBytes);
        SecretKeySpec secret = this.getSecret(saltBytes);

        cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(ivBytes1));
        byte[] decryptedTextBytes;
        try{
            decryptedTextBytes = cipher.doFinal(encryptedTextBytes);
        } catch (IllegalBlockSizeException | BadPaddingException e){
            log.error("Cipher not able to decrypt value", e);
            throw e;
        }
        return new String(decryptedTextBytes);
    }

    @Override
    public boolean isEncrypted(String value) {
        return this.isBase64String(value) && canBeDecrypted(value);
    }

    private boolean canBeDecrypted(String value) {
        int supposedPrependedSize = SALT_SIZE + 16;
        try{
            ByteBuffer buffer = ByteBuffer.wrap(new Base64().decode(value));
            int bufferSizeWithOutSaltAndVi = buffer.capacity() - supposedPrependedSize;
            return buffer.capacity() > supposedPrependedSize && (bufferSizeWithOutSaltAndVi % 16) == 0;
        } catch (IllegalArgumentException e){
            return false;
        }
    }

    private boolean isBase64String(String value) {
        return value.matches("[A-Za-z0-9+=/*]*");
    }


    private SecretKeySpec getSecret(byte[] saltBytes) throws InvalidKeySpecException, NoSuchAlgorithmException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance(SECRET_KEY_FACTORY);
        PBEKeySpec spec = new PBEKeySpec(GEN_KEY.toCharArray(), saltBytes, 65556, 256);
        SecretKey secretKey = factory.generateSecret(spec);
        return new SecretKeySpec(secretKey.getEncoded(), ALGORITHM);

    }

}
