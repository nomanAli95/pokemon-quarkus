package com.zextras.pokemon.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "pokemon_moves")
@IdClass(PokemonMoveId.class)
public class PokemonMove extends PanacheEntityBase {

  @Id
  @Column(name = "pokemon_id")
  public Integer pokemonId;

  @Id
  @Column(name = "version_group_id")
  public Integer versionGroupId;

  @Id
  @Column(name = "move_id")
  public Integer moveId;

  @Id
  @Column(name = "learn_method")
  public String learnMethod;

  public Integer level;

  @ManyToOne
  @JoinColumn(name = "move_id", insertable = false, updatable = false)
  public Move move;
}
