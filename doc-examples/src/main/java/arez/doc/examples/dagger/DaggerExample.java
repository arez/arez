package arez.doc.examples.dagger;

public class DaggerExample
{
  public static void main( String[] args )
  {
    final ExampleDaggerComponent component = DaggerExampleDaggerComponent.create();
    component.getExampleConsumer().performSomeAction( 42 );
  }
}
