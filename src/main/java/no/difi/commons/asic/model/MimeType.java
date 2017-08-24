package no.difi.commons.asic.model;

import java.io.Serializable;

public interface MimeType extends Serializable {

    /**
     * The MIME type, which should be the very first entry in the container
     */
    MimeType APPLICATION_ASICE = of("application/vnd.etsi.asic-e+zip");

    MimeType APPLICATION_PDF = of("application/pdf");

    MimeType APPLICATION_XML = of("application/xml");

    String getValue();

    static MimeType of(String mimeType) {
        return new DefaultMimeType(mimeType);
    }

    class DefaultMimeType implements MimeType {

        private static final long serialVersionUID = 5501916549809487201L;

        private final String value;

        private DefaultMimeType(String mimeType) {
            this.value = mimeType;
        }

        public String getValue() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || !(o instanceof MimeType)) return false;

            MimeType mimeType = (MimeType) o;

            return value.equals(mimeType.getValue());
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
}
