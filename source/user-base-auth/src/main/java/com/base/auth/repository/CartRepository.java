package com.base.auth.repository;

import com.base.auth.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long>, JpaSpecificationExecutor<Cart> {

    boolean existsByCode(String code);

    Optional<Cart> findByCustomerId(Long customerId);

}
