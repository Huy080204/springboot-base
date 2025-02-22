package com.base.auth.model.criteria;


import com.base.auth.model.Category;
import com.base.auth.model.News;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class NewsCriteria implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String title;
    private String content;
    private String avatar;
    private String banner;
    private String description;
    private Integer pinTop;
    private String categoryName;

    public Specification<News> getSpecification() {
        return new Specification<News>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<News> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if (getId() != null) {
                    predicates.add(cb.equal(root.get("id"), getId()));
                }
                if (!StringUtils.isEmpty(getTitle())) {
                    predicates.add(cb.like(cb.lower(root.get("title")), "%" + getTitle().toLowerCase() + "%"));
                }
                if (!StringUtils.isEmpty(getContent())) {
                    predicates.add(cb.like(cb.lower(root.get("content")), "%" + getContent().toLowerCase() + "%"));
                }
                if (!StringUtils.isEmpty(getAvatar())) {
                    predicates.add(cb.like(cb.lower(root.get("avatar")), "%" + getAvatar().toLowerCase() + "%"));
                }
                if (!StringUtils.isEmpty(getBanner())) {
                    predicates.add(cb.like(cb.lower(root.get("banner")), "%" + getBanner().toLowerCase() + "%"));
                }
                if (!StringUtils.isEmpty(getDescription())) {
                    predicates.add(cb.like(cb.lower(root.get("description")), "%" + getDescription().toLowerCase() + "%"));
                }
                if (getPinTop() != null) {
                    predicates.add(cb.equal(root.get("pinTop"), getPinTop()));
                }
                if (!StringUtils.isEmpty(getCategoryName())) {
                    Join<News, Category> newsCategoryJoin = root.join("category");
                    predicates.add(cb.like(cb.lower(newsCategoryJoin.get("name")), "%" + getCategoryName().toLowerCase() + "%"));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }

}
