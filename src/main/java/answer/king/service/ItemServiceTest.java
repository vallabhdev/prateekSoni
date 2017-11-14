package answer.king.service;

import answer.king.model.Item;
import answer.king.repo.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ItemServiceTest {
    private static final Long ITEM_ID = 1L;
    private static final String ITEM_NAME = "testItem";
    private static final BigDecimal existingPrice = new BigDecimal(100);
    private static Item item;

    @Mock
    private ItemRepository itemRepository;
    @InjectMocks
    private ItemService itemService;

    @Before
    public void setup() {
        item = new Item();
        item.setId(ITEM_ID);
        item.setName(ITEM_NAME);
        item.setPrice(existingPrice);
    }

    @Test
    public void getAll() {
        itemService.getAll();

        verify(itemRepository).findAll();
    }

    @Test
    public void save() {
        itemService.save(item);

        verify(itemRepository).save(item);
    }

    @Test
    public void update() {
        BigDecimal newPrice = new BigDecimal(200);
        when(itemRepository.findOne(ITEM_ID)).thenReturn(item);

        Item updatedItem = itemService.update(ITEM_ID, newPrice);

        verify(itemRepository).findOne(ITEM_ID);
        assertThat(updatedItem.getPrice(), is(newPrice));
    }
}