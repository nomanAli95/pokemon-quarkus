package com.zextras.pokemon.basic.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "pokemon_overview")
@NamedQuery(
    name = "PokemonOverview.byType",
    query = "FROM PokemonOverview WHERE LOWER(type1) = LOWER(:type) OR LOWER(type2) = LOWER(:type)"
)
public class PokemonOverview extends PanacheEntityBase {

  @Id
  public Integer id;

  public String name;
  public Integer height;
  public Integer weight;

  @Column(name = "base_experience")
  public Integer baseExperience;

  // species
  public String color;
  public String shape;
  public String genus;
  public String habitat;

  @Column(name = "gender_rate")
  public Integer genderRate;

  @Column(name = "capture_rate")
  public Integer captureRate;

  @Column(name = "base_happiness")
  public Integer baseHappiness;

  @Column(name = "is_baby")
  public boolean baby;

  @Column(name = "hatch_counter")
  public Integer hatchCounter;

  @Column(name = "growth_rate")
  public String growthRate;

  @Column(name = "is_legendary")
  public boolean legendary;

  @Column(name = "is_mythical")
  public boolean mythical;

  @Column(name = "evolves_from_species_id")
  public Integer evolvesFromSpeciesId;

  @Column(name = "evolution_chain_id")
  public Integer evolutionChainId;

  // generation & region
  public String generation;
  public String region;

  // types
  public String type1;
  public String type2;

  // base stats
  public Integer hp;
  public Integer attack;
  public Integer defense;

  @Column(name = "sp_attack")
  public Integer spAttack;

  @Column(name = "sp_defense")
  public Integer spDefense;

  public Integer speed;

  @Column(name = "base_stat_total")
  public Integer baseStatTotal;

  // ev yields
  @Column(name = "ev_hp")
  public Integer evHp;

  @Column(name = "ev_attack")
  public Integer evAttack;

  @Column(name = "ev_defense")
  public Integer evDefense;

  @Column(name = "ev_sp_attack")
  public Integer evSpAttack;

  @Column(name = "ev_sp_defense")
  public Integer evSpDefense;

  @Column(name = "ev_speed")
  public Integer evSpeed;

  @Column(name = "official_artwork_url")
  public String officialArtworkUrl;
}
