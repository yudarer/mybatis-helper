package ren.yuda.encryption;

public interface EncryptAlgorithm {
    String DEFAULT_ENCRYPT_FLAG = "$$$$";

    default String getEncryptedFlag() {
        return DEFAULT_ENCRYPT_FLAG;
    }

    default boolean encrypted(String data) {
        return data.startsWith(getEncryptedFlag());
    }


    String registerName();


    String encrypt(String data);

    String decrypt(String encryptText);

}
