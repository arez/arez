package com.example.repository;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.component.internal.AbstractRepository;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Singleton;

@Generated("arez.processor.ArezProcessor")
@ArezComponent
@Singleton
public abstract class RepositoryWithExplicitIdRepository extends AbstractRepository<Integer, RepositoryWithExplicitId, RepositoryWithExplicitIdRepository> {
  RepositoryWithExplicitIdRepository() {
  }

  @Nonnull
  static RepositoryWithExplicitIdRepository newRepository() {
    return new Arez_RepositoryWithExplicitIdRepository();
  }

  @Action(
      name = "create_packageName_name"
  )
  @Nonnull
  RepositoryWithExplicitId create(@Nonnull final String packageName, @Nonnull final String name) {
    final Arez_RepositoryWithExplicitId entity = new Arez_RepositoryWithExplicitId(packageName,name);
    attach( entity );
    return entity;
  }

  @Nullable
  final RepositoryWithExplicitId findById(final int id) {
    return findByArezId( id );
  }

  @Nonnull
  final RepositoryWithExplicitId getById(final int id) {
    return getByArezId( id );
  }

  @Override
  @Action(
      reportParameters = false
  )
  protected void destroy(@Nonnull final RepositoryWithExplicitId entity) {
    super.destroy( entity );
  }
}
