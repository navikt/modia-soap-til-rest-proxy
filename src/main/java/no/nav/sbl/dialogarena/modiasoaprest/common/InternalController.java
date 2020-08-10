package no.nav.sbl.dialogarena.modiasoaprest.common;

import no.nav.common.health.selftest.SelfTestCheck;
import no.nav.common.health.selftest.SelfTestUtils;
import no.nav.common.health.selftest.SelftTestCheckResult;
import no.nav.common.health.selftest.SelftestHtmlGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/internal")
public class InternalController {
    @Autowired
    private List<? extends SelfTestCheck> injectChecks;

    private final List<SelfTestCheck> selftestChecks;

    public InternalController() {
        this.selftestChecks = new ArrayList<>();
        this.selftestChecks.add(DiskCheck.asSelftestCheck());
        this.selftestChecks.add(TruststoreCheck.asSelftestCheck());
        this.selftestChecks.addAll(injectChecks);
    }

    @GetMapping("/isReady")
    public void isReady() {
    }

    @GetMapping("/isAlive")
    public void isAlive() {
    }

    @GetMapping("/selftest")
    public ResponseEntity selftest() {
        List<SelftTestCheckResult> result = SelfTestUtils.checkAllParallel(this.selftestChecks);

        return ResponseEntity
                .status(SelfTestUtils.findHttpStatusCode(result))
                .contentType(MediaType.TEXT_HTML)
                .body(SelftestHtmlGenerator.generate(result));
    }
}
