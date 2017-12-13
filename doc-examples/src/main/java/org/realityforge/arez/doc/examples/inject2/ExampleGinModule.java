package org.realityforge.arez.doc.examples.inject2;

import com.google.gwt.inject.client.AbstractGinModule;

public class ExampleGinModule
  extends AbstractGinModule
{
  @Override
  protected void configure()
  {
    bind( MyService.class ).to( Arez_MyService.class ).asEagerSingleton();
    bind( MyEntityRepository.class ).to( Arez_MyEntityRepository.class ).asEagerSingleton();
  }
}
