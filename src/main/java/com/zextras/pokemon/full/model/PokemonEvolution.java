package com.zextras.pokemon.full.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "pokemon_evolution")
public class PokemonEvolution extends PanacheEntityBase {

  @Id
  public Integer id;

  @Column(name = "evolved_species_id")
  public Integer evolvedSpeciesId;

  @Column(name = "evolution_trigger")
  public String evolutionTrigger;

  @Column(name = "minimum_level")
  public Integer minimumLevel;

  @Column(name = "trigger_item")
  public String triggerItem;

  @Column(name = "held_item")
  public String heldItem;

  @Column(name = "time_of_day")
  public String timeOfDay;

  @Column(name = "minimum_happiness")
  public Integer minimumHappiness;

  @Column(name = "minimum_affection")
  public Integer minimumAffection;

  @Column(name = "known_move_id")
  public Integer knownMoveId;

  @Column(name = "trade_species_id")
  public Integer tradeSpeciesId;

  @Column(name = "relative_physical_stats")
  public Integer relativePhysicalStats;

  @Column(name = "needs_overworld_rain")
  public boolean needsOverworldRain;

  @Column(name = "turn_upside_down")
  public boolean turnUpsideDown;
}
