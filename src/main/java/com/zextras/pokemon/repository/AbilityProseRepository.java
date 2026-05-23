package com.zextras.pokemon.repository;

import com.zextras.pokemon.model.AbilityProse;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AbilityProseRepository implements PanacheRepositoryBase<AbilityProse, Integer> {
}
