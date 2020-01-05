package com.example.inject;

import dagger.Subcomponent;
import javax.annotation.Generated;

@Generated("arez.processor.ArezProcessor")
public interface CtorInjectWithFactoryModelDaggerComponentExtension {
  DaggerSubcomponent getCtorInjectWithFactoryModelDaggerSubcomponent();

  @Subcomponent
  interface DaggerSubcomponent {
    Arez_CtorInjectWithFactoryModel.Factory createFactory();
  }
}
