package no.difi.asic.config;

import no.difi.asic.annotation.OIDValue;
import no.difi.asic.annotation.StringValue;
import no.difi.asic.annotation.URIValue;
import no.difi.asic.util.EnumUtil;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;

import java.lang.reflect.Field;

/**
 * @author erlend
 */
public class ValueWrapper<T> {

    private T source;

    private Field field;

    public ValueWrapper(T value) {
        this.source = value;
        this.field = EnumUtil.getField(value);
    }

    public ASN1ObjectIdentifier getOid() {
        return new ASN1ObjectIdentifier(field.getAnnotation(OIDValue.class).value());
    }

    public String getString() {
        return field.getAnnotation(StringValue.class).value();
    }

    public T getSource() {
        return source;
    }

    public String getURI() {
        return field.getAnnotation(URIValue.class).value();
    }
}
