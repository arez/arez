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
public abstract class RepositoryWithMultipleCtorsRepository extends AbstractRepository<Long, RepositoryWithMultipleCtors, RepositoryWithMultipleCtorsRepository> {
  RepositoryWithMultipleCtorsRepository() {
  }

  @Nonnull
  public static RepositoryWithMultipleCtorsRepository newRepository() {
    return new Arez_RepositoryWithMultipleCtorsRepository();
  }

  @Action(
      name = "create_packageName_name"
  )
  @Nonnull
  public RepositoryWithMultipleCtors create(@Nonnull final String packageName,
      @Nonnull final String name) {
    final Arez_RepositoryWithMultipleCtors entity = new Arez_RepositoryWithMultipleCtors(packageName,name);
    attach( entity );
    return entity;
  }

  @Action(
      name = "create_name"
  )
  @Nonnull
  public RepositoryWithMultipleCtors create(@Nonnull final String name) {
    final Arez_RepositoryWithMultipleCtors entity = new Arez_RepositoryWithMultipleCtors(name);
    attach( entity );
    return entity;
  }

  @Action(
      name = "create"
  )
  @Nonnull
  public RepositoryWithMultipleCtors create() {
    final Arez_RepositoryWithMultipleCtors entity = new Arez_RepositoryWithMultipleCtors();
    attach( entity );
    return entity;
  }

  @Override
  @Action(
      reportParameters = false
  )
  public void destroy(@Nonnull final RepositoryWithMultipleCtors entity) {
    super.destroy( entity );
  }
}
