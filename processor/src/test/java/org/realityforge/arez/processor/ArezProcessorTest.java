package org.realityforge.arez.processor;

import org.testng.annotations.Test;

public class ArezProcessorTest
  extends AbstractArezProcessorTest
{
  @Test
  public void processTimeModel()
  {
    assertSuccessfulCompile( "TimeModel" );
  }
}
