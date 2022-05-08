package co.edu.sena.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Client.
 */
@Entity
@Table(name = "client")
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToMany(mappedBy = "client")
    @JsonIgnoreProperties(value = { "employee", "detailSale", "client" }, allowSetters = true)
    private Set<PurchaseReceipt> purchaseReceipts = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "employees", "clients", "documentType" }, allowSetters = true)
    private Person person;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Client id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<PurchaseReceipt> getPurchaseReceipts() {
        return this.purchaseReceipts;
    }

    public void setPurchaseReceipts(Set<PurchaseReceipt> purchaseReceipts) {
        if (this.purchaseReceipts != null) {
            this.purchaseReceipts.forEach(i -> i.setClient(null));
        }
        if (purchaseReceipts != null) {
            purchaseReceipts.forEach(i -> i.setClient(this));
        }
        this.purchaseReceipts = purchaseReceipts;
    }

    public Client purchaseReceipts(Set<PurchaseReceipt> purchaseReceipts) {
        this.setPurchaseReceipts(purchaseReceipts);
        return this;
    }

    public Client addPurchaseReceipt(PurchaseReceipt purchaseReceipt) {
        this.purchaseReceipts.add(purchaseReceipt);
        purchaseReceipt.setClient(this);
        return this;
    }

    public Client removePurchaseReceipt(PurchaseReceipt purchaseReceipt) {
        this.purchaseReceipts.remove(purchaseReceipt);
        purchaseReceipt.setClient(null);
        return this;
    }

    public Person getPerson() {
        return this.person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Client person(Person person) {
        this.setPerson(person);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Client)) {
            return false;
        }
        return id != null && id.equals(((Client) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Client{" +
            "id=" + getId() +
            "}";
    }
}
