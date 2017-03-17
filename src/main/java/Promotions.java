import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Promotions {

    private final Pricing pricing;
    private final Map<String, Discount> disountsForProducts;

    public Promotions(Pricing pricing, Collection<Discount> disounts) {
        this.pricing = pricing;
        this.disountsForProducts = disounts.stream()
                .collect(Collectors.toMap(d -> d.product, Function.identity()));
    }

    public int calculatePrice(String product, int numberOfItems) {
        Discount discount = disountsForProducts.getOrDefault(product, NO_DISCOUNT);
        int itemPrice = pricing.getPrice(product);
        return discount.calculatePrice(numberOfItems, itemPrice);
    }

    private static final Discount NO_DISCOUNT = new Discount("", -1, 0);

    public static class Discount {
        public final String product;
        public final int numberOfItems;
        public final int discount;

        public Discount(String product, int numberOfItems, int discount) {
            this.product = product;
            this.numberOfItems = numberOfItems;
            this.discount = discount;
        }

        public int calculatePrice(int numberOfItems, int itemPrice) {
            if (this.numberOfItems <= numberOfItems) {
                int nonDiscounted = numberOfItems % this.numberOfItems;
                int discounted = numberOfItems - nonDiscounted;
                return nonDiscounted * itemPrice + discounted * itemPrice * (100 - this.discount) / 100;
            }
            return itemPrice * numberOfItems;
        }
    }
}
