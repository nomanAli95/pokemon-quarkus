package com.zextras.pokemon.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "pokemon_egg_groups")
@IdClass(PokemonEggGroupId.class)
public class PokemonEggGroup extends PanacheEntityBase {

  @Id
  @Column(name = "species_id")
  public Integer speciesId;

  @Id
  @Column(name = "egg_group_id")
  public Integer eggGroupId;

  @ManyToOne
  @JoinColumn(name = "egg_group_id", insertable = false, updatable = false)
  public EggGroup eggGroup;
}
