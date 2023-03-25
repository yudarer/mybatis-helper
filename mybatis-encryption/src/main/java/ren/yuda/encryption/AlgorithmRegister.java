package ren.yuda.encryption;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AlgorithmRegister {

    private static volatile AlgorithmRegister instance;

    private final Map<String, EncryptAlgorithm> algorithms = new ConcurrentHashMap<>();


    public static AlgorithmRegister getInstance() {
        if (instance == null) {
            synchronized (AlgorithmRegister.class) {
                if (instance == null) {
                    instance = new AlgorithmRegister();
                }
            }
        }
        return instance;
    }

    private AlgorithmRegister() {
        if (instance != null) {
            throw new UnsupportedOperationException("exist a instance");
        }
    }

    public void registerAlgorithm(EncryptAlgorithm algorithm) {
        String registerName = algorithm.registerName();
        EncryptAlgorithm exist = algorithms.get(registerName);
        if (exist != null && !exist.equals(algorithm)) {
            throw new RuntimeException(String.format("register duplicate algorithm! name=%s", registerName));
        }
        algorithms.put(registerName, algorithm);
    }

    public EncryptAlgorithm getAlgorithm(String name) {
        EncryptAlgorithm algorithm = algorithms.get(name);
        if (algorithm == null) {
            throw new RuntimeException(String.format("not exist algorithm! name = %s", name));
        }
        return algorithm;
    }

}
