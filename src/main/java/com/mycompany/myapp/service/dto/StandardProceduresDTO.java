package com.mycompany.myapp.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.StandardProcedures} entity.
 */
@Schema(description = "规范流程。")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StandardProceduresDTO implements Serializable {

    private Long id;

    @NotNull
    private Boolean isActive;

    @Lob
    private String specification;

    @NotNull
    private String userLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StandardProceduresDTO)) {
            return false;
        }

        StandardProceduresDTO standardProceduresDTO = (StandardProceduresDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, standardProceduresDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StandardProceduresDTO{" +
            "id=" + getId() +
            ", isActive='" + getIsActive() + "'" +
            ", specification='" + getSpecification() + "'" +
            ", userLogin='" + getUserLogin() + "'" +
            "}";
    }
}
