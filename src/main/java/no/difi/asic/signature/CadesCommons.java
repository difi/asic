package no.difi.asic.signature;

import no.difi.asic.util.BCUtil;
import no.difi.commons.asic.jaxb.cades.ASiCManifestType;
import no.difi.commons.asic.jaxb.cades.ObjectFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.security.Provider;

/**
 * @author erlend
 */
abstract class CadesCommons {

    protected static final Provider PROVIDER = BCUtil.PROVIDER;

    protected static final ObjectFactory OBJECT_FACTORY = new ObjectFactory();

    protected static final JAXBContext JAXB_CONTEXT;

    static {
        try {
            JAXB_CONTEXT = JAXBContext.newInstance(ASiCManifestType.class);
        } catch (JAXBException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
