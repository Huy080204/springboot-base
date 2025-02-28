package com.base.auth.model.criteria;


import com.base.auth.model.Order;
import com.base.auth.model.Product;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderCriteria implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String code;
    private String state;

    public Specification<Order> getSpecification() {
        return new Specification<Order>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if (getId() != null) {
                    predicates.add(cb.equal(root.get("id"), getId()));
                }
                if (!StringUtils.isEmpty(getCode())) {
                    predicates.add(cb.like(cb.lower(root.get("code")), "%" + getCode().toLowerCase() + "%"));
                }
                if (getState() != null) {
                    predicates.add(cb.equal(root.get("state"), getState()));
                }

                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }

}
