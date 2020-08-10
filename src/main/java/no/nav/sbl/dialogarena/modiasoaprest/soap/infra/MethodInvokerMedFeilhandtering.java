package no.nav.sbl.dialogarena.modiasoaprest.soap.infra;

import no.nav.common.types.feil.Feil;
import no.nav.common.types.feil.FeilDTO;
import no.nav.common.types.feil.FeilType;
import org.apache.cxf.jaxws.JAXWSMethodInvoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.soap.SOAPFaultException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static javax.xml.transform.OutputKeys.INDENT;
import static net.logstash.logback.encoder.org.apache.commons.lang.exception.ExceptionUtils.getStackTrace;
import static no.nav.common.json.JsonUtils.toJson;
import static no.nav.common.types.feil.FeilType.*;
import static no.nav.common.utils.EnvironmentUtils.isDevelopment;
import static no.nav.common.utils.StringUtils.notNullOrEmpty;

public class MethodInvokerMedFeilhandtering extends JAXWSMethodInvoker {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodInvokerMedFeilhandtering.class);

    private final SOAPFactory soapFactory;

    public MethodInvokerMedFeilhandtering(Object serviceBean) {
        super(serviceBean);
        try {
            soapFactory = SOAPFactory.newInstance();
        } catch (SOAPException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected SOAPFaultException findSoapFaultException(Throwable throwable) {
        FeilDTO feilDTO = new FeilDTO(
                UUID.randomUUID().toString(),
                getType(throwable).getName(),
                isDevelopment().orElse(false) ? finnDetaljer(throwable) : null
        );
        try {
            SOAPFault fault = soapFactory.createFault();
            fault.setFaultString(feilDTO.id);
            fault.setFaultCode(feilDTO.type);
            String detaljerJson = toJson(feilDTO.detaljer);
            if (notNullOrEmpty(detaljerJson)) {
                fault.addDetail().addTextNode(detaljerJson);
            }
            return new SOAPFaultException(fault);
        } catch (SOAPException e) {
            LOGGER.error(e.getMessage(), e);
            return super.findSoapFaultException(throwable);
        }
    }

    public static Feil.Type getType(Throwable throwable) {
        if (throwable instanceof Feil) {
            return ((Feil) throwable).getType();
        } else if (throwable instanceof SOAPFaultException) {
            return valueOf(FeilType.class, ((SOAPFaultException) throwable).getFault().getFaultCodeAsName().getLocalName()).orElse(UKJENT);
        } else if (throwable instanceof IllegalArgumentException) {
            return UGYLDIG_REQUEST;
        } else if (throwable instanceof NotFoundException) {
            return FINNES_IKKE;
        } else if (throwable instanceof NotAuthorizedException) {
            return INGEN_TILGANG;
        } else {
            return UKJENT;
        }
    }

    private static FeilDTO.Detaljer finnDetaljer(Throwable exception) {
        return new FeilDTO.Detaljer(exception.getClass().getName(), exception.getMessage(), finnStackTrace(exception));
    }

    private static String finnStackTrace(Throwable exception) {
        String stackTrace = getStackTrace(exception);
        if (exception instanceof SOAPFaultException) {
            return finnSoapStacktrace((SOAPFaultException) exception);
        } else {
            return stackTrace;
        }
    }

    private static String finnSoapStacktrace(SOAPFaultException exception) {
        try {
            StringWriter writer = new StringWriter();
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(INDENT, "yes");
            transformer.transform(new DOMSource(exception.getFault()), new StreamResult(writer));
            return writer.toString();
        } catch (Exception e) {
            return exception.getMessage();
        }
    }

    private static <T extends Enum> Optional<T> valueOf(Class<T> enumClass, String name) {
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(e -> e.name().equals(name))
                .findAny();
    }
}
