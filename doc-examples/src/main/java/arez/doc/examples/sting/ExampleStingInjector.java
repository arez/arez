package arez.doc.examples.sting;

import sting.Injector;

@Injector( includes = MyService.class, fragmentOnly = false )
public interface ExampleStingInjector
{
  static ExampleStingInjector create()
  {
    return new Sting_ExampleStingInjector();
  }

  ExampleConsumer getExampleConsumer();
}
