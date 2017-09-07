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
        new Object[]{ "InterfaceModel", "@Container target must be a class" },
        new Object[]{ "AbstractModel", "@Container target must not be abstract" },
        new Object[]{ "FinalModel", "@Container target must not be final" }
      };
  }

  @Test( dataProvider = "failedCompiles" )
  public void processFailedCompile( @Nonnull final String classname, @Nonnull final String errorMessageFragment )
  {
    assertFailedCompile( classname, errorMessageFragment );
  }
}
