package arez.doc.examples.sting;

import sting.Injector;

@Injector( includes = MyService.class )
public interface ExampleStingInjector
{
  ExampleConsumer getExampleConsumer();
}
