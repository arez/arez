package com.example.repository;

import dagger.Module;
import dagger.Provides;
import javax.annotation.Generated;
import javax.inject.Singleton;

@Generated("arez.processor.ArezProcessor")
@Module
public interface DaggerEnabledRepositoryRepositoryDaggerModule {
  @Provides
  @Singleton
  static DaggerEnabledRepositoryRepository create() {
    return new Arez_DaggerEnabledRepositoryRepository();
  }
}
