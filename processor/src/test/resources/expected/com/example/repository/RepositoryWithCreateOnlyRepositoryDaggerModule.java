package com.example.repository;

import dagger.Module;
import dagger.Provides;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.inject.Singleton;

@Generated("arez.processor.ArezProcessor")
@Module
public interface RepositoryWithCreateOnlyRepositoryDaggerModule {
  @Nonnull
  @Provides
  @Singleton
  static RepositoryWithCreateOnlyRepository provideComponent(final Arez_RepositoryWithCreateOnlyRepository component) {
    return component;
  }
}
