package arez;

import jsinterop.annotations.JsProperty;

final class AssertUtil
{
  private AssertUtil()
  {
  }

  @JsProperty( namespace = "<window>", name = "debugger" )
  static native void debugger();
}
