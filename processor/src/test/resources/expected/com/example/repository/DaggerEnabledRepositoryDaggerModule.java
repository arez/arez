package com.example.repository;

import dagger.Module;
import dagger.Provides;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.inject.Singleton;

@Generated("org.realityforge.arez.processor.ArezProcessor")
@Module
public interface DaggerEnabledRepositoryDaggerModule {
  @Nonnull
  @Singleton
  @Provides
  static DaggerEnabledRepositoryRepository provideRepository(final Arez_DaggerEnabledRepositoryRepository repository) {
    return repository;
  }
}
