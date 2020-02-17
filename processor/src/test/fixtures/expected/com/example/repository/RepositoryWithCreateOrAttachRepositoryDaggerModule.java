package com.example.repository;

import dagger.Module;
import dagger.Provides;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.inject.Singleton;

@Generated("arez.processor.ArezProcessor")
@Module
public interface RepositoryWithCreateOrAttachRepositoryDaggerModule {
  @Provides
  @Nonnull
  @Singleton
  static RepositoryWithCreateOrAttachRepository create() {
    return new Arez_RepositoryWithCreateOrAttachRepository();
  }
}
