package no.difi.commons.asic.model;

import no.difi.commons.asic.code.MessageDigestAlgorithm;
import no.difi.commons.asic.security.MultiMessageDigest;

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

    public boolean update(MultiMessageDigest multiMessageDigest) {
        for (MessageDigestAlgorithm algorithm : multiMessageDigest.algorithms()) {
            if (hashes.containsKey(algorithm)
                    && !Arrays.equals(multiMessageDigest.digest(algorithm), hashes.get(algorithm)))
                return false;

            set(algorithm, multiMessageDigest.digest(algorithm));
        }

        return true;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("Hashes:");

        for (MessageDigestAlgorithm algorithm : hashes.keySet())
            stringBuilder.append("\r\n  ").append(algorithm.getString())
                    .append(": ").append(Arrays.toString(hashes.get(algorithm)));

        return stringBuilder.toString();
    }
}
