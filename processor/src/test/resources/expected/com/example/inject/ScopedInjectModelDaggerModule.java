package com.example.inject;

import dagger.Module;
import dagger.Provides;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.inject.Singleton;

@Generated("org.realityforge.arez.processor.ArezProcessor")
@Module
public interface ScopedInjectModelDaggerModule {
  @Nonnull
  @Provides
  @Singleton
  static ScopedInjectModel provideComponent(final Arez_ScopedInjectModel component) {
    return component;
  }
}
