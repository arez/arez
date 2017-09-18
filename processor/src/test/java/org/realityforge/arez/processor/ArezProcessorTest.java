package org.realityforge.arez.processor;

import com.google.testing.compile.JavaFileObjects;
import com.google.testing.compile.JavaSourcesSubjectFactory;
import java.util.Arrays;
import javax.annotation.Nonnull;
import javax.tools.JavaFileObject;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static com.google.common.truth.Truth.assert_;

public class ArezProcessorTest
  extends AbstractArezProcessorTest
{
  @DataProvider( name = "successfulCompiles" )
  public Object[][] successfulCompiles()
  {
    return new Object[][]
      {
        new Object[]{ "com.example.SubpackageModel" },
        new Object[]{ "com.example.action.FunctionActionThrowsRuntimeExceptionModel" },
        new Object[]{ "com.example.action.FunctionActionThrowsThrowableModel" },
        new Object[]{ "com.example.action.UnsafeSpecificFunctionActionModel" },
        new Object[]{ "com.example.action.UnsafeSpecificProcedureActionModel" },
        new Object[]{ "com.example.action.UnsafeFunctionActionModel" },
        new Object[]{ "com.example.action.UnsafeProcedureActionModel" },
        new Object[]{ "com.example.action.ReadOnlyActionModel" },
        new Object[]{ "com.example.action.BasicFunctionActionModel" },
        new Object[]{ "com.example.action.BasicActionModel" },
        new Object[]{ "com.example.computed.ComputedWithNameVariationsModel" },
        new Object[]{ "com.example.computed.BasicComputedModel" },
        new Object[]{ "NotDisposableModel" },
        new Object[]{ "ObservableGuessingModel" },
        new Object[]{ "AnnotationsOnModel" },
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
    throws Exception
  {
    assertSuccessfulCompile( classname );
  }

  @Test
  public void processSuccessfulNestedCompile()
    throws Exception
  {
    assertSuccessfulCompile( "input/NestedModel.java", "expected/NestedModel$Arez_BasicActionModel.java" );
  }

  @Test
  public void processSuccessfulNestedNestedCompile()
    throws Exception
  {
    assertSuccessfulCompile( "input/NestedNestedModel.java",
                             "expected/NestedNestedModel$Something$Arez_BasicActionModel.java" );
  }

  @Test
  public void processSuccessfulWhereAnnotationsSourcedFromInterface()
  {
    final JavaFileObject source1 = JavaFileObjects.forResource( "input/DefaultMethodsModel.java" );
    final JavaFileObject source2 = JavaFileObjects.forResource( "input/MyAnnotatedInterface.java" );
    assert_().about( JavaSourcesSubjectFactory.javaSources() ).
      that( Arrays.asList( source1, source2 ) ).
      processedWith( new ArezProcessor() ).
      compilesWithoutError().
      and().generatesSources( JavaFileObjects.forResource( "expected/Arez_DefaultMethodsModel.java" ) );
  }

  @Test
  public void processSuccessfulWhereTraceInheritanceChain()
  {
    final JavaFileObject source1 = JavaFileObjects.forResource( "input/com/example/inheritance/BaseModel.java" );
    final JavaFileObject source2 = JavaFileObjects.forResource( "input/com/example/inheritance/ParentModel.java" );
    final JavaFileObject source3 = JavaFileObjects.forResource( "input/com/example/inheritance/MyModel.java" );
    final JavaFileObject source4 = JavaFileObjects.forResource( "input/com/example/inheritance/MyInterface1.java" );
    final JavaFileObject source5 = JavaFileObjects.forResource( "input/com/example/inheritance/MyInterface2.java" );
    assert_().about( JavaSourcesSubjectFactory.javaSources() ).
      that( Arrays.asList( source1, source2, source3, source4, source5 ) ).
      processedWith( new ArezProcessor() ).
      compilesWithoutError().
      and().generatesSources( JavaFileObjects.forResource( "expected/com/example/inheritance/Arez_MyModel.java" ) );
  }

  @DataProvider( name = "failedCompiles" )
  public Object[][] failedCompiles()
  {
    return new Object[][]
      {
        new Object[]{ "OnActivateNoComputedModel",
                      "@OnActivate exists but there is no corresponding @Computed" },
        new Object[]{ "OnActivateBadNameModel",
                      "@OnActivate as does not match on[Name]Activate pattern. Please specify name." },
        new Object[]{ "OnActivatePrivateModel", "@OnActivate target must not be private" },
        new Object[]{ "OnActivateStaticModel", "@OnActivate target must not be static" },
        new Object[]{ "OnActivateParametersModel", "@OnActivate target must not have any parameters" },
        new Object[]{ "OnActivateReturnValueModel", "@OnActivate target must not return a value" },
        new Object[]{ "OnActivateThrowsExceptionModel", "@OnActivate target must not throw any exceptions" },
        new Object[]{ "OnActivateDuplicateModel", "@OnActivate target duplicates existing method named foo" },

        new Object[]{ "OnDeactivateNoComputedModel",
                      "@OnDeactivate exists but there is no corresponding @Computed" },
        new Object[]{ "OnDeactivateBadNameModel",
                      "@OnDeactivate as does not match on[Name]Deactivate pattern. Please specify name." },
        new Object[]{ "OnDeactivatePrivateModel", "@OnDeactivate target must not be private" },
        new Object[]{ "OnDeactivateStaticModel", "@OnDeactivate target must not be static" },
        new Object[]{ "OnDeactivateParametersModel", "@OnDeactivate target must not have any parameters" },
        new Object[]{ "OnDeactivateReturnValueModel", "@OnDeactivate target must not return a value" },
        new Object[]{ "OnDeactivateThrowsExceptionModel", "@OnDeactivate target must not throw any exceptions" },
        new Object[]{ "OnDeactivateDuplicateModel", "@OnDeactivate target duplicates existing method named foo" },

        new Object[]{ "OnStaleNoComputedModel",
                      "@OnStale exists but there is no corresponding @Computed" },
        new Object[]{ "OnStaleBadNameModel",
                      "@OnStale as does not match on[Name]Stale pattern. Please specify name." },
        new Object[]{ "OnStalePrivateModel", "@OnStale target must not be private" },
        new Object[]{ "OnStaleStaticModel", "@OnStale target must not be static" },
        new Object[]{ "OnStaleParametersModel", "@OnStale target must not have any parameters" },
        new Object[]{ "OnStaleReturnValueModel", "@OnStale target must not return a value" },
        new Object[]{ "OnStaleThrowsExceptionModel", "@OnStale target must not throw any exceptions" },
        new Object[]{ "OnStaleDuplicateModel", "@OnStale target duplicates existing method named foo" },

        new Object[]{ "PreDisposeNotDisposableModel",
                      "@PreDispose must not exist if @Container set disposable to false" },
        new Object[]{ "PreDisposePrivateModel", "@PreDispose target must not be private" },
        new Object[]{ "PreDisposeStaticModel", "@PreDispose target must not be static" },
        new Object[]{ "PreDisposeParametersModel", "@PreDispose target must not have any parameters" },
        new Object[]{ "PreDisposeReturnValueModel", "@PreDispose target must not return a value" },
        new Object[]{ "PreDisposeThrowsExceptionModel", "@PreDispose target must not throw any exceptions" },
        new Object[]{ "PreDisposeDuplicateModel", "@PreDispose target duplicates existing method named foo" },

        new Object[]{ "PostDisposeNotDisposableModel",
                      "@PostDispose must not exist if @Container set disposable to false" },
        new Object[]{ "PostDisposePrivateModel", "@PostDispose target must not be private" },
        new Object[]{ "PostDisposeStaticModel", "@PostDispose target must not be static" },
        new Object[]{ "PostDisposeParametersModel", "@PostDispose target must not have any parameters" },
        new Object[]{ "PostDisposeReturnValueModel", "@PostDispose target must not return a value" },
        new Object[]{ "PostDisposeThrowsExceptionModel", "@PostDispose target must not throw any exceptions" },
        new Object[]{ "PostDisposeDuplicateModel", "@PostDispose target duplicates existing method named foo" },

        new Object[]{ "StaticActionModel", "@Action target must not be static" },
        new Object[]{ "PrivateActionModel", "@Action target must not be private" },
        new Object[]{ "PrivateComputedModel", "@Computed target must not be private" },
        new Object[]{ "PrivateObservableGetterModel", "@Observable target must not be private" },
        new Object[]{ "PrivateObservableSetterModel", "@Observable target must not be private" },
        new Object[]{ "MissingObservableGetterModel",
                      "@Observable target defined setter but no getter was defined and no getter could be automatically determined" },
        new Object[]{ "MissingObservableSetterModel",
                      "@Observable target defined getter but no setter was defined and no setter could be automatically determined" },
        new Object[]{ "ActionAndComputedSameNameModel",
                      "Method annotated with @Action specified name x that duplicates @Computed defined by method m1" },
        new Object[]{ "ActionAndObservableSameNameModel",
                      "Method annotated with @Observable specified name x that duplicates @Action defined by method m1" },
        new Object[]{ "ComputedAndObservableSameNameModel",
                      "Method annotated with @Observable specified name x that duplicates @Computed defined by method m1" },
        new Object[]{ "FinalComputedModel", "@Computed target must not be final" },
        new Object[]{ "StaticComputedModel", "@Computed target must not be static" },
        new Object[]{ "VoidComputedModel", "@Computed target must not have a void return type" },
        new Object[]{ "ParameterizedComputedModel", "@Computed target must not have parameters" },
        new Object[]{ "BadActionNameModel", "Method annotated with @Action specified invalid name -ace" },
        new Object[]{ "BadActionName2Model", "Method annotated with @Action specified invalid name ace-" },
        new Object[]{ "DuplicateActionModel",
                      "Method annotated with @Action specified name ace that duplicates action defined by method setField" },
        new Object[]{ "DuplicateComputedModel",
                      "Method annotated with @Computed specified name ace that duplicates computed defined by method getX" },
        new Object[]{ "BadComputedNameModel", "Method annotated with @Computed specified invalid name -ace" },
        new Object[]{ "BadComputedName2Model", "Method annotated with @Computed specified invalid name ace-" },
        new Object[]{ "ComputedThrowsExceptionModel", "@Computed target must not throw exceptions" },
        new Object[]{ "EmptyContainerModel",
                      "@Container target has no methods annotated with @Action, @Computed or @Observable" },
        new Object[]{ "ContainerIdOnSingletonModel", "@ContainerId must not exist if @Container is a singleton" },
        new Object[]{ "ContainerIdDuplicatedModel", "@ContainerId target duplicates existing method named getId" },
        new Object[]{ "ContainerIdMustNotHaveParametersModel", "@ContainerId target must not have any parameters" },
        new Object[]{ "ContainerIdMustReturnValueModel", "@ContainerId target must return a value" },
        new Object[]{ "ContainerIdNotFinalModel", "@ContainerId target must be final" },
        new Object[]{ "ContainerIdNotStaticModel", "@ContainerId target must not be static" },
        new Object[]{ "ContainerIdNotPrivateModel", "@ContainerId target must not be private" },
        new Object[]{ "ObservableAndActionMethodModel",
                      "Method can not be annotated with both @Action and @Observable" },
        new Object[]{ "ObservableAndPreDisposeMethodModel",
                      "Method can not be annotated with both @Observable and @PreDispose" },
        new Object[]{ "ObservableAndPostDisposeMethodModel",
                      "Method can not be annotated with both @Observable and @PostDispose" },
        new Object[]{ "ObservableAndComputedMethodModel",
                      "Method can not be annotated with both @Observable and @Computed" },
        new Object[]{ "ObservableAndContainerIdMethodModel",
                      "Method can not be annotated with both @Observable and @ContainerId" },
        new Object[]{ "ObservableAndOnActivateMethodModel",
                      "Method can not be annotated with both @Observable and @OnActivate" },
        new Object[]{ "ObservableAndOnDeactivateMethodModel",
                      "Method can not be annotated with both @Observable and @OnDeactivate" },
        new Object[]{ "ObservableAndOnStaleMethodModel",
                      "Method can not be annotated with both @Observable and @OnStale" },
        new Object[]{ "ActionAndComputedMethodModel", "Method can not be annotated with both @Action and @Computed" },
        new Object[]{ "ActionAndPreDisposeMethodModel",
                      "Method can not be annotated with both @Action and @PreDispose" },
        new Object[]{ "ActionAndPostDisposeMethodModel",
                      "Method can not be annotated with both @Action and @PostDispose" },
        new Object[]{ "ActionAndContainerIdMethodModel",
                      "Method can not be annotated with both @Action and @ContainerId" },
        new Object[]{ "ActionAndOnActivateMethodModel",
                      "Method can not be annotated with both @Action and @OnActivate" },
        new Object[]{ "ActionAndOnDeactivateMethodModel",
                      "Method can not be annotated with both @Action and @OnDeactivate" },
        new Object[]{ "ActionAndOnStaleMethodModel",
                      "Method can not be annotated with both @Action and @OnStale" },
        new Object[]{ "ComputedAndContainerIdMethodModel",
                      "Method can not be annotated with both @Computed and @ContainerId" },
        new Object[]{ "ComputedAndPreDisposeMethodModel",
                      "Method can not be annotated with both @Computed and @PreDispose" },
        new Object[]{ "ComputedAndPostDisposeMethodModel",
                      "Method can not be annotated with both @Computed and @PostDispose" },
        new Object[]{ "ComputedAndOnActivateMethodModel",
                      "Method can not be annotated with both @Computed and @OnActivate" },
        new Object[]{ "ComputedAndOnDeactivateMethodModel",
                      "Method can not be annotated with both @Computed and @OnDeactivate" },
        new Object[]{ "ComputedAndOnStaleMethodModel",
                      "Method can not be annotated with both @Computed and @OnStale" },
        new Object[]{ "PreDisposeAndPostDisposeMethodModel",
                      "Method can not be annotated with both @PreDispose and @PostDispose" },
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
    throws Exception
  {
    assertFailedCompile( classname, errorMessageFragment );
  }
}
