package arez.gwt.examples;

import arez.Arez;
import arez.browser.extras.IdleStatus;
import com.google.gwt.core.client.EntryPoint;
import elemental2.dom.DomGlobal;

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
