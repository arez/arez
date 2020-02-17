package com.example.repository;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.component.internal.AbstractRepository;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("arez.processor.ArezProcessor")
@ArezComponent(
    service = Feature.ENABLE,
    dagger = Feature.DISABLE,
    sting = Feature.ENABLE
)
public abstract class DaggerDisabledRepositoryRepository extends AbstractRepository<Integer, DaggerDisabledRepository, DaggerDisabledRepositoryRepository> {
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
    attach( entity );
    return entity;
  }

  @Override
  @Action(
      reportParameters = false
  )
  public void destroy(@Nonnull final DaggerDisabledRepository entity) {
    super.destroy( entity );
  }
}
