package com.zextras.pokemon.full.repository;

import com.zextras.pokemon.full.model.TypeEfficacy;
import com.zextras.pokemon.full.model.TypeEfficacyId;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class TypeEfficacyRepository implements PanacheRepositoryBase<TypeEfficacy, TypeEfficacyId> {

  public List<TypeEfficacy> findByTargetTypeIdentifier(String identifier) {
    return list("targetType.identifier = ?1", identifier);
  }
}
