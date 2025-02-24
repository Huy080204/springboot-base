package com.base.auth.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "db_user_base_customer")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Customer extends Auditable<String> {
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "com.base.auth.service.id.IdGenerator")
    @GeneratedValue(generator = "idGenerator")
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    private Account account;

    private LocalDateTime birthDay;
    private Integer gender;
    private String address;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "province_id")
    private Nation province;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "district_id")
    private Nation district;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "commune_id")
    private Nation commune;

}
