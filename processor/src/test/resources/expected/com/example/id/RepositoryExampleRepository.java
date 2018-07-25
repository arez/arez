package com.example.id;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.component.AbstractRepository;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.inject.Singleton;

@Generated("arez.processor.ArezProcessor")
@ArezComponent
@Singleton
abstract class RepositoryExampleRepository extends AbstractRepository<Integer, RepositoryExample, RepositoryExampleRepository> {
  RepositoryExampleRepository() {
  }

  @Nonnull
  static RepositoryExampleRepository newRepository() {
    return new Arez_RepositoryExampleRepository();
  }

  @Action(
      name = "create"
  )
  @Nonnull
  RepositoryExample create() {
    final Arez_RepositoryExample entity = new Arez_RepositoryExample();
    attach( entity );
    return entity;
  }

  @Override
  @Action(
      reportParameters = false
  )
  protected void destroy(@Nonnull final RepositoryExample entity) {
    super.destroy( entity );
  }
}
