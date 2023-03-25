package ren.yuda.encryption.support;

import ren.yuda.encryption.EncryptAlgorithm;
import ren.yuda.encryption.toolkit.DESUtils;

public class DESEncryptAlgorithm implements EncryptAlgorithm {
    private final String key;

    public DESEncryptAlgorithm(String key) {
        this.key = key;
    }

    @Override
    public String registerName() {
        return "des";
    }

    @Override
    public String encrypt(String data) {
        if (data == null) {
            return null;
        }
        return DESUtils.encrypt(data.getBytes(), key);
    }

    @Override
    public String decrypt(String encryptText) {
        if (encryptText == null || encryptText.isEmpty()) {
            return null;
        }
        return DESUtils.decrypt(encryptText, key);
    }


}
