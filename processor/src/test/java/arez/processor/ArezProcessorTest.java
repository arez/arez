package arez.processor;

import com.google.testing.compile.JavaFileObjects;
import java.util.Arrays;
import java.util.Collections;
import javax.annotation.Nonnull;
import javax.tools.JavaFileObject;
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
        new Object[]{ "com.example.SubpackageModel", false, false, false },
        new Object[]{ "com.example.action.ActionTypeParametersModel", false, false, false },
        new Object[]{ "com.example.action.NewTypeParametersModel", false, false, false },
        new Object[]{ "com.example.action.NoReportParametersModel", false, false, false },
        new Object[]{ "com.example.action.FunctionActionThrowsRuntimeExceptionModel", false, false, false },
        new Object[]{ "com.example.action.FunctionActionThrowsThrowableModel", false, false, false },
        new Object[]{ "com.example.action.MultiThrowAction", false, false, false },
        new Object[]{ "com.example.action.UnsafeSpecificFunctionActionModel", false, false, false },
        new Object[]{ "com.example.action.UnsafeSpecificProcedureActionModel", false, false, false },
        new Object[]{ "com.example.action.UnsafeFunctionActionModel", false, false, false },
        new Object[]{ "com.example.action.UnsafeProcedureActionModel", false, false, false },
        new Object[]{ "com.example.action.ReadOnlyActionModel", false, false, false },
        new Object[]{ "com.example.action.BasicFunctionActionModel", false, false, false },
        new Object[]{ "com.example.action.BasicActionModel", false, false, false },
        new Object[]{ "com.example.autorun.BasicAutorunModel", false, false, false },
        new Object[]{ "com.example.autorun.ReadWriteAutorunModel", false, false, false },
        new Object[]{ "com.example.autorun.ScheduleAfterConstructedModel", false, false, false },
        new Object[]{ "com.example.autorun.ScheduleDeferredModel", false, false, false },
        new Object[]{ "com.example.component.AnnotatedConcreteModel", false, false, false },
        new Object[]{ "com.example.component_id.BooleanComponentId", false, false, false },
        new Object[]{ "com.example.component_id.ByteComponentId", false, false, false },
        new Object[]{ "com.example.component_id.CharComponentId", false, false, false },
        new Object[]{ "com.example.component_id.ComponentIdOnModel", false, false, false },
        new Object[]{ "com.example.component_id.DoubleComponentId", false, false, false },
        new Object[]{ "com.example.component_id.FloatComponentId", false, false, false },
        new Object[]{ "com.example.component_id.IntComponentId", false, false, false },
        new Object[]{ "com.example.component_id.LongComponentId", false, false, false },
        new Object[]{ "com.example.component_id.ObjectComponentId", false, false, false },
        new Object[]{ "com.example.component_id.ShortComponentId", false, false, false },
        new Object[]{ "com.example.component_id.ComponentIdOnSingletonModel", false, false, false },
        new Object[]{ "com.example.component_name_ref.ComponentNameModel", false, false, false },
        new Object[]{ "com.example.component_name_ref.ComponentTypeNameModel", false, false, false },
        new Object[]{ "com.example.component_name_ref.ComponentTypeNameAloneOnSingletonModel", false, false, false },
        new Object[]{ "com.example.component_name_ref.ComponentNameOnSingletonModel", false, false, false },
        new Object[]{ "com.example.component_ref.AnnotatedComponent", false, false, false },
        new Object[]{ "com.example.component_ref.SimpleComponent", false, false, false },
        new Object[]{ "com.example.component_ref.ProtectedAccessComponent", false, false, false },
        new Object[]{ "com.example.computed.ComputedWithNameVariationsModel", false, false, false },
        new Object[]{ "com.example.computed.ComputedWithHooksModel", false, false, false },
        new Object[]{ "com.example.computed.BasicComputedModel", false, false, false },
        new Object[]{ "com.example.computed.TypeParametersModel", false, false, false },
        new Object[]{ "com.example.computed_value_ref.DefaultRefNameModel", false, false, false },
        new Object[]{ "com.example.computed_value_ref.NonStandardNameModel", false, false, false },
        new Object[]{ "com.example.computed_value_ref.RawComputedValueModel", false, false, false },
        new Object[]{ "com.example.context_ref.AnnotatedComponent", false, false, false },
        new Object[]{ "com.example.context_ref.SimpleComponent", false, false, false },
        new Object[]{ "com.example.context_ref.ProtectedAccessComponent", false, false, false },
        new Object[]{ "com.example.deprecated.DeprecatedActionModel", false, false, false },
        new Object[]{ "com.example.deprecated.DeprecatedAutorunModel", false, false, false },
        new Object[]{ "com.example.deprecated.DeprecatedComputedModel1", false, false, false },
        new Object[]{ "com.example.deprecated.DeprecatedComputedModel2", false, false, false },
        new Object[]{ "com.example.deprecated.DeprecatedComputedModel3", false, false, false },
        new Object[]{ "com.example.deprecated.DeprecatedComputedModel4", false, false, false },
        new Object[]{ "com.example.deprecated.DeprecatedComputedModel5", false, false, false },
        new Object[]{ "com.example.deprecated.DeprecatedMemoizeModel", false, false, false },
        new Object[]{ "com.example.deprecated.DeprecatedObservableModel1", false, false, false },
        new Object[]{ "com.example.deprecated.DeprecatedObservableModel2", false, false, false },
        new Object[]{ "com.example.deprecated.DeprecatedPostConstructModel", false, false, false },
        new Object[]{ "com.example.deprecated.DeprecatedTrackedModel1", false, false, false },
        new Object[]{ "com.example.deprecated.DeprecatedTrackedModel2", false, false, false },
        new Object[]{ "com.example.deprecated.DeprecatedTrackedModel3", false, false, false },
        new Object[]{ "com.example.deprecated.DeprecatedTrackedModel4", false, false, false },
        new Object[]{ "com.example.inject.BasicInjectModel", false, false, false },
        new Object[]{ "com.example.inject.DefaultCtorModel", false, false, false },
        new Object[]{ "com.example.inject.MultipleArgsModel", false, false, false },
        new Object[]{ "com.example.inject.NoInjectModel", false, false, false },
        new Object[]{ "com.example.inject.ScopedButNoDaggerModel", false, false, false },
        new Object[]{ "com.example.inject.ScopedInjectModel", true, false, false },
        new Object[]{ "com.example.memoize.BasicMemoizeModel", false, false, false },
        new Object[]{ "com.example.memoize.TypeParamMemoizeModel", false, false, false },
        new Object[]{ "com.example.observable.AbstractNonPrimitiveObservablesModel", false, false, false },
        new Object[]{ "com.example.observable.AbstractObservablesModel", false, false, false },
        new Object[]{ "com.example.observable.ObservableWithNoSetter", false, false, false },
        new Object[]{ "com.example.observable_ref.DefaultRefNameModel", false, false, false },
        new Object[]{ "com.example.observable_ref.NonStandardNameModel", false, false, false },
        new Object[]{ "com.example.observable_ref.RawObservableModel", false, false, false },
        new Object[]{ "com.example.observer_ref.CustomNameRefOnAutorunModel", false, false, false },
        new Object[]{ "com.example.observer_ref.CustomNameRefOnTrackedModel", false, false, false },
        new Object[]{ "com.example.observer_ref.RefOnAutorunModel", false, false, false },
        new Object[]{ "com.example.observer_ref.RefOnBothModel", false, false, false },
        new Object[]{ "com.example.observer_ref.RefOnTrackedModel", false, false, false },
        new Object[]{ "com.example.overloaded_names.OverloadedActions", false, false, false },
        new Object[]{ "com.example.post_construct.PostConstructModel", false, false, false },
        new Object[]{ "com.example.repository.CompleteRepositoryExample", false, true, true },
        new Object[]{ "com.example.repository.DaggerDisabledRepository", false, true, false },
        new Object[]{ "com.example.repository.DaggerEnabledRepository", false, true, true },
        new Object[]{ "com.example.repository.InjectEnabledRepository", false, true, true },
        new Object[]{ "com.example.repository.PackageAccessRepositoryExample", false, true, true },
        new Object[]{ "com.example.repository.RepositoryPreDisposeHook", false, true, true },
        new Object[]{ "com.example.repository.RepositoryWithExplicitId", false, true, true },
        new Object[]{ "com.example.repository.RepositoryWithImplicitId", false, true, true },
        new Object[]{ "com.example.repository.RepositoryWithMultipleCtors", false, true, true },
        new Object[]{ "com.example.repository.RepositoryWithProtectedConstructor", false, true, true },
        new Object[]{ "com.example.repository.RepositoryWithSingleton", false, true, true },
        new Object[]{ "com.example.to_string.NoToStringPresent", false, false, false },
        new Object[]{ "com.example.to_string.ToStringPresent", false, false, false },
        new Object[]{ "com.example.tracked.BasicTrackedModel", false, false, false },
        new Object[]{ "com.example.tracked.BasicTrackedWithExceptionsModel", false, false, false },
        new Object[]{ "com.example.tracked.DeriveFinalOnDepsChangedModel", false, false, false },
        new Object[]{ "com.example.tracked.DeriveOnDepsChangedModel", false, false, false },
        new Object[]{ "com.example.tracked.DeriveTrackedModel", false, false, false },
        new Object[]{ "com.example.tracked.NoReportParametersModel", false, false, false },
        new Object[]{ "com.example.tracked.ProtectedAccessTrackedModel", false, false, false },
        new Object[]{ "com.example.tracked.TrackedAllTypesModel", false, false, false },
        new Object[]{ "com.example.type_access_levels.ReduceAccessLevelModel", false, false, false },
        new Object[]{ "DisposingModel", false, false, false },
        new Object[]{ "ObservableTypeParametersModel", false, false, false },
        new Object[]{ "TypeParametersOnModel", false, false, false },
        new Object[]{ "ObservableGuessingModel", false, false, false },
        new Object[]{ "AnnotationsOnModel", false, false, false },
        new Object[]{ "ObservableWithAnnotatedCtorModel", false, false, false },
        new Object[]{ "ObservableModelWithUnconventionalNames", false, false, false },
        new Object[]{ "DifferentObservableTypesModel", false, false, false },
        new Object[]{ "ObservableWithExceptingCtorModel", false, false, false },
        new Object[]{ "OverrideNamesInModel", false, false, false },
        new Object[]{ "ImplicitSingletonModel", true, false, false },
        new Object[]{ "SingletonModel", false, false, false },
        new Object[]{ "SingletonWithIdModel", true, false, false },
        new Object[]{ "EmptyModel", false, false, false },
        new Object[]{ "BasicModelWithDifferentAccessLevels", false, false, false },
        new Object[]{ "ObservableWithCtorModel", false, false, false },
        new Object[]{ "ObservableWithSpecificExceptionModel", false, false, false },
        new Object[]{ "ObservableWithExceptionModel", false, false, false },
        new Object[]{ "BasicObservableModel", false, false, false }
      };
  }

  @Test( dataProvider = "successfulCompiles" )
  public void processSuccessfulCompile( @Nonnull final String classname,
                                        final boolean componentDaggerEnabled,
                                        final boolean repositoryEnabled,
                                        final boolean repositoryDaggerEnabled )
    throws Exception
  {
    assertSuccessfulCompile( classname, componentDaggerEnabled, repositoryEnabled, repositoryDaggerEnabled );
  }

  @Test
  public void processSuccessfulInheritedProtectedAccessInDifferentPackage()
    throws Exception
  {
    final JavaFileObject source1 =
      JavaFileObjects.forResource( "input/com/example/tracked/InheritProtectedAccessTrackedModel.java" );
    final JavaFileObject source2 =
      JavaFileObjects.forResource( "input/com/example/tracked/other/BaseModelProtectedAccess.java" );
    final String output = "expected/com/example/tracked/Arez_InheritProtectedAccessTrackedModel.java";
    assertSuccessfulCompile( Arrays.asList( source1, source2 ), Collections.singletonList( output ) );
  }

  @Test
  public void processSuccessfulReactArezGenericsScenario()
    throws Exception
  {
    final JavaFileObject source1 =
      JavaFileObjects.forResource( "input/com/example/override_generics/BaseReactComponent.java" );
    final JavaFileObject source2 =
      JavaFileObjects.forResource( "input/com/example/override_generics/ArezReactComponent.java" );
    final JavaFileObject source3 =
      JavaFileObjects.forResource( "input/com/example/override_generics/MyArezReactComponent.java" );
    final JavaFileObject source4 =
      JavaFileObjects.forResource( "input/com/example/override_generics/MyArezReactComponent_.java" );
    final String output = "expected/com/example/override_generics/Arez_MyArezReactComponent_.java";
    assertSuccessfulCompile( Arrays.asList( source1, source2, source3, source4 ), Collections.singletonList( output ) );
  }

  @Test
  public void processSuccessfulWhereAbstractMethodWithGenericParameterIsRefinedInMiddleComponent()
    throws Exception
  {
    final JavaFileObject source1 =
      JavaFileObjects.forResource( "input/com/example/override_generics/BaseModel.java" );
    final JavaFileObject source2 =
      JavaFileObjects.forResource( "input/com/example/override_generics/MiddleModel.java" );
    final JavaFileObject source3 =
      JavaFileObjects.forResource( "input/com/example/override_generics/LeafModel.java" );
    final String output = "expected/com/example/override_generics/Arez_LeafModel.java";
    assertSuccessfulCompile( Arrays.asList( source1, source2, source3 ), Collections.singletonList( output ) );
  }

  @Test
  public void processSuccessfulNestedCompileWithRepositories()
    throws Exception
  {
    assertSuccessfulCompile( "input/com/example/repository/NestedModel.java",
                             "expected/com/example/repository/NestedModel_Arez_BasicActionModel.java",
                             "expected/com/example/repository/NestedModel_BasicActionModelRepositoryDaggerModule.java",
                             "expected/com/example/repository/NestedModel_BasicActionModelRepository.java",
                             "expected/com/example/repository/Arez_NestedModel_BasicActionModelRepository.java" );
  }

  @Test
  public void processSuccessfulToStringInPresent()
    throws Exception
  {
    final JavaFileObject source1 =
      JavaFileObjects.forResource( "input/com/example/to_string/ToStringPresentInParent.java" );
    final JavaFileObject source2 = JavaFileObjects.forResource( "input/com/example/to_string/ParentType.java" );
    final String output = "expected/com/example/to_string/Arez_ToStringPresentInParent.java";
    assertSuccessfulCompile( Arrays.asList( source1, source2 ), Collections.singletonList( output ) );
  }

  @Test
  public void processSuccessfulNestedCompile()
    throws Exception
  {
    assertSuccessfulCompile( "input/NestedModel.java", "expected/NestedModel_Arez_BasicActionModel.java" );
  }

  @Test
  public void processSuccessfulNestedNestedCompile()
    throws Exception
  {
    assertSuccessfulCompile( "input/NestedNestedModel.java",
                             "expected/NestedNestedModel_Something_Arez_BasicActionModel.java" );
  }

  @Test
  public void processSuccessfulWhereAnnotationsSourcedFromInterface()
    throws Exception
  {
    final JavaFileObject source1 = JavaFileObjects.forResource( "input/DefaultMethodsModel.java" );
    final JavaFileObject source2 = JavaFileObjects.forResource( "input/MyAnnotatedInterface.java" );
    final String output1 = "expected/Arez_DefaultMethodsModel.java";
    assertSuccessfulCompile( Arrays.asList( source1, source2 ),
                             Collections.singletonList( output1 ) );
  }

  @Test
  public void processSuccessfulWhereTypeResolvedInInheritanceHierarchy()
    throws Exception
  {
    final JavaFileObject source1 = JavaFileObjects.forResource( "input/com/example/type_params/AbstractModel.java" );
    final JavaFileObject source2 = JavaFileObjects.forResource( "input/com/example/type_params/MiddleModel.java" );
    final JavaFileObject source3 = JavaFileObjects.forResource( "input/com/example/type_params/ConcreteModel.java" );
    final String output1 = "expected/com/example/type_params/Arez_ConcreteModel.java";
    assertSuccessfulCompile( Arrays.asList( source1, source2, source3 ), Collections.singletonList( output1 ) );
  }

  @Test
  public void processResolvedParameterizedType()
    throws Exception
  {
    final JavaFileObject source1 =
      JavaFileObjects.forResource( "input/com/example/parameterized_type/ParentModel.java" );
    final JavaFileObject source2 =
      JavaFileObjects.forResource( "input/com/example/parameterized_type/ResolvedModel.java" );
    final String output1 = "expected/com/example/parameterized_type/Arez_ResolvedModel.java";
    assertSuccessfulCompile( Arrays.asList( source1, source2 ), Collections.singletonList( output1 ) );
  }

  @Test
  public void processUnresolvedParameterizedType()
    throws Exception
  {
    final JavaFileObject source1 =
      JavaFileObjects.forResource( "input/com/example/parameterized_type/ParentModel.java" );
    final JavaFileObject source2 =
      JavaFileObjects.forResource( "input/com/example/parameterized_type/UnresolvedModel.java" );
    final String output1 = "expected/com/example/parameterized_type/Arez_UnresolvedModel.java";
    assertSuccessfulCompile( Arrays.asList( source1, source2 ), Collections.singletonList( output1 ) );
  }

  @Test
  public void processSuccessfulWhereGenericsRefinedAndActionsOverriddenHierarchy()
    throws Exception
  {
    final JavaFileObject source1 =
      JavaFileObjects.forResource( "input/com/example/override_generics/GenericsBaseModel.java" );
    final JavaFileObject source2 =
      JavaFileObjects.forResource( "input/com/example/override_generics/GenericsMiddleModel.java" );
    final JavaFileObject source3 =
      JavaFileObjects.forResource( "input/com/example/override_generics/GenericsModel.java" );
    final String output1 = "expected/com/example/override_generics/Arez_GenericsModel.java";
    assertSuccessfulCompile( Arrays.asList( source1, source2, source3 ), Collections.singletonList( output1 ) );
  }

  @Test
  public void processSuccessfulWhereTraceInheritanceChain()
    throws Exception
  {
    final JavaFileObject source1 = JavaFileObjects.forResource( "input/com/example/inheritance/BaseModel.java" );
    final JavaFileObject source2 = JavaFileObjects.forResource( "input/com/example/inheritance/ParentModel.java" );
    final JavaFileObject source3 = JavaFileObjects.forResource( "input/com/example/inheritance/MyModel.java" );
    final JavaFileObject source4 = JavaFileObjects.forResource( "input/com/example/inheritance/MyInterface1.java" );
    final JavaFileObject source5 = JavaFileObjects.forResource( "input/com/example/inheritance/MyInterface2.java" );
    final String output1 = "expected/com/example/inheritance/Arez_MyModel.java";
    assertSuccessfulCompile( Arrays.asList( source1, source2, source3, source4, source5 ),
                             Collections.singletonList( output1 ) );
  }

  @DataProvider( name = "failedCompiles" )
  public Object[][] failedCompiles()
  {
    return new Object[][]
      {
        new Object[]{ "com.example.action.AbstractActionModel", "@Action target must not be abstract" },
        new Object[]{ "com.example.action.BadActionName2Model",
                      "@Action target specified an invalid name 'ace-'. The name must be a valid java identifier." },
        new Object[]{ "com.example.action.BadActionNameModel",
                      "@Action target specified an invalid name 'assert'. The name must not be a java keyword." },
        new Object[]{ "com.example.action.DuplicateActionModel",
                      "Method annotated with @Action specified name ace that duplicates @Action defined by method setField" },
        new Object[]{ "com.example.action.PrivateActionModel", "@Action target must not be private" },
        new Object[]{ "com.example.action.StaticActionModel", "@Action target must not be static" },

        new Object[]{ "com.example.autorun.AutorunAbstractModel", "@Autorun target must not be abstract" },
        new Object[]{ "com.example.autorun.AutorunBadNameModel",
                      "@Autorun target specified an invalid name '-ace'. The name must be a valid java identifier." },
        new Object[]{ "com.example.autorun.AutorunBadNameModel2",
                      "@Autorun target specified an invalid name 'float'. The name must not be a java keyword." },
        new Object[]{ "com.example.autorun.AutorunDuplicateModel",
                      "@Autorun specified name doStuff that duplicates @Autorun defined by method foo" },
        new Object[]{ "com.example.autorun.AutorunParametersModel", "@Autorun target must not have any parameters" },
        new Object[]{ "com.example.autorun.AutorunPrivateModel", "@Autorun target must not be private" },
        new Object[]{ "com.example.autorun.AutorunReturnsValueModel", "@Autorun target must not return a value" },
        new Object[]{ "com.example.autorun.AutorunStaticModel", "@Autorun target must not be static" },
        new Object[]{ "com.example.autorun.AutorunThrowsExceptionModel",
                      "@Autorun target must not throw any exceptions" },

        new Object[]{ "com.example.component.ConcreteComponent",
                      "@ArezComponent target must be abstract unless the allowConcrete parameter is set to true" },
        new Object[]{ "com.example.component.ModelWithAbstractMethod",
                      "@ArezComponent target has an abstract method not implemented by framework" },
        new Object[]{ "com.example.component.BadTypeComponent",
                      "@ArezComponent target specified an invalid type ''. The type must be a valid java identifier." },
        new Object[]{ "com.example.component.BadTypeComponent2",
                      "@ArezComponent target specified an invalid type 'long'. The type must not be a java keyword." },
        new Object[]{ "com.example.component.EmptyComponent",
                      "@ArezComponent target has no methods annotated with @Action, @Computed, @Memoize, @Observable, @Track or @Autorun" },
        new Object[]{ "com.example.component.EmptyTypeComponent",
                      "@ArezComponent target specified an invalid type ''. The type must be a valid java identifier." },
        new Object[]{ "com.example.component.EnumModel", "@ArezComponent target must be a class" },
        new Object[]{ "com.example.component.FinalModel", "@ArezComponent target must not be final" },
        new Object[]{ "com.example.component.InterfaceModel", "@ArezComponent target must be a class" },
        new Object[]{ "com.example.component.NonStaticNestedModel",
                      "@ArezComponent target must not be a non-static nested class" },

        new Object[]{ "com.example.component_id.ComponentIdDuplicatedModel",
                      "@ComponentId target duplicates existing method named getId" },
        new Object[]{ "com.example.component_id.ComponentIdMustNotHaveParametersModel",
                      "@ComponentId target must not have any parameters" },
        new Object[]{ "com.example.component_id.ComponentIdNotAbstractModel",
                      "@ComponentId target must not be abstract" },
        new Object[]{ "com.example.component_id.ComponentIdMustReturnValueModel",
                      "@ComponentId target must return a value" },
        new Object[]{ "com.example.component_id.ComponentIdNotFinalModel", "@ComponentId target must be final" },
        new Object[]{ "com.example.component_id.ComponentIdNotPrivateModel",
                      "@ComponentId target must not be private" },
        new Object[]{ "com.example.component_id.ComponentIdNotStaticModel", "@ComponentId target must not be static" },

        new Object[]{ "com.example.component_type_name.ComponentTypeNameDuplicateModel",
                      "@ComponentTypeNameRef target duplicates existing method named getTypeName" },
        new Object[]{ "com.example.component_type_name.ComponentTypeNameFinalModel",
                      "@ComponentTypeNameRef target must not be final" },
        new Object[]{ "com.example.component_type_name.ComponentTypeNameMustNotHaveParametersModel",
                      "@ComponentTypeNameRef target must not have any parameters" },
        new Object[]{ "com.example.component_type_name.ComponentTypeNameMustReturnValueModel",
                      "@ComponentTypeNameRef target must return a value" },
        new Object[]{ "com.example.component_type_name.ComponentTypeNamePrivateModel",
                      "@ComponentTypeNameRef target must not be private" },
        new Object[]{ "com.example.component_type_name.ComponentTypeNameReturnNonStringModel",
                      "@ComponentTypeNameRef target must return a String" },
        new Object[]{ "com.example.component_type_name.ComponentTypeNameStaticModel",
                      "@ComponentTypeNameRef target must not be static" },
        new Object[]{ "com.example.component_type_name.ComponentTypeNameThrowsExceptionModel",
                      "@ComponentTypeNameRef target must not throw any exceptions" },

        new Object[]{ "com.example.component_name_ref.ComponentNameRefDuplicateModel",
                      "@ComponentNameRef target duplicates existing method named getTypeName" },
        new Object[]{ "com.example.component_name_ref.ComponentNameRefFinalModel",
                      "@ComponentNameRef target must not be final" },
        new Object[]{ "com.example.component_name_ref.ComponentNameRefMustNotHaveParametersModel",
                      "@ComponentNameRef target must not have any parameters" },
        new Object[]{ "com.example.component_name_ref.ComponentNameRefMustReturnValueModel",
                      "@ComponentNameRef target must return a value" },
        new Object[]{ "com.example.component_name_ref.ComponentNameRefPrivateModel",
                      "@ComponentNameRef target must not be private" },
        new Object[]{ "com.example.component_name_ref.ComponentNameRefStaticModel",
                      "@ComponentNameRef target must not be static" },
        new Object[]{ "com.example.component_name_ref.ComponentNameRefThrowsExceptionModel",
                      "@ComponentNameRef target must not throw any exceptions" },

        new Object[]{ "com.example.computed.AbstractComputedModel", "@Computed target must not be abstract" },
        new Object[]{ "com.example.computed.BadComputedName2Model",
                      "@Computed target specified an invalid name 'public'. The name must not be a java keyword." },
        new Object[]{ "com.example.computed.BadComputedNameModel",
                      "@Computed target specified an invalid name '-ace'. The name must be a valid java identifier." },
        new Object[]{ "com.example.computed.ComputedThrowsExceptionModel",
                      "@Computed target must not throw any exceptions" },
        new Object[]{ "com.example.computed.DuplicateComputedModel",
                      "Method annotated with @Computed specified name ace that duplicates @Computed defined by method getX" },
        new Object[]{ "com.example.computed.FinalComputedModel", "@Computed target must not be final" },
        new Object[]{ "com.example.computed.ParameterizedComputedModel",
                      "@Computed target must not have any parameters" },
        new Object[]{ "com.example.computed.PrivateComputedModel", "@Computed target must not be private" },
        new Object[]{ "com.example.computed.StaticComputedModel", "@Computed target must not be static" },
        new Object[]{ "com.example.computed.VoidComputedModel", "@Computed target must return a value" },

        new Object[]{ "com.example.memoized.AbstractMemoizeModel", "@Memoize target must not be abstract" },
        new Object[]{ "com.example.memoized.BadName2MemoizeModel",
                      "@Memoize target specified an invalid name 'protected'. The name must not be a java keyword." },
        new Object[]{ "com.example.memoized.BadNameMemoizeModel",
                      "@Memoize target specified an invalid name '-ace'. The name must be a valid java identifier." },
        new Object[]{ "com.example.memoized.ThrowsExceptionMemoizeModel",
                      "@Memoize target must not throw any exceptions" },
        new Object[]{ "com.example.memoized.DuplicateMemoizeModel",
                      "Method annotated with @Memoize specified name method1 that duplicates @Memoize defined by method method1" },
        new Object[]{ "com.example.memoized.FinalMemoizeModel", "@Memoize target must not be final" },
        new Object[]{ "com.example.memoized.NoParamMemoizeModel",
                      "@Memoize target must have parameters" },
        new Object[]{ "com.example.memoized.PrivateMemoizeModel", "@Memoize target must not be private" },
        new Object[]{ "com.example.memoized.StaticMemoizeModel", "@Memoize target must not be static" },
        new Object[]{ "com.example.memoized.VoidMemoizeModel", "@Memoize target must return a value" },

        new Object[]{ "com.example.component_ref.FinalModel", "@ComponentRef target must not be final" },
        new Object[]{ "com.example.component_ref.StaticModel", "@ComponentRef target must not be static" },
        new Object[]{ "com.example.component_ref.PrivateModel", "@ComponentRef target must not be private" },
        new Object[]{ "com.example.component_ref.VoidModel", "@ComponentRef target must return a value" },
        new Object[]{ "com.example.component_ref.BadTypeModel",
                      "Method annotated with @ComponentRef must return an instance of arez.Component" },
        new Object[]{ "com.example.component_ref.ThrowsExceptionModel",
                      "@ComponentRef target must not throw any exceptions" },
        new Object[]{ "com.example.component_ref.DuplicateModel",
                      "@ComponentRef target duplicates existing method named getComponent" },
        new Object[]{ "com.example.component_ref.ParametersModel",
                      "@ComponentRef target must not have any parameters" },

        new Object[]{ "com.example.context_ref.FinalModel", "@ContextRef target must not be final" },
        new Object[]{ "com.example.context_ref.StaticModel", "@ContextRef target must not be static" },
        new Object[]{ "com.example.context_ref.PrivateModel", "@ContextRef target must not be private" },
        new Object[]{ "com.example.context_ref.VoidModel", "@ContextRef target must return a value" },
        new Object[]{ "com.example.context_ref.BadTypeModel",
                      "Method annotated with @ContextRef must return an instance of arez.ArezContext" },
        new Object[]{ "com.example.context_ref.ThrowsExceptionModel",
                      "@ContextRef target must not throw any exceptions" },
        new Object[]{ "com.example.context_ref.DuplicateModel",
                      "@ContextRef target duplicates existing method named getContext" },
        new Object[]{ "com.example.context_ref.ParametersModel", "@ContextRef target must not have any parameters" },

        new Object[]{ "com.example.inject.MultipleConstructorsModel",
                      "@ArezComponent specified inject parameter but has more than one constructor" },
        new Object[]{ "com.example.inject.MultipleConstructorsScopedModel",
                      "@ArezComponent target has specified a scope annotation but has more than one constructor and thus is not a candidate for injection" },
        new Object[]{ "com.example.inject.MultipleScopesModel",
                      "@ArezComponent target has specified multiple scope annotations: [javax.inject.Singleton, com.example.inject.MultipleScopesModel.MyScope]" },

        new Object[]{ "com.example.observable_ref.BadNameModel",
                      "@ObservableRef target specified an invalid name '-ace'. The name must be a valid java identifier." },
        new Object[]{ "com.example.observable_ref.BadNameModel2",
                      "@ObservableRef target specified an invalid name 'const'. The name must not be a java keyword." },
        new Object[]{ "com.example.observable_ref.BadReturnTypeModel",
                      "Method annotated with @ObservableRef must return an instance of arez.Observable" },
        new Object[]{ "com.example.observable_ref.BadReturnTypeParameter2Model",
                      "@ObservableRef target has a type parameter of ? but @Observable method returns type of long" },
        new Object[]{ "com.example.observable_ref.BadReturnTypeParameterModel",
                      "@ObservableRef target has a type parameter of java.lang.String but @Observable method returns type of long" },
        new Object[]{ "com.example.observable_ref.DuplicateRefMethodModel",
                      "Method annotated with @ObservableRef defines duplicate ref accessor for observable named time" },
        new Object[]{ "com.example.observable_ref.FinalModel", "@ObservableRef target must not be final" },
        new Object[]{ "com.example.observable_ref.NonAlignedNameModel",
                      "Method annotated with @ObservableRef should specify name or be named according to the convention get[Name]Observable" },
        new Object[]{ "com.example.observable_ref.NoObservableModel",
                      "@ObservableRef target unable to associated with an Observable property" },
        new Object[]{ "com.example.observable_ref.ParametersModel",
                      "@ObservableRef target must not have any parameters" },
        new Object[]{ "com.example.observable_ref.PrivateModel", "@ObservableRef target must not be private" },
        new Object[]{ "com.example.observable_ref.StaticModel", "@ObservableRef target must not be static" },
        new Object[]{ "com.example.observable_ref.ThrowsExceptionModel",
                      "@ObservableRef target must not throw any exceptions" },

        new Object[]{ "com.example.computed_value_ref.BadNameModel",
                      "@ComputedValueRef target specified an invalid name '-ace'. The name must be a valid java identifier." },
        new Object[]{ "com.example.computed_value_ref.BadNameModel2",
                      "@ComputedValueRef target specified an invalid name 'private'. The name must not be a java keyword." },
        new Object[]{ "com.example.computed_value_ref.BadReturnTypeModel",
                      "Method annotated with @ComputedValueRef must return an instance of arez.ComputedValue" },
        new Object[]{ "com.example.computed_value_ref.BadReturnType2Model",
                      "@ComputedValueRef target has a type parameter of ? but @Computed method returns type of long" },
        new Object[]{ "com.example.computed_value_ref.BadReturnType3Model",
                      "@ComputedValueRef target has a type parameter of java.lang.String but @Computed method returns type of long" },
        new Object[]{ "com.example.computed_value_ref.DuplicateRefMethodModel",
                      "@ComputedValueRef target duplicates existing method named getTimeComputedValue" },
        new Object[]{ "com.example.computed_value_ref.FinalModel", "@ComputedValueRef target must not be final" },
        new Object[]{ "com.example.computed_value_ref.NoComputedValueModel",
                      "@ComputedValueRef exists but there is no corresponding @Computed" },
        new Object[]{ "com.example.computed_value_ref.NonAlignedNameModel",
                      "Method annotated with @ComputedValueRef should specify name or be named according to the convention get[Name]ComputedValue" },
        new Object[]{ "com.example.computed_value_ref.ParametersModel",
                      "@ComputedValueRef target must not have any parameters" },
        new Object[]{ "com.example.computed_value_ref.PrivateModel", "@ComputedValueRef target must not be private" },
        new Object[]{ "com.example.computed_value_ref.StaticModel", "@ComputedValueRef target must not be static" },
        new Object[]{ "com.example.computed_value_ref.ThrowsExceptionModel",
                      "@ComputedValueRef target must not throw any exceptions" },

        new Object[]{ "com.example.observer_ref.BadNameModel",
                      "@ObserverRef target specified an invalid name '-ace'. The name must be a valid java identifier." },
        new Object[]{ "com.example.observer_ref.BadNameModel2",
                      "@ObserverRef target specified an invalid name 'int'. The name must not be a java keyword." },
        new Object[]{ "com.example.observer_ref.BadReturnTypeModel",
                      "Method annotated with @ObserverRef must return an instance of arez.Observer" },
        new Object[]{ "com.example.observer_ref.DuplicateNameModel",
                      "Method annotated with @ObserverRef defines duplicate ref accessor for observer named doStuff" },
        new Object[]{ "com.example.observer_ref.ExceptionModel", "@ObserverRef target must not throw any exceptions" },
        new Object[]{ "com.example.observer_ref.FinalModel", "@ObserverRef target must not be final" },
        new Object[]{ "com.example.observer_ref.NoNameModel",
                      "Method annotated with @ObserverRef should specify name or be named according to the convention get[Name]Observer" },
        new Object[]{ "com.example.observer_ref.ParametersModel",
                      "@ObserverRef target must not have any parameters" },
        new Object[]{ "com.example.observer_ref.PrivateModel", "@ObserverRef target must not be private" },
        new Object[]{ "com.example.observer_ref.RefOnNeitherModel",
                      "@ObserverRef target defined observer named 'render' but no @Autorun or @Track method with that name exists" },
        new Object[]{ "com.example.observer_ref.StaticModel", "@ObserverRef target must not be static" },
        new Object[]{ "com.example.observer_ref.VoidReturnModel",
                      "Method annotated with @ObserverRef must return an instance of arez.Observer" },

        new Object[]{ "com.example.name_duplicates.ActionDuplicatesObservableNameModel",
                      "Method annotated with @Action specified name field that duplicates @Observable defined by method getField" },
        new Object[]{ "com.example.on_activate.OnActivateAbstractModel", "@OnActivate target must not be abstract" },
        new Object[]{ "com.example.on_activate.OnActivateNoComputedModel",
                      "@OnActivate exists but there is no corresponding @Computed" },
        new Object[]{ "com.example.on_activate.OnActivateBadNameModel",
                      "@OnActivate as does not match on[Name]Activate pattern. Please specify name." },
        new Object[]{ "com.example.on_activate.OnActivateBadNameModel2",
                      "@OnActivate target specified an invalid name 'final'. The name must not be a java keyword." },
        new Object[]{ "com.example.on_activate.OnActivateBadNameModel3",
                      "@OnActivate target specified an invalid name '-f-f-'. The name must be a valid java identifier." },
        new Object[]{ "com.example.on_activate.OnActivatePrivateModel", "@OnActivate target must not be private" },
        new Object[]{ "com.example.on_activate.OnActivateStaticModel", "@OnActivate target must not be static" },
        new Object[]{ "com.example.on_activate.OnActivateParametersModel",
                      "@OnActivate target must not have any parameters" },
        new Object[]{ "com.example.on_activate.OnActivateReturnValueModel",
                      "@OnActivate target must not return a value" },
        new Object[]{ "com.example.on_activate.OnActivateThrowsExceptionModel",
                      "@OnActivate target must not throw any exceptions" },
        new Object[]{ "com.example.on_activate.OnActivateDuplicateModel",
                      "@OnActivate target duplicates existing method named foo" },
        new Object[]{ "com.example.on_deactivate.OnDeactivateAbstractModel",
                      "@OnDeactivate target must not be abstract" },
        new Object[]{ "com.example.on_deactivate.OnDeactivateNoComputedModel",
                      "@OnDeactivate exists but there is no corresponding @Computed" },
        new Object[]{ "com.example.on_deactivate.OnDeactivateBadNameModel",
                      "@OnDeactivate as does not match on[Name]Deactivate pattern. Please specify name." },
        new Object[]{ "com.example.on_deactivate.OnDeactivateBadNameModel2",
                      "@OnDeactivate target specified an invalid name 'abstract'. The name must not be a java keyword." },
        new Object[]{ "com.example.on_deactivate.OnDeactivateBadNameModel3",
                      "@OnDeactivate target specified an invalid name '-a-'. The name must be a valid java identifier." },
        new Object[]{ "com.example.on_deactivate.OnDeactivatePrivateModel",
                      "@OnDeactivate target must not be private" },
        new Object[]{ "com.example.on_deactivate.OnDeactivateStaticModel", "@OnDeactivate target must not be static" },
        new Object[]{ "com.example.on_deactivate.OnDeactivateParametersModel",
                      "@OnDeactivate target must not have any parameters" },
        new Object[]{ "com.example.on_deactivate.OnDeactivateReturnValueModel",
                      "@OnDeactivate target must not return a value" },
        new Object[]{ "com.example.on_deactivate.OnDeactivateThrowsExceptionModel",
                      "@OnDeactivate target must not throw any exceptions" },
        new Object[]{ "com.example.on_deactivate.OnDeactivateDuplicateModel",
                      "@OnDeactivate target duplicates existing method named foo" },
        new Object[]{ "com.example.on_stale.OnStaleAbstractModel", "@OnStale target must not be abstract" },
        new Object[]{ "com.example.on_stale.OnStaleNoComputedModel",
                      "@OnStale exists but there is no corresponding @Computed" },
        new Object[]{ "com.example.on_stale.OnStaleBadNameModel",
                      "@OnStale as does not match on[Name]Stale pattern. Please specify name." },
        new Object[]{ "com.example.on_stale.OnStaleBadNameModel2",
                      "@OnStale target specified an invalid name 'if'. The name must not be a java keyword." },
        new Object[]{ "com.example.on_stale.OnStaleBadNameModel3",
                      "@OnStale target specified an invalid name '-a-'. The name must be a valid java identifier." },
        new Object[]{ "com.example.on_stale.OnStalePrivateModel", "@OnStale target must not be private" },
        new Object[]{ "com.example.on_stale.OnStaleStaticModel", "@OnStale target must not be static" },
        new Object[]{ "com.example.on_stale.OnStaleParametersModel", "@OnStale target must not have any parameters" },
        new Object[]{ "com.example.on_stale.OnStaleReturnValueModel", "@OnStale target must not return a value" },
        new Object[]{ "com.example.on_stale.OnStaleThrowsExceptionModel",
                      "@OnStale target must not throw any exceptions" },
        new Object[]{ "com.example.on_stale.OnStaleDuplicateModel",
                      "@OnStale target duplicates existing method named foo" },
        new Object[]{ "com.example.on_dispose.OnDisposeAbstractModel", "@OnDispose target must not be abstract" },
        new Object[]{ "com.example.on_dispose.OnDisposeNoComputedModel",
                      "@OnDispose exists but there is no corresponding @Computed" },
        new Object[]{ "com.example.on_dispose.OnDisposeBadNameModel",
                      "@OnDispose as does not match on[Name]Dispose pattern. Please specify name." },
        new Object[]{ "com.example.on_dispose.OnDisposeBadNameModel2",
                      "@OnDispose target specified an invalid name 'import'. The name must not be a java keyword." },
        new Object[]{ "com.example.on_dispose.OnDisposeBadNameModel3",
                      "@OnDispose target specified an invalid name '-n-'. The name must be a valid java identifier." },
        new Object[]{ "com.example.on_dispose.OnDisposePrivateModel", "@OnDispose target must not be private" },
        new Object[]{ "com.example.on_dispose.OnDisposeStaticModel", "@OnDispose target must not be static" },
        new Object[]{ "com.example.on_dispose.OnDisposeParametersModel",
                      "@OnDispose target must not have any parameters" },
        new Object[]{ "com.example.on_dispose.OnDisposeReturnValueModel", "@OnDispose target must not return a value" },
        new Object[]{ "com.example.on_dispose.OnDisposeThrowsExceptionModel",
                      "@OnDispose target must not throw any exceptions" },
        new Object[]{ "com.example.on_dispose.OnDisposeDuplicateModel",
                      "@OnDispose target duplicates existing method named foo" },
        new Object[]{ "com.example.pre_dispose.PreDisposeAbstractModel", "@PreDispose target must not be abstract" },
        new Object[]{ "com.example.pre_dispose.PreDisposePrivateModel", "@PreDispose target must not be private" },
        new Object[]{ "com.example.pre_dispose.PreDisposeStaticModel", "@PreDispose target must not be static" },
        new Object[]{ "com.example.pre_dispose.PreDisposeParametersModel",
                      "@PreDispose target must not have any parameters" },
        new Object[]{ "com.example.pre_dispose.PreDisposeReturnValueModel",
                      "@PreDispose target must not return a value" },
        new Object[]{ "com.example.pre_dispose.PreDisposeThrowsExceptionModel",
                      "@PreDispose target must not throw any exceptions" },
        new Object[]{ "com.example.pre_dispose.PreDisposeDuplicateModel",
                      "@PreDispose target duplicates existing method named foo" },
        new Object[]{ "com.example.post_dispose.PostDisposeAbstractModel", "@PostDispose target must not be abstract" },
        new Object[]{ "com.example.post_dispose.PostDisposePrivateModel", "@PostDispose target must not be private" },
        new Object[]{ "com.example.post_dispose.PostDisposeStaticModel", "@PostDispose target must not be static" },
        new Object[]{ "com.example.post_dispose.PostDisposeParametersModel",
                      "@PostDispose target must not have any parameters" },
        new Object[]{ "com.example.post_dispose.PostDisposeReturnValueModel",
                      "@PostDispose target must not return a value" },
        new Object[]{ "com.example.post_dispose.PostDisposeThrowsExceptionModel",
                      "@PostDispose target must not throw any exceptions" },
        new Object[]{ "com.example.post_dispose.PostDisposeDuplicateModel",
                      "@PostDispose target duplicates existing method named foo" },
        new Object[]{ "com.example.observable.PrivateObservableGetterModel", "@Observable target must not be private" },
        new Object[]{ "com.example.observable.PrivateObservableSetterModel", "@Observable target must not be private" },
        new Object[]{ "com.example.observable.MissingObservableGetterModel",
                      "@Observable target defined setter but no getter was defined and no getter could be automatically determined" },
        new Object[]{ "com.example.observable.MissingObservableSetterModel",
                      "@Observable target defined getter but no setter was defined and no setter could be automatically determined" },
        new Object[]{ "com.example.name_duplicates.ActionAndComputedSameNameModel",
                      "Method annotated with @Action specified name x that duplicates @Computed defined by method m1" },
        new Object[]{ "com.example.name_duplicates.ActionAndObservableSameNameModel",
                      "Method annotated with @Observable specified name x that duplicates @Action defined by method m1" },
        new Object[]{ "com.example.name_duplicates.ActionAndObservableSameNameNoGetterYetModel",
                      "Method annotated with @Action specified name x that duplicates @Observable defined by method setTime" },
        new Object[]{ "com.example.name_duplicates.ComputedAndObservableSameNameModel",
                      "Method annotated with @Observable specified name x that duplicates @Computed defined by method m1" },
        new Object[]{ "com.example.tracked.TrackedDuplicatedModel",
                      "@Track target duplicates existing method named render" },
        new Object[]{ "com.example.tracked.TrackedDuplicatedName",
                      "Method annotated with @Computed specified name render that duplicates @Track defined by method render" },
        new Object[]{ "com.example.tracked.TrackedAbstractModel", "@Track target must not be abstract" },
        new Object[]{ "com.example.tracked.TrackedFinalModel", "@Track target must not be final" },
        new Object[]{ "com.example.tracked.TrackedNotStaticModel", "@Track target must not be static" },
        new Object[]{ "com.example.tracked.TrackedNotPrivateModel", "@Track target must not be private" },
        new Object[]{ "com.example.tracked.TrackedMissingOnDepsChanged",
                      "@Track target has no corresponding @OnDepsChanged that could be automatically determined" },
        new Object[]{ "com.example.tracked.TrackedBadNameModel",
                      "@Track target specified an invalid name '-ace'. The name must be a valid java identifier." },
        new Object[]{ "com.example.tracked.TrackedBadNameModel2",
                      "@Track target specified an invalid name 'import'. The name must not be a java keyword." },
        new Object[]{ "com.example.on_deps_updated.OnDepsChangedNotAbstractModel",
                      "@OnDepsChanged target must not be abstract" },
        new Object[]{ "com.example.on_deps_updated.OnDepsChangedDuplicatedModel",
                      "@OnDepsChanged target duplicates existing method named onRenderDepsChanged" },
        new Object[]{ "com.example.on_deps_updated.OnDepsChangedNotStaticModel",
                      "@OnDepsChanged target must not be static" },
        new Object[]{ "com.example.on_deps_updated.OnDepsChangedNotPrivateModel",
                      "@OnDepsChanged target must not be private" },
        new Object[]{ "com.example.on_deps_updated.OnDepsChangedMustNotHaveParametersModel",
                      "@OnDepsChanged target must not have any parameters" },
        new Object[]{ "com.example.on_deps_updated.OnDepsChangedMustNotReturnValueModel",
                      "@OnDepsChanged target must not return a value" },
        new Object[]{ "com.example.on_deps_updated.OnDepsChangedThrowsExceptionModel",
                      "@OnDepsChanged target must not throw any exceptions" },
        new Object[]{ "com.example.on_deps_updated.OnDepsChangedBadName",
                      "@OnDepsChanged target specified an invalid name '-ace'. The name must be a valid java identifier." },
        new Object[]{ "com.example.on_deps_updated.OnDepsChangedBadName2",
                      "@OnDepsChanged target specified an invalid name 'class'. The name must not be a java keyword." },
        new Object[]{ "com.example.on_deps_updated.OnDepsChangedBadName3",
                      "@OnDepsChanged target specified an invalid name '-ace-'. The name must be a valid java identifier." },
        new Object[]{ "com.example.on_deps_updated.OnDepsChangedNoTracked",
                      "@OnDepsChanged target has no corresponding @Track that could be automatically determined" },
        new Object[]{ "com.example.post_construct.EjbPostConstructModel",
                      "@javax.annotation.PostConstruct annotation not supported in components annotated with @ArezComponent, use the @arez.annotations.PostConstruct annotation instead." },
        new Object[]{ "com.example.post_construct.PostConstructAbstractModel",
                      "@PostConstruct target must not be abstract" },
        new Object[]{ "com.example.post_construct.PostConstructDuplicateModel",
                      "@PostConstruct target duplicates existing method named postConstruct1" },
        new Object[]{ "com.example.post_construct.PostConstructMustNotHaveParametersModel",
                      "@PostConstruct target must not have any parameters" },
        new Object[]{ "com.example.post_construct.PostConstructMustNotReturnValueModel",
                      "@PostConstruct target must not return a value" },
        new Object[]{ "com.example.post_construct.PostConstructNotStaticModel",
                      "@PostConstruct target must not be static" },
        new Object[]{ "com.example.post_construct.PostConstructNotPrivateModel",
                      "@PostConstruct target must not be private" },
        new Object[]{ "com.example.name_duplicates.ObservableAndActionMethodModel",
                      "Method can not be annotated with both @Action and @Observable" },
        new Object[]{ "com.example.name_duplicates.ObservableAndPreDisposeMethodModel",
                      "Method can not be annotated with both @Observable and @PreDispose" },
        new Object[]{ "com.example.name_duplicates.ObservableAndPostDisposeMethodModel",
                      "Method can not be annotated with both @Observable and @PostDispose" },
        new Object[]{ "com.example.name_duplicates.ObservableAndComputedMethodModel",
                      "Method can not be annotated with both @Observable and @Computed" },
        new Object[]{ "com.example.name_duplicates.ObservableAndContainerIdMethodModel",
                      "Method can not be annotated with both @Observable and @ComponentId" },
        new Object[]{ "com.example.name_duplicates.ObservableAndOnActivateMethodModel",
                      "Method can not be annotated with both @Observable and @OnActivate" },
        new Object[]{ "com.example.name_duplicates.ObservableAndOnDeactivateMethodModel",
                      "Method can not be annotated with both @Observable and @OnDeactivate" },
        new Object[]{ "com.example.name_duplicates.ObservableAndOnStaleMethodModel",
                      "Method can not be annotated with both @Observable and @OnStale" },
        new Object[]{ "com.example.name_duplicates.ActionAndAutorunMethodModel",
                      "Method can not be annotated with both @Action and @Autorun" },
        new Object[]{ "com.example.name_duplicates.ActionAndComputedMethodModel",
                      "Method can not be annotated with both @Action and @Computed" },
        new Object[]{ "com.example.name_duplicates.ActionAndPreDisposeMethodModel",
                      "Method can not be annotated with both @Action and @PreDispose" },
        new Object[]{ "com.example.name_duplicates.ActionAndPostDisposeMethodModel",
                      "Method can not be annotated with both @Action and @PostDispose" },
        new Object[]{ "com.example.name_duplicates.ActionAndContainerIdMethodModel",
                      "Method can not be annotated with both @Action and @ComponentId" },
        new Object[]{ "com.example.name_duplicates.ActionAndOnActivateMethodModel",
                      "Method can not be annotated with both @Action and @OnActivate" },
        new Object[]{ "com.example.name_duplicates.ActionAndOnDeactivateMethodModel",
                      "Method can not be annotated with both @Action and @OnDeactivate" },
        new Object[]{ "com.example.name_duplicates.ActionAndOnStaleMethodModel",
                      "Method can not be annotated with both @Action and @OnStale" },
        new Object[]{ "com.example.name_duplicates.ComputedAndContainerIdMethodModel",
                      "Method can not be annotated with both @Computed and @ComponentId" },
        new Object[]{ "com.example.name_duplicates.ComputedAndPreDisposeMethodModel",
                      "Method can not be annotated with both @Computed and @PreDispose" },
        new Object[]{ "com.example.name_duplicates.ComputedAndTrackMethodModel",
                      "Method can not be annotated with both @Track and @Computed" },
        new Object[]{ "com.example.name_duplicates.ComputedAndPostDisposeMethodModel",
                      "Method can not be annotated with both @Computed and @PostDispose" },
        new Object[]{ "com.example.name_duplicates.ComputedAndOnActivateMethodModel",
                      "Method can not be annotated with both @Computed and @OnActivate" },
        new Object[]{ "com.example.name_duplicates.ComputedAndOnDeactivateMethodModel",
                      "Method can not be annotated with both @Computed and @OnDeactivate" },
        new Object[]{ "com.example.name_duplicates.ComputedAndOnStaleMethodModel",
                      "Method can not be annotated with both @Computed and @OnStale" },
        new Object[]{ "com.example.pre_dispose.PreDisposeAndPostDisposeMethodModel",
                      "Method can not be annotated with both @PreDispose and @PostDispose" },

        new Object[]{ "com.example.observable.AbstractGetterNoSetterModel",
                      "@Observable target defines expectSetter = false but there is no ref method for observable and thus never possible to report it as changed and thus should not be observable." },
        new Object[]{ "com.example.observable.NonAbstractGetterModel",
                      "@Observable property defines an abstract setter but a concrete getter. Both getter and setter must be concrete or both must be abstract." },
        new Object[]{ "com.example.observable.NonAbstractSetterModel",
                      "@Observable property defines an abstract getter but a concrete setter. Both getter and setter must be concrete or both must be abstract." },
        new Object[]{ "com.example.observable.BadTypesModel",
                      "@Observable property defines a setter and getter with different types. Getter type: long Setter type: int." },
        new Object[]{ "com.example.observable.NoSetterOrRefModel",
                      "@Observable target defines expectSetter = false but there is no ref method for observable and thus never possible to report it as changed and thus should not be observable." },
        new Object[]{ "com.example.observable.SetterButExpectSetterFalseModel",
                      "Method annotated with @Observable defines expectSetter = false but a setter exists for observable named field" },
        new Object[]{ "com.example.observable.SetterButExpectSetterFalse2Model",
                      "Method annotated with @Observable defines expectSetter = false but a setter exists named setFieldfor observable named field" },
        new Object[]{ "com.example.observable.SetterWithExpectSetterFalseModel",
                      "Method annotated with @Observable is a setter but defines expectSetter = false for observable named field" },

        new Object[]{ "com.example.observable.BadObservableNameModel",
                      "@Observable target specified an invalid name '-ace'. The name must be a valid java identifier." },
        new Object[]{ "com.example.observable.BadObservableName2Model",
                      "@Observable target specified an invalid name 'default'. The name must not be a java keyword." },
        new Object[]{ "com.example.observable.ExtraParameterSetterModel",
                      "Method annotated with @Observable should be a setter or getter" },
        new Object[]{ "com.example.observable.ExtraParameterGetterModel",
                      "Method annotated with @Observable should be a setter or getter" },
        new Object[]{ "com.example.observable.DuplicateSetterModel",
                      "@Observable defines duplicate setter for observable named field" },
        new Object[]{ "com.example.observable.DuplicateGetterModel",
                      "@Observable defines duplicate getter for observable named field" },
        new Object[]{ "com.example.observable.StaticObservableGetterModel", "@Observable target must not be static" },
        new Object[]{ "com.example.observable.StaticObservableSetterModel", "@Observable target must not be static" },
        new Object[]{ "com.example.observable.FinalObservableGetterModel", "@Observable target must not be final" },
        new Object[]{ "com.example.observable.FinalObservableSetterModel", "@Observable target must not be final" },
        new Object[]{ "com.example.repository.RepositoryExtensionIsClass",
                      "Class annotated with @Repository defined an extension that is not an interface. Extension: com.example.repository.RepositoryExtensionIsClass.Foo" },
        new Object[]{ "com.example.repository.RepositoryExtensionNotInterface",
                      "Class annotated with @Repository defined an extension that is not an interface. Extension: com.example.repository.RepositoryExtensionNotInterface.Foo" },
        new Object[]{ "com.example.repository.RepositoryExtensionHasBadSelf",
                      "Class annotated with @Repository defined an extension that has a non default method. Extension: com.example.repository.RepositoryExtensionHasBadSelf.Foo Method: self(int)" },
        new Object[]{ "com.example.repository.RepositoryExtensionHasAbstractMethod",
                      "Class annotated with @Repository defined an extension that has a non default method. Extension: com.example.repository.RepositoryExtensionHasAbstractMethod.Foo Method: other(int)" }
      };
  }

  @Test( dataProvider = "failedCompiles" )
  public void processFailedCompile( @Nonnull final String classname, @Nonnull final String errorMessageFragment )
    throws Exception
  {
    assertFailedCompile( classname, errorMessageFragment );
  }
}
