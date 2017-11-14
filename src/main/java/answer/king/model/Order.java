package answer.king.model;

import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "T_ORDER")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private Boolean paid = false;

	@OneToMany(mappedBy = "order", cascade = { CascadeType.ALL, CascadeType.PERSIST })
	private List<Item> items;

	@OneToOne(mappedBy = "order", cascade = { CascadeType.ALL, CascadeType.PERSIST })
	private Receipt receipt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getPaid() {
		return paid;
	}

	public void setPaid(Boolean paid) {
		this.paid = paid;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public Receipt getReceipt() {
		return receipt;
	}

	public void setReceipt(Receipt receipt) {
		this.receipt = receipt;
	}
}
