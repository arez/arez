package com.example.inject;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.inject.Provider;

@Generated("arez.processor.ArezProcessor")
public interface InjectViaAnnotationWithPostConstructModelDaggerComponentExtension {
  Provider<InjectViaAnnotationWithPostConstructModel> createProvider();

  void inject(@Nonnull Arez_InjectViaAnnotationWithPostConstructModel component);

  default void bindInjectViaAnnotationWithPostConstructModel() {
    InjectSupport.c_enhancer = this::inject;
  }

  final class InjectSupport {
    private static Arez_InjectViaAnnotationWithPostConstructModel.Enhancer c_enhancer;
  }

  @Module(
      includes = EnhancerDaggerModule.class
  )
  interface DaggerModule {
    @Binds
    InjectViaAnnotationWithPostConstructModel bindComponent(
        Arez_InjectViaAnnotationWithPostConstructModel component);
  }

  @Module
  class EnhancerDaggerModule {
    @Provides
    static Arez_InjectViaAnnotationWithPostConstructModel.Enhancer provideEnhancer() {
      return InjectSupport.c_enhancer;
    }
  }
}
