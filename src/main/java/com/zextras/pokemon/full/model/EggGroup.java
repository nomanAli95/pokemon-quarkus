package com.zextras.pokemon.full.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "egg_groups")
public class EggGroup extends PanacheEntityBase {

  @Id
  public Integer id;

  public String identifier;
}
