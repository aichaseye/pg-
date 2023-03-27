package mfpai.gouv.sn.domain;

import static org.assertj.core.api.Assertions.assertThat;

import mfpai.gouv.sn.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BFPATest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BFPA.class);
        BFPA bFPA1 = new BFPA();
        bFPA1.setId(1L);
        BFPA bFPA2 = new BFPA();
        bFPA2.setId(bFPA1.getId());
        assertThat(bFPA1).isEqualTo(bFPA2);
        bFPA2.setId(2L);
        assertThat(bFPA1).isNotEqualTo(bFPA2);
        bFPA1.setId(null);
        assertThat(bFPA1).isNotEqualTo(bFPA2);
    }
}
