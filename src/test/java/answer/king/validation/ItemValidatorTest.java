package answer.king.validation;

import answer.king.model.Item;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ItemValidatorTest {
    private ItemValidator itemValidator = new ItemValidator();

    @Test
    public void validateNullableItem() {
        assertFalse(itemValidator.validateItem(null));
    }

    @Test
    public void validateItemName() {
        assertFalse(itemValidator.validateItem(createTestItem("Item", new BigDecimal(-2))));
        assertFalse(itemValidator.validateItem(createTestItem("", new BigDecimal(2))));
    }

    @Test
    public void validateItemPrice() {
        assertTrue(itemValidator.validatePrice(new BigDecimal(10)));
        assertFalse(itemValidator.validatePrice(new BigDecimal(0)));
        assertFalse(itemValidator.validatePrice(new BigDecimal(-5)));
        assertFalse(itemValidator.validatePrice(new BigDecimal(10101010)));
    }

    private Item createTestItem(String name, BigDecimal price) {
        Item item = new Item();
        item.setName(name);
        item.setPrice(price);
        return item;
    }
}