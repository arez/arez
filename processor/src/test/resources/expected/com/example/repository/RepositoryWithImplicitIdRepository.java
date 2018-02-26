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
public abstract class RepositoryWithImplicitIdRepository extends AbstractRepository<Long, RepositoryWithImplicitId, RepositoryWithImplicitIdRepository> {
  RepositoryWithImplicitIdRepository() {
  }

  @Nonnull
  public static RepositoryWithImplicitIdRepository newRepository() {
    return new Arez_RepositoryWithImplicitIdRepository();
  }

  @Action(
      name = "create_packageName_name"
  )
  @Nonnull
  public RepositoryWithImplicitId create(@Nonnull final String packageName,
      @Nonnull final String name) {
    final Arez_RepositoryWithImplicitId entity = new Arez_RepositoryWithImplicitId(packageName,name);
    registerEntity( entity );
    return entity;
  }
}
