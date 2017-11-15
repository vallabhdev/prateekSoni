package answer.king.service;

import answer.king.model.Item;
import answer.king.model.LineItem;
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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {

    private static final Long ORDER_ID = 1L;
    private static final Long ITEM_ID = 2L;
    private static Order testOrder;
    private static Item testItem;
    private static LineItem lineItem;

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
        testOrder.setLineItems(new ArrayList<>());

        testItem = new Item();
        testItem.setId(ITEM_ID);
        testItem.setPrice(new BigDecimal(50));

        lineItem = new LineItem();
        lineItem.setOrder(testOrder);
        lineItem.setItem(testItem);
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

        orderService.addItem(ORDER_ID, ITEM_ID, 2);

        verify(orderRepository).findOne(ORDER_ID);
        verify(itemRepository).findOne(ITEM_ID);
        verify(orderRepository).save(testOrder);

        assertThat(testOrder.getLineItems().get(0).getOrderedPrice().intValue(), is(50));
        assertThat(testOrder.getLineItems().get(0).getQuantity(), is(2));
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
        order.setLineItems(Arrays.asList(createLineItem(1L, "item1", 1000),
                createLineItem(2L, "item2", 500)));

        when(orderRepository.findOne(ORDER_ID)).thenReturn(order);

        Receipt receipt = orderService.pay(ORDER_ID, payment);

        assertFalse(receipt.getOrder().getPaid());
        verify(receiptRepository, never()).save(receipt);
    }

    @Test
    public void saveTheReceiptOnlyForClearedPayment() {
        BigDecimal payment = new BigDecimal(1000);

        when(orderRepository.findOne(ORDER_ID)).thenReturn(testOrder);

        Receipt receipt = orderService.pay(ORDER_ID, payment);

        assertTrue(receipt.getOrder().getPaid());
        verify(receiptRepository).save(receipt);
    }

    @Test
    public void increaseQuantityWhenSameItemIsAdded() {
        when(orderRepository.findOne(ORDER_ID)).thenReturn(testOrder);
        when(itemRepository.findOne(ITEM_ID)).thenReturn(testItem);

        orderService.addItem(ORDER_ID, ITEM_ID, 2);
        orderService.addItem(ORDER_ID, ITEM_ID, 4);

        assertThat(testOrder.getLineItems().get(0).getQuantity(), is(6));
        assertThat(testOrder.getLineItems().size(), is(1));
    }

    private LineItem createLineItem(Long id, String name, int price) {
        LineItem lineItem = new LineItem();
        Item i = new Item();
        i.setId(id);
        i.setName(name);
        i.setPrice(new BigDecimal(price));
        lineItem.setItem(i);
        lineItem.setOrderedPrice(i.getPrice());
        return lineItem;
    }
}