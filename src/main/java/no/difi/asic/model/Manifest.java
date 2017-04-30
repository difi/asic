package no.difi.asic.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author erlend
 */
public class Manifest implements Serializable {

    private static final long serialVersionUID = -6617875094704469455L;

    private DataObject dataObject;

    private List<Signer> signers;

    public Manifest(DataObject dataObject) {
        this.dataObject = dataObject;
    }
}
