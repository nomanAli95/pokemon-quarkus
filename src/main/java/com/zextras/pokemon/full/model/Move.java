package com.zextras.pokemon.full.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "moves")
public class Move extends PanacheEntityBase {

  @Id
  public Integer id;

  public String identifier;
  public String name;

  @Column(name = "damage_class")
  public String damageClass;

  public Integer power;
  public Integer pp;
  public Integer accuracy;

  @ManyToOne
  @JoinColumn(name = "type_id")
  public Type type;
}
