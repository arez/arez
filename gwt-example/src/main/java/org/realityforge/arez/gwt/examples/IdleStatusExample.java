package org.realityforge.arez.gwt.examples;

import com.google.gwt.core.client.EntryPoint;
import elemental2.dom.DomGlobal;
import org.realityforge.arez.Arez;
import org.realityforge.arez.browser.extras.IdleStatus;

public class IdleStatusExample
  implements EntryPoint
{
  @Override
  public void onModuleLoad()
  {
    final IdleStatus idleStatus = IdleStatus.create();
    Arez.context().autorun( () -> {
      final String message = "Interaction Status: " + ( idleStatus.isIdle() ? "Idle" : "Active" );
      DomGlobal.console.log( message );
    } );
  }
}
