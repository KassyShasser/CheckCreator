package checkApp.app;

import org.jetbrains.annotations.NotNull;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Check {
    private DiscountCard card;
    private ArrayList<Product> productList;
    private final double wholesaleDiscount = 10;
    private String header;

    public DiscountCard getCard() {
        return card;
    }

    public void setCard(DiscountCard card) {
        this.card = card;
    }

    public ArrayList<Product> getProductList() {
        return productList;
    }

    public void setProductList(@NotNull ArrayList<Product> productList) {
            this.productList = productList;
    }

    public double getWholesaleDiscount() {
        return wholesaleDiscount;
    }

    public String getHeader() {
        return header;
    }

    public void createHeader() {
        String headline = " ".repeat(20) + "CASH RECEIPT";
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String dateAndTime = String.format("DATE: %-30s TIME: %-30s", dateFormat.format(date), timeFormat.format(date));
        String cardDescription;
        if (card != null) {
            cardDescription = String.format("Discount card number \"%s\", amount of discount: %5.2f%%",
                    card.getNumberOfCard(), card.getDiscount());
        }
        else {
            cardDescription = "Discount card was not used.";
        }
        String caption = String.format("  %-4s %-30s %-7s %-8s", "QNT", "DESCRIPTION", "PRICE", "TOTAL");
        this.header = headline + "\n" + dateAndTime + "\n" + cardDescription + "\n"+ caption + "\n" + "_".repeat(63);
    }
}
