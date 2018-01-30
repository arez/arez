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
    nameIncludesId = false,
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
  DaggerEnabledRepository create(@Nonnull final String name) {
    final Arez_DaggerEnabledRepository entity = new Arez_DaggerEnabledRepository(name);
    entity.$$arez$$_setOnDispose( e -> destroy( e ) );
    registerEntity( entity );
    return entity;
  }

  @Override
  protected void preDisposeEntity(@Nonnull final DaggerEnabledRepository entity) {
    ((Arez_DaggerEnabledRepository) entity).$$arez$$_setOnDispose( null );
  }
}
