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
    service = Feature.ENABLE,
    dagger = Feature.ENABLE,
    sting = Feature.ENABLE
)
@Singleton
public abstract class RepositoryWithDetachOnlyRepository extends AbstractRepository<Integer, RepositoryWithDetachOnly, RepositoryWithDetachOnlyRepository> {
  RepositoryWithDetachOnlyRepository() {
  }

  @Nonnull
  static RepositoryWithDetachOnlyRepository newRepository() {
    return new Arez_RepositoryWithDetachOnlyRepository();
  }

  @Action(
      name = "create_name"
  )
  @Nonnull
  RepositoryWithDetachOnly create(@Nonnull final String name) {
    final Arez_RepositoryWithDetachOnly entity = new Arez_RepositoryWithDetachOnly(name);
    attach( entity );
    return entity;
  }

  @Override
  @Action(
      reportParameters = false
  )
  protected void detach(@Nonnull final RepositoryWithDetachOnly entity) {
    super.detach( entity );
  }
}
