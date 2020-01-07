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
    defaultReadOutsideTransaction = Feature.ENABLE
)
@Singleton
public abstract class ReadOutsideTransactionWithRepositoryModelRepository extends AbstractRepository<Integer, ReadOutsideTransactionWithRepositoryModel, ReadOutsideTransactionWithRepositoryModelRepository> {
  ReadOutsideTransactionWithRepositoryModelRepository() {
  }

  @Nonnull
  public static ReadOutsideTransactionWithRepositoryModelRepository newRepository() {
    return new Arez_ReadOutsideTransactionWithRepositoryModelRepository();
  }

  @Action(
      name = "create"
  )
  @Nonnull
  public ReadOutsideTransactionWithRepositoryModel create(@Nonnull final String name) {
    final Arez_ReadOutsideTransactionWithRepositoryModel entity = new Arez_ReadOutsideTransactionWithRepositoryModel(name);
    attach( entity );
    return entity;
  }

  @Override
  @Action(
      reportParameters = false
  )
  public void destroy(@Nonnull final ReadOutsideTransactionWithRepositoryModel entity) {
    super.destroy( entity );
  }
}
