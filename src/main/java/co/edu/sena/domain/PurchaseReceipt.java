package co.edu.sena.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A PurchaseReceipt.
 */
@Entity
@Table(name = "purchase_receipt")
public class PurchaseReceipt implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "date", nullable = false)
    private ZonedDateTime date;

    @NotNull
    @Column(name = "total_sale", nullable = false)
    private Double totalSale;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user", "purchaseReceipts", "person" }, allowSetters = true)
    private Employee employee;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "purchaseReceipts", "productElaborated", "presentation" }, allowSetters = true)
    private DetailSale detailSale;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "purchaseReceipts", "person" }, allowSetters = true)
    private Client client;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PurchaseReceipt id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDate() {
        return this.date;
    }

    public PurchaseReceipt date(ZonedDateTime date) {
        this.setDate(date);
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Double getTotalSale() {
        return this.totalSale;
    }

    public PurchaseReceipt totalSale(Double totalSale) {
        this.setTotalSale(totalSale);
        return this;
    }

    public void setTotalSale(Double totalSale) {
        this.totalSale = totalSale;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public PurchaseReceipt employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    public DetailSale getDetailSale() {
        return this.detailSale;
    }

    public void setDetailSale(DetailSale detailSale) {
        this.detailSale = detailSale;
    }

    public PurchaseReceipt detailSale(DetailSale detailSale) {
        this.setDetailSale(detailSale);
        return this;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public PurchaseReceipt client(Client client) {
        this.setClient(client);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PurchaseReceipt)) {
            return false;
        }
        return id != null && id.equals(((PurchaseReceipt) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PurchaseReceipt{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", totalSale=" + getTotalSale() +
            "}";
    }
}
