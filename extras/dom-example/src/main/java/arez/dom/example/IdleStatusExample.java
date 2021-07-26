package arez.dom.example;

import akasha.Console;
import arez.Arez;
import arez.dom.IdleStatus;
import com.google.gwt.core.client.EntryPoint;

public class IdleStatusExample
  implements EntryPoint
{
  @Override
  public void onModuleLoad()
  {
    final IdleStatus idleStatus = IdleStatus.create();
    Arez.context().observer( () -> Console.log( "Interaction Status: " + ( idleStatus.isIdle() ? "Idle" : "Active" ) ) );
  }
}
