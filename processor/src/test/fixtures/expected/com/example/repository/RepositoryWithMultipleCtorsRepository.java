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
public abstract class RepositoryWithMultipleCtorsRepository extends AbstractRepository<Integer, RepositoryWithMultipleCtors, RepositoryWithMultipleCtorsRepository> {
  RepositoryWithMultipleCtorsRepository() {
  }

  @Nonnull
  static RepositoryWithMultipleCtorsRepository newRepository() {
    return new Arez_RepositoryWithMultipleCtorsRepository();
  }

  @Action(
      name = "create_packageName_name"
  )
  @Nonnull
  RepositoryWithMultipleCtors create(@Nonnull final String packageName,
      @Nonnull final String name) {
    final Arez_RepositoryWithMultipleCtors entity = new Arez_RepositoryWithMultipleCtors(packageName,name);
    attach( entity );
    return entity;
  }

  @Action(
      name = "create_name"
  )
  @Nonnull
  RepositoryWithMultipleCtors create(@Nonnull final String name) {
    final Arez_RepositoryWithMultipleCtors entity = new Arez_RepositoryWithMultipleCtors(name);
    attach( entity );
    return entity;
  }

  @Action(
      name = "create"
  )
  @Nonnull
  RepositoryWithMultipleCtors create() {
    final Arez_RepositoryWithMultipleCtors entity = new Arez_RepositoryWithMultipleCtors();
    attach( entity );
    return entity;
  }

  @Override
  @Action(
      reportParameters = false
  )
  protected void destroy(@Nonnull final RepositoryWithMultipleCtors entity) {
    super.destroy( entity );
  }
}
