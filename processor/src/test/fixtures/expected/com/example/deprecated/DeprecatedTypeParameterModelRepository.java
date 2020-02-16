package com.example.deprecated;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.component.internal.AbstractRepository;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("arez.processor.ArezProcessor")
@SuppressWarnings("deprecation")
@ArezComponent(
    dagger = Feature.DISABLE
)
public abstract class DeprecatedTypeParameterModelRepository<T extends MyDeprecatedEntity> extends AbstractRepository<Integer, DeprecatedTypeParameterModel<T>, DeprecatedTypeParameterModelRepository<T>> {
  DeprecatedTypeParameterModelRepository() {
  }

  @Nonnull
  @SuppressWarnings("deprecation")
  public static <T extends MyDeprecatedEntity> DeprecatedTypeParameterModelRepository<T> newRepository(
      ) {
    return new Arez_DeprecatedTypeParameterModelRepository<>();
  }

  @Action(
      name = "create"
  )
  @Nonnull
  public DeprecatedTypeParameterModel<T> create() {
    final Arez_DeprecatedTypeParameterModel<T> entity = new Arez_DeprecatedTypeParameterModel<T>();
    attach( entity );
    return entity;
  }

  @Override
  @Action(
      reportParameters = false
  )
  public void destroy(@Nonnull final DeprecatedTypeParameterModel<T> entity) {
    super.destroy( entity );
  }
}
