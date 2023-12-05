package com.volvo.congestiontax.repository;

import com.volvo.congestiontax.model.TaxRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Repository
public interface TaxRuleRepository extends JpaRepository<TaxRule, Long> {

    Optional<TaxRule> findByCity(String city);

    @Transactional
    void deleteByCity(String city);

}
