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
public abstract class RepositoryWithDestroyAndDetachRepository extends AbstractRepository<Integer, RepositoryWithDestroyAndDetach, RepositoryWithDestroyAndDetachRepository> {
  RepositoryWithDestroyAndDetachRepository() {
  }

  @Nonnull
  static RepositoryWithDestroyAndDetachRepository newRepository() {
    return new Arez_RepositoryWithDestroyAndDetachRepository();
  }

  @Action(
      name = "create_name"
  )
  @Nonnull
  RepositoryWithDestroyAndDetach create(@Nonnull final String name) {
    final Arez_RepositoryWithDestroyAndDetach entity = new Arez_RepositoryWithDestroyAndDetach(name);
    attach( entity );
    return entity;
  }

  @Override
  @Action(
      reportParameters = false
  )
  protected void destroy(@Nonnull final RepositoryWithDestroyAndDetach entity) {
    super.destroy( entity );
  }

  @Override
  @Action(
      reportParameters = false
  )
  protected void detach(@Nonnull final RepositoryWithDestroyAndDetach entity) {
    super.detach( entity );
  }
}
