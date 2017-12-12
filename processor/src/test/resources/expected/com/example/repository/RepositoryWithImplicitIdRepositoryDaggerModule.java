package com.example.repository;

import dagger.Module;
import dagger.Provides;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.inject.Singleton;

@Generated("org.realityforge.arez.processor.ArezProcessor")
@Module
public interface RepositoryWithImplicitIdRepositoryDaggerModule {
  @Nonnull
  @Singleton
  @Provides
  static RepositoryWithImplicitIdRepository provideRepository(final Arez_RepositoryWithImplicitIdRepository repository) {
    return repository;
  }
}
