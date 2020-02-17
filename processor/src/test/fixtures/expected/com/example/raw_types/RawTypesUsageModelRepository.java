package com.example.raw_types;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.component.internal.AbstractRepository;
import java.util.List;
import java.util.concurrent.Callable;
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
public abstract class RawTypesUsageModelRepository extends AbstractRepository<Integer, RawTypesUsageModel, RawTypesUsageModelRepository> {
  RawTypesUsageModelRepository() {
  }

  @Nonnull
  public static RawTypesUsageModelRepository newRepository() {
    return new Arez_RawTypesUsageModelRepository();
  }

  @Action(
      name = "create"
  )
  @Nonnull
  @SuppressWarnings("rawtypes")
  public RawTypesUsageModel create(final Callable myCallable, final List<Callable> myCallableList) {
    final Arez_RawTypesUsageModel entity = new Arez_RawTypesUsageModel(myCallable,myCallableList);
    attach( entity );
    return entity;
  }

  @Override
  @Action(
      reportParameters = false
  )
  public void destroy(@Nonnull final RawTypesUsageModel entity) {
    super.destroy( entity );
  }
}
