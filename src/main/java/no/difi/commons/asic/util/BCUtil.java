package no.difi.commons.asic.util;

import no.difi.commons.asic.code.MessageDigestAlgorithm;
import no.difi.commons.asic.lang.AsicException;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.util.HashMap;
import java.util.Map;

/**
 * @author erlend
 */
public class BCUtil {

    public static final Provider PROVIDER;

    static {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null)
            Security.addProvider(new BouncyCastleProvider());

        PROVIDER = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME);
    }

    public static MessageDigest createMessageDigest(MessageDigestAlgorithm algorithm) throws AsicException {
        return createMessageDigest(algorithm.getString());
    }

    public static MessageDigest createMessageDigest(String algorithm) throws AsicException {
        try {
            return MessageDigest.getInstance(algorithm, PROVIDER);
        } catch (NoSuchAlgorithmException e) {
            throw new AsicException(e.getMessage(), e);
        }
    }

    public static Map<MessageDigestAlgorithm, MessageDigest> createMessageDigests(MessageDigestAlgorithm... algorithms)
            throws AsicException {
        Map<MessageDigestAlgorithm, MessageDigest> result = new HashMap<>();
        for (MessageDigestAlgorithm algorithm : algorithms)
            result.put(algorithm, createMessageDigest(algorithm));

        return result;
    }
}
