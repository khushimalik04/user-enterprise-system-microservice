package com.address.repository;

import com.address.model.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Address persistence with queries scoped to a single user.
 */
public interface AddressRepository extends JpaRepository<Address, Long> {
    // Used when returning or synchronizing the full address set for one user.
    List<Address> findAllByUserId(Long userId);
}
