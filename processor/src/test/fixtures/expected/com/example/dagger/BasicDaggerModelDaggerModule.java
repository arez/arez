package com.example.dagger;

import dagger.Module;
import dagger.Provides;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("arez.processor.ArezProcessor")
@Module
public interface BasicDaggerModelDaggerModule {
  @Provides
  @Nonnull
  static BasicDaggerModel create() {
    return new Arez_BasicDaggerModel();
  }
}
