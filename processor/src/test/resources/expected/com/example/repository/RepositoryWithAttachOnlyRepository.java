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
public abstract class RepositoryWithAttachOnlyRepository extends AbstractRepository<Integer, RepositoryWithAttachOnly, RepositoryWithAttachOnlyRepository> {
  RepositoryWithAttachOnlyRepository() {
  }

  @Nonnull
  public static RepositoryWithAttachOnlyRepository newRepository() {
    return new Arez_RepositoryWithAttachOnlyRepository();
  }

  @Override
  @Action(
      reportParameters = false
  )
  public void attach(@Nonnull final RepositoryWithAttachOnly entity) {
    super.attach( entity );
  }

  @Override
  @Action(
      reportParameters = false
  )
  public void destroy(@Nonnull final RepositoryWithAttachOnly entity) {
    super.destroy( entity );
  }
}
