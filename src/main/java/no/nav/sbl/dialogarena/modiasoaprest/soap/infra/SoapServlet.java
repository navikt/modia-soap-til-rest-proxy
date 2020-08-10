package no.nav.sbl.dialogarena.modiasoaprest.soap.infra;

import no.nav.common.cxf.CXFEndpoint;
import no.nav.common.cxf.saml.CXFServletWithAuth;
import org.apache.cxf.BusFactory;
import org.apache.cxf.logging.FaultListener;

import javax.servlet.ServletConfig;
import java.util.Map;

public class SoapServlet extends CXFServletWithAuth {
    private final Map<String, Object> endpoints;

    public SoapServlet(Map<String, Object> endpoints) {
        this.endpoints = endpoints;
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
