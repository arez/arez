package com.example.repository;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.component.AbstractRepository;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.inject.Singleton;

@Generated("arez.processor.ArezProcessor")
@ArezComponent(
    nameIncludesId = false
)
@Singleton
public abstract class RepositoryWithMultipleCtorsRepository extends AbstractRepository<Long, RepositoryWithMultipleCtors, RepositoryWithMultipleCtorsRepository> {
  RepositoryWithMultipleCtorsRepository() {
  }

  @Nonnull
  public static RepositoryWithMultipleCtorsRepository newRepository() {
    return new Arez_RepositoryWithMultipleCtorsRepository();
  }

  @Action(
      name = "create_packageName_name"
  )
  @Nonnull
  RepositoryWithMultipleCtors create(@Nonnull final String packageName,
      @Nonnull final String name) {
    final Arez_RepositoryWithMultipleCtors entity = new Arez_RepositoryWithMultipleCtors(packageName,name);
    entity.$$arez$$_setOnDispose( e -> destroy( e ) );
    registerEntity( entity );
    return entity;
  }

  @Action(
      name = "create_name"
  )
  @Nonnull
  RepositoryWithMultipleCtors create(@Nonnull final String name) {
    final Arez_RepositoryWithMultipleCtors entity = new Arez_RepositoryWithMultipleCtors(name);
    entity.$$arez$$_setOnDispose( e -> destroy( e ) );
    registerEntity( entity );
    return entity;
  }

  @Action(
      name = "create"
  )
  @Nonnull
  RepositoryWithMultipleCtors create() {
    final Arez_RepositoryWithMultipleCtors entity = new Arez_RepositoryWithMultipleCtors();
    entity.$$arez$$_setOnDispose( e -> destroy( e ) );
    registerEntity( entity );
    return entity;
  }

  @Override
  protected void preDisposeEntity(@Nonnull final RepositoryWithMultipleCtors entity) {
    ((Arez_RepositoryWithMultipleCtors) entity).$$arez$$_setOnDispose( null );
  }
}
