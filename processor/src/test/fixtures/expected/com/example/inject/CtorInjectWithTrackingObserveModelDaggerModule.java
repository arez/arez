package com.example.inject;

import dagger.Binds;
import dagger.Module;
import javax.annotation.Generated;
import javax.inject.Singleton;

@Generated("arez.processor.ArezProcessor")
@Module
public interface CtorInjectWithTrackingObserveModelDaggerModule {
  @Binds
  @Singleton
  CtorInjectWithTrackingObserveModel bindComponent(
      Arez_CtorInjectWithTrackingObserveModel component);
}
