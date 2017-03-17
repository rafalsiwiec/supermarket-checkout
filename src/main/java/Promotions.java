import java.util.*;
import java.util.stream.Collectors;

public class Promotions {

    private final Pricing pricing;
    private final Map<String, List<Discount>> disountsForProducts;

    public Promotions(Pricing pricing, Collection<Discount> disounts) {
        this.pricing = pricing;
        this.disountsForProducts = disounts.stream()
                .collect(Collectors.groupingBy(d -> d.product));
    }

    public int calculatePrice(String product, int numberOfItems) {
        List<Discount> discounts = disountsForProducts.getOrDefault(product, Collections.emptyList());
        int itemPrice = pricing.getPrice(product);
        OptionalInt potentialPrice = discounts.stream()
                .mapToInt(discount -> discount.calculatePrice(numberOfItems, itemPrice))
                .min();
        return potentialPrice.orElseGet(() -> numberOfItems * itemPrice);
    }

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
