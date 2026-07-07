package arez.doc.examples.sting;

public class StingExample
{
  public static void main( String[] args )
  {
    final ExampleStingInjector component = ExampleStingInjector.create();
    component.getExampleConsumer().performSomeAction( 42 );
  }
}
