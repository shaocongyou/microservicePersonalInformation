package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.StandardProcedures;
import com.mycompany.myapp.service.dto.StandardProceduresDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link StandardProcedures} and its DTO {@link StandardProceduresDTO}.
 */
@Mapper(componentModel = "spring")
public interface StandardProceduresMapper extends EntityMapper<StandardProceduresDTO, StandardProcedures> {}
