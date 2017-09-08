package org.realityforge.arez.processor;

import javax.annotation.Nonnull;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ArezProcessorTest
  extends AbstractArezProcessorTest
{

  @DataProvider( name = "successfulCompiles" )
  public Object[][] successfulCompiles()
  {
    return new Object[][]
      {
        new Object[]{ "BasicComputedModel" },
        new Object[]{ "UnsafeSpecificFunctionActionModel" },
        new Object[]{ "UnsafeSpecificProcedureActionModel" },
        new Object[]{ "UnsafeFunctionActionModel" },
        new Object[]{ "UnsafeProcedureActionModel" },
        new Object[]{ "ReadOnlyActionModel" },
        new Object[]{ "BasicFunctionActionModel" },
        new Object[]{ "BasicActionModel" },
        new Object[]{ "ObservableWithAnnotatedCtorModel" },
        new Object[]{ "ObservableModelWithUnconventionalNames" },
        new Object[]{ "DifferentObservableTypesModel" },
        new Object[]{ "ObservableWithExceptingCtorModel" },
        new Object[]{ "OverrideNamesInModel" },
        new Object[]{ "SingletonModel" },
        new Object[]{ "ContainerIdOnModel" },
        new Object[]{ "BasicModelWithDifferentAccessLevels" },
        new Object[]{ "ObservableWithCtorModel" },
        new Object[]{ "ObservableWithSpecificExceptionModel" },
        new Object[]{ "ObservableWithExceptionModel" },
        new Object[]{ "BasicObservableModel" }
      };
  }

  @Test( dataProvider = "successfulCompiles" )
  public void processSuccessfulCompile( @Nonnull final String classname )
  {
    assertSuccessfulCompile( classname );
  }

  @Test
  public void processWIP()
  {
    assertSuccessfulCompile( "ObservableWithAnnotatedCtorModel" );
  }

  @DataProvider( name = "failedCompiles" )
  public Object[][] failedCompiles()
  {
    return new Object[][]
      {
        new Object[]{ "FinalComputedModel", "@Computed target must not be final" },
        new Object[]{ "StaticComputedModel", "@Computed target must not be static" },
        new Object[]{ "VoidComputedModel", "@Computed target must not have a void return type" },
        new Object[]{ "ParameterizedComputedModel", "@Computed target must not have parameters" },
        new Object[]{ "BadActionNameModel", "Method annotated with @Action specified invalid name -ace" },
        new Object[]{ "BadActionName2Model", "Method annotated with @Action specified invalid name ace-" },
        new Object[]{ "DuplicateActionModel", "Method annotated with @Action specified name ace that duplicates action defined by method setField" },
        new Object[]{ "DuplicateComputedModel", "Method annotated with @Computed specified name ace that duplicates computed defined by method getX" },
        new Object[]{ "BadComputedNameModel", "Method annotated with @Computed specified invalid name -ace" },
        new Object[]{ "BadComputedName2Model", "Method annotated with @Computed specified invalid name ace-" },
        new Object[]{ "ComputedThrowsExceptionModel", "@Computed target must not throw exceptions" },
        new Object[]{ "EmptyContainerModel", "@Container target has no methods annotated with @Action, @Computed or @Observable" },
        new Object[]{ "ContainerIdOnSingletonModel", "@ContainerId must not exist if @Container is a singleton" },
        new Object[]{ "ContainerIdDuplicatedModel", "@ContainerId target duplicates existing method named getId" },
        new Object[]{ "ContainerIdMustNotHaveParametersModel", "@ContainerId target must not have any parameters" },
        new Object[]{ "ContainerIdMustReturnValueModel", "@ContainerId target must return a value" },
        new Object[]{ "ContainerIdNotFinalModel", "@ContainerId target must be final" },
        new Object[]{ "ContainerIdNotStaticModel", "@ContainerId target must not be static" },
        new Object[]{ "ContainerIdNotPrivateModel", "@ContainerId target must not be private" },
        new Object[]{ "ObservableAndActionMethodModel", "Method can not be annotated with both @Action and @Observable" },
        new Object[]{ "ObservableAndComputedMethodModel", "Method can not be annotated with both @Observable and @Computed" },
        new Object[]{ "ObservableAndContainerIdMethodModel", "Method can not be annotated with both @Observable and @ContainerId" },
        new Object[]{ "ActionAndComputedMethodModel", "Method can not be annotated with both @Action and @Computed" },
        new Object[]{ "ActionAndContainerIdMethodModel", "Method can not be annotated with both @Action and @ContainerId" },
        new Object[]{ "ComputedAndContainerIdMethodModel", "Method can not be annotated with both @ContainerId and @Computed" },
        new Object[]{ "BadObservableNameModel", "Method annotated with @Observable specified invalid name -ace" },
        new Object[]{ "BadObservableName2Model", "Method annotated with @Observable specified invalid name ace-" },
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
