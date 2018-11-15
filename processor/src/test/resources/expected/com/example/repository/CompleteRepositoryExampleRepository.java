package com.example.repository;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.component.internal.AbstractRepository;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Singleton;

@Generated("arez.processor.ArezProcessor")
@ArezComponent
@Singleton
public abstract class CompleteRepositoryExampleRepository extends AbstractRepository<Integer, CompleteRepositoryExample, CompleteRepositoryExampleRepository> implements CompleteRepositoryExampleRepositoryExtension {
  CompleteRepositoryExampleRepository() {
  }

  @Nonnull
  public static CompleteRepositoryExampleRepository newRepository() {
    return new Arez_CompleteRepositoryExampleRepository();
  }

  @Action(
      name = "create_packageName_name"
  )
  @Nonnull
  public CompleteRepositoryExample create(@Nonnull final String packageName,
      @Nonnull final String name) {
    final Arez_CompleteRepositoryExample entity = new Arez_CompleteRepositoryExample(packageName,name);
    attach( entity );
    return entity;
  }

  @Nullable
  public final CompleteRepositoryExample findById(final int id) {
    return findByArezId( id );
  }

  @Nonnull
  public final CompleteRepositoryExample getById(final int id) {
    return getByArezId( id );
  }

  @Override
  @Action(
      reportParameters = false
  )
  public void destroy(@Nonnull final CompleteRepositoryExample entity) {
    super.destroy( entity );
  }
}
