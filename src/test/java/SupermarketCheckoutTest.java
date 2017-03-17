import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class SupermarketCheckoutTest {

    private static final String APPLE = "apple";
    private static final String LEMON = "lemon";
    private static final String ORANGE = "orange";

    private Pricing pricing = Mockito.mock(Pricing.class);
    private SupermarketCheckout supermarketCheckout;

    @Before
    public void setUp() throws Exception {
        noDiscounts();
    }

    @Test
    public void shouldReturnZeroForNoProducts() throws Exception {
        assertThatTotalPriceIsEqualTo(0);
    }

    @Test
    public void shouldCalculatePriceForSingleProduct() throws Exception {
        setUpProductPrice(APPLE, 2);

        addProducts(APPLE);

        assertThatTotalPriceIsEqualTo(2);
    }

    @Test
    public void shouldCalculatePriceForTwoProducts() throws Exception {
        setUpProductPrice(APPLE, 1);
        setUpProductPrice(LEMON, 2);

        addProducts(APPLE, LEMON);

        assertThatTotalPriceIsEqualTo(3);
    }

    @Test
    public void shouldApplyPromotion() throws Exception {
        setUpProductPrice(APPLE, 5);
        setUpDiscounts(discount(APPLE, 2, 10));

        addProducts(APPLE, APPLE);

        assertThatTotalPriceIsEqualTo(9);
    }

    @Test
    public void shouldApplyMultiplePromotions() throws Exception {
        setUpProductPrice(APPLE, 10);
        setUpProductPrice(LEMON, 5);
        setUpProductPrice(ORANGE, 8);

        setUpDiscounts(
                discount(APPLE, 2, 10),
                discount(LEMON, 3, 20)
        );

        addProducts(APPLE, APPLE, LEMON, LEMON, LEMON, ORANGE);

        assertThatTotalPriceIsEqualTo(18 + 12 + 8);
    }

    @Test
    public void shouldApplyPromotionForPartialNumberOfProducts() throws Exception {
        setUpProductPrice(APPLE, 10);
        setUpDiscounts(discount(APPLE, 2, 20));

        addProducts(APPLE, APPLE, APPLE);

        assertThatTotalPriceIsEqualTo(16 + 10);
    }

    @Test
    public void shouldChooseOneOfMultipleDiscountsForParticularProduct() throws Exception {
        setUpProductPrice(APPLE, 5);
        setUpDiscounts(
                discount(APPLE, 2, 10),
                discount(APPLE, 3, 20)
        );

        addProducts(APPLE, APPLE, APPLE);

        assertThatTotalPriceIsEqualTo(12);
    }

    private static Promotions.Discount discount(String product, int numberOfItems, int discount) {
        return new Promotions.Discount(product, numberOfItems, discount);
    }

    private void noDiscounts() {
        setUpDiscounts();
    }

    private void setUpDiscounts(Promotions.Discount... discounts) {
        Promotions promotions = new Promotions(pricing, Arrays.asList(discounts));
        supermarketCheckout = new SupermarketCheckout(promotions);
    }

    private void setUpProductPrice(String product, int price) {
        Mockito.when(pricing.getPrice(product))
                .thenReturn(price);
    }

    private void assertThatTotalPriceIsEqualTo(int expectedTotalPrice) {
        assertThat(supermarketCheckout.getTotalPrice())
                .isEqualTo(expectedTotalPrice);
    }

    private void addProducts(String... products) {
        for (String product : products) {
            supermarketCheckout.addProduct(product);
        }
    }
}
