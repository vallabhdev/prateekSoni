package answer.king.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "T_LINEITEM")
public class LineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_ID")
    private Item item;

    private BigDecimal orderedPrice; //I assumed better name of current price as OrderedPrice,
    // because we're updating price in Item domain already. so this price will remain same once ordered is paid.

    private int quantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public BigDecimal getOrderedPrice() {
        return orderedPrice;
    }

    public void setOrderedPrice(BigDecimal orderedPrice) {
        this.orderedPrice = orderedPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "LineItem [id=" + id + ", OrderedPrice=" + orderedPrice + ", OrderId=" + order.getId() +
                ", ItemName=" + item.getName() + ", Quantity=" + quantity + "]";
    }
}
