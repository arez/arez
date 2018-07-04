package com.example.repository;

import dagger.Module;
import dagger.Provides;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.inject.Singleton;

@Generated("arez.processor.ArezProcessor")
@Module
public interface RepositoryWithMultipleInitializersModelRepositoryDaggerModule {
  @Nonnull
  @Provides
  @Singleton
  static RepositoryWithMultipleInitializersModelRepository provideComponent(final Arez_RepositoryWithMultipleInitializersModelRepository component) {
    return component;
  }
}
