package com.example.repository;

import dagger.Module;
import dagger.Provides;
import javax.annotation.Generated;
import javax.inject.Singleton;

@Generated("arez.processor.ArezProcessor")
@Module
public interface NestedModel_BasicActionModelRepositoryDaggerModule {
  @Provides
  @Singleton
  static NestedModel_BasicActionModelRepository create() {
    return new Arez_NestedModel_BasicActionModelRepository();
  }
}
