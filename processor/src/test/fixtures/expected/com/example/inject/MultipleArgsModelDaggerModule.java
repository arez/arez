package com.example.inject;

import dagger.Module;
import dagger.Provides;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("arez.processor.ArezProcessor")
@Module
public interface MultipleArgsModelDaggerModule {
  @Provides
  @Nonnull
  static MultipleArgsModel create(final int i, final String foo) {
    return new Arez_MultipleArgsModel(i, foo);
  }
}
