package no.difi.commons.asic.security;

import no.difi.commons.asic.api.MessageDigestAlgorithm;
import no.difi.commons.asic.lang.AsicException;
import no.difi.commons.asic.model.Hash;

import java.security.MessageDigest;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author erlend
 */
public class MultiMessageDigest {

    private final Map<MessageDigestAlgorithm, MessageDigest> messageDigestMap;

    public MultiMessageDigest(MessageDigestAlgorithm... messageDigestAlgorithms) throws AsicException {
        this.messageDigestMap = BCHelper.createMessageDigests(messageDigestAlgorithms);
    }

    public MultiMessageDigest(List<MessageDigestAlgorithm> messageDigestAlgorithms) throws AsicException {
        this(messageDigestAlgorithms.toArray(new MessageDigestAlgorithm[messageDigestAlgorithms.size()]));
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

    public Set<MessageDigestAlgorithm> algorithms() {
        return messageDigestMap.keySet();
    }

    public byte[] digest(MessageDigestAlgorithm algorithm) {
        return messageDigestMap.get(algorithm).digest();
    }

    public Hash toHash() {
        Hash hash = new Hash();

        for (MessageDigestAlgorithm algorithm : messageDigestMap.keySet())
            hash.set(algorithm, messageDigestMap.get(algorithm).digest());

        return hash;
    }
}
