package com.example.inject;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.Subcomponent;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.inject.Provider;

@Generated("arez.processor.ArezProcessor")
public interface ConsumeInjectViaAnnotationWithPostConstructModelDaggerComponentExtension {
  DaggerSubcomponent getConsumeInjectViaAnnotationWithPostConstructModelDaggerSubcomponent();

  default void bindConsumeInjectViaAnnotationWithPostConstructModel() {
    InjectSupport.c_enhancer = instance -> getConsumeInjectViaAnnotationWithPostConstructModelDaggerSubcomponent().inject( instance );
  }

  final class InjectSupport {
    private static Arez_ConsumeInjectViaAnnotationWithPostConstructModel.Enhancer c_enhancer;
  }

  @Module(
      includes = EnhancerDaggerModule.class
  )
  interface DaggerModule {
    @Binds
    ConsumeInjectViaAnnotationWithPostConstructModel bindComponent(
        Arez_ConsumeInjectViaAnnotationWithPostConstructModel component);
  }

  @Module
  class EnhancerDaggerModule {
    @Provides
    static Arez_ConsumeInjectViaAnnotationWithPostConstructModel.Enhancer provideEnhancer() {
      return InjectSupport.c_enhancer;
    }
  }

  @Subcomponent(
      modules = DaggerModule.class
  )
  interface DaggerSubcomponent {
    Provider<ConsumeInjectViaAnnotationWithPostConstructModel> createProvider();

    void inject(@Nonnull Arez_ConsumeInjectViaAnnotationWithPostConstructModel component);
  }
}
