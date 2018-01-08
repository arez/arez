package com.example.repository;

import dagger.Module;
import dagger.Provides;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.inject.Singleton;

@Generated("arez.processor.ArezProcessor")
@Module
public interface InjectEnabledRepositoryRepositoryDaggerModule {
  @Nonnull
  @Provides
  @Singleton
  static InjectEnabledRepositoryRepository provideComponent(final Arez_InjectEnabledRepositoryRepository component) {
    return component;
  }
}
