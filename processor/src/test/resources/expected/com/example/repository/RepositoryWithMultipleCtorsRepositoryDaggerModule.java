package com.example.repository;

import dagger.Module;
import dagger.Provides;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.inject.Singleton;

@Generated("org.realityforge.arez.processor.ArezProcessor")
@Module
public interface RepositoryWithMultipleCtorsRepositoryDaggerModule {
  @Nonnull
  @Singleton
  @Provides
  static RepositoryWithMultipleCtorsRepository provideRepository(final Arez_RepositoryWithMultipleCtorsRepository repository) {
    return repository;
  }
}
