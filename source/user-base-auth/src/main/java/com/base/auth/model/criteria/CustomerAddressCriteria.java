package com.base.auth.model.criteria;


import com.base.auth.model.Customer;
import com.base.auth.model.CustomerAddress;
import com.base.auth.model.Nation;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class CustomerAddressCriteria implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String province;
    private String district;
    private String commune;
    private String address;
    private Integer type;
    private Boolean defaultAddress;

    public Specification<CustomerAddress> getSpecification() {
        return new Specification<CustomerAddress>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<CustomerAddress> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if (getId() != null) {
                    predicates.add(cb.equal(root.get("id"), getId()));
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
                if (getType() != null) {
                    predicates.add(cb.equal(root.get("type"), getType()));
                }
                if (getDefaultAddress() != null) {
                    predicates.add(cb.equal(root.get("defaultAddress"), getDefaultAddress()));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }

}
