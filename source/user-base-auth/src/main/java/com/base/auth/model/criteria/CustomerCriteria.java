package com.base.auth.model.criteria;


import com.base.auth.model.Account;
import com.base.auth.model.Customer;
import com.base.auth.model.Nation;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class CustomerCriteria implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private LocalDateTime birthDay;
    private Integer gender;
    private Integer address;
    private String province;
    private String district;
    private String commune;

    public Specification<Customer> getSpecification() {
        return new Specification<Customer>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if (getId() != null) {
                    predicates.add(cb.equal(root.get("id"), getId()));
                }
                Join<Customer, Account> accountJoin = root.join("account");
                if (!StringUtils.isEmpty(getUsername())) {
                    predicates.add(cb.like(cb.lower(accountJoin.get("username")), "%" + getUsername().toLowerCase() + "%"));
                }
                if (!StringUtils.isEmpty(getEmail())) {
                    predicates.add(cb.like(cb.lower(accountJoin.get("email")), "%" + getEmail().toLowerCase() + "%"));
                }
                if (!StringUtils.isEmpty(getFullName())) {
                    predicates.add(cb.like(cb.lower(accountJoin.get("fullName")), "%" + getFullName().toLowerCase() + "%"));
                }
                if (!StringUtils.isEmpty(getProvince())) {
                    Join<Customer, Nation> provinceJoin = root.join("province");
                    predicates.add(cb.like(cb.lower(provinceJoin.get("name")), "%" + getProvince().toLowerCase() + "%"));
                }
                if (!StringUtils.isEmpty(getDistrict())) {
                    Join<Customer, Nation> districtJoin = root.join("district");
                    predicates.add(cb.like(cb.lower(districtJoin.get("name")), "%" + getDistrict().toLowerCase() + "%"));
                }
                if (!StringUtils.isEmpty(getCommune())) {
                    Join<Customer, Nation> communeJoin = root.join("commune");
                    predicates.add(cb.like(cb.lower(communeJoin.get("name")), "%" + getCommune().toLowerCase() + "%"));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }

}
