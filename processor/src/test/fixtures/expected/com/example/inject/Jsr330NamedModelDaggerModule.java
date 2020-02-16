package com.example.inject;

import dagger.Module;
import dagger.Provides;
import javax.annotation.Generated;

@Generated("arez.processor.ArezProcessor")
@Module
public interface Jsr330NamedModelDaggerModule {
  @Provides
  static Jsr330NamedModel create() {
    return new Arez_Jsr330NamedModel();
  }
}
