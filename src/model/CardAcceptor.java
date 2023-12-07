package model;

import model.AbstractClasses.PaymentMethod;

public class CardAcceptor extends PaymentMethod {
    int cardNumber;
    int password;
    public CardAcceptor(int amount) {
        super(amount);
    }

    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setPassword(int password) {
        this.password = password;
    }
}
