package com.example.repository;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.component.AbstractRepository;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Singleton;

@Generated("arez.processor.ArezProcessor")
@ArezComponent
@Singleton
abstract class PackageAccessRepositoryExampleRepository extends AbstractRepository<Integer, PackageAccessRepositoryExample, PackageAccessRepositoryExampleRepository> implements PackageAccessRepositoryExampleRepositoryExtension {
  PackageAccessRepositoryExampleRepository() {
  }

  @Nonnull
  static PackageAccessRepositoryExampleRepository newRepository() {
    return new Arez_PackageAccessRepositoryExampleRepository();
  }

  @Action(
      name = "create_packageName_name"
  )
  @Nonnull
  PackageAccessRepositoryExample create(@Nonnull final String packageName,
      @Nonnull final String name) {
    final Arez_PackageAccessRepositoryExample entity = new Arez_PackageAccessRepositoryExample(packageName,name);
    attach( entity );
    return entity;
  }

  @Nullable
  final PackageAccessRepositoryExample findById(final int id) {
    return findByArezId( id );
  }

  @Nonnull
  final PackageAccessRepositoryExample getById(final int id) {
    return getByArezId( id );
  }

  @Override
  @Action(
      reportParameters = false
  )
  protected void destroy(@Nonnull final PackageAccessRepositoryExample entity) {
    super.destroy( entity );
  }
}
