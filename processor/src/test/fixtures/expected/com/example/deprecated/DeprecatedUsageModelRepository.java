package com.example.deprecated;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.component.internal.AbstractRepository;
import java.util.List;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.inject.Singleton;

@Generated("arez.processor.ArezProcessor")
@ArezComponent(
    dagger = Feature.ENABLE
)
@Singleton
public abstract class DeprecatedUsageModelRepository extends AbstractRepository<Integer, DeprecatedUsageModel, DeprecatedUsageModelRepository> {
  DeprecatedUsageModelRepository() {
  }

  @Nonnull
  public static DeprecatedUsageModelRepository newRepository() {
    return new Arez_DeprecatedUsageModelRepository();
  }

  @Action(
      name = "create"
  )
  @Nonnull
  @SuppressWarnings("deprecation")
  public DeprecatedUsageModel create(final MyDeprecatedEntity myEntity,
      final List<MyDeprecatedEntity> myEntityList) {
    final Arez_DeprecatedUsageModel entity = new Arez_DeprecatedUsageModel(myEntity,myEntityList);
    attach( entity );
    return entity;
  }

  @Override
  @Action(
      reportParameters = false
  )
  public void destroy(@Nonnull final DeprecatedUsageModel entity) {
    super.destroy( entity );
  }
}
