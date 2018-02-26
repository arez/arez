package com.example.repository;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.component.AbstractRepository;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.inject.Singleton;

@Generated("arez.processor.ArezProcessor")
@ArezComponent(
    dagger = Feature.ENABLE
)
@Singleton
public abstract class DaggerEnabledRepositoryRepository extends AbstractRepository<Long, DaggerEnabledRepository, DaggerEnabledRepositoryRepository> {
  DaggerEnabledRepositoryRepository() {
  }

  @Nonnull
  public static DaggerEnabledRepositoryRepository newRepository() {
    return new Arez_DaggerEnabledRepositoryRepository();
  }

  @Action(
      name = "create_name"
  )
  @Nonnull
  public DaggerEnabledRepository create(@Nonnull final String name) {
    final Arez_DaggerEnabledRepository entity = new Arez_DaggerEnabledRepository(name);
    registerEntity( entity );
    return entity;
  }
}
