package arez.doc.examples.inject2;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import javax.inject.Singleton;

@Singleton
@ArezComponent
public abstract class MyService
{
  @Action
  public void performAction( final int value )
  {
  }
}
