package checkApp.app;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class CheckBuilder {
    private HashMap<String, Product> allProducts;
    private HashMap<String, DiscountCard> allCards;
    private ArrayList<DiscountCard> cards;
    private ArrayList<Product> products;
    private String productsInputFilename, cardsInputFilename;

    public ArrayList<DiscountCard> getCards() {
        return cards;
    }

    public void setCardsList(ArrayList<DiscountCard> cards) {
        this.cards = cards;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProductsList(ArrayList<Product> products) {
        this.products = products;
    }

    public String getProductsInputFilename() {
        return productsInputFilename;
    }

    public void setProductsInputFilename(String filename) {
        this.productsInputFilename = filename;
    }

    public String getCardsInputFilename() {
        return cardsInputFilename;
    }

    public void setCardsInputFilename(String filename) {
        this.cardsInputFilename = filename;
    }

    public HashMap<String, Product> getAllProductsMap() {
        return allProducts;
    }

    public void setAllProductsMap(HashMap<String, Product>  allProducts) {
        this.allProducts = allProducts;
    }

    public HashMap<String, DiscountCard> getAllCardsMap() {
        return allCards;
    }

    public void setAllCardsMap(HashMap<String, DiscountCard>  allCards) {
        this.allCards = allCards;
    }

    public ArrayList<String> readLinesFromFile(String filename) {
        ArrayList<String> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            while (reader.ready()) {
                list.add(reader.readLine());
            }
        }
        catch (IOException e) {
            System.err.println("Error: some problems appeared while loading from file \"" + filename + "\".");
            return null;
        }
        return list;
    }

    public void loadAllProducts() {
        HashMap<String, Product> allProductsMap = new HashMap<>();
        ArrayList<String> allProductsPreview = null;
        if (getProductsInputFilename() != null) {
            allProductsPreview = readLinesFromFile(getProductsInputFilename());
        }
        if (allProductsPreview == null) {
            System.err.println("Warning: some problems appeared while file loading, default set of products was used.");
            allProductsPreview = new ArrayList<>();
            allProductsPreview.add("1 Loren ipsum 1.55");
            allProductsPreview.add("2 Dolor 2.34");
            allProductsPreview.add("3 Sir axet 3.32");
            allProductsPreview.add("4 Consextetur adiping 10.50");
            allProductsPreview.add("5 Elit 3.12");
        }
        for (String line : allProductsPreview) {
            Product product = new Product();
            String[] array = line.split(" ");
            String id = array[0];
            product.setId(id);
            String name = "";
            for (int i = 1; i < array.length - 1; i++) {
                name = name.concat(array[i] + " ");
            }
            product.setName(name);
            try {
                double price = Double.parseDouble(array[array.length - 1]);
                product.setPrice(price);
            } catch (Exception ignore) {}
            allProductsMap.put(id, product);
        }
        setAllProductsMap(allProductsMap);
    }

    public void loadAllCards() {
        HashMap<String, DiscountCard> allCardsMap = new HashMap<>();
        ArrayList<String> allCardsPreview = null;
        if (getCardsInputFilename() != null) {
            allCardsPreview = readLinesFromFile(getCardsInputFilename());
        }
        if (allCardsPreview == null) {
            System.err.println("Warning: some problems appeared while file loading, default set of cards was used.");
            allCardsPreview = new ArrayList<>();
            allCardsPreview.add("1 0.1");
            allCardsPreview.add("2 1");
            allCardsPreview.add("3 1.5");
            allCardsPreview.add("4 1.9");
            allCardsPreview.add("5 2");
        }
        for (String line : allCardsPreview) {
            DiscountCard card = new DiscountCard();
            String[] array = line.split(" ");
            String number = array[0];
            card.setNumberOfCard(number);
            try {
                card.setDiscount(Float.parseFloat(array[1]));
            } catch (Exception ignore) {}
            allCardsMap.put(number, card);
        }
        setAllCardsMap(allCardsMap);
    }

    public Check createCheck() {
        loadAllCards();
        loadAllProducts();

        Check check = new Check();

        float maxDiscount = 0;
        if (cards.size() > 1) {
            System.err.println("Warning: several discount cards were offered, the card with the highest discount was used.");
        }
        for (DiscountCard card : cards) {
            if (allCards.containsKey(card.getNumberOfCard())) {
                card.setDiscount(allCards.get(card.getNumberOfCard()).getDiscount());
                if (maxDiscount < card.getDiscount()) {
                    maxDiscount = card.getDiscount();
                    check.setCard(card);
                }
            } else {
                System.err.println("Error: discount card with number \"" + card.getNumberOfCard()
                        + "\" was not found.");
            }
        }

        ArrayList<Product> productsWithoutRepeats = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            Product product1 = products.get(i);
            if (!product1.getId().equals("0")) {
                for (int j = i + 1; j < products.size(); j++) {
                    Product product2 = products.get(j);
                    if (product1.getId().equals(product2.getId())) {
                        product1.setQuantity(product1.getQuantity() + product2.getQuantity());
                        product2.setId("0");
                    }
                }
                productsWithoutRepeats.add(product1);
            }
        }

        ArrayList<Product> productList = new ArrayList<>();
        for (Product product : productsWithoutRepeats) {
            if (allProducts.containsKey(product.getId())) {
                Product productDescription = allProducts.get(product.getId());
                product.setName(productDescription.getName());
                product.setPrice(productDescription.getPrice());
                if (product.getQuantity() > 0) {
                    productList.add(product);
                } else {
                    System.err.println("Warning: quantity of product \"" + product.getName() + "\" was set to 0 and " +
                            "this product was not added to the final check.");
                }
            } else {
                System.err.println("Error: product with id \"" + product.getId() + "\" was not found.");
            }
        }
        check.setProductList(productList);
        check.createHeader();

        return check;
    }
}
