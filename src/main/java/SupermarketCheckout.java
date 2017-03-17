import java.util.HashMap;
import java.util.Map;
import java.util.function.ToIntBiFunction;
import java.util.function.ToIntFunction;

public class SupermarketCheckout {

    private final Promotions promotions;
    private final Map<String, Integer> products = new HashMap<>();

    public SupermarketCheckout(Promotions promotions) {
        this.promotions = promotions;
    }

    public int getTotalPrice() {
        return products.entrySet().stream()
                .mapToInt(entry(promotions::calculatePrice))
                .sum();
    }

    public void addProduct(String product) {
        products.compute(product, (prod, number) -> number == null ? 1 : number + 1);
    }

    private static <K, V> ToIntFunction<Map.Entry<K, V>> entry(ToIntBiFunction<K, V> mapper) {
        return entry -> mapper.applyAsInt(entry.getKey(), entry.getValue());
    }
}
