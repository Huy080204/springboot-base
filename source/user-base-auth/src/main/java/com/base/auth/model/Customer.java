package com.base.auth.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "db_user_base_customer")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Customer extends Auditable<String> {

    @Id
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.EAGER)
    private Account account;

    private LocalDate birthDay;

    private Integer gender;

}
