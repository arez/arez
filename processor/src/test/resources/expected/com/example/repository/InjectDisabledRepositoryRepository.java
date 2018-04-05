package com.example.repository;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.component.AbstractRepository;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("arez.processor.ArezProcessor")
@ArezComponent(
    nameIncludesId = false,
    inject = Feature.DISABLE
)
public abstract class InjectDisabledRepositoryRepository extends AbstractRepository<Long, InjectDisabledRepository, InjectDisabledRepositoryRepository> {
  InjectDisabledRepositoryRepository() {
  }

  @Nonnull
  public static InjectDisabledRepositoryRepository newRepository() {
    return new Arez_InjectDisabledRepositoryRepository();
  }

  @Action(
      name = "create_name"
  )
  @Nonnull
  public InjectDisabledRepository create(@Nonnull final String name) {
    final Arez_InjectDisabledRepository entity = new Arez_InjectDisabledRepository(name);
    registerEntity( entity );
    return entity;
  }
}
