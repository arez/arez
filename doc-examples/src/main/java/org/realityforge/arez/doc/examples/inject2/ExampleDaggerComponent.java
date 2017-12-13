package org.realityforge.arez.doc.examples.inject2;

import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component( modules = { MyServiceDaggerModule.class, MyEntityRepositoryDaggerModule.class } )
public interface ExampleDaggerComponent
{
  ExampleConsumer getExampleConsumer();
}
