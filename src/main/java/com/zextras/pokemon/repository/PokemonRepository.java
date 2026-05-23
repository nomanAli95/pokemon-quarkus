package com.zextras.pokemon.repository;

import com.zextras.pokemon.resource.PokemonFilter;
import com.zextras.pokemon.model.PokemonOverview;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class PokemonRepository implements PanacheRepositoryBase<PokemonOverview, Integer> {

  private static final int MAX_SIZE = 100;
  private static final String TYPE_CONDITION = "(LOWER(type1) = LOWER(:type) OR LOWER(type2) = LOWER(:type))";

  public List<PokemonOverview> list(PokemonFilter filter) {
    int size = Math.min(filter.size, MAX_SIZE);

    StringBuilder query = new StringBuilder("1=1");
    Map<String, Object> params = new HashMap<>();

    if (filter.type != null) {
      query.append(" AND ").append(TYPE_CONDITION);
      params.put("type", filter.type);
    }
    if (filter.color != null) {
      query.append(" AND LOWER(color) = LOWER(:color)");
      params.put("color", filter.color);
    }
    if (filter.legendary != null) {
      query.append(" AND legendary = :legendary");
      params.put("legendary", filter.legendary);
    }
    if (filter.mythical != null) {
      query.append(" AND mythical = :mythical");
      params.put("mythical", filter.mythical);
    }

    return find(query.toString(), params).page(filter.page, size).list();
  }
}
