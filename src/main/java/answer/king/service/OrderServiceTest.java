package answer.king.service;

import answer.king.model.Item;
import answer.king.model.Order;
import answer.king.model.Receipt;
import answer.king.repo.ItemRepository;
import answer.king.repo.OrderRepository;
import answer.king.repo.ReceiptRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {

    private static final Long ORDER_ID = 1L;
    private static final Long ITEM_ID = 2L;
    private static Order testOrder;
    private static Item testItem;

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ReceiptRepository receiptRepository;
    @InjectMocks
    private OrderService orderService;

    @Before
    public void setup() {
        testOrder = new Order();
        testOrder.setId(ORDER_ID);
        testOrder.setPaid(false);
        testOrder.setItems(new ArrayList<>());

        testItem = new Item();
        testItem.setId(ITEM_ID);
        testItem.setPrice(new BigDecimal(50));
        testItem.setOrder(testOrder);
    }

    @Test
    public void testGetAll() {
        orderService.getAll();

        verify(orderRepository).findAll();
    }

    @Test
    public void testSave() {
        Order testOrder = new Order();

        orderService.save(testOrder);

        verify(orderRepository).save(testOrder);
    }

    @Test
    public void testAddItem() {
        when(orderRepository.findOne(ORDER_ID)).thenReturn(testOrder);
        when(itemRepository.findOne(ITEM_ID)).thenReturn(testItem);

        orderService.addItem(ORDER_ID, ITEM_ID);

        verify(orderRepository).findOne(ORDER_ID);
        verify(itemRepository).findOne(ITEM_ID);
        verify(orderRepository).save(testOrder);
    }

    @Test
    public void testPay() {
        BigDecimal payment = new BigDecimal(2500);

        when(orderRepository.findOne(ORDER_ID)).thenReturn(testOrder);

        Receipt receipt = orderService.pay(ORDER_ID, payment);

        assertEquals(ORDER_ID, receipt.getOrder().getId());
        assertTrue(receipt.getOrder().getPaid());
        assertEquals(receipt.getPayment(), payment);
    }

    @Test
    public void paymentWithInSufficientFunds() {
        BigDecimal payment = new BigDecimal(1000);
        Order order = new Order();
        order.setId(ORDER_ID);
        order.setPaid(false);
        order.setItems(Arrays.asList(createItem(1L, "item1", 1000),
                createItem(2L, "item2", 500)));

        when(orderRepository.findOne(ORDER_ID)).thenReturn(order);

        Receipt receipt = orderService.pay(ORDER_ID, payment);

        assertFalse(receipt.getOrder().getPaid());
    }

    @Test
    public void saveTheReceiptOnlyForClearedPayment() {
        BigDecimal payment = new BigDecimal(1000);

        when(orderRepository.findOne(ORDER_ID)).thenReturn(testOrder);

        Receipt receipt = orderService.pay(ORDER_ID, payment);

        assertTrue(receipt.getOrder().getPaid());
        verify(receiptRepository).save(receipt);
    }

    private Item createItem(Long id, String name, int price) {
        Item i = new Item();
        i.setId(id);
        i.setName(name);
        i.setPrice(new BigDecimal(price));
        return i;
    }
}