package com.maukaim.org.utils.confidential;

public interface EncryptorService {

    String encrypt(String word) throws Exception;
    String decrypt(String encryptedValue) throws Exception;
    boolean isEncrypted(String value);
}
