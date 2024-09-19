package ethereumfetcher.persistence.model;

public enum TransactionStatus {

    FAILURE((short) 0),
    SUCCESS((short) 1);

    private final Short status;

    TransactionStatus(Short status) {
        this.status = status;
    }

    public Short getStatus() {
        return status;
    }

    public static TransactionStatus fromValue(Short value) {
        if (value == 0) {
            return FAILURE;
        } else if (value == 1) {
            return SUCCESS;
        } else {
            throw new IllegalArgumentException("Invalid status value: " + value);
        }
    }
}
