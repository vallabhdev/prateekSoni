package answer.king.service;

import answer.king.model.Item;
import answer.king.model.LineItem;
import answer.king.model.Order;
import answer.king.model.Receipt;
import answer.king.repo.ItemRepository;
import answer.king.repo.OrderRepository;
import answer.king.repo.ReceiptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Service
@Transactional
public class OrderService {
    private static final String FAILURE_REMARK = "In-Sufficient funds to complete the payment";
    private static final String SUCCESS_REMARK = "Payment completed";

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ReceiptRepository receiptRepository;

    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    public Order save(Order order) {
        return orderRepository.save(order);
    }

    public void addItem(Long id, Long itemId, int quantity) {
        Order order = orderRepository.findOne(id);
        Item item = itemRepository.findOne(itemId);

        Predicate<LineItem> predicate = l -> l.getItem().getId().equals(itemId);

        if (order.getLineItems().stream().anyMatch(predicate)) {
            updateExistingLineItem(quantity, order, predicate);
        } else {
            createNewLineItem(quantity, order, item);
        }
        orderRepository.save(order);
    }

    public Receipt pay(Long id, BigDecimal payment) {
        Order order = orderRepository.findOne(id);

        Receipt receipt = new Receipt();
        receipt.setPayment(payment);
        receipt.setOrder(order);
        if (receipt.getChange().intValue() >= 0) {
            order.setPaid(true);
            order.getLineItems().forEach(lineItem -> lineItem.setOrderedPrice(lineItem.getItem().getPrice()));
            receipt.setRemark(SUCCESS_REMARK);
            receiptRepository.save(receipt);
            return receipt;
        }
        receipt.setRemark(FAILURE_REMARK);
        return receipt;
    }

    private void updateExistingLineItem(int quantity, Order order, Predicate<LineItem> predicate) {
        Optional<LineItem> lineItem = order.getLineItems().stream().filter(predicate).findFirst();
        if (lineItem.isPresent()) {
            LineItem existingLineItem = lineItem.get();
            int oldQuantity = existingLineItem.getQuantity();
            existingLineItem.setQuantity(oldQuantity + quantity);
        }
    }

    private void createNewLineItem(int quantity, Order order, Item item) {
        LineItem lineItem = new LineItem();
        lineItem.setItem(item);
        lineItem.setOrder(order);
        lineItem.setOrderedPrice(item.getPrice());
        lineItem.setQuantity(quantity);

        order.getLineItems().add(lineItem);
    }
}