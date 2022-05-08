package co.edu.sena.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Person.
 */
@Entity
@Table(name = "person")
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @NotNull
    @Size(max = 50)
    @Column(name = "email", length = 50, nullable = false, unique = true)
    private String email;

    @NotNull
    @Size(max = 40)
    @Column(name = "adress", length = 40, nullable = false)
    private String adress;

    @OneToMany(mappedBy = "person")
    @JsonIgnoreProperties(value = { "user", "purchaseReceipts", "person" }, allowSetters = true)
    private Set<Employee> employees = new HashSet<>();

    @OneToMany(mappedBy = "person")
    @JsonIgnoreProperties(value = { "purchaseReceipts", "person" }, allowSetters = true)
    private Set<Client> clients = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "people" }, allowSetters = true)
    private DocumentType documentType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Person id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Person name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public Person email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdress() {
        return this.adress;
    }

    public Person adress(String adress) {
        this.setAdress(adress);
        return this;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public Set<Employee> getEmployees() {
        return this.employees;
    }

    public void setEmployees(Set<Employee> employees) {
        if (this.employees != null) {
            this.employees.forEach(i -> i.setPerson(null));
        }
        if (employees != null) {
            employees.forEach(i -> i.setPerson(this));
        }
        this.employees = employees;
    }

    public Person employees(Set<Employee> employees) {
        this.setEmployees(employees);
        return this;
    }

    public Person addEmployee(Employee employee) {
        this.employees.add(employee);
        employee.setPerson(this);
        return this;
    }

    public Person removeEmployee(Employee employee) {
        this.employees.remove(employee);
        employee.setPerson(null);
        return this;
    }

    public Set<Client> getClients() {
        return this.clients;
    }

    public void setClients(Set<Client> clients) {
        if (this.clients != null) {
            this.clients.forEach(i -> i.setPerson(null));
        }
        if (clients != null) {
            clients.forEach(i -> i.setPerson(this));
        }
        this.clients = clients;
    }

    public Person clients(Set<Client> clients) {
        this.setClients(clients);
        return this;
    }

    public Person addClient(Client client) {
        this.clients.add(client);
        client.setPerson(this);
        return this;
    }

    public Person removeClient(Client client) {
        this.clients.remove(client);
        client.setPerson(null);
        return this;
    }

    public DocumentType getDocumentType() {
        return this.documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public Person documentType(DocumentType documentType) {
        this.setDocumentType(documentType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Person)) {
            return false;
        }
        return id != null && id.equals(((Person) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Person{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", email='" + getEmail() + "'" +
            ", adress='" + getAdress() + "'" +
            "}";
    }
}
