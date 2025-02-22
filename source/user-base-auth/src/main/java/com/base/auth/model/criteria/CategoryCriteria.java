package com.base.auth.model.criteria;


import com.base.auth.model.Category;
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
public class CategoryCriteria implements Serializable{
    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private String description;
    private String image;
    private Integer ordering;
    private Integer kind;

    public Specification<Category> getSpecification() {
        return new Specification<Category>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Category> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if(getId() != null){
                    predicates.add(cb.equal(root.get("id"), getId()));
                }
                if(getKind() > 0){
                    predicates.add(cb.equal(root.get("kind"), getKind()));
                }
                if(!StringUtils.isEmpty(getName())){
                    predicates.add(cb.like(cb.lower(root.get("name")), "%"+ getName().toLowerCase()+"%"));
                }
                if(!StringUtils.isEmpty(getDescription())){
                    predicates.add(cb.like(cb.lower(root.get("description")), "%"+ getDescription().toLowerCase()+"%"));
                }
                if(!StringUtils.isEmpty(getImage())){
                    predicates.add(cb.like(cb.lower(root.get("image")), "%"+ getImage().toLowerCase()+"%"));
                }
                if(getOrdering() != null){
                    predicates.add(cb.equal(root.get("ordering"), getOrdering()));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }

}
