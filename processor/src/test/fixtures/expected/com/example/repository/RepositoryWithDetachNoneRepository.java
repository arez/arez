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
public abstract class RepositoryWithDetachNoneRepository extends AbstractRepository<Integer, RepositoryWithDetachNone, RepositoryWithDetachNoneRepository> {
  RepositoryWithDetachNoneRepository() {
  }

  @Nonnull
  static RepositoryWithDetachNoneRepository newRepository() {
    return new Arez_RepositoryWithDetachNoneRepository();
  }

  @Action(
      name = "create_name"
  )
  @Nonnull
  RepositoryWithDetachNone create(@Nonnull final String name) {
    final Arez_RepositoryWithDetachNone entity = new Arez_RepositoryWithDetachNone(name);
    attach( entity );
    return entity;
  }
}
