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
public abstract class RepositoryWithCreateOrAttachRepository extends AbstractRepository<Integer, RepositoryWithCreateOrAttach, RepositoryWithCreateOrAttachRepository> {
  RepositoryWithCreateOrAttachRepository() {
  }

  @Nonnull
  public static RepositoryWithCreateOrAttachRepository newRepository() {
    return new Arez_RepositoryWithCreateOrAttachRepository();
  }

  @Action(
      name = "create_name"
  )
  @Nonnull
  public RepositoryWithCreateOrAttach create(@Nonnull final String name) {
    final Arez_RepositoryWithCreateOrAttach entity = new Arez_RepositoryWithCreateOrAttach(name);
    attach( entity );
    return entity;
  }

  @Override
  @Action(
      reportParameters = false
  )
  public void attach(@Nonnull final RepositoryWithCreateOrAttach entity) {
    super.attach( entity );
  }

  @Override
  @Action(
      reportParameters = false
  )
  public void destroy(@Nonnull final RepositoryWithCreateOrAttach entity) {
    super.destroy( entity );
  }
}
