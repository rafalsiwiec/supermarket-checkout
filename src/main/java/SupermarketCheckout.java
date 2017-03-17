import java.util.HashMap;
import java.util.Map;

public class SupermarketCheckout {

    private final Promotions promotions;

    private final Map<String, Integer> products = new HashMap<>();

    public SupermarketCheckout(Promotions promotions) {
        this.promotions = promotions;
    }

    public int getTotalPrice() {
        return products.entrySet().stream()
                .mapToInt(entry -> {
                    String product = entry.getKey();
                    Integer numberOfItems = entry.getValue();
                    return promotions.calculatePrice(product, numberOfItems);
                }).sum();
    }

    public void addProduct(String product) {
        products.compute(product, (prod, number) -> number == null ? 1 : number + 1);
    }
}
