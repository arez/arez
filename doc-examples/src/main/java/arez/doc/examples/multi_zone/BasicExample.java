package arez.doc.examples.multi_zone;

import arez.Arez;
import arez.ObservableValue;
import arez.Zone;

public class BasicExample
{
  public static void main( final String[] args )
    throws Throwable
  {
    // Create zone
    final Zone zone = Arez.createZone();
    // Activate the newly created zone and suspend the current zone if any
    zone.run( () -> {
      // Create new observableValue and ensure it is bound to the current zone
      final ObservableValue<Object> observableValue = Arez.context().observable();

      // Zone is deactivated when it leaves this block
    } );
  }
}
