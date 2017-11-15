package answer.king.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "T_RECEIPT")
public class Receipt {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private BigDecimal payment;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public BigDecimal getPayment() {
        return payment;
    }

    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

    public BigDecimal getChange() {
        BigDecimal totalOrderPrice = order.getLineItems()
                .stream()
                .map(LineItem::getOrderedPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return payment.subtract(totalOrderPrice);
    }

    @Override
    public String toString() {
        return "Receipt [id=" + id + ", Payment=" + payment + ", OrderId=" + order.getId() + "]";
    }
}
