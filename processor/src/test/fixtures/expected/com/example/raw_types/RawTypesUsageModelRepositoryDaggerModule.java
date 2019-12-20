package com.example.raw_types;

import dagger.Binds;
import dagger.Module;
import javax.annotation.Generated;
import javax.inject.Singleton;

@Generated("arez.processor.ArezProcessor")
@Module
public interface RawTypesUsageModelRepositoryDaggerModule {
  @Binds
  @Singleton
  RawTypesUsageModelRepository bindComponent(Arez_RawTypesUsageModelRepository component);
}
