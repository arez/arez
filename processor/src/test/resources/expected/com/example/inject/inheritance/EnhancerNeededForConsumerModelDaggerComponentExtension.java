package com.example.inject.inheritance;

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
public interface EnhancerNeededForConsumerModelDaggerComponentExtension {
  DaggerSubcomponent getEnhancerNeededForConsumerModelDaggerSubcomponent();

  default void bindEnhancerNeededForConsumerModel() {
    InjectSupport.c_subComponent = getEnhancerNeededForConsumerModelDaggerSubcomponent();
    InjectSupport.c_enhancer = instance -> InjectSupport.c_subComponent.inject( instance );
  }

  final class InjectSupport {
    static DaggerSubcomponent c_subComponent;

    private static Arez_EnhancerNeededForConsumerModel.Enhancer c_enhancer;
  }

  @Module(
      includes = EnhancerDaggerModule.class
  )
  interface DaggerModule {
    @Binds
    EnhancerNeededForConsumerModel bindComponent(Arez_EnhancerNeededForConsumerModel component);
  }

  @Module
  class EnhancerDaggerModule {
    private static Arez_EnhancerNeededForConsumerModel.Enhancer getEnhancer() {
      if ( Arez.shouldCheckApiInvariants() ) {
        Guards.apiInvariant( () -> null != InjectSupport.c_enhancer, () -> "Attempted to create an instance of the Arez component named 'EnhancerNeededForConsumerModel' before the dependency injection provider has been initialized. Please see the documentation at https://arez.github.io/docs/dependency_injection.html for directions how to configure dependency injection." );
      }
      return InjectSupport.c_enhancer;
    }

    @Nonnull
    @Provides
    final Arez_EnhancerNeededForConsumerModel create() {
      return new Arez_EnhancerNeededForConsumerModel( getEnhancer() );
    }
  }

  @Subcomponent(
      modules = DaggerModule.class
  )
  interface DaggerSubcomponent {
    Provider<EnhancerNeededForConsumerModel> createProvider();

    void inject(@Nonnull Arez_EnhancerNeededForConsumerModel component);
  }
}
