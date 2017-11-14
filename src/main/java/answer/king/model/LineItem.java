package answer.king.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "T_LINEITEM")
public class LineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private BigDecimal payment;

}
