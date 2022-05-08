package co.edu.sena.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A MeasureUnit.
 */
@Entity
@Table(name = "measure_unit")
public class MeasureUnit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 30)
    @Column(name = "name_unit", length = 30, nullable = false, unique = true)
    private String nameUnit;

    @OneToMany(mappedBy = "measureUnit")
    @JsonIgnoreProperties(value = { "detailSales", "products", "measureUnit" }, allowSetters = true)
    private Set<Presentation> presentations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MeasureUnit id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameUnit() {
        return this.nameUnit;
    }

    public MeasureUnit nameUnit(String nameUnit) {
        this.setNameUnit(nameUnit);
        return this;
    }

    public void setNameUnit(String nameUnit) {
        this.nameUnit = nameUnit;
    }

    public Set<Presentation> getPresentations() {
        return this.presentations;
    }

    public void setPresentations(Set<Presentation> presentations) {
        if (this.presentations != null) {
            this.presentations.forEach(i -> i.setMeasureUnit(null));
        }
        if (presentations != null) {
            presentations.forEach(i -> i.setMeasureUnit(this));
        }
        this.presentations = presentations;
    }

    public MeasureUnit presentations(Set<Presentation> presentations) {
        this.setPresentations(presentations);
        return this;
    }

    public MeasureUnit addPresentation(Presentation presentation) {
        this.presentations.add(presentation);
        presentation.setMeasureUnit(this);
        return this;
    }

    public MeasureUnit removePresentation(Presentation presentation) {
        this.presentations.remove(presentation);
        presentation.setMeasureUnit(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MeasureUnit)) {
            return false;
        }
        return id != null && id.equals(((MeasureUnit) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MeasureUnit{" +
            "id=" + getId() +
            ", nameUnit='" + getNameUnit() + "'" +
            "}";
    }
}
