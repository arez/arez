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
public abstract class RepositoryWithImplicitIdRepository extends AbstractRepository<Integer, RepositoryWithImplicitId, RepositoryWithImplicitIdRepository> {
  RepositoryWithImplicitIdRepository() {
  }

  @Nonnull
  static RepositoryWithImplicitIdRepository newRepository() {
    return new Arez_RepositoryWithImplicitIdRepository();
  }

  @Action(
      name = "create_packageName_name"
  )
  @Nonnull
  RepositoryWithImplicitId create(@Nonnull final String packageName, @Nonnull final String name) {
    final Arez_RepositoryWithImplicitId entity = new Arez_RepositoryWithImplicitId(packageName,name);
    attach( entity );
    return entity;
  }

  @Override
  @Action(
      reportParameters = false
  )
  protected void destroy(@Nonnull final RepositoryWithImplicitId entity) {
    super.destroy( entity );
  }
}
