package arez.doc.examples.inject1;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import javax.inject.Singleton;

@Singleton
@ArezComponent
public class MyService
{
  @Action
  public void performAction( final int value )
  {
  }
}
