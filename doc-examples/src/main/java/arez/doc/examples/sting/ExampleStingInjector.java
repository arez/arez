package arez.doc.examples.sting;

import sting.Injector;

@Injector( includes = MyService.class, fragmentOnly = false )
public interface ExampleStingInjector
{
  ExampleConsumer getExampleConsumer();
}
