package com.example.id;

import dagger.Module;
import dagger.Provides;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.inject.Singleton;

@Generated("arez.processor.ArezProcessor")
@Module
public interface RepositoryExampleRepositoryDaggerModule {
  @Nonnull
  @Provides
  @Singleton
  static RepositoryExampleRepository provideComponent(final Arez_RepositoryExampleRepository component) {
    return component;
  }
}
