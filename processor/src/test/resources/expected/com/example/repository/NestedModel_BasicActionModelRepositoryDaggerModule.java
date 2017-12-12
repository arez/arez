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
  @Provides
  @Singleton
  static NestedModel_BasicActionModelRepository provideComponent(final Arez_NestedModel_BasicActionModelRepository component) {
    return component;
  }
}
