package com.zextras.pokemon.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "ability_prose")
public class AbilityProse extends PanacheEntityBase {

  @Id
  @Column(name = "ability_id")
  public Integer abilityId;

  @Column(name = "short_effect")
  public String shortEffect;
}
