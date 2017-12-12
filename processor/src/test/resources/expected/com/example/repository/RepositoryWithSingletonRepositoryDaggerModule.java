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
  @Singleton
  @Provides
  static RepositoryWithSingletonRepository provideRepository(final Arez_RepositoryWithSingletonRepository repository) {
    return repository;
  }
}
