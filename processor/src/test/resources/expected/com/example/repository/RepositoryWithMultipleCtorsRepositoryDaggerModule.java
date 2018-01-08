package com.example.repository;

import dagger.Module;
import dagger.Provides;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.inject.Singleton;

@Generated("arez.processor.ArezProcessor")
@Module
public interface RepositoryWithMultipleCtorsRepositoryDaggerModule {
  @Nonnull
  @Provides
  @Singleton
  static RepositoryWithMultipleCtorsRepository provideComponent(final Arez_RepositoryWithMultipleCtorsRepository component) {
    return component;
  }
}
