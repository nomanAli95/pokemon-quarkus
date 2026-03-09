package com.zextras.pokemon.full.repository;

import com.zextras.pokemon.full.model.AbilityProse;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AbilityProseRepository implements PanacheRepositoryBase<AbilityProse, Integer> {
}
