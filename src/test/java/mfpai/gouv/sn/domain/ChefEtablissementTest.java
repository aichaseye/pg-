package mfpai.gouv.sn.domain;

import static org.assertj.core.api.Assertions.assertThat;

import mfpai.gouv.sn.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ChefEtablissementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChefEtablissement.class);
        ChefEtablissement chefEtablissement1 = new ChefEtablissement();
        chefEtablissement1.setId(1L);
        ChefEtablissement chefEtablissement2 = new ChefEtablissement();
        chefEtablissement2.setId(chefEtablissement1.getId());
        assertThat(chefEtablissement1).isEqualTo(chefEtablissement2);
        chefEtablissement2.setId(2L);
        assertThat(chefEtablissement1).isNotEqualTo(chefEtablissement2);
        chefEtablissement1.setId(null);
        assertThat(chefEtablissement1).isNotEqualTo(chefEtablissement2);
    }
}
