package com.example.repository;

import dagger.Module;
import dagger.Provides;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.inject.Singleton;

@Generated("org.realityforge.arez.processor.ArezProcessor")
@Module
public interface RepositoryWithProtectedConstructorRepositoryDaggerModule {
  @Nonnull
  @Provides
  @Singleton
  static RepositoryWithProtectedConstructorRepository provideComponent(final Arez_RepositoryWithProtectedConstructorRepository component) {
    return component;
  }
}
