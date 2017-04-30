package no.difi.asic;

import java.util.regex.Pattern;

public class AsicUtils {

    /**
     * The MIME type, which should be the very first entry in the container
     */
    public static final String MIMETYPE_ASICE = "application/vnd.etsi.asic-e+zip";

    static final Pattern PATTERN_CADES_MANIFEST = Pattern.compile("META-INF/asicmanifest(.*)\\.xml", Pattern.CASE_INSENSITIVE);
    static final Pattern PATTERN_CADES_SIGNATURE = Pattern.compile("META-INF/signature(.*)\\.p7s", Pattern.CASE_INSENSITIVE);
    static final Pattern PATTERN_XADES_SIGNATURES = Pattern.compile("META-INF/signatures(.*)\\.xml", Pattern.CASE_INSENSITIVE);

    static final Pattern PATTERN_EXTENSION_ASICE = Pattern.compile(".+\\.(asice|sce)", Pattern.CASE_INSENSITIVE);

}