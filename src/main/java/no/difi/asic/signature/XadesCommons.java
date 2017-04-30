package no.difi.asic.signature;

import no.difi.commons.asic.jaxb.cades.XAdESSignaturesType;
import no.difi.commons.asic.jaxb.xades.QualifyingPropertiesType;
import no.difi.commons.asic.jaxb.xmldsig.X509DataType;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

/**
 * @author erlend
 */
abstract class XadesCommons {

    protected static final JAXBContext JAXB_CONTEXT;

    static {
        try {
            JAXB_CONTEXT = JAXBContext.newInstance(XAdESSignaturesType.class, X509DataType.class,
                    QualifyingPropertiesType.class);
        } catch (JAXBException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
