package org.realityforge.arez.doc.examples.inject2;

import javax.inject.Singleton;
import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;

@Singleton
@ArezComponent
public class MyService
{
  @Action
  public void performAction( final int value )
  {
  }
}
