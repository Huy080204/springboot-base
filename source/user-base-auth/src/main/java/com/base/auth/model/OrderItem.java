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

    private Double sellOff;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    public void calculateSinglePrice() {
        this.singlePrice = product.getPrice() * quantity;
    }

    public void calculateSellOff() {
        this.sellOff = product.getPrice() * (product.getSellOff() / 100.0);
    }

}
