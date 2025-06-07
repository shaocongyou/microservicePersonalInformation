package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.StandardProceduresRepository;
import com.mycompany.myapp.security.SecurityUtils;
import com.mycompany.myapp.service.StandardProceduresService;
import com.mycompany.myapp.service.dto.StandardProceduresDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.StandardProcedures}.
 */
@RestController
@RequestMapping("/api/standard-procedures")
public class StandardProceduresResource {

    private static final Logger LOG = LoggerFactory.getLogger(StandardProceduresResource.class);

    private static final String ENTITY_NAME = "microservicePersonalInformationStandardProcedures";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StandardProceduresService standardProceduresService;

    private final StandardProceduresRepository standardProceduresRepository;

    public StandardProceduresResource(
        StandardProceduresService standardProceduresService,
        StandardProceduresRepository standardProceduresRepository
    ) {
        this.standardProceduresService = standardProceduresService;
        this.standardProceduresRepository = standardProceduresRepository;
    }

    /**
     * Get the current user UUID or throw an access denied exception.
     * @return the current user UUID
     * @throws AccessDeniedException if user is not authenticated
     */
    private String getCurrentUserUuidOrThrow() {
        return SecurityUtils.getCurrentUserUuid()
            .orElseThrow(() -> new AccessDeniedException("User not authenticated"));
    }

    /**
     * {@code POST  /standard-procedures} : Create a new standardProcedures.
     *
     * @param standardProceduresDTO the standardProceduresDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new standardProceduresDTO, or with status {@code 400 (Bad Request)} if the standardProcedures has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<StandardProceduresDTO> createStandardProcedures(@Valid @RequestBody StandardProceduresDTO standardProceduresDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save StandardProcedures : {}", standardProceduresDTO);
        if (standardProceduresDTO.getId() != null) {
            throw new BadRequestAlertException("A new standardProcedures cannot already have an ID", ENTITY_NAME, "idexists");
        }
        String currentUserUuid = getCurrentUserUuidOrThrow();
        standardProceduresDTO.setUserUUID(currentUserUuid);
        standardProceduresDTO = standardProceduresService.save(standardProceduresDTO);
        LOG.debug("Created StandardProcedures for user UUID: {}", currentUserUuid);
        return ResponseEntity.created(new URI("/api/standard-procedures/" + standardProceduresDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, standardProceduresDTO.getId().toString()))
            .body(standardProceduresDTO);
    }

    /**
     * {@code PUT  /standard-procedures/:id} : Updates an existing standardProcedures.
     *
     * @param id the id of the standardProceduresDTO to save.
     * @param standardProceduresDTO the standardProceduresDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated standardProceduresDTO,
     * or with status {@code 400 (Bad Request)} if the standardProceduresDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the standardProceduresDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<StandardProceduresDTO> updateStandardProcedures(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody StandardProceduresDTO standardProceduresDTO
    ) throws URISyntaxException {
        String currentUserUuid = getCurrentUserUuidOrThrow();
        LOG.debug("REST request to update StandardProcedures : {}, {} for user UUID: {}", id, standardProceduresDTO, currentUserUuid);

        if (standardProceduresDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, standardProceduresDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        // Verify the existing record belongs to current user
        Optional<StandardProceduresDTO> existingDTO = standardProceduresService.findOne(id);
        if (existingDTO.isEmpty() || !currentUserUuid.equals(existingDTO.get().getUserUUID())) {
            LOG.warn("User {} attempted to update StandardProcedures {} belonging to another user",
                    currentUserUuid, id);
            return ResponseEntity.status(403).build();
        }

        // Ensure userUUID cannot be modified
        standardProceduresDTO.setUserUUID(currentUserUuid);

        standardProceduresDTO = standardProceduresService.update(standardProceduresDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, standardProceduresDTO.getId().toString()))
            .body(standardProceduresDTO);
    }

    /**
     * {@code PATCH  /standard-procedures/:id} : Partial updates given fields of an existing standardProcedures, field will ignore if it is null
     *
     * @param id the id of the standardProceduresDTO to save.
     * @param standardProceduresDTO the standardProceduresDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated standardProceduresDTO,
     * or with status {@code 400 (Bad Request)} if the standardProceduresDTO is not valid,
     * or with status {@code 404 (Not Found)} if the standardProceduresDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the standardProceduresDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StandardProceduresDTO> partialUpdateStandardProcedures(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody StandardProceduresDTO standardProceduresDTO
    ) throws URISyntaxException {
        String currentUserUuid = getCurrentUserUuidOrThrow();
        LOG.debug("REST request to partial update StandardProcedures partially : {}, {} for user UUID: {}",
            id, standardProceduresDTO, currentUserUuid);

        if (standardProceduresDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, standardProceduresDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        // Verify the existing record belongs to current user
        Optional<StandardProceduresDTO> existingDTO = standardProceduresService.findOne(id);
        if (existingDTO.isEmpty() || !currentUserUuid.equals(existingDTO.get().getUserUUID())) {
            LOG.warn("User {} attempted to update StandardProcedures {} belonging to another user",
                    currentUserUuid, id);
            return ResponseEntity.status(403).build();
        }

        // Ensure userUUID cannot be modified
        standardProceduresDTO.setUserUUID(currentUserUuid);

        Optional<StandardProceduresDTO> result = standardProceduresService.partialUpdate(standardProceduresDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, standardProceduresDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /standard-procedures} : get all the standardProcedures.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of standardProcedures in body.
     */
    @GetMapping("")
    public ResponseEntity<List<StandardProceduresDTO>> getAllStandardProcedures(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        String currentUserUuid = getCurrentUserUuidOrThrow();
        LOG.debug("REST request to get a page of StandardProcedures for user UUID: {}", currentUserUuid);
        Page<StandardProceduresDTO> page = standardProceduresService.findAllByUserUuid(currentUserUuid, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /standard-procedures/:id} : get the "id" standardProcedures.
     *
     * @param id the id of the standardProceduresDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the standardProceduresDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StandardProceduresDTO> getStandardProcedures(@PathVariable("id") Long id) {
        String currentUserUuid = getCurrentUserUuidOrThrow();
        LOG.debug("REST request to get StandardProcedures : {} for user UUID: {}", id, currentUserUuid);
        Optional<StandardProceduresDTO> standardProceduresDTO = standardProceduresService.findOne(id);

        if (standardProceduresDTO.isPresent() && !currentUserUuid.equals(standardProceduresDTO.get().getUserUUID())) {
            LOG.warn("User {} attempted to access StandardProcedures {} belonging to another user",
                    currentUserUuid, id);
            return ResponseEntity.status(403).build();
        }

        return ResponseUtil.wrapOrNotFound(standardProceduresDTO);
    }

    /**
     * {@code GET  /standard-procedures/current-user-info} : get the current user's information including UUID and login.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body containing user info,
     *         or with status {@code 401 (Unauthorized)} if user is not authenticated.
     */
    @GetMapping("/current-user-info")
    public ResponseEntity<Map<String, String>> getCurrentUserInfo() {
        LOG.debug("REST request to get current user info");

        Optional<String> userUuid = SecurityUtils.getCurrentUserUuid();
        Optional<String> currentUserLogin = SecurityUtils.getCurrentUserLogin();

        if (userUuid.isPresent()) {
            Map<String, String> userInfo = new HashMap<>();
            userInfo.put("uuid", userUuid.get());
            userInfo.put("login", currentUserLogin.orElse("unknown"));

            LOG.debug("Current user info - UUID: {}, Login: {}", userUuid.get(), currentUserLogin.orElse("unknown"));
            return ResponseEntity.ok(userInfo);
        } else {
            LOG.warn("No user found in security context");
            return ResponseEntity.status(401).build();
        }
    }

    /**
     * {@code DELETE  /standard-procedures/:id} : delete the "id" standardProcedures.
     *
     * @param id the id of the standardProceduresDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStandardProcedures(@PathVariable("id") Long id) {
        String currentUserUuid = getCurrentUserUuidOrThrow();
        LOG.debug("REST request to delete StandardProcedures : {} for user UUID: {}", id, currentUserUuid);

        Optional<StandardProceduresDTO> existingDTO = standardProceduresService.findOne(id);
        if (existingDTO.isEmpty() || !currentUserUuid.equals(existingDTO.get().getUserUUID())) {
            LOG.warn("User {} attempted to delete StandardProcedures {} belonging to another user",
                    currentUserUuid, id);
            return ResponseEntity.status(403).build();
        }

        standardProceduresService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
