package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.StandardProceduresAsserts.*;
import static com.mycompany.myapp.domain.StandardProceduresTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StandardProceduresMapperTest {

    private StandardProceduresMapper standardProceduresMapper;

    @BeforeEach
    void setUp() {
        standardProceduresMapper = new StandardProceduresMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getStandardProceduresSample1();
        var actual = standardProceduresMapper.toEntity(standardProceduresMapper.toDto(expected));
        assertStandardProceduresAllPropertiesEquals(expected, actual);
    }
}
