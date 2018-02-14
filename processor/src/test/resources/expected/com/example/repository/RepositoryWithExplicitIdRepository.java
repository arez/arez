package com.example.repository;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.component.AbstractRepository;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Singleton;

@Generated("arez.processor.ArezProcessor")
@ArezComponent(
    nameIncludesId = false
)
@Singleton
public abstract class RepositoryWithExplicitIdRepository extends AbstractRepository<Integer, RepositoryWithExplicitId, RepositoryWithExplicitIdRepository> {
  RepositoryWithExplicitIdRepository() {
  }

  @Nonnull
  public static RepositoryWithExplicitIdRepository newRepository() {
    return new Arez_RepositoryWithExplicitIdRepository();
  }

  @Action(
      name = "create_packageName_name"
  )
  @Nonnull
  public RepositoryWithExplicitId create(@Nonnull final String packageName,
      @Nonnull final String name) {
    final Arez_RepositoryWithExplicitId entity = new Arez_RepositoryWithExplicitId(packageName,name);
    registerEntity( entity );
    return entity;
  }

  @Nullable
  public final RepositoryWithExplicitId findById(final int id) {
    return findByArezId( id );
  }

  @Nonnull
  public final RepositoryWithExplicitId getById(final int id) {
    return getByArezId( id );
  }
}
