package com.example.repository;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.component.AbstractRepository;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.inject.Singleton;

@Generated("arez.processor.ArezProcessor")
@ArezComponent(
    nameIncludesId = false
)
@Singleton
public abstract class RepositoryWithSingletonRepository extends AbstractRepository<Long, RepositoryWithSingleton, RepositoryWithSingletonRepository> {
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
  RepositoryWithSingleton create(@Nonnull final String name) {
    final Arez_RepositoryWithSingleton entity = new Arez_RepositoryWithSingleton(name);
    entity.$$arez$$_setOnDispose( e -> destroy( e ) );
    registerEntity( entity );
    return entity;
  }

  @Override
  protected void preDisposeEntity(@Nonnull final RepositoryWithSingleton entity) {
    ((Arez_RepositoryWithSingleton) entity).$$arez$$_setOnDispose( null );
  }
}
