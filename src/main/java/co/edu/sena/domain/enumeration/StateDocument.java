package co.edu.sena.domain.enumeration;

/**
 * The StateDocument enumeration.
 */
public enum StateDocument {
    ACTIVE("ACTIVO"),
    INACTIVE("INACTIVO");

    private final String value;

    StateDocument(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
