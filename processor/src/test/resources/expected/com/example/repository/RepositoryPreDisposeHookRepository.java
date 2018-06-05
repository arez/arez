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
  public RepositoryPreDisposeHook create(@Nonnull final String name) {
    final Arez_RepositoryPreDisposeHook entity = new Arez_RepositoryPreDisposeHook(name);
    attach( entity );
    return entity;
  }

  @Override
  @Action(
      reportParameters = false
  )
  public void destroy(@Nonnull final RepositoryPreDisposeHook entity) {
    super.destroy( entity );
  }
}
