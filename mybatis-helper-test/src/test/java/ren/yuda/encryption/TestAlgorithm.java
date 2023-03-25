package ren.yuda.encryption;

public class TestAlgorithm implements EncryptAlgorithm {
    @Override
    public String registerName() {
        return "test";
    }

    @Override
    public String encrypt(String data) {
        return "1";
    }

    @Override
    public String decrypt(String encryptText) {
        return "2";
    }
}
