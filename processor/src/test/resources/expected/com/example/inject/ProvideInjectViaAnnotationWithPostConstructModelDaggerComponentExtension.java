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
public interface ProvideInjectViaAnnotationWithPostConstructModelDaggerComponentExtension {
  Provider<ProvideInjectViaAnnotationWithPostConstructModel> createProvideInjectViaAnnotationWithPostConstructModelProvider(
      );

  void inject(@Nonnull Arez_ProvideInjectViaAnnotationWithPostConstructModel component);

  default void bindProvideInjectViaAnnotationWithPostConstructModel() {
    InjectSupport.c_enhancer = this::inject;
  }

  final class InjectSupport {
    private static Arez_ProvideInjectViaAnnotationWithPostConstructModel.Enhancer c_enhancer;
  }

  @Module(
      includes = EnhancerDaggerModule.class
  )
  interface DaggerModule {
    @Binds
    ProvideInjectViaAnnotationWithPostConstructModel bindComponent(
        Arez_ProvideInjectViaAnnotationWithPostConstructModel component);
  }

  @Module
  class EnhancerDaggerModule {
    private static Arez_ProvideInjectViaAnnotationWithPostConstructModel.Enhancer getEnhancer() {
      if ( Arez.shouldCheckApiInvariants() ) {
        Guards.apiInvariant( () -> null != InjectSupport.c_enhancer, () -> "Attempted to create an instance of the Arez component named 'ProvideInjectViaAnnotationWithPostConstructModel' before the dependency injection provider has been initialized. Please see the documentation at https://arez.github.io/docs/dependency_injection.html for directions how to configure dependency injection." );
      }
      return InjectSupport.c_enhancer;
    }

    @Nonnull
    @Provides
    final Arez_ProvideInjectViaAnnotationWithPostConstructModel create() {
      return new Arez_ProvideInjectViaAnnotationWithPostConstructModel( getEnhancer() );
    }
  }
}
