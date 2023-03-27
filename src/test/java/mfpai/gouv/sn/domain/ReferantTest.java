package mfpai.gouv.sn.domain;

import static org.assertj.core.api.Assertions.assertThat;

import mfpai.gouv.sn.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReferantTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Referant.class);
        Referant referant1 = new Referant();
        referant1.setId(1L);
        Referant referant2 = new Referant();
        referant2.setId(referant1.getId());
        assertThat(referant1).isEqualTo(referant2);
        referant2.setId(2L);
        assertThat(referant1).isNotEqualTo(referant2);
        referant1.setId(null);
        assertThat(referant1).isNotEqualTo(referant2);
    }
}
