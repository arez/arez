package com.example.inject;

import dagger.Binds;
import dagger.Module;
import javax.annotation.Generated;

@Generated("arez.processor.ArezProcessor")
@Module
public interface DefaultCtorModelDaggerModule {
  @Binds
  DefaultCtorModel bindComponent(Arez_DefaultCtorModel component);
}
