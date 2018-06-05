package com.example.repository;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.component.AbstractRepository;
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
  public static RepositoryWithExplicitNonStandardIdRepository newRepository() {
    return new Arez_RepositoryWithExplicitNonStandardIdRepository();
  }

  @Action(
      name = "create_packageName_name"
  )
  @Nonnull
  public RepositoryWithExplicitNonStandardId create(@Nonnull final String packageName,
      @Nonnull final String name) {
    final Arez_RepositoryWithExplicitNonStandardId entity = new Arez_RepositoryWithExplicitNonStandardId(packageName,name);
    registerEntity( entity );
    return entity;
  }

  @Nullable
  public final RepositoryWithExplicitNonStandardId findById(final int id) {
    return findByArezId( id );
  }

  @Nonnull
  public final RepositoryWithExplicitNonStandardId getById(final int id) {
    return getByArezId( id );
  }

  @Override
  @Action(
      reportParameters = false
  )
  public void destroy(@Nonnull final RepositoryWithExplicitNonStandardId entity) {
    super.destroy( entity );
  }
}
