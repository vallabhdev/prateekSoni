package answer.king.validation;

import answer.king.model.Item;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

@Component
public class ItemValidator {
    //assuming the price range between(1, 10000), just to check more than nullity of price.
    private static final BigDecimal MIN = BigDecimal.ONE;
    private static final BigDecimal MAX = new BigDecimal(10000);

    public boolean validateItem(Item item) {
        return item != null && validateName(item) && item.getPrice() != null && validatePrice(item.getPrice());
    }

    private boolean validateName(Item item) {
        return !StringUtils.isEmpty(item.getName());
    }

    public boolean validatePrice(BigDecimal price) {
        return price.compareTo(MIN) >= 0 && price.compareTo(MAX) == -1;
    }
}
