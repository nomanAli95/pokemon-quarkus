package com.zextras.pokemon.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "pokemon_species")
public class PokemonSpecies extends PanacheEntityBase {

  @Id
  public Integer id;

  public String identifier;

  @Column(name = "evolves_from_species_id")
  public Integer evolvesFromSpeciesId;

  @Column(name = "evolution_chain_id")
  public Integer evolutionChainId;

  @Column(name = "sort_order")
  public Integer sortOrder;
}
