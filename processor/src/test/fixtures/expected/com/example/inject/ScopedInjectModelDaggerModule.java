package com.example.inject;

import dagger.Module;
import dagger.Provides;
import javax.annotation.Generated;
import javax.inject.Singleton;

@Generated("arez.processor.ArezProcessor")
@Module
public interface ScopedInjectModelDaggerModule {
  @Provides
  @Singleton
  static ScopedInjectModel create() {
    return new Arez_ScopedInjectModel();
  }
}
