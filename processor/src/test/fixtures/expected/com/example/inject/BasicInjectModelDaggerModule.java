package com.example.inject;

import dagger.Module;
import dagger.Provides;
import javax.annotation.Generated;

@Generated("arez.processor.ArezProcessor")
@Module
public interface BasicInjectModelDaggerModule {
  @Provides
  static BasicInjectModel create() {
    return new Arez_BasicInjectModel();
  }
}
