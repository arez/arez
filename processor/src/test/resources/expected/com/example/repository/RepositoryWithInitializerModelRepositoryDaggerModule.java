package com.example.repository;

import dagger.Module;
import dagger.Provides;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.inject.Singleton;

@Generated("arez.processor.ArezProcessor")
@Module
public interface RepositoryWithInitializerModelRepositoryDaggerModule {
  @Nonnull
  @Provides
  @Singleton
  static RepositoryWithInitializerModelRepository provideComponent(final Arez_RepositoryWithInitializerModelRepository component) {
    return component;
  }
}
