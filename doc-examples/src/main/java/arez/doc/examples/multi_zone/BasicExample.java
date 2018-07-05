package arez.doc.examples.multi_zone;

import arez.Arez;
import arez.Observable;
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
      // Create new observable and ensure it is bound to the current zone
      final Observable<Object> observable = Arez.context().observable();

      // Zone is deactivated when it leaves this block
    } );
  }
}
