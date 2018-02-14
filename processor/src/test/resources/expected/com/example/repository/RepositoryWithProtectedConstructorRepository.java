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
public abstract class RepositoryWithProtectedConstructorRepository extends AbstractRepository<Long, RepositoryWithProtectedConstructor, RepositoryWithProtectedConstructorRepository> {
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
    registerEntity( entity );
    return entity;
  }
}
