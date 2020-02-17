package com.example.inject;

import dagger.Module;
import dagger.Provides;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.inject.Named;

@Generated("arez.processor.ArezProcessor")
@Module
public interface DualNamedArgInjectModelDaggerModule {
  @Provides
  @Nonnull
  static DualNamedArgInjectModel create(@Named("Port") final int port) {
    return new Arez_DualNamedArgInjectModel(port);
  }
}
