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
        new Object[]{ "ExtraParameterSetterModel", "Method annotated with @Observable should be a setter or getter" },
        new Object[]{ "ExtraParameterGetterModel", "Method annotated with @Observable should be a setter or getter" },
        new Object[]{ "DuplicateSetterModel", "@Observable defines duplicate setter for observable named field" },
        new Object[]{ "DuplicateGetterModel", "@Observable defines duplicate getter for observable named field" },
        new Object[]{ "StaticObservableGetterModel", "@Observable target must not be static" },
        new Object[]{ "StaticObservableSetterModel", "@Observable target must not be static" },
        new Object[]{ "FinalObservableGetterModel", "@Observable target must not be final" },
        new Object[]{ "FinalObservableSetterModel", "@Observable target must not be final" },
        new Object[]{ "NonStaticNestedModel", "@Container target must not be a non-static nested class" },
        new Object[]{ "EnumModel", "@Container target must be a class" },
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
