package com.example.repository;

import dagger.Module;
import dagger.Provides;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.inject.Singleton;

@Generated("arez.processor.ArezProcessor")
@Module
public interface CompleteRepositoryExampleRepositoryDaggerModule {
  @Nonnull
  @Provides
  @Singleton
  static CompleteRepositoryExampleRepository provideComponent(final Arez_CompleteRepositoryExampleRepository component) {
    return component;
  }
}
