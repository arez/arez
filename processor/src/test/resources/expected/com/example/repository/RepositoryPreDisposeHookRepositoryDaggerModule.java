package com.example.repository;

import dagger.Module;
import dagger.Provides;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.inject.Singleton;

@Generated("org.realityforge.arez.processor.ArezProcessor")
@Module
public interface RepositoryPreDisposeHookRepositoryDaggerModule {
  @Nonnull
  @Provides
  @Singleton
  static RepositoryPreDisposeHookRepository provideComponent(final Arez_RepositoryPreDisposeHookRepository component) {
    return component;
  }
}
