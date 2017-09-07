package org.realityforge.arez.processor;

import javax.annotation.Nonnull;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ArezProcessorTest
  extends AbstractArezProcessorTest
{
  @Test
  public void processTimeModel()
  {
    assertSuccessfulCompile( "TimeModel" );
  }

  @DataProvider( name = "failedCompiles" )
  public Object[][] failedCompiles()
  {
    return new Object[][]
      {
        new Object[]{ "AbstractModel", "@Container target must not be abstract" }
      };
  }

  @Test( dataProvider = "failedCompiles" )
  public void processAbstractModel( @Nonnull final String classname, @Nonnull final String errorMessageFragment )
  {
    assertFailedCompile( classname, errorMessageFragment );
  }
}
