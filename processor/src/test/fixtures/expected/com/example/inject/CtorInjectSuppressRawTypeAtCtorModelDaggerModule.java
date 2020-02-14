package com.example.inject;

import dagger.Module;
import dagger.Provides;
import java.util.concurrent.Callable;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.inject.Singleton;

@Generated("arez.processor.ArezProcessor")
@Module
public interface CtorInjectSuppressRawTypeAtCtorModelDaggerModule {
  @Provides
  @Singleton
  @SuppressWarnings("rawtypes")
  static CtorInjectSuppressRawTypeAtCtorModel create(@Nonnull final Callable action) {
    return new Arez_CtorInjectSuppressRawTypeAtCtorModel(action);
  }
}
