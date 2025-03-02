package com.base.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "db_user_base_order")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Order extends Auditable<String> {
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "com.base.auth.service.id.IdGenerator")
    @GeneratedValue(generator = "idGenerator")
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    private Double totalMoney = 0.0;

    private Double totalSellOff = 0.0;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    private Customer customer;

    private Integer state = 1;

    @JsonIgnore
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    private List<OrderItem> orderItems;
}
