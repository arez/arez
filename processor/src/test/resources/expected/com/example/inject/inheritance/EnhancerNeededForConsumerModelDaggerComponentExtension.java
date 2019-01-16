package com.example.inject.inheritance;

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
public interface EnhancerNeededForConsumerModelDaggerComponentExtension {
  DaggerSubcomponent getEnhancerNeededForConsumerModelDaggerSubcomponent();

  default void bindEnhancerNeededForConsumerModel() {
    InjectSupport.c_enhancer = instance -> getEnhancerNeededForConsumerModelDaggerSubcomponent().inject( instance );
  }

  final class InjectSupport {
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
    @Provides
    static Arez_EnhancerNeededForConsumerModel.Enhancer provideEnhancer() {
      if ( Arez.shouldCheckApiInvariants() ) {
        Guards.apiInvariant( () -> null != InjectSupport.c_enhancer, () -> "Attempted to create an instance of the Arez component named 'EnhancerNeededForConsumerModel' before the dependency injection provider has been initialized. Please see the documentation at https://arez.github.io/docs/dependency_injection.html for directions how to configure dependency injection." );
      }
      return InjectSupport.c_enhancer;
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
