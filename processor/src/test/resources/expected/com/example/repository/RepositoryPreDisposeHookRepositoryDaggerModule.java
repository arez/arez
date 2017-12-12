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
  @Singleton
  @Provides
  static RepositoryPreDisposeHookRepository provideRepository(final Arez_RepositoryPreDisposeHookRepository repository) {
    return repository;
  }
}
