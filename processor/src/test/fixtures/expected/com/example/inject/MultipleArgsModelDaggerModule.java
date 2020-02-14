package com.example.inject;

import dagger.Module;
import dagger.Provides;
import javax.annotation.Generated;

@Generated("arez.processor.ArezProcessor")
@Module
public interface MultipleArgsModelDaggerModule {
  @Provides
  static MultipleArgsModel create(final int i, final String foo) {
    return new Arez_MultipleArgsModel(i, foo);
  }
}
