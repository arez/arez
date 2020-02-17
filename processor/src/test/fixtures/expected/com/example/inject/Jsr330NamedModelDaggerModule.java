package com.example.inject;

import dagger.Module;
import dagger.Provides;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("arez.processor.ArezProcessor")
@Module
public interface Jsr330NamedModelDaggerModule {
  @Provides
  @Nonnull
  static Jsr330NamedModel create() {
    return new Arez_Jsr330NamedModel();
  }
}
