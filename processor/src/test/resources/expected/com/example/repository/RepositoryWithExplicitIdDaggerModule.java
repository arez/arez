package com.example.repository;

import dagger.Module;
import dagger.Provides;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.inject.Singleton;

@Generated("org.realityforge.arez.processor.ArezProcessor")
@Module
public interface RepositoryWithExplicitIdDaggerModule {
  @Nonnull
  @Singleton
  @Provides
  static RepositoryWithExplicitIdRepository provideRepository(final Arez_RepositoryWithExplicitIdRepository repository) {
    return repository;
  }
}
