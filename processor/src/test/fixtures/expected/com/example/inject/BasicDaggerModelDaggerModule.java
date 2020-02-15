package com.example.inject;

import dagger.Module;
import dagger.Provides;
import javax.annotation.Generated;

@Generated("arez.processor.ArezProcessor")
@Module
public interface BasicDaggerModelDaggerModule {
  @Provides
  static BasicDaggerModel create() {
    return new Arez_BasicDaggerModel();
  }
}
