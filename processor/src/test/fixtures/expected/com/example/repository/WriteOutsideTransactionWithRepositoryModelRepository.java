package com.example.repository;

import arez.ArezContext;
import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ContextRef;
import arez.annotations.Feature;
import arez.component.internal.AbstractRepository;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.inject.Singleton;

@Generated("arez.processor.ArezProcessor")
@ArezComponent(
    dagger = Feature.ENABLE,
    defaultWriteOutsideTransaction = Feature.ENABLE
)
@Singleton
public abstract class WriteOutsideTransactionWithRepositoryModelRepository extends AbstractRepository<Integer, WriteOutsideTransactionWithRepositoryModel, WriteOutsideTransactionWithRepositoryModelRepository> {
  WriteOutsideTransactionWithRepositoryModelRepository() {
  }

  @Nonnull
  public static WriteOutsideTransactionWithRepositoryModelRepository newRepository() {
    return new Arez_WriteOutsideTransactionWithRepositoryModelRepository();
  }

  @Nonnull
  public WriteOutsideTransactionWithRepositoryModel create(@Nonnull final String name) {
    final Arez_WriteOutsideTransactionWithRepositoryModel entity = new Arez_WriteOutsideTransactionWithRepositoryModel(name);
    attach( entity );
    return entity;
  }

  @Override
  @Action(
      reportParameters = false
  )
  public void destroy(@Nonnull final WriteOutsideTransactionWithRepositoryModel entity) {
    super.destroy( entity );
  }

  @ContextRef
  abstract ArezContext context();

  @Override
  protected final boolean reportWrite() {
    return context().isTrackingTransactionActive();
  }
}
