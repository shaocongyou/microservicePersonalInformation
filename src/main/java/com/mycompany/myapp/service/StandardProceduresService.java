package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.StandardProceduresDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.StandardProcedures}.
 */
public interface StandardProceduresService {
    /**
     * Save a standardProcedures.
     *
     * @param standardProceduresDTO the entity to save.
     * @return the persisted entity.
     */
    StandardProceduresDTO save(StandardProceduresDTO standardProceduresDTO);

    /**
     * Updates a standardProcedures.
     *
     * @param standardProceduresDTO the entity to update.
     * @return the persisted entity.
     */
    StandardProceduresDTO update(StandardProceduresDTO standardProceduresDTO);

    /**
     * Partially updates a standardProcedures.
     *
     * @param standardProceduresDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<StandardProceduresDTO> partialUpdate(StandardProceduresDTO standardProceduresDTO);

    /**
     * Get all the standardProcedures.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<StandardProceduresDTO> findAll(Pageable pageable);

    /**
     * Get the "id" standardProcedures.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StandardProceduresDTO> findOne(Long id);

    /**
     * Delete the "id" standardProcedures.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
