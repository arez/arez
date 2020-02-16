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
public abstract class RepositoryWithCreateOrAttachRepository extends AbstractRepository<Integer, RepositoryWithCreateOrAttach, RepositoryWithCreateOrAttachRepository> {
  RepositoryWithCreateOrAttachRepository() {
  }

  @Nonnull
  static RepositoryWithCreateOrAttachRepository newRepository() {
    return new Arez_RepositoryWithCreateOrAttachRepository();
  }

  @Action(
      name = "create_name"
  )
  @Nonnull
  RepositoryWithCreateOrAttach create(@Nonnull final String name) {
    final Arez_RepositoryWithCreateOrAttach entity = new Arez_RepositoryWithCreateOrAttach(name);
    attach( entity );
    return entity;
  }

  @Override
  @Action(
      reportParameters = false
  )
  protected void attach(@Nonnull final RepositoryWithCreateOrAttach entity) {
    super.attach( entity );
  }

  @Override
  @Action(
      reportParameters = false
  )
  protected void destroy(@Nonnull final RepositoryWithCreateOrAttach entity) {
    super.destroy( entity );
  }
}
