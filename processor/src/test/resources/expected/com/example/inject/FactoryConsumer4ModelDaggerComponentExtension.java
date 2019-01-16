package com.example.inject;

import arez.Arez;
import dagger.Module;
import dagger.Provides;
import dagger.Subcomponent;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import org.realityforge.braincheck.Guards;

@Generated("arez.processor.ArezProcessor")
public interface FactoryConsumer4ModelDaggerComponentExtension {
  DaggerSubcomponent getFactoryConsumer4ModelDaggerSubcomponent();

  default void bindFactoryConsumer4Model() {
    InjectSupport.c_enhancer = instance -> getFactoryConsumer4ModelDaggerSubcomponent().inject( instance );
  }

  final class InjectSupport {
    private static Arez_FactoryConsumer4Model.Enhancer c_enhancer;
  }

  @Module
  class DaggerModule {
    @Provides
    static Arez_FactoryConsumer4Model.Enhancer provideEnhancer() {
      if ( Arez.shouldCheckApiInvariants() ) {
        Guards.apiInvariant( () -> null != InjectSupport.c_enhancer, () -> "Attempted to create an instance of the Arez component named 'FactoryConsumer4Model' before the dependency injection provider has been initialized. Please see the documentation at https://arez.github.io/docs/dependency_injection.html for directions how to configure dependency injection." );
      }
      return InjectSupport.c_enhancer;
    }
  }

  @Subcomponent(
      modules = DaggerModule.class
  )
  interface DaggerSubcomponent {
    Arez_FactoryConsumer4Model.Factory createFactory();

    void inject(@Nonnull Arez_FactoryConsumer4Model component);
  }
}
