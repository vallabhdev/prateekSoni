package answer.king.service;

import answer.king.model.Item;
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

@Service
@Transactional
public class OrderService {

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

    public void addItem(Long id, Long itemId) {
        Order order = orderRepository.findOne(id);
        Item item = itemRepository.findOne(itemId);

        item.setOrder(order);
        order.getItems().add(item);

        orderRepository.save(order);
    }

    public Receipt pay(Long id, BigDecimal payment) {
        Order order = orderRepository.findOne(id);

        Receipt receipt = new Receipt();
        receipt.setPayment(payment);
        receipt.setOrder(order);
        if (receipt.getChange().intValue() >= 0) {
            order.setPaid(true);
            receiptRepository.save(receipt);
        }
        return receipt;
    }
}
