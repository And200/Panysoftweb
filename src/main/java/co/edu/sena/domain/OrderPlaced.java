package co.edu.sena.domain;

import co.edu.sena.domain.enumeration.StateOrder;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A OrderPlaced.
 */
@Entity
@Table(name = "order_placed")
public class OrderPlaced implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "order_placed_state", nullable = false)
    private StateOrder orderPlacedState;

    @OneToMany(mappedBy = "orderPlaced")
    @JsonIgnoreProperties(value = { "provider", "orderPlaced" }, allowSetters = true)
    private Set<DetailOrder> detailOrders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OrderPlaced id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StateOrder getOrderPlacedState() {
        return this.orderPlacedState;
    }

    public OrderPlaced orderPlacedState(StateOrder orderPlacedState) {
        this.setOrderPlacedState(orderPlacedState);
        return this;
    }

    public void setOrderPlacedState(StateOrder orderPlacedState) {
        this.orderPlacedState = orderPlacedState;
    }

    public Set<DetailOrder> getDetailOrders() {
        return this.detailOrders;
    }

    public void setDetailOrders(Set<DetailOrder> detailOrders) {
        if (this.detailOrders != null) {
            this.detailOrders.forEach(i -> i.setOrderPlaced(null));
        }
        if (detailOrders != null) {
            detailOrders.forEach(i -> i.setOrderPlaced(this));
        }
        this.detailOrders = detailOrders;
    }

    public OrderPlaced detailOrders(Set<DetailOrder> detailOrders) {
        this.setDetailOrders(detailOrders);
        return this;
    }

    public OrderPlaced addDetailOrder(DetailOrder detailOrder) {
        this.detailOrders.add(detailOrder);
        detailOrder.setOrderPlaced(this);
        return this;
    }

    public OrderPlaced removeDetailOrder(DetailOrder detailOrder) {
        this.detailOrders.remove(detailOrder);
        detailOrder.setOrderPlaced(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderPlaced)) {
            return false;
        }
        return id != null && id.equals(((OrderPlaced) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderPlaced{" +
            "id=" + getId() +
            ", orderPlacedState='" + getOrderPlacedState() + "'" +
            "}";
    }
}
