package checkApp.app;

import org.apache.commons.math3.util.Precision;
import org.jetbrains.annotations.NotNull;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CheckRunner {
    private String checkOutputFilename;

    public static void main(String[] args) {
        CheckRunner checkRunner = new CheckRunner();
        CheckBuilder checkBuilder = checkRunner.parseCommandLineArguments(args);
        Check check = checkBuilder.createCheck();
        checkRunner.printCheck(check);
        if (checkRunner.getCheckOutputFilename() != null) {
            checkRunner.writeCheckToFile(check, checkRunner.getCheckOutputFilename());
        }
    }

    public String getCheckOutputFilename() {
        return checkOutputFilename;
    }

    public void setCheckOutputFilename(String filename) {
        this.checkOutputFilename = filename;
    }

    public CheckBuilder parseCommandLineArguments(@NotNull String[] array) {
        ArrayList<Product> products = new ArrayList<>();
        ArrayList<DiscountCard> cards = new ArrayList<>();
        ArrayList<String> productsIF = new ArrayList<>();
        ArrayList<String> cardsIF = new ArrayList<>();
        ArrayList<String> checkOF = new ArrayList<>();
        for (String arg : array) {
            String[] substring = arg.split("-", 3);
            if (substring[0].equalsIgnoreCase("Card")) {
                DiscountCard card = new DiscountCard();
                card.setNumberOfCard(substring[1]);
                cards.add(card);
            }
            else if (substring[0].equalsIgnoreCase("File")) {
                switch (substring[1]) {
                    case "ProductsInput" -> productsIF.add(substring[2]);
                    case "CardsInput" -> cardsIF.add(substring[2]);
                    case "CheckOutput" -> checkOF.add(substring[2]);
                    default -> System.err.println("Warning: unknown file resource - " + substring[1] +
                            ". Required value: ProductsInput, CardsInput or CheckOutput.");
                }
            }
            else {
                Product product = new Product();
                product.setId(substring[0]);
                try {
                    product.setQuantity(Double.parseDouble(substring[1]));
                } catch (Exception ignore) {}
                products.add(product);
            }
        }

        CheckBuilder checkBuilder = new CheckBuilder();
        checkBuilder.setProductsList(products);
        checkBuilder.setCardsList(cards);
        if (!productsIF.isEmpty()) {
            if (productsIF.size() > 1) {
                System.err.println("Warning: several variants of files was offered as a source of products description, " +
                        "the last one was used.");
            }
            checkBuilder.setProductsInputFilename(productsIF.get(productsIF.size() - 1));
        }
        if (!cardsIF.isEmpty()) {
            if (cardsIF.size() > 1) {
                System.err.println("Warning: several variants of files was offered as a source of discount cards description, " +
                        "the last one was used.");
            }
            checkBuilder.setCardsInputFilename(cardsIF.get(cardsIF.size() - 1));
        }
        if (!checkOF.isEmpty()) {
            if (checkOF.size() > 1) {
                System.err.println("Warning: several variants of files to load a check was offered, " +
                        "the last one was used.");
            }
            setCheckOutputFilename(checkOF.get(checkOF.size() - 1));
        }

        return checkBuilder;
    }

    public ArrayList<String> processCheckToString(@NotNull Check check) {
        ArrayList<String> checkList = new ArrayList<>();
        checkList.add(check.getHeader());

        double discountCoefficient = 1;
        if (check.getCard() != null) {
            discountCoefficient = 1 - check.getCard().getDiscount() * 0.01;
        }

        double wholesaleDiscountCoefficient = 1 - check.getWholesaleDiscount() * 0.01;

        double totalCost = 0;
        for (Product product : check.getProductList()) {
            String productDescription = String.format("%7.3f %-30s $%-6.2f",
                    product.getQuantity(), product.getName(), product.getPrice());
            String priceDescription;
            double totalPrice = product.getPrice() * product.getQuantity() * discountCoefficient;
            if (product.getQuantity() >= 5) {
                totalPrice *=  wholesaleDiscountCoefficient;
                priceDescription = String.format("$%-9.2f (-10%%)", totalPrice);
            } else {
                priceDescription = String.format("$%-9.2f", totalPrice);
            }
            checkList.add(productDescription.concat(priceDescription));
            totalCost += Precision.round(totalPrice, 2);
        }
        checkList.add(String.format("_".repeat(63) + "\n" + "%-45s $%-12.2f", "TOTAL:", totalCost));
        return checkList;
    }

    public void printCheck(Check check) {
        ArrayList<String> list = processCheckToString(check);
        for (String s : list) {
            System.out.println(s);
        }
    }

    public void writeCheckToFile(Check check, String filename) {
        ArrayList<String> list = processCheckToString(check);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (String s : list) {
                writer.write(s + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error: some problems appeared uploading the check to the file: " +
                    this.getCheckOutputFilename());
        }
    }
}