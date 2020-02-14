package com.example.inject;

import dagger.Module;
import dagger.Provides;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.inject.Singleton;

@Generated("arez.processor.ArezProcessor")
@Module
public interface CtorInjectWithPostConstructModelDaggerModule {
  @Provides
  @Singleton
  static CtorInjectWithPostConstructModel create(@Nonnull final Runnable action) {
    return new Arez_CtorInjectWithPostConstructModel(action);
  }
}
