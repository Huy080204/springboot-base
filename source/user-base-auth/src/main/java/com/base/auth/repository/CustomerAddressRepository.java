package com.base.auth.repository;

import com.base.auth.model.CustomerAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;


@Repository
public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, Long>, JpaSpecificationExecutor<CustomerAddress> {

    @Transactional
    @Modifying
    @Query("UPDATE CustomerAddress SET defaultAddress = false WHERE customer.id = :customerId AND defaultAddress = true")
    void resetDefaultAddress(@Param("customerId") Long customerId);

    @Query("SELECT COUNT(ca) FROM CustomerAddress ca " +
            "WHERE ca.province.id = :nationId OR ca.district.id = :nationId OR ca.commune.id = :nationId")
    Long countCustomerByNationId(@Param("nationId") Long nationId);

}
