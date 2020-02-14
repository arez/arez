package com.example.inject;

import dagger.Module;
import dagger.Provides;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.inject.Singleton;

@Generated("arez.processor.ArezProcessor")
@Module
public interface CtorInjectModelDaggerModule {
  @Provides
  @Singleton
  static CtorInjectModel create(@Nonnull final Runnable action) {
    return new Arez_CtorInjectModel(action);
  }
}
