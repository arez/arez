package com.example.repository;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.component.AbstractRepository;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.inject.Singleton;

@Generated("arez.processor.ArezProcessor")
@ArezComponent(
    inject = Feature.ENABLE
)
@Singleton
public abstract class InjectEnabledRepositoryRepository extends AbstractRepository<Long, InjectEnabledRepository, InjectEnabledRepositoryRepository> {
  InjectEnabledRepositoryRepository() {
  }

  @Nonnull
  public static InjectEnabledRepositoryRepository newRepository() {
    return new Arez_InjectEnabledRepositoryRepository();
  }

  @Action(
      name = "create_name"
  )
  @Nonnull
  public InjectEnabledRepository create(@Nonnull final String name) {
    final Arez_InjectEnabledRepository entity = new Arez_InjectEnabledRepository(name);
    attach( entity );
    return entity;
  }

  @Override
  @Action(
      reportParameters = false
  )
  public void destroy(@Nonnull final InjectEnabledRepository entity) {
    super.destroy( entity );
  }
}
