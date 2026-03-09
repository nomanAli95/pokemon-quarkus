package com.zextras.pokemon.full.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Entity
@Table(name = "pokemon_abilities")
@IdClass(PokemonAbilityId.class)
public class PokemonAbility extends PanacheEntityBase {

  @Id
  @Column(name = "pokemon_id")
  public Integer pokemonId;

  @Id
  public Integer slot;

  @ManyToOne
  @JoinColumn(name = "ability_id")
  public Ability ability;

  @Column(name = "is_hidden")
  public boolean hidden;
}
