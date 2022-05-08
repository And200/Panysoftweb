package co.edu.sena.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Employee.
 */
@Entity
@Table(name = "employee")
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "employee")
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

    public Employee id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Employee user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<PurchaseReceipt> getPurchaseReceipts() {
        return this.purchaseReceipts;
    }

    public void setPurchaseReceipts(Set<PurchaseReceipt> purchaseReceipts) {
        if (this.purchaseReceipts != null) {
            this.purchaseReceipts.forEach(i -> i.setEmployee(null));
        }
        if (purchaseReceipts != null) {
            purchaseReceipts.forEach(i -> i.setEmployee(this));
        }
        this.purchaseReceipts = purchaseReceipts;
    }

    public Employee purchaseReceipts(Set<PurchaseReceipt> purchaseReceipts) {
        this.setPurchaseReceipts(purchaseReceipts);
        return this;
    }

    public Employee addPurchaseReceipt(PurchaseReceipt purchaseReceipt) {
        this.purchaseReceipts.add(purchaseReceipt);
        purchaseReceipt.setEmployee(this);
        return this;
    }

    public Employee removePurchaseReceipt(PurchaseReceipt purchaseReceipt) {
        this.purchaseReceipts.remove(purchaseReceipt);
        purchaseReceipt.setEmployee(null);
        return this;
    }

    public Person getPerson() {
        return this.person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Employee person(Person person) {
        this.setPerson(person);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Employee)) {
            return false;
        }
        return id != null && id.equals(((Employee) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Employee{" +
            "id=" + getId() +
            "}";
    }
}
