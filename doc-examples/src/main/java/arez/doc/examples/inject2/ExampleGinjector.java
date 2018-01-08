package arez.doc.examples.inject2;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules( { ExampleGinModule.class } )
public interface ExampleGinjector
  extends Ginjector
{
  ExampleConsumer getExampleConsumer();
}
