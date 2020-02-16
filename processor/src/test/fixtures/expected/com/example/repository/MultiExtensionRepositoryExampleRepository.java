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
public abstract class MultiExtensionRepositoryExampleRepository extends AbstractRepository<Integer, MultiExtensionRepositoryExample, MultiExtensionRepositoryExampleRepository> implements Extension1, Extension2 {
  MultiExtensionRepositoryExampleRepository() {
  }

  @Nonnull
  static MultiExtensionRepositoryExampleRepository newRepository() {
    return new Arez_MultiExtensionRepositoryExampleRepository();
  }

  @Action(
      name = "create"
  )
  @Nonnull
  MultiExtensionRepositoryExample create(@Nonnull final String name) {
    final Arez_MultiExtensionRepositoryExample entity = new Arez_MultiExtensionRepositoryExample(name);
    attach( entity );
    return entity;
  }

  @Override
  @Action(
      reportParameters = false
  )
  protected void destroy(@Nonnull final MultiExtensionRepositoryExample entity) {
    super.destroy( entity );
  }
}
