package no.difi.commons.asic.model;

import java.io.Serializable;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.List;

/**
 * @author erlend
 */
public class Manifest implements Serializable {

    private static final long serialVersionUID = -6617875094704469455L;

    private String rootFile;

    private Collection<DataObject> dataObjects;

    private List<Signer> signers;

    protected Manifest(String rootFile, Collection<DataObject> dataObjects, List<Signer> signers) {
        this.rootFile = rootFile;
        this.dataObjects = dataObjects;
        this.signers = signers;
    }

    public String getRootFile() {
        return rootFile;
    }

    public Collection<DataObject> getDataObjects() {
        return dataObjects;
    }

    public List<Signer> getSigners() {
        return signers;
    }
}
