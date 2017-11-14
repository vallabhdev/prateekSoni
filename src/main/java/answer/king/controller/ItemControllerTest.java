package answer.king.controller;

import answer.king.Exception.BadRequestException;
import answer.king.model.Item;
import answer.king.service.ItemService;
import answer.king.validation.ItemValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ItemControllerTest {
    private static final Long ITEM_ID = 1L;
    private static final String ITEM_NAME = "Any item";
    private static final BigDecimal ITEM_PRICE = new BigDecimal(20);

    @Mock
    private ItemService itemService;
    @Mock
    private ItemValidator itemValidator;
    @InjectMocks
    private ItemController itemController;

    @Test
    public void getAllShouldCallService() {
        itemController.getAll();

        verify(itemService).getAll();
    }

    @Test
    public void testClassAnnotation() {
        assertEquals(2, itemController.getClass().getAnnotations().length);
        assertTrue(itemController.getClass().isAnnotationPresent(RequestMapping.class));
        assertTrue(itemController.getClass().isAnnotationPresent(RestController.class));
        assertEquals("/item", itemController.getClass().getDeclaredAnnotation(RequestMapping.class).value()[0]);
    }

    @Test
    public void checkMethodAndParamAnnotations() throws NoSuchMethodException {
        assertTrue(itemController.getClass().getDeclaredMethod("create", Item.class).isAnnotationPresent(RequestMapping.class));

        assertTrue(itemController.getClass().getDeclaredMethod("getAll").isAnnotationPresent(RequestMapping.class));
    }

    @Test
    public void createServiceShouldSaveAndReturnSameEntity() {
        Item testItem = createTestItem(ITEM_NAME, ITEM_PRICE);

        when(itemService.save(any(Item.class))).thenReturn(testItem);
        when(itemValidator.validateItem(any(Item.class))).thenReturn(true);

        Item returnedItem = itemController.create(testItem);

        verify(itemService).save(testItem);
        assertThat(returnedItem.getName(), is(ITEM_NAME));
        assertThat(returnedItem.getPrice(), is(ITEM_PRICE));
    }

    @Test(expected = BadRequestException.class)
    public void validateItemBeforeCreating() {
        itemController.create(null);
    }

    @Test(expected = BadRequestException.class)
    public void validateItemAttributeBeforeCreating() {
        itemController.create(createTestItem("", null));
    }

    @Test
    public void shouldBeAbleToUpdatePriceOfAnItem() {
        BigDecimal newPrice = new BigDecimal(250);
        when(itemValidator.validatePrice(newPrice)).thenReturn(true);

        itemController.update(ITEM_ID, newPrice);

        verify(itemService).update(ITEM_ID, newPrice);
    }

    private Item createTestItem(String name, BigDecimal price) {
        Item item = new Item();
        item.setName(name);
        item.setPrice(price);
        return item;
    }
}