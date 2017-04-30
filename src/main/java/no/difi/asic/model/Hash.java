package no.difi.asic.model;

import no.difi.asic.code.MessageDigestAlgorithm;
import no.difi.asic.security.MultiMessageDigest;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author erlend
 */
public class Hash implements Serializable {

    private static final long serialVersionUID = 8807990029770542293L;

    private Map<MessageDigestAlgorithm, byte[]> hashes = new HashMap<>();

    public void set(MessageDigestAlgorithm algorithm, byte[] hash) {
        hashes.put(algorithm, hash);
    }

    public byte[] get(MessageDigestAlgorithm algorithm) {
        return hashes.get(algorithm);
    }

    boolean verify(MessageDigestAlgorithm algorithm, byte[] digest) {
        return hashes.containsKey(algorithm) && Arrays.equals(hashes.get(algorithm), digest);
    }

    public void update(MultiMessageDigest multiMessageDigest) {
        for (MessageDigestAlgorithm messageDigestAlgorithm : multiMessageDigest.algorithms())
            set(messageDigestAlgorithm, multiMessageDigest.digest(messageDigestAlgorithm));
    }
}
