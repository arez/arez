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
  @Singleton
  @Provides
  static RepositoryWithProtectedConstructorRepository provideRepository(final Arez_RepositoryWithProtectedConstructorRepository repository) {
    return repository;
  }
}
