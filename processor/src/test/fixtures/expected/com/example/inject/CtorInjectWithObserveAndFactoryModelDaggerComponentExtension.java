package com.example.inject;

import dagger.Subcomponent;
import javax.annotation.Generated;

@Generated("arez.processor.ArezProcessor")
public interface CtorInjectWithObserveAndFactoryModelDaggerComponentExtension {
  DaggerSubcomponent getCtorInjectWithObserveAndFactoryModelDaggerSubcomponent();

  @Subcomponent
  interface DaggerSubcomponent {
    Arez_CtorInjectWithObserveAndFactoryModel.Factory createFactory();
  }
}
