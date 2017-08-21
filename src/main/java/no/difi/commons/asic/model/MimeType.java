package no.difi.commons.asic.model;

import java.io.Serializable;

public class MimeType implements Serializable {

    private static final long serialVersionUID = 5501916549809487201L;

    public static MimeType forString(String mimeType) {
        return new MimeType(mimeType);
    }

    private final String value;

    private MimeType(String mimeType) {
        this.value = mimeType;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MimeType mimeType = (MimeType) o;

        return value.equals(mimeType.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value;
    }
}
