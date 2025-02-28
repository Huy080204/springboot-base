package com.base.auth.repository;

import com.base.auth.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    List<Order> findByCustomerId(Long customerId);

    boolean existsByCode(String code);

    Page<Order> findAllByCustomerAccountUsername(String username, Specification<Order> spec, Pageable pageable);

}
