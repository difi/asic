package no.difi.asic.security;

import no.difi.asic.code.MessageDigestAlgorithm;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author erlend
 */
public class MultiMessageDigest {

    private Map<MessageDigestAlgorithm, MessageDigest> messageDigestMap = new HashMap<>();

    public MultiMessageDigest(MessageDigestAlgorithm... messageDigestAlgorithms) {

    }

    public void reset() {
        for (MessageDigest messageDigest : messageDigestMap.values())
            messageDigest.reset();
    }

    public void update(byte input) {
        for (MessageDigest messageDigest : messageDigestMap.values())
            messageDigest.update(input);
    }

    public void update(byte[] input, int offset, int len) {
        for (MessageDigest messageDigest : messageDigestMap.values())
            messageDigest.update(input, offset, len);
    }
}
