package co.edu.sena.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A DetailOrder.
 */
@Entity
@Table(name = "detail_order")
public class DetailOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "quantity_ordered", nullable = false)
    private Integer quantityOrdered;

    @NotNull
    @Column(name = "date", nullable = false)
    private ZonedDateTime date;

    @NotNull
    @Size(max = 30)
    @Column(name = "invoice_number", length = 30, nullable = false, unique = true)
    private String invoiceNumber;

    @NotNull
    @Size(max = 30)
    @Column(name = "product_ordered", length = 30, nullable = false)
    private String productOrdered;

    @NotNull
    @Column(name = "price_payment", nullable = false)
    private Double pricePayment;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "products", "detailOrders" }, allowSetters = true)
    private Provider provider;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "detailOrders" }, allowSetters = true)
    private OrderPlaced orderPlaced;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DetailOrder id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantityOrdered() {
        return this.quantityOrdered;
    }

    public DetailOrder quantityOrdered(Integer quantityOrdered) {
        this.setQuantityOrdered(quantityOrdered);
        return this;
    }

    public void setQuantityOrdered(Integer quantityOrdered) {
        this.quantityOrdered = quantityOrdered;
    }

    public ZonedDateTime getDate() {
        return this.date;
    }

    public DetailOrder date(ZonedDateTime date) {
        this.setDate(date);
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getInvoiceNumber() {
        return this.invoiceNumber;
    }

    public DetailOrder invoiceNumber(String invoiceNumber) {
        this.setInvoiceNumber(invoiceNumber);
        return this;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getProductOrdered() {
        return this.productOrdered;
    }

    public DetailOrder productOrdered(String productOrdered) {
        this.setProductOrdered(productOrdered);
        return this;
    }

    public void setProductOrdered(String productOrdered) {
        this.productOrdered = productOrdered;
    }

    public Double getPricePayment() {
        return this.pricePayment;
    }

    public DetailOrder pricePayment(Double pricePayment) {
        this.setPricePayment(pricePayment);
        return this;
    }

    public void setPricePayment(Double pricePayment) {
        this.pricePayment = pricePayment;
    }

    public Provider getProvider() {
        return this.provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public DetailOrder provider(Provider provider) {
        this.setProvider(provider);
        return this;
    }

    public OrderPlaced getOrderPlaced() {
        return this.orderPlaced;
    }

    public void setOrderPlaced(OrderPlaced orderPlaced) {
        this.orderPlaced = orderPlaced;
    }

    public DetailOrder orderPlaced(OrderPlaced orderPlaced) {
        this.setOrderPlaced(orderPlaced);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DetailOrder)) {
            return false;
        }
        return id != null && id.equals(((DetailOrder) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DetailOrder{" +
            "id=" + getId() +
            ", quantityOrdered=" + getQuantityOrdered() +
            ", date='" + getDate() + "'" +
            ", invoiceNumber='" + getInvoiceNumber() + "'" +
            ", productOrdered='" + getProductOrdered() + "'" +
            ", pricePayment=" + getPricePayment() +
            "}";
    }
}
