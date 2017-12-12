package com.example.repository;

import dagger.Module;
import dagger.Provides;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.inject.Singleton;

@Generated("org.realityforge.arez.processor.ArezProcessor")
@Module
public interface InjectEnabledRepositoryRepositoryDaggerModule {
  @Nonnull
  @Singleton
  @Provides
  static InjectEnabledRepositoryRepository provideRepository(final Arez_InjectEnabledRepositoryRepository repository) {
    return repository;
  }
}
