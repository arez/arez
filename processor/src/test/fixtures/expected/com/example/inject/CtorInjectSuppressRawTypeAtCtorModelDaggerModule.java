package com.example.inject;

import dagger.Binds;
import dagger.Module;
import javax.annotation.Generated;
import javax.inject.Singleton;

@Generated("arez.processor.ArezProcessor")
@Module
public interface CtorInjectSuppressRawTypeAtCtorModelDaggerModule {
  @Binds
  @Singleton
  CtorInjectSuppressRawTypeAtCtorModel bindComponent(
      Arez_CtorInjectSuppressRawTypeAtCtorModel component);
}