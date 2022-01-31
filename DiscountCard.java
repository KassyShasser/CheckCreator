package checkApp.app;

import org.jetbrains.annotations.NotNull;

public class DiscountCard {
    private String numberOfCard;
    private float discount;

    public String getNumberOfCard() {
        return numberOfCard;
    }

    public void setNumberOfCard(@NotNull String numberOfCard) {
        this.numberOfCard = numberOfCard;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        if (discount > 0 && discount < 100) {
            this.discount = discount;
        }
        else {
            System.err.println("Error: discount was not set. Required value - from 0,1% to 99,9%.");
        }
    }
}
