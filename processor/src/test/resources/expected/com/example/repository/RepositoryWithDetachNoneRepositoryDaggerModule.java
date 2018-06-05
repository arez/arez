package com.example.repository;

import dagger.Module;
import dagger.Provides;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.inject.Singleton;

@Generated("arez.processor.ArezProcessor")
@Module
public interface RepositoryWithDetachNoneRepositoryDaggerModule {
  @Nonnull
  @Provides
  @Singleton
  static RepositoryWithDetachNoneRepository provideComponent(final Arez_RepositoryWithDetachNoneRepository component) {
    return component;
  }
}
