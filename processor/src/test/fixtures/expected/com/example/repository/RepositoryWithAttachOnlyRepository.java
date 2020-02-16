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
public abstract class RepositoryWithAttachOnlyRepository extends AbstractRepository<Integer, RepositoryWithAttachOnly, RepositoryWithAttachOnlyRepository> {
  RepositoryWithAttachOnlyRepository() {
  }

  @Nonnull
  static RepositoryWithAttachOnlyRepository newRepository() {
    return new Arez_RepositoryWithAttachOnlyRepository();
  }

  @Override
  @Action(
      reportParameters = false
  )
  protected void attach(@Nonnull final RepositoryWithAttachOnly entity) {
    super.attach( entity );
  }

  @Override
  @Action(
      reportParameters = false
  )
  protected void destroy(@Nonnull final RepositoryWithAttachOnly entity) {
    super.destroy( entity );
  }
}
