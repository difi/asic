package no.difi.asic.util;

import no.difi.asic.code.MessageDigestAlgorithm;
import no.difi.asic.lang.AsicException;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author erlend
 */
public class BCUtil {

    static {
        register();
    }

    /**
     * Register Bouncy Castle as Security Provider if it is not registered.
     */
    public static void register() {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null)
            Security.addProvider(new BouncyCastleProvider());
    }

    public static MessageDigest createMessageDigest(MessageDigestAlgorithm algorithm) throws AsicException {
        return createMessageDigest(algorithm.getString());
    }

    public static MessageDigest createMessageDigest(String algorithm) throws AsicException {
        try {
            return MessageDigest.getInstance(algorithm, BouncyCastleProvider.PROVIDER_NAME);
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
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
