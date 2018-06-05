package com.example.repository;

import dagger.Module;
import dagger.Provides;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.inject.Singleton;

@Generated("arez.processor.ArezProcessor")
@Module
public interface RepositoryWithDetachOnlyRepositoryDaggerModule {
  @Nonnull
  @Provides
  @Singleton
  static RepositoryWithDetachOnlyRepository provideComponent(final Arez_RepositoryWithDetachOnlyRepository component) {
    return component;
  }
}
