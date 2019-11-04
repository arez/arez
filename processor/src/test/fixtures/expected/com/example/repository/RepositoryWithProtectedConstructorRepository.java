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
public abstract class RepositoryWithProtectedConstructorRepository extends AbstractRepository<Integer, RepositoryWithProtectedConstructor, RepositoryWithProtectedConstructorRepository> {
  RepositoryWithProtectedConstructorRepository() {
  }

  @Nonnull
  public static RepositoryWithProtectedConstructorRepository newRepository() {
    return new Arez_RepositoryWithProtectedConstructorRepository();
  }

  @Action(
      name = "create_name"
  )
  @Nonnull
  public RepositoryWithProtectedConstructor create(@Nonnull final String name) {
    final Arez_RepositoryWithProtectedConstructor entity = new Arez_RepositoryWithProtectedConstructor(name);
    attach( entity );
    return entity;
  }

  @Override
  @Action(
      reportParameters = false
  )
  public void destroy(@Nonnull final RepositoryWithProtectedConstructor entity) {
    super.destroy( entity );
  }
}
