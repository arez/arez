package com.example.inject;

import arez.Arez;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.inject.Provider;
import org.realityforge.braincheck.Guards;

@Generated("arez.processor.ArezProcessor")
public interface InjectViaAnnotationWithPostConstructModelDaggerComponentExtension {
  Provider<InjectViaAnnotationWithPostConstructModel> createInjectViaAnnotationWithPostConstructModelProvider(
      );

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
      if ( Arez.shouldCheckApiInvariants() ) {
        Guards.apiInvariant( () -> null != InjectSupport.c_enhancer, () -> "Attempted to create an instance of the Arez component named 'InjectViaAnnotationWithPostConstructModel' before the dependency injection provider has been initialized. Please see the documentation at https://arez.github.io/docs/dependency_injection.html for directions how to configure dependency injection." );
      }
      return InjectSupport.c_enhancer;
    }
  }
}
