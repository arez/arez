package com.example.inject;

import dagger.Module;
import dagger.Provides;
import javax.annotation.Generated;

@Generated("arez.processor.ArezProcessor")
@Module
public interface DefaultCtorModelDaggerModule {
  @Provides
  static DefaultCtorModel create() {
    return new Arez_DefaultCtorModel();
  }
}
