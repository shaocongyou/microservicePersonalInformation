package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StandardProceduresDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StandardProceduresDTO.class);
        StandardProceduresDTO standardProceduresDTO1 = new StandardProceduresDTO();
        standardProceduresDTO1.setId(1L);
        StandardProceduresDTO standardProceduresDTO2 = new StandardProceduresDTO();
        assertThat(standardProceduresDTO1).isNotEqualTo(standardProceduresDTO2);
        standardProceduresDTO2.setId(standardProceduresDTO1.getId());
        assertThat(standardProceduresDTO1).isEqualTo(standardProceduresDTO2);
        standardProceduresDTO2.setId(2L);
        assertThat(standardProceduresDTO1).isNotEqualTo(standardProceduresDTO2);
        standardProceduresDTO1.setId(null);
        assertThat(standardProceduresDTO1).isNotEqualTo(standardProceduresDTO2);
    }
}
