package com.example.inject;

import dagger.Binds;
import dagger.Module;
import javax.annotation.Generated;

@Generated("arez.processor.ArezProcessor")
@Module
public interface BasicInjectModelDaggerModule {
  @Binds
  BasicInjectModel bindComponent(Arez_BasicInjectModel component);
}
