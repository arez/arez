package com.example.dagger;

import dagger.Module;
import dagger.Provides;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("arez.processor.ArezProcessor")
@Module
public interface MultipleArgsDaggerModelDaggerModule {
  @Provides
  @Nonnull
  static MultipleArgsDaggerModel create(final int i, final String foo) {
    return new Arez_MultipleArgsDaggerModel(i, foo);
  }
}
