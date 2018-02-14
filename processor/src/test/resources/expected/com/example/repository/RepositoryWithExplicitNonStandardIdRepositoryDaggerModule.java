package com.example.repository;

import dagger.Module;
import dagger.Provides;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.inject.Singleton;

@Generated("arez.processor.ArezProcessor")
@Module
public interface RepositoryWithExplicitNonStandardIdRepositoryDaggerModule {
  @Nonnull
  @Provides
  @Singleton
  static RepositoryWithExplicitNonStandardIdRepository provideComponent(final Arez_RepositoryWithExplicitNonStandardIdRepository component) {
    return component;
  }
}
