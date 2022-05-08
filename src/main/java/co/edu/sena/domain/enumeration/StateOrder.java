package co.edu.sena.domain.enumeration;

/**
 * The StateOrder enumeration.
 */
public enum StateOrder {
    DELIVERED("ENTREGADO"),
    UNDELIVERED("NOENTREGADO");

    private final String value;

    StateOrder(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
