package com.example.repository;

import dagger.Module;
import dagger.Provides;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.inject.Singleton;

@Generated("org.realityforge.arez.processor.ArezProcessor")
@Module
public interface CompleteRepositoryExampleDaggerModule {
  @Nonnull
  @Singleton
  @Provides
  static CompleteRepositoryExampleRepository provideRepository(final Arez_CompleteRepositoryExampleRepository repository) {
    return repository;
  }
}
