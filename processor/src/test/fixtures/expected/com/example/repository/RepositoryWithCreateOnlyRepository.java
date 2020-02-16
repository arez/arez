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
public abstract class RepositoryWithCreateOnlyRepository extends AbstractRepository<Integer, RepositoryWithCreateOnly, RepositoryWithCreateOnlyRepository> {
  RepositoryWithCreateOnlyRepository() {
  }

  @Nonnull
  static RepositoryWithCreateOnlyRepository newRepository() {
    return new Arez_RepositoryWithCreateOnlyRepository();
  }

  @Action(
      name = "create_name"
  )
  @Nonnull
  RepositoryWithCreateOnly create(@Nonnull final String name) {
    final Arez_RepositoryWithCreateOnly entity = new Arez_RepositoryWithCreateOnly(name);
    attach( entity );
    return entity;
  }

  @Override
  @Action(
      reportParameters = false
  )
  protected void destroy(@Nonnull final RepositoryWithCreateOnly entity) {
    super.destroy( entity );
  }
}
