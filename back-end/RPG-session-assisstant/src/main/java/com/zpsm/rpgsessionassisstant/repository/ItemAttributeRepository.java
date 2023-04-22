package com.zpsm.rpgsessionassisstant.repository;

import com.zpsm.rpgsessionassisstant.model.ItemAttribute;
import com.zpsm.rpgsessionassisstant.model.ItemAttributeKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ItemAttributeRepository extends JpaRepository<ItemAttribute, ItemAttributeKey> {

    @Query("select a.name from ItemAttribute ia join ia.attribute a where a.name = :name")
    Optional<ItemAttribute> findByName(String name);

}
