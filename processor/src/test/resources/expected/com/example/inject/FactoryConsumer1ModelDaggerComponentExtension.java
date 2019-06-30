package com.example.inject;

import arez.Arez;
import dagger.Module;
import dagger.Provides;
import dagger.Subcomponent;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import org.realityforge.braincheck.Guards;

@Generated("arez.processor.ArezProcessor")
public interface FactoryConsumer1ModelDaggerComponentExtension {
  DaggerSubcomponent getFactoryConsumer1ModelDaggerSubcomponent();

  default void bindFactoryConsumer1Model() {
    InjectSupport.c_subComponent = getFactoryConsumer1ModelDaggerSubcomponent();
    InjectSupport.c_enhancer = instance -> InjectSupport.c_subComponent.inject( instance );
  }

  final class InjectSupport {
    static DaggerSubcomponent c_subComponent;

    private static Arez_FactoryConsumer1Model.Enhancer c_enhancer;
  }

  @Module
  class DaggerModule {
    @Provides
    static Arez_FactoryConsumer1Model.Enhancer provideEnhancer() {
      if ( Arez.shouldCheckApiInvariants() ) {
        Guards.apiInvariant( () -> null != InjectSupport.c_enhancer, () -> "Attempted to create an instance of the Arez component named 'FactoryConsumer1Model' before the dependency injection provider has been initialized. Please see the documentation at https://arez.github.io/docs/dependency_injection.html for directions how to configure dependency injection." );
      }
      return InjectSupport.c_enhancer;
    }
  }

  @Subcomponent(
      modules = DaggerModule.class
  )
  interface DaggerSubcomponent {
    Arez_FactoryConsumer1Model.Factory createFactory();

    void inject(@Nonnull Arez_FactoryConsumer1Model component);
  }
}
