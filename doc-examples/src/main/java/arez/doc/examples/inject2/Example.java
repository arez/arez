package arez.doc.examples.inject2;

import com.google.gwt.core.client.GWT;

public class Example
{
  public static void main( String[] args )
  {
    final ExampleGinjector injector = GWT.create( ExampleGinjector.class );
    injector.getExampleConsumer().performSomeAction();
  }
}
