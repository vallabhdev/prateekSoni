package answer.king.controller;

import answer.king.model.Order;
import answer.king.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class OrderControllerTest {

    private static final long ANY_ID = 1L;
    private static final long ITEM_ID = 2L;

    @Mock
    private OrderService orderService;
    @InjectMocks
    private OrderController orderController;

    @Test
    public void checkClassAnnotations() {
        assertEquals(2, orderController.getClass().getAnnotations().length);
        assertTrue(orderController.getClass().isAnnotationPresent(RequestMapping.class));
        assertTrue(orderController.getClass().isAnnotationPresent(RestController.class));
        assertEquals("/order", orderController.getClass().getDeclaredAnnotation(RequestMapping.class).value()[0]);
    }

    @Test
    public void assertMethodAnnotations() throws NoSuchMethodException {
        assertTrue(orderController.getClass().getDeclaredMethod("getAll").isAnnotationPresent(RequestMapping.class));
        assertTrue(orderController.getClass().getDeclaredMethod("create").isAnnotationPresent(RequestMapping.class));

        assertTrue(orderController.getClass().getDeclaredMethod("addItem", Long.class, Long.class)
                .isAnnotationPresent(RequestMapping.class));

        assertTrue(orderController.getClass().getDeclaredMethod("pay", Long.class, BigDecimal.class)
                .isAnnotationPresent(RequestMapping.class));
        assertEquals("/{id}/pay", orderController.getClass()
                .getDeclaredMethod("pay", Long.class, BigDecimal.class)
                .getDeclaredAnnotation(RequestMapping.class).value()[0]);
        assertEquals("PUT", orderController.getClass()
                .getDeclaredMethod("pay", Long.class, BigDecimal.class)
                .getDeclaredAnnotation(RequestMapping.class).method()[0].name());
    }

    @Test
    public void testGetAllService() {
        orderController.getAll();

        verify(orderService).getAll();
    }

    @Test
    public void testCreateService() throws NoSuchMethodException {
        orderController.create();

        verify(orderService).save(any(Order.class));
    }

    @Test
    public void testAddItemService() {
        orderController.addItem(ANY_ID, ITEM_ID);

        verify(orderService).addItem(ANY_ID, ITEM_ID);
    }

    @Test
    public void pay() {
        BigDecimal payment = new BigDecimal(2000);

        orderController.pay(ANY_ID, payment);

        verify(orderService).pay(ANY_ID, payment);
    }
}