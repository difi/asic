package no.difi.asic.util;

import no.difi.asic.config.ValueWrapper;
import no.difi.asic.lang.AsicExcepion;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;

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

    public static MessageDigest createMessageDigest(ValueWrapper valueWrapper) throws AsicExcepion {
        return createMessageDigest(valueWrapper.getString());
    }

    public static MessageDigest createMessageDigest(String algorithm) throws AsicExcepion {
        try {
            return MessageDigest.getInstance(algorithm, BouncyCastleProvider.PROVIDER_NAME);
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new AsicExcepion(e.getMessage(), e);
        }
    }

    public static List<MessageDigest> messageDigests(String... algorithms) throws AsicExcepion {
        List result = new ArrayList();
        for (String algorithm : algorithms)
            result.add(createMessageDigest(algorithm));

        return result;
    }
}
