package com.example.repository;

import dagger.Module;
import dagger.Provides;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.inject.Singleton;

@Generated("arez.processor.ArezProcessor")
@Module
public interface RepositoryWithDestroyAndDetachRepositoryDaggerModule {
  @Nonnull
  @Provides
  @Singleton
  static RepositoryWithDestroyAndDetachRepository provideComponent(final Arez_RepositoryWithDestroyAndDetachRepository component) {
    return component;
  }
}
