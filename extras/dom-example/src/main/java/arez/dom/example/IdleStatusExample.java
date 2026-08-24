package arez.dom.example;

import arez.Arez;
import arez.dom.IdleStatus;
import com.google.gwt.core.client.EntryPoint;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

public class IdleStatusExample
  implements EntryPoint
{
  @JsType( isNative = true, name = "globalThis.console", namespace = JsPackage.GLOBAL )
  private static final class NativeConsole
  {
    @JsMethod
    private static native void log( Object message );
  }

  @Override
  public void onModuleLoad()
  {
    final IdleStatus idleStatus = IdleStatus.create();
    Arez.context().observer( () ->
                               NativeConsole.log( "Interaction Status: " +
                                                  ( idleStatus.isIdle() ? "Idle" : "Active" ) ) );
  }
}
