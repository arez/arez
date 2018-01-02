package org.realityforge.arez.doc.examples.multi_zone;

import org.realityforge.arez.Arez;
import org.realityforge.arez.Observable;
import org.realityforge.arez.Zone;

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
      final Observable<Object> observable = Arez.context().createObservable();

      // Zone is deactivated when it leaves this block
    } );
  }
}
