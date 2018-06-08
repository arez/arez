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
public abstract class RepositoryWithDestroyAndDetachRepository extends AbstractRepository<Integer, RepositoryWithDestroyAndDetach, RepositoryWithDestroyAndDetachRepository> {
  RepositoryWithDestroyAndDetachRepository() {
  }

  @Nonnull
  public static RepositoryWithDestroyAndDetachRepository newRepository() {
    return new Arez_RepositoryWithDestroyAndDetachRepository();
  }

  @Action(
      name = "create_name"
  )
  @Nonnull
  public RepositoryWithDestroyAndDetach create(@Nonnull final String name) {
    final Arez_RepositoryWithDestroyAndDetach entity = new Arez_RepositoryWithDestroyAndDetach(name);
    attach( entity );
    return entity;
  }

  @Override
  @Action(
      reportParameters = false
  )
  public void destroy(@Nonnull final RepositoryWithDestroyAndDetach entity) {
    super.destroy( entity );
  }

  @Override
  @Action(
      reportParameters = false
  )
  public void detach(@Nonnull final RepositoryWithDestroyAndDetach entity) {
    super.detach( entity );
  }
}
