package no.nav.sbl.dialogarena.modiasoaprest.soap.infra;

import no.nav.common.auth.subject.Subject;
import no.nav.common.cxf.CXFEndpoint;
import no.nav.common.cxf.saml.SAMLInInterceptor;
import org.apache.cxf.BusFactory;
import org.apache.cxf.logging.FaultListener;
import org.apache.cxf.transport.servlet.CXFNonSpringServlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.Map;

import static no.nav.common.auth.subject.SubjectHandler.withSubjectProvider;

public class SoapServlet extends CXFNonSpringServlet {
    private final Map<String, Object> endpoints;

    public SoapServlet(Map<String, Object> endpoints) {
        this.endpoints = endpoints;
    }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        withSubjectProvider(
                () -> (Subject) req.getAttribute(SAMLInInterceptor.SUBJECT_REQUEST_ATTRIBUTE_NAME),
                () -> super.service(req, res)
        );
    }


    @Override
    protected void loadBus(ServletConfig sc) {
        super.loadBus(sc);
        BusFactory.setDefaultBus(getBus());

        endpoints.forEach(SoapServlet::loadWSService);
    }

    private static void loadWSService(String url, Object serviceBean) {
        CXFEndpoint cxfEndpoint = new CXFEndpoint()
                .address(url)
                .serviceBean(serviceBean);

        cxfEndpoint.factoryBean.setInvoker(new MethodInvokerMedFeilhandtering(serviceBean));
        cxfEndpoint.setProperty(FaultListener.class.getName(), new SoapFaultListener());

        cxfEndpoint.create();
    }
}
