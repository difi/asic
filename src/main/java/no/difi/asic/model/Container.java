package no.difi.asic.model;

import no.difi.asic.code.MessageDigestAlgorithm;
import no.difi.asic.lang.AsicException;

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

    public void add(DataObject dataObject) {
        dataObjects.put(dataObject.getFilename(), dataObject);
    }

    public void verify(Signer signer, String filename, MessageDigestAlgorithm algorithm, byte[] digest)
            throws AsicException {
        if (!dataObjects.get(filename).verify(signer, algorithm, digest))
            throw new AsicException(String.format("Unable to verify digest for file '%s'.", filename));

        if (!signers.contains(signer))
            signers.add(signer);
    }

    public Collection<DataObject> getDataObjects() {
        return Collections.unmodifiableCollection(dataObjects.values());
    }

    public enum Mode {
        READER,
        WRITER;
    }
}
