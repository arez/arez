package com.example.repository;

import dagger.Module;
import dagger.Provides;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.inject.Singleton;

@Generated("org.realityforge.arez.processor.ArezProcessor")
@Module
public interface RepositoryWithExplicitIdRepositoryDaggerModule {
  @Nonnull
  @Provides
  @Singleton
  static RepositoryWithExplicitIdRepository provideComponent(final Arez_RepositoryWithExplicitIdRepository component) {
    return component;
  }
}
