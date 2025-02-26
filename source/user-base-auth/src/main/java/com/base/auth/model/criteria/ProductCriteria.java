package com.base.auth.model.criteria;


import com.base.auth.model.Account;
import com.base.auth.model.Customer;
import com.base.auth.model.Nation;
import com.base.auth.model.Product;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ProductCriteria implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private String description;
    private String shortDescription;
    private Double price;
    private Integer sellOff;
    private String image;

    public Specification<Product> getSpecification() {
        return new Specification<Product>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if (getId() != null) {
                    predicates.add(cb.equal(root.get("id"), getId()));
                }
                if (!StringUtils.isEmpty(getName())) {
                    predicates.add(cb.like(cb.lower(root.get("name")), "%" + getName().toLowerCase() + "%"));
                }
                if (!StringUtils.isEmpty(getDescription())) {
                    predicates.add(cb.like(cb.lower(root.get("description")), "%" + getDescription().toLowerCase() + "%"));
                }
                if (!StringUtils.isEmpty(getShortDescription())) {
                    predicates.add(cb.like(cb.lower(root.get("shortDescription")), "%" + getShortDescription().toLowerCase() + "%"));
                }
                if (getPrice() != null) {
                    predicates.add(cb.equal(root.get("price"), getPrice()));
                }
                if (getSellOff() != null) {
                    predicates.add(cb.equal(root.get("sellOff"), getSellOff()));
                }
                if (!StringUtils.isEmpty(getImage())) {
                    predicates.add(cb.like(cb.lower(root.get("image")), "%" + getImage().toLowerCase() + "%"));
                }

                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }

}
