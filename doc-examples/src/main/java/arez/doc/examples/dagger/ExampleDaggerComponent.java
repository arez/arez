package arez.doc.examples.dagger;

import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component( modules = MyServiceDaggerModule.class )
public interface ExampleDaggerComponent
{
  ExampleConsumer getExampleConsumer();
}
