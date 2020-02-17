package com.example.dagger;

import dagger.Module;
import dagger.Provides;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.inject.Singleton;

@Generated("arez.processor.ArezProcessor")
@Module
public interface Jsr330ScopedDaggerModelDaggerModule {
  @Provides
  @Nonnull
  @Singleton
  static Jsr330ScopedDaggerModel create() {
    return new Arez_Jsr330ScopedDaggerModel();
  }
}
