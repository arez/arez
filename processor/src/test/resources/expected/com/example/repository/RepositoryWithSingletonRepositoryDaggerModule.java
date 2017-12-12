package com.example.repository;

import dagger.Module;
import dagger.Provides;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.inject.Singleton;

@Generated("org.realityforge.arez.processor.ArezProcessor")
@Module
public interface RepositoryWithSingletonRepositoryDaggerModule {
  @Nonnull
  @Provides
  @Singleton
  static RepositoryWithSingletonRepository provideComponent(final Arez_RepositoryWithSingletonRepository component) {
    return component;
  }
}
