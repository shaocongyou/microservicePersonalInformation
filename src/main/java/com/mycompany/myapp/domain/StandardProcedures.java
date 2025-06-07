package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 规范流程。
 */
@Entity
@Table(name = "standard_procedures")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StandardProcedures implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Lob
    @Column(name = "specification", nullable = false)
    private String specification;

    @NotNull
    @Column(name = "user_uuid", nullable = false)
    private String userUUID;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public StandardProcedures id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public StandardProcedures isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getSpecification() {
        return this.specification;
    }

    public StandardProcedures specification(String specification) {
        this.setSpecification(specification);
        return this;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getUserUUID() {
        return this.userUUID;
    }

    public StandardProcedures userUUID(String userUUID) {
        this.setUserUUID(userUUID);
        return this;
    }

    public void setUserUUID(String userUUID) {
        this.userUUID = userUUID;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StandardProcedures)) {
            return false;
        }
        return getId() != null && getId().equals(((StandardProcedures) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StandardProcedures{" +
            "id=" + getId() +
            ", isActive='" + getIsActive() + "'" +
            ", specification='" + getSpecification() + "'" +
            ", userUUID='" + getUserUUID() + "'" +
            "}";
    }
}
