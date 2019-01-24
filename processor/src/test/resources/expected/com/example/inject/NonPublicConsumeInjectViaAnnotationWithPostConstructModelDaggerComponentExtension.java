package com.example.inject;

import arez.Arez;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.Subcomponent;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.inject.Provider;
import org.realityforge.braincheck.Guards;

@Generated("arez.processor.ArezProcessor")
public interface NonPublicConsumeInjectViaAnnotationWithPostConstructModelDaggerComponentExtension {
  DaggerSubcomponent getNonPublicConsumeInjectViaAnnotationWithPostConstructModelDaggerSubcomponent(
      );

  default void bindNonPublicConsumeInjectViaAnnotationWithPostConstructModel() {
    InjectSupport.c_enhancer = instance -> getNonPublicConsumeInjectViaAnnotationWithPostConstructModelDaggerSubcomponent().inject( instance );
  }

  final class InjectSupport {
    private static Arez_NonPublicConsumeInjectViaAnnotationWithPostConstructModel.Enhancer c_enhancer;
  }

  @Module(
      includes = EnhancerDaggerModule.class
  )
  interface DaggerModule {
    @Binds
    Object bindComponent(Arez_NonPublicConsumeInjectViaAnnotationWithPostConstructModel component);
  }

  @Module
  class EnhancerDaggerModule {
    private static Arez_NonPublicConsumeInjectViaAnnotationWithPostConstructModel.Enhancer getEnhancer(
        ) {
      if ( Arez.shouldCheckApiInvariants() ) {
        Guards.apiInvariant( () -> null != InjectSupport.c_enhancer, () -> "Attempted to create an instance of the Arez component named 'NonPublicConsumeInjectViaAnnotationWithPostConstructModel' before the dependency injection provider has been initialized. Please see the documentation at https://arez.github.io/docs/dependency_injection.html for directions how to configure dependency injection." );
      }
      return InjectSupport.c_enhancer;
    }

    @Nonnull
    @Provides
    final Arez_NonPublicConsumeInjectViaAnnotationWithPostConstructModel create() {
      return new Arez_NonPublicConsumeInjectViaAnnotationWithPostConstructModel( getEnhancer() );
    }
  }

  @Subcomponent(
      modules = DaggerModule.class
  )
  interface DaggerSubcomponent {
    Provider<Object> createRawProvider();

    @SuppressWarnings("unchecked")
    default Provider<NonPublicConsumeInjectViaAnnotationWithPostConstructModel> createProvider() {
      return (Provider<NonPublicConsumeInjectViaAnnotationWithPostConstructModel>) (Provider) createRawProvider();
    }

    void inject(@Nonnull Arez_NonPublicConsumeInjectViaAnnotationWithPostConstructModel component);
  }
}
