package model.AbstractClasses;

public abstract class PaymentMethod {
    int amount;

    public PaymentMethod(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
