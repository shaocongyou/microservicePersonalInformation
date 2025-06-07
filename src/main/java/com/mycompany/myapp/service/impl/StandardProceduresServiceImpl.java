package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.StandardProcedures;
import com.mycompany.myapp.repository.StandardProceduresRepository;
import com.mycompany.myapp.service.StandardProceduresService;
import com.mycompany.myapp.service.dto.StandardProceduresDTO;
import com.mycompany.myapp.service.mapper.StandardProceduresMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.StandardProcedures}.
 */
@Service
@Transactional
public class StandardProceduresServiceImpl implements StandardProceduresService {

    private static final Logger LOG = LoggerFactory.getLogger(StandardProceduresServiceImpl.class);

    private final StandardProceduresRepository standardProceduresRepository;

    private final StandardProceduresMapper standardProceduresMapper;

    public StandardProceduresServiceImpl(
        StandardProceduresRepository standardProceduresRepository,
        StandardProceduresMapper standardProceduresMapper
    ) {
        this.standardProceduresRepository = standardProceduresRepository;
        this.standardProceduresMapper = standardProceduresMapper;
    }

    @Override
    public StandardProceduresDTO save(StandardProceduresDTO standardProceduresDTO) {
        LOG.debug("Request to save StandardProcedures : {}", standardProceduresDTO);
        StandardProcedures standardProcedures = standardProceduresMapper.toEntity(standardProceduresDTO);
        standardProcedures = standardProceduresRepository.save(standardProcedures);
        return standardProceduresMapper.toDto(standardProcedures);
    }

    @Override
    public StandardProceduresDTO update(StandardProceduresDTO standardProceduresDTO) {
        LOG.debug("Request to update StandardProcedures : {}", standardProceduresDTO);
        StandardProcedures standardProcedures = standardProceduresMapper.toEntity(standardProceduresDTO);
        standardProcedures = standardProceduresRepository.save(standardProcedures);
        return standardProceduresMapper.toDto(standardProcedures);
    }

    @Override
    public Optional<StandardProceduresDTO> partialUpdate(StandardProceduresDTO standardProceduresDTO) {
        LOG.debug("Request to partially update StandardProcedures : {}", standardProceduresDTO);

        return standardProceduresRepository
            .findById(standardProceduresDTO.getId())
            .map(existingStandardProcedures -> {
                standardProceduresMapper.partialUpdate(existingStandardProcedures, standardProceduresDTO);

                return existingStandardProcedures;
            })
            .map(standardProceduresRepository::save)
            .map(standardProceduresMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StandardProceduresDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all StandardProcedures");
        return standardProceduresRepository.findAll(pageable).map(standardProceduresMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StandardProceduresDTO> findOne(Long id) {
        LOG.debug("Request to get StandardProcedures : {}", id);
        return standardProceduresRepository.findById(id).map(standardProceduresMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete StandardProcedures : {}", id);
        standardProceduresRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StandardProceduresDTO> findAllByUserUuid(String userUuid, Pageable pageable) {
        LOG.debug("Request to get all StandardProcedures for user UUID : {}", userUuid);
        return standardProceduresRepository.findByUserUUID(userUuid, pageable)
            .map(standardProceduresMapper::toDto);
    }
}
