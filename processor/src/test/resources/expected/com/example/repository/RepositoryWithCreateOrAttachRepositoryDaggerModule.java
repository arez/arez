package com.example.repository;

import dagger.Module;
import dagger.Provides;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.inject.Singleton;

@Generated("arez.processor.ArezProcessor")
@Module
public interface RepositoryWithCreateOrAttachRepositoryDaggerModule {
  @Nonnull
  @Provides
  @Singleton
  static RepositoryWithCreateOrAttachRepository provideComponent(final Arez_RepositoryWithCreateOrAttachRepository component) {
    return component;
  }
}
