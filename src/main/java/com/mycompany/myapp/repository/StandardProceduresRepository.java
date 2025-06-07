package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.StandardProcedures;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the StandardProcedures entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StandardProceduresRepository extends JpaRepository<StandardProcedures, Long> {

    Page<StandardProcedures> findByUserUUID(String userUUID, Pageable pageable);
}
