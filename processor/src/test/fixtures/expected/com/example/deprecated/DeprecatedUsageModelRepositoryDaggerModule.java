package com.example.deprecated;

import dagger.Module;
import dagger.Provides;
import javax.annotation.Generated;
import javax.inject.Singleton;

@Generated("arez.processor.ArezProcessor")
@Module
public interface DeprecatedUsageModelRepositoryDaggerModule {
  @Provides
  @Singleton
  static DeprecatedUsageModelRepository create() {
    return new Arez_DeprecatedUsageModelRepository();
  }
}
