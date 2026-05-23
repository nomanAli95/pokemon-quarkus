package com.zextras.pokemon.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "type_efficacy")
@IdClass(TypeEfficacyId.class)
public class TypeEfficacy extends PanacheEntityBase {

  @Id
  @Column(name = "damage_type_id")
  public Integer damageTypeId;

  @Id
  @Column(name = "target_type_id")
  public Integer targetTypeId;

  @Column(name = "damage_factor")
  public Integer damageFactor;

  @ManyToOne
  @JoinColumn(name = "damage_type_id", insertable = false, updatable = false)
  public Type damageType;

  @ManyToOne
  @JoinColumn(name = "target_type_id", insertable = false, updatable = false)
  public Type targetType;
}
