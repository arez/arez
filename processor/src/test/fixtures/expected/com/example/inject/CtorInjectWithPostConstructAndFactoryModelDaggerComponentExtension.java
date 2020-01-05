package com.example.inject;

import dagger.Subcomponent;
import javax.annotation.Generated;

@Generated("arez.processor.ArezProcessor")
public interface CtorInjectWithPostConstructAndFactoryModelDaggerComponentExtension {
  DaggerSubcomponent getCtorInjectWithPostConstructAndFactoryModelDaggerSubcomponent();

  @Subcomponent
  interface DaggerSubcomponent {
    Arez_CtorInjectWithPostConstructAndFactoryModel.Factory createFactory();
  }
}
