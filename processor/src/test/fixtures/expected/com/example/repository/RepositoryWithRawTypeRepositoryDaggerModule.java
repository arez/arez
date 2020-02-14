package com.example.repository;

import dagger.Module;
import dagger.Provides;
import javax.annotation.Generated;
import javax.inject.Singleton;

@Generated("arez.processor.ArezProcessor")
@Module
public interface RepositoryWithRawTypeRepositoryDaggerModule {
  @Provides
  @Singleton
  static RepositoryWithRawTypeRepository create() {
    return new Arez_RepositoryWithRawTypeRepository();
  }
}
