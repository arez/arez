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
    dagger = Feature.DISABLE
)
@Singleton
public abstract class DaggerDisabledRepositoryRepository extends AbstractRepository<Long, DaggerDisabledRepository, DaggerDisabledRepositoryRepository> {
  DaggerDisabledRepositoryRepository() {
  }

  @Nonnull
  public static DaggerDisabledRepositoryRepository newRepository() {
    return new Arez_DaggerDisabledRepositoryRepository();
  }

  @Action(
      name = "create_name"
  )
  @Nonnull
  public DaggerDisabledRepository create(@Nonnull final String name) {
    final Arez_DaggerDisabledRepository entity = new Arez_DaggerDisabledRepository(name);
    registerEntity( entity );
    return entity;
  }

  @Action(
      reportParameters = false
  )
  public void destroy(@Nonnull final DaggerDisabledRepository entity) {
    super.destroy( entity );
  }
}
