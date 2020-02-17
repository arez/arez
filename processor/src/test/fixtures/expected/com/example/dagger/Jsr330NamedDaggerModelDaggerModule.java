package com.example.dagger;

import dagger.Module;
import dagger.Provides;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("arez.processor.ArezProcessor")
@Module
public interface Jsr330NamedDaggerModelDaggerModule {
  @Provides
  @Nonnull
  static Jsr330NamedDaggerModel create() {
    return new Arez_Jsr330NamedDaggerModel();
  }
}
