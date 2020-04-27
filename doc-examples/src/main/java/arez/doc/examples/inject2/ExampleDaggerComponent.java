package arez.doc.examples.inject2;

import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component( modules = MyServiceDaggerModule.class )
public interface ExampleDaggerComponent
{
  ExampleConsumer getExampleConsumer();
}
