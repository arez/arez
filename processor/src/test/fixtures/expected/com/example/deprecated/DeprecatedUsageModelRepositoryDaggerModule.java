package com.example.deprecated;

import dagger.Binds;
import dagger.Module;
import javax.annotation.Generated;
import javax.inject.Singleton;

@Generated("arez.processor.ArezProcessor")
@Module
public interface DeprecatedUsageModelRepositoryDaggerModule {
  @Binds
  @Singleton
  DeprecatedUsageModelRepository bindComponent(Arez_DeprecatedUsageModelRepository component);
}
