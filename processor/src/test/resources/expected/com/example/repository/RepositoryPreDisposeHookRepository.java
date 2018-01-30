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
public abstract class RepositoryPreDisposeHookRepository extends AbstractRepository<Long, RepositoryPreDisposeHook, RepositoryPreDisposeHookRepository> {
  RepositoryPreDisposeHookRepository() {
  }

  @Nonnull
  public static RepositoryPreDisposeHookRepository newRepository() {
    return new Arez_RepositoryPreDisposeHookRepository();
  }

  @Action(
      name = "create_name"
  )
  @Nonnull
  RepositoryPreDisposeHook create(@Nonnull final String name) {
    final Arez_RepositoryPreDisposeHook entity = new Arez_RepositoryPreDisposeHook(name);
    entity.$$arez$$_setOnDispose( e -> destroy( e ) );
    registerEntity( entity );
    return entity;
  }

  @Override
  protected void preDisposeEntity(@Nonnull final RepositoryPreDisposeHook entity) {
    ((Arez_RepositoryPreDisposeHook) entity).$$arez$$_setOnDispose( null );
  }
}
