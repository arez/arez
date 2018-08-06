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
public abstract class RepositoryWithInitializerNameCollisionModelRepository extends AbstractRepository<Integer, RepositoryWithInitializerNameCollisionModel, RepositoryWithInitializerNameCollisionModelRepository> {
  RepositoryWithInitializerNameCollisionModelRepository() {
  }

  @Nonnull
  public static RepositoryWithInitializerNameCollisionModelRepository newRepository() {
    return new Arez_RepositoryWithInitializerNameCollisionModelRepository();
  }

  @Action(
      name = "create_time"
  )
  @Nonnull
  public RepositoryWithInitializerNameCollisionModel create(final int time,
      final long $$arezip$$_time) {
    final Arez_RepositoryWithInitializerNameCollisionModel entity = new Arez_RepositoryWithInitializerNameCollisionModel(time,$$arezip$$_time);
    attach( entity );
    return entity;
  }

  @Override
  @Action(
      reportParameters = false
  )
  public void destroy(@Nonnull final RepositoryWithInitializerNameCollisionModel entity) {
    super.destroy( entity );
  }
}
