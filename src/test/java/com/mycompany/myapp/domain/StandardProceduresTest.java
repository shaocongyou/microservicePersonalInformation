package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.StandardProceduresTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StandardProceduresTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StandardProcedures.class);
        StandardProcedures standardProcedures1 = getStandardProceduresSample1();
        StandardProcedures standardProcedures2 = new StandardProcedures();
        assertThat(standardProcedures1).isNotEqualTo(standardProcedures2);

        standardProcedures2.setId(standardProcedures1.getId());
        assertThat(standardProcedures1).isEqualTo(standardProcedures2);

        standardProcedures2 = getStandardProceduresSample2();
        assertThat(standardProcedures1).isNotEqualTo(standardProcedures2);
    }
}
