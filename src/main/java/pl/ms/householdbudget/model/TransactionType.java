package pl.ms.householdbudget.model;

public enum TransactionType {
    EXPENSE("wydatek"), EARNING("przychód");

    private final String description;

    public String getDescription() {
        return description;
    }

    TransactionType(String description) {
        this.description = description;
    }
}
