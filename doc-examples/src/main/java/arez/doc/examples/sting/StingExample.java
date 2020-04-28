package arez.doc.examples.sting;

public class StingExample
{
  public static void main( String[] args )
  {
    final ExampleStingInjector component = new Sting_ExampleStingInjector();
    component.getExampleConsumer().performSomeAction( 42 );
  }
}
