package com.example.repository;

import dagger.Binds;
import dagger.Module;
import javax.annotation.Generated;
import javax.inject.Singleton;

@Generated("arez.processor.ArezProcessor")
@Module
public interface NestedModel_BasicActionModelRepositoryDaggerModule {
  @Binds
  @Singleton
  NestedModel_BasicActionModelRepository bindComponent(
      Arez_NestedModel_BasicActionModelRepository component);
}
