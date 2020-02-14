package com.example.raw_types;

import dagger.Module;
import dagger.Provides;
import javax.annotation.Generated;
import javax.inject.Singleton;

@Generated("arez.processor.ArezProcessor")
@Module
public interface RawTypesUsageModelRepositoryDaggerModule {
  @Provides
  @Singleton
  static RawTypesUsageModelRepository create() {
    return new Arez_RawTypesUsageModelRepository();
  }
}
