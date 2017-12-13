package org.realityforge.arez.doc.examples.inject2;

public class DaggerExample
{
  public static void main( String[] args )
  {
    final ExampleDaggerComponent component = DaggerExampleDaggerComponent.create();
    component.getExampleConsumer().performSomeAction();
  }
}
