package com.example.inject;

import dagger.Module;
import dagger.Provides;
import javax.annotation.Generated;
import javax.inject.Singleton;

@Generated("arez.processor.ArezProcessor")
@Module
public interface Jsr330ScopedModelDaggerModule {
  @Provides
  @Singleton
  static Jsr330ScopedModel create() {
    return new Arez_Jsr330ScopedModel();
  }
}
