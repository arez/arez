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
public abstract class RepositoryWithInitializerNameCollisionModelRepository extends AbstractRepository<Integer, RepositoryWithInitializerNameCollisionModel, RepositoryWithInitializerNameCollisionModelRepository> {
  RepositoryWithInitializerNameCollisionModelRepository() {
  }

  @Nonnull
  static RepositoryWithInitializerNameCollisionModelRepository newRepository() {
    return new Arez_RepositoryWithInitializerNameCollisionModelRepository();
  }

  @Action(
      name = "create_time"
  )
  @Nonnull
  RepositoryWithInitializerNameCollisionModel create(final int time, final long $$arezip$$_time) {
    final Arez_RepositoryWithInitializerNameCollisionModel entity = new Arez_RepositoryWithInitializerNameCollisionModel(time,$$arezip$$_time);
    attach( entity );
    return entity;
  }

  @Override
  @Action(
      reportParameters = false
  )
  protected void destroy(@Nonnull final RepositoryWithInitializerNameCollisionModel entity) {
    super.destroy( entity );
  }
}
