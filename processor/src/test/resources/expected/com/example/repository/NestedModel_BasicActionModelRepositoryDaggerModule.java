package com.example.repository;

import dagger.Module;
import dagger.Provides;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.inject.Singleton;

@Generated("org.realityforge.arez.processor.ArezProcessor")
@Module
public interface NestedModel_BasicActionModelRepositoryDaggerModule {
  @Nonnull
  @Singleton
  @Provides
  static NestedModel_BasicActionModelRepository provideRepository(final Arez_NestedModel_BasicActionModelRepository repository) {
    return repository;
  }
}
