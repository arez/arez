package com.example.repository;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.component.internal.AbstractRepository;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.inject.Singleton;

@Generated("arez.processor.ArezProcessor")
@ArezComponent
@Singleton
public abstract class RepositoryWithSingletonRepository extends AbstractRepository<Integer, RepositoryWithSingleton, RepositoryWithSingletonRepository> {
  RepositoryWithSingletonRepository() {
  }

  @Nonnull
  public static RepositoryWithSingletonRepository newRepository() {
    return new Arez_RepositoryWithSingletonRepository();
  }

  @Action(
      name = "create_name"
  )
  @Nonnull
  public RepositoryWithSingleton create(@Nonnull final String name) {
    final Arez_RepositoryWithSingleton entity = new Arez_RepositoryWithSingleton(name);
    attach( entity );
    return entity;
  }

  @Override
  @Action(
      reportParameters = false
  )
  public void destroy(@Nonnull final RepositoryWithSingleton entity) {
    super.destroy( entity );
  }
}
