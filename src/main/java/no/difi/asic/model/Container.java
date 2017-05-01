package no.difi.asic.model;

import no.difi.asic.code.MessageDigestAlgorithm;
import no.difi.asic.lang.AsicException;
import no.difi.asic.lang.ChecksumException;
import no.difi.asic.security.MultiMessageDigest;

import java.io.Serializable;
import java.util.*;

/**
 * @author erlend
 */
public class Container implements Serializable {

    private static final long serialVersionUID = -5478541041467973689L;

    private Mode mode;

    private Map<String, DataObject> dataObjects = new HashMap<>();

    private List<Signer> signers = new ArrayList<>();

    private String rootFile;

    public Container(Mode mode) {
        this.mode = mode;
    }

    public String getRootFile() {
        return rootFile;
    }

    public void setRootFile(String rootFile) throws AsicException {
        if (this.rootFile != null)
            throw new AsicException("Root file is already set.");

        if (!dataObjects.containsKey(rootFile))
            throw new AsicException(String.format("File '%s' is not known.", rootFile));

        this.rootFile = rootFile;
    }

    public void update(String filename, DataObject.Type type, MimeType mimeType) {
        DataObject dataObject = dataObjects.get(filename);

        if (dataObject == null) {
            dataObject = new DataObject(filename);
            dataObjects.put(filename, dataObject);
        }

        dataObject.setType(type);
        dataObject.setMimeType(mimeType);
    }

    public void update(String filename, MultiMessageDigest messageDigest) throws AsicException {
        DataObject dataObject = dataObjects.get(filename);

        if (dataObject == null) {
            dataObject = new DataObject(filename);
            dataObjects.put(filename, dataObject);
        }

        if (!dataObject.getHash().update(messageDigest))
            throw new ChecksumException(String.format("Invalid checksum for '%s'.", filename));
    }

    public void verify(Signer signer, String filename, MessageDigestAlgorithm algorithm, byte[] digest)
            throws AsicException {
        if (mode != Mode.READER)
            throw new IllegalStateException("Verification of content is performed when reading a container.");

        if (!dataObjects.get(filename).verify(signer, algorithm, digest))
            throw new ChecksumException(String.format("Unable to verify digest for file '%s'.", filename));

        if (!signers.contains(signer))
            signers.add(signer);
    }

    public Collection<DataObject> getDataObjects() {
        return Collections.unmodifiableCollection(dataObjects.values());
    }

    public enum Mode {
        READER,
        WRITER
    }
}
