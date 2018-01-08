package com.example.repository;

import dagger.Module;
import dagger.Provides;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.inject.Singleton;

@Generated("arez.processor.ArezProcessor")
@Module
public interface DaggerEnabledRepositoryRepositoryDaggerModule {
  @Nonnull
  @Provides
  @Singleton
  static DaggerEnabledRepositoryRepository provideComponent(final Arez_DaggerEnabledRepositoryRepository component) {
    return component;
  }
}
