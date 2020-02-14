package com.example.repository;

import dagger.Module;
import dagger.Provides;
import javax.annotation.Generated;
import javax.inject.Singleton;

@Generated("arez.processor.ArezProcessor")
@Module
public interface InjectEnabledRepositoryRepositoryDaggerModule {
  @Provides
  @Singleton
  static InjectEnabledRepositoryRepository create() {
    return new Arez_InjectEnabledRepositoryRepository();
  }
}
