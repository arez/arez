package com.example.dagger;

import dagger.Module;
import dagger.Provides;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.inject.Named;

@Generated("arez.processor.ArezProcessor")
@Module
public interface Jsr330NamedArgDaggerModelDaggerModule {
  @Provides
  @Nonnull
  static Jsr330NamedArgDaggerModel create(@Named("Port") final int port) {
    return new Arez_Jsr330NamedArgDaggerModel(port);
  }
}
