package com.example.repository;

import dagger.Module;
import dagger.Provides;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.inject.Singleton;

@Generated("arez.processor.ArezProcessor")
@Module
public interface PackageAccessRepositoryExampleRepositoryDaggerModule {
  @Nonnull
  @Provides
  @Singleton
  static PackageAccessRepositoryExampleRepository provideComponent(final Arez_PackageAccessRepositoryExampleRepository component) {
    return component;
  }
}
