package com.base.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name = "db_user_base_order_item")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class OrderItem extends Auditable<String> {
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "com.base.auth.service.id.IdGenerator")
    @GeneratedValue(generator = "idGenerator")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private Integer quantity;

    private Double singlePrice;

    private Integer sellOff;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    public void calculateSinglePrice() {
        if (product != null && product.getPrice() != null) {
            double price = product.getPrice() * quantity;
            double discount = (sellOff != null) ? (price * sellOff / 100.0) : 0;
            this.singlePrice = price - discount;
        } else {
            this.singlePrice = 0.0;
        }
    }

}
