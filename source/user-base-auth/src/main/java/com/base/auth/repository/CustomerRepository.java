package com.base.auth.repository;

import com.base.auth.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {

    @Query("SELECT COUNT(c) FROM Customer c " +
            "WHERE c.province.id = :nationId OR c.district.id = :nationId OR c.commune.id = :nationId")
    Long countCustomerByNationId(@Param("nationId") Long nationId);

}
