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
public abstract class RepositoryWithDetachOnlyRepository extends AbstractRepository<Long, RepositoryWithDetachOnly, RepositoryWithDetachOnlyRepository> {
  RepositoryWithDetachOnlyRepository() {
  }

  @Nonnull
  public static RepositoryWithDetachOnlyRepository newRepository() {
    return new Arez_RepositoryWithDetachOnlyRepository();
  }

  @Action(
      name = "create_name"
  )
  @Nonnull
  public RepositoryWithDetachOnly create(@Nonnull final String name) {
    final Arez_RepositoryWithDetachOnly entity = new Arez_RepositoryWithDetachOnly(name);
    attach( entity );
    return entity;
  }

  @Override
  @Action(
      reportParameters = false
  )
  public void detach(@Nonnull final RepositoryWithDetachOnly entity) {
    super.detach( entity );
  }
}
