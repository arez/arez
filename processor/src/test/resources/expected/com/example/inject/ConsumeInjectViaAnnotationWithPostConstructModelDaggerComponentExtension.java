package com.example.inject;

import arez.Arez;
import arez.Guards;
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
    InjectSupport.c_subComponent = getConsumeInjectViaAnnotationWithPostConstructModelDaggerSubcomponent();
    InjectSupport.c_enhancer = instance -> InjectSupport.c_subComponent.inject( instance );
  }

  final class InjectSupport {
    static DaggerSubcomponent c_subComponent;

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
    private static Arez_ConsumeInjectViaAnnotationWithPostConstructModel.Enhancer getEnhancer() {
      if ( Arez.shouldCheckApiInvariants() ) {
        Guards.apiInvariant( () -> null != InjectSupport.c_enhancer, () -> "Attempted to create an instance of the Arez component named 'ConsumeInjectViaAnnotationWithPostConstructModel' before the dependency injection provider has been initialized. Please see the documentation at https://arez.github.io/docs/dependency_injection.html for directions how to configure dependency injection." );
      }
      return InjectSupport.c_enhancer;
    }

    @Nonnull
    @Provides
    final Arez_ConsumeInjectViaAnnotationWithPostConstructModel create() {
      return new Arez_ConsumeInjectViaAnnotationWithPostConstructModel( getEnhancer() );
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
