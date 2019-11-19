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
public abstract class RepositoryWithExplicitNonStandardIdRepository extends AbstractRepository<Integer, RepositoryWithExplicitNonStandardId, RepositoryWithExplicitNonStandardIdRepository> {
  RepositoryWithExplicitNonStandardIdRepository() {
  }

  @Nonnull
  static RepositoryWithExplicitNonStandardIdRepository newRepository() {
    return new Arez_RepositoryWithExplicitNonStandardIdRepository();
  }

  @Action(
      name = "create_packageName_name"
  )
  @Nonnull
  RepositoryWithExplicitNonStandardId create(@Nonnull final String packageName,
      @Nonnull final String name) {
    final Arez_RepositoryWithExplicitNonStandardId entity = new Arez_RepositoryWithExplicitNonStandardId(packageName,name);
    attach( entity );
    return entity;
  }

  @Nullable
  final RepositoryWithExplicitNonStandardId findById(final int id) {
    return findByArezId( id );
  }

  @Nonnull
  final RepositoryWithExplicitNonStandardId getById(final int id) {
    return getByArezId( id );
  }

  @Override
  @Action(
      reportParameters = false
  )
  protected void destroy(@Nonnull final RepositoryWithExplicitNonStandardId entity) {
    super.destroy( entity );
  }
}
