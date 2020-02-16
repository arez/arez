package com.example.repository;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.component.internal.AbstractRepository;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.inject.Singleton;

@Generated("arez.processor.ArezProcessor")
@ArezComponent(
    dagger = Feature.ENABLE
)
@Singleton
public abstract class RepositoryPreDisposeHookRepository extends AbstractRepository<Integer, RepositoryPreDisposeHook, RepositoryPreDisposeHookRepository> {
  RepositoryPreDisposeHookRepository() {
  }

  @Nonnull
  static RepositoryPreDisposeHookRepository newRepository() {
    return new Arez_RepositoryPreDisposeHookRepository();
  }

  @Action(
      name = "create_name"
  )
  @Nonnull
  RepositoryPreDisposeHook create(@Nonnull final String name) {
    final Arez_RepositoryPreDisposeHook entity = new Arez_RepositoryPreDisposeHook(name);
    attach( entity );
    return entity;
  }

  @Override
  @Action(
      reportParameters = false
  )
  protected void destroy(@Nonnull final RepositoryPreDisposeHook entity) {
    super.destroy( entity );
  }
}
