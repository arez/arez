package com.example.repository;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.component.AbstractRepository;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.inject.Singleton;

@Generated("arez.processor.ArezProcessor")
@ArezComponent
@Singleton
public abstract class RepositoryWithInitializerModelRepository extends AbstractRepository<Integer, RepositoryWithInitializerModel, RepositoryWithInitializerModelRepository> {
  RepositoryWithInitializerModelRepository() {
  }

  @Nonnull
  public static RepositoryWithInitializerModelRepository newRepository() {
    return new Arez_RepositoryWithInitializerModelRepository();
  }

  @Action(
      name = "create"
  )
  @Nonnull
  public RepositoryWithInitializerModel create(final long time) {
    final Arez_RepositoryWithInitializerModel entity = new Arez_RepositoryWithInitializerModel(time);
    attach( entity );
    return entity;
  }

  @Override
  @Action(
      reportParameters = false
  )
  public void destroy(@Nonnull final RepositoryWithInitializerModel entity) {
    super.destroy( entity );
  }
}
