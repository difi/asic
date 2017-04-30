package no.difi.asic.processor;

import no.difi.commons.asic.jaxb.opendocument.manifest.Manifest;
import no.difi.commons.asic.jaxb.opendocument.manifest.ObjectFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

/**
 * @author erlend
 */
public abstract class OasisManifestCommons {

    protected static final JAXBContext JAXB_CONTEXT;

    protected static final ObjectFactory OBJECT_FACTORY = new ObjectFactory();

    static {
        try {
            JAXB_CONTEXT = JAXBContext.newInstance(Manifest.class);
        } catch (JAXBException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
