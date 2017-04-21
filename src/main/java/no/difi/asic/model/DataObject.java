package no.difi.asic.model;

import no.difi.asic.MimeType;

import java.io.Serializable;

/**
 * @author erlend
 */
public class DataObject implements Serializable {

    private static final long serialVersionUID = -1971339559017072869L;

    private String filename;

    private String mimeType;

    private byte[] hash;

    public DataObject(String filename, String mimeType, byte[] hash) {
        this.filename = filename;
        this.mimeType = mimeType;
        this.hash = hash;
    }

    public DataObject(String filename, MimeType mimeType, byte[] hash) {
        this.filename = filename;
        this.mimeType = mimeType.toString();
        this.hash = hash;
    }

    public String getFilename() {
        return filename;
    }

    public String getMimeType() {
        return mimeType;
    }

    public byte[] getHash() {
        return hash;
    }
}
