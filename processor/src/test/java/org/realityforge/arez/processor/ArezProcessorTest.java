package org.realityforge.arez.processor;

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
        new Object[]{ "com.example.SubpackageModel", false },
        new Object[]{ "com.example.action.ActionTypeParametersModel", false },
        new Object[]{ "com.example.action.NewTypeParametersModel", false },
        new Object[]{ "com.example.action.NoReportParametersModel", false },
        new Object[]{ "com.example.action.FunctionActionThrowsRuntimeExceptionModel", false },
        new Object[]{ "com.example.action.FunctionActionThrowsThrowableModel", false },
        new Object[]{ "com.example.action.UnsafeSpecificFunctionActionModel", false },
        new Object[]{ "com.example.action.UnsafeSpecificProcedureActionModel", false },
        new Object[]{ "com.example.action.UnsafeFunctionActionModel", false },
        new Object[]{ "com.example.action.UnsafeProcedureActionModel", false },
        new Object[]{ "com.example.action.ReadOnlyActionModel", false },
        new Object[]{ "com.example.action.BasicFunctionActionModel", false },
        new Object[]{ "com.example.action.BasicActionModel", false },
        new Object[]{ "com.example.autorun.BasicAutorunModel", false },
        new Object[]{ "com.example.autorun.ReadWriteAutorunModel", false },
        new Object[]{ "com.example.autorun.ScheduleAfterConstructedModel", false },
        new Object[]{ "com.example.component_id.BooleanComponentId", false },
        new Object[]{ "com.example.component_id.ByteComponentId", false },
        new Object[]{ "com.example.component_id.CharComponentId", false },
        new Object[]{ "com.example.component_id.ComponentIdOnModel", false },
        new Object[]{ "com.example.component_id.DoubleComponentId", false },
        new Object[]{ "com.example.component_id.FloatComponentId", false },
        new Object[]{ "com.example.component_id.IntComponentId", false },
        new Object[]{ "com.example.component_id.LongComponentId", false },
        new Object[]{ "com.example.component_id.ObjectComponentId", false },
        new Object[]{ "com.example.component_id.ShortComponentId", false },
        new Object[]{ "com.example.component_id.ComponentIdOnSingletonModel", false },
        new Object[]{ "com.example.component_name.ComponentNameModel", false },
        new Object[]{ "com.example.component_name.ComponentTypeNameModel", false },
        new Object[]{ "com.example.component_name.ComponentTypeNameAloneOnSingletonModel", false },
        new Object[]{ "com.example.component_name.ComponentNameOnSingletonModel", false },
        new Object[]{ "com.example.component_ref.AnnotatedComponent", false },
        new Object[]{ "com.example.component_ref.SimpleComponent", false },
        new Object[]{ "com.example.component_ref.ProtectedAccessComponent", false },
        new Object[]{ "com.example.computed.ComputedWithNameVariationsModel", false },
        new Object[]{ "com.example.computed.ComputedWithHooksModel", false },
        new Object[]{ "com.example.computed.BasicComputedModel", false },
        new Object[]{ "com.example.computed.TypeParametersModel", false },
        new Object[]{ "com.example.computed_value_ref.DefaultRefNameModel", false },
        new Object[]{ "com.example.computed_value_ref.NonStandardNameModel", false },
        new Object[]{ "com.example.computed_value_ref.RawComputedValueModel", false },
        new Object[]{ "com.example.context_ref.AnnotatedComponent", false },
        new Object[]{ "com.example.context_ref.SimpleComponent", false },
        new Object[]{ "com.example.context_ref.ProtectedAccessComponent", false },
        new Object[]{ "com.example.observable.ObservableWithNoSetter", false },
        new Object[]{ "com.example.observable_ref.DefaultRefNameModel", false },
        new Object[]{ "com.example.observable_ref.NonStandardNameModel", false },
        new Object[]{ "com.example.observable_ref.RawObservableModel", false },
        new Object[]{ "com.example.observer_ref.CustomNameRefOnAutorunModel", false },
        new Object[]{ "com.example.observer_ref.CustomNameRefOnTrackedModel", false },
        new Object[]{ "com.example.observer_ref.RefOnAutorunModel", false },
        new Object[]{ "com.example.observer_ref.RefOnBothModel", false },
        new Object[]{ "com.example.observer_ref.RefOnTrackedModel", false },
        new Object[]{ "com.example.overloaded_names.OverloadedActions", false },
        new Object[]{ "com.example.post_construct.PostConstructModel", false },
        new Object[]{ "com.example.repository.CompleteRepositoryExample", true },
        new Object[]{ "com.example.repository.RepositoryPreDisposeHook", true },
        new Object[]{ "com.example.repository.RepositoryWithExplicitId", true },
        new Object[]{ "com.example.repository.RepositoryWithImplicitId", true },
        new Object[]{ "com.example.repository.RepositoryWithMultipleCtors", true },
        new Object[]{ "com.example.repository.RepositoryWithProtectedConstructor", true },
        new Object[]{ "com.example.repository.RepositoryWithSingleton", true },
        new Object[]{ "com.example.to_string.NoToStringPresent", false },
        new Object[]{ "com.example.to_string.ToStringPresent", false },
        new Object[]{ "com.example.tracked.BasicTrackedModel", false },
        new Object[]{ "com.example.tracked.BasicTrackedWithExceptionsModel", false },
        new Object[]{ "com.example.tracked.DeriveOnDepsChangedModel", false },
        new Object[]{ "com.example.tracked.DeriveTrackedModel", false },
        new Object[]{ "com.example.tracked.NoReportParametersModel", false },
        new Object[]{ "com.example.tracked.ProtectedAccessTrackedModel", false },
        new Object[]{ "com.example.tracked.TrackedAllTypesModel", false },
        new Object[]{ "DisposingModel", false },
        new Object[]{ "ObservableTypeParametersModel", false },
        new Object[]{ "TypeParametersOnModel", false },
        new Object[]{ "ObservableGuessingModel", false },
        new Object[]{ "AnnotationsOnModel", false },
        new Object[]{ "ObservableWithAnnotatedCtorModel", false },
        new Object[]{ "ObservableModelWithUnconventionalNames", false },
        new Object[]{ "DifferentObservableTypesModel", false },
        new Object[]{ "ObservableWithExceptingCtorModel", false },
        new Object[]{ "OverrideNamesInModel", false },
        new Object[]{ "SingletonModel", false },
        new Object[]{ "EmptyModel", false },
        new Object[]{ "BasicModelWithDifferentAccessLevels", false },
        new Object[]{ "ObservableWithCtorModel", false },
        new Object[]{ "ObservableWithSpecificExceptionModel", false },
        new Object[]{ "ObservableWithExceptionModel", false },
        new Object[]{ "BasicObservableModel", false }
      };
  }

  @Test( dataProvider = "successfulCompiles" )
  public void processSuccessfulCompile( @Nonnull final String classname, final boolean repositoryEnabled )
    throws Exception
  {
    assertSuccessfulCompile( classname, repositoryEnabled );
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
  public void processSuccessfulNestedCompileWithRepositories()
    throws Exception
  {
    assertSuccessfulCompile( "input/com/example/repository/NestedModel.java",
                             "expected/com/example/repository/NestedModel$Arez_BasicActionModel.java",
                             "expected/com/example/repository/NestedModel$Arez_BasicActionModel.java",
                             "expected/com/example/repository/NestedModel$Arez_BasicActionModel.java",
                             "expected/com/example/repository/NestedModel$Arez_BasicActionModel.java" );
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
        new Object[]{ "com.example.action.BadActionName2Model",
                      "Method annotated with @Action specified invalid name ace-" },
        new Object[]{ "com.example.action.BadActionNameModel",
                      "Method annotated with @Action specified invalid name -ace" },
        new Object[]{ "com.example.action.DuplicateActionModel",
                      "Method annotated with @Action specified name ace that duplicates @Action defined by method setField" },
        new Object[]{ "com.example.action.PrivateActionModel", "@Action target must not be private" },
        new Object[]{ "com.example.action.StaticActionModel", "@Action target must not be static" },

        new Object[]{ "com.example.autorun.AutorunBadNameModel",
                      "Method annotated with @Autorun specified invalid name -ace" },
        new Object[]{ "com.example.autorun.AutorunDuplicateModel",
                      "@Autorun specified name doStuff that duplicates @Autorun defined by method foo" },
        new Object[]{ "com.example.autorun.AutorunParametersModel", "@Autorun target must not have any parameters" },
        new Object[]{ "com.example.autorun.AutorunPrivateModel", "@Autorun target must not be private" },
        new Object[]{ "com.example.autorun.AutorunReturnsValueModel", "@Autorun target must not return a value" },
        new Object[]{ "com.example.autorun.AutorunStaticModel", "@Autorun target must not be static" },
        new Object[]{ "com.example.autorun.AutorunThrowsExceptionModel",
                      "@Autorun target must not throw any exceptions" },

        new Object[]{ "com.example.component.AbstractModel", "@ArezComponent target must not be abstract" },
        new Object[]{ "com.example.component.BadTypeComponent", "@ArezComponent specified invalid type parameter" },
        new Object[]{ "com.example.component.EmptyComponent",
                      "@ArezComponent target has no methods annotated with @Action, @Computed, @Observable, @Track or @Autorun" },
        new Object[]{ "com.example.component.EmptyTypeComponent", "@ArezComponent specified invalid type parameter" },
        new Object[]{ "com.example.component.EnumModel", "@ArezComponent target must be a class" },
        new Object[]{ "com.example.component.FinalModel", "@ArezComponent target must not be final" },
        new Object[]{ "com.example.component.InterfaceModel", "@ArezComponent target must be a class" },
        new Object[]{ "com.example.component.NonStaticNestedModel",
                      "@ArezComponent target must not be a non-static nested class" },

        new Object[]{ "com.example.component_id.ComponentIdDuplicatedModel",
                      "@ComponentId target duplicates existing method named getId" },
        new Object[]{ "com.example.component_id.ComponentIdMustNotHaveParametersModel",
                      "@ComponentId target must not have any parameters" },
        new Object[]{ "com.example.component_id.ComponentIdMustReturnValueModel",
                      "@ComponentId target must return a value" },
        new Object[]{ "com.example.component_id.ComponentIdNotFinalModel", "@ComponentId target must be final" },
        new Object[]{ "com.example.component_id.ComponentIdNotPrivateModel",
                      "@ComponentId target must not be private" },
        new Object[]{ "com.example.component_id.ComponentIdNotStaticModel", "@ComponentId target must not be static" },

        new Object[]{ "com.example.component_type_name.ComponentTypeNameDuplicateModel",
                      "@ComponentTypeName target duplicates existing method named getTypeName" },
        new Object[]{ "com.example.component_type_name.ComponentTypeNameFinalModel",
                      "@ComponentTypeName target must not be final" },
        new Object[]{ "com.example.component_type_name.ComponentTypeNameMustNotHaveParametersModel",
                      "@ComponentTypeName target must not have any parameters" },
        new Object[]{ "com.example.component_type_name.ComponentTypeNameMustReturnValueModel",
                      "@ComponentTypeName target must return a value" },
        new Object[]{ "com.example.component_type_name.ComponentTypeNamePrivateModel",
                      "@ComponentTypeName target must not be private" },
        new Object[]{ "com.example.component_type_name.ComponentTypeNameReturnNonStringModel",
                      "@ComponentTypeName target must return a String" },
        new Object[]{ "com.example.component_type_name.ComponentTypeNameStaticModel",
                      "@ComponentTypeName target must not be static" },
        new Object[]{ "com.example.component_type_name.ComponentTypeNameThrowsExceptionModel",
                      "@ComponentTypeName target must not throw any exceptions" },

        new Object[]{ "com.example.component_name.ComponentNameDuplicateModel",
                      "@ComponentName target duplicates existing method named getTypeName" },
        new Object[]{ "com.example.component_name.ComponentNameFinalModel", "@ComponentName target must not be final" },
        new Object[]{ "com.example.component_name.ComponentNameMustNotHaveParametersModel",
                      "@ComponentName target must not have any parameters" },
        new Object[]{ "com.example.component_name.ComponentNameMustReturnValueModel",
                      "@ComponentName target must return a value" },
        new Object[]{ "com.example.component_name.ComponentNamePrivateModel",
                      "@ComponentName target must not be private" },
        new Object[]{ "com.example.component_name.ComponentNameStaticModel",
                      "@ComponentName target must not be static" },
        new Object[]{ "com.example.component_name.ComponentNameThrowsExceptionModel",
                      "@ComponentName target must not throw any exceptions" },

        new Object[]{ "com.example.computed.BadComputedName2Model",
                      "Method annotated with @Computed specified invalid name ace-" },
        new Object[]{ "com.example.computed.BadComputedNameModel",
                      "Method annotated with @Computed specified invalid name -ace" },
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

        new Object[]{ "com.example.component_ref.FinalModel", "@ComponentRef target must not be final" },
        new Object[]{ "com.example.component_ref.StaticModel", "@ComponentRef target must not be static" },
        new Object[]{ "com.example.component_ref.PrivateModel", "@ComponentRef target must not be private" },
        new Object[]{ "com.example.component_ref.VoidModel", "@ComponentRef target must return a value" },
        new Object[]{ "com.example.component_ref.BadTypeModel",
                      "Method annotated with @ComponentRef must return an instance of org.realityforge.arez.Component" },
        new Object[]{ "com.example.component_ref.ThrowsExceptionModel",
                      "@ComponentRef target must not throw any exceptions" },
        new Object[]{ "com.example.component_ref.DuplicateModel",
                      "@ComponentRef target duplicates existing method named getComponent" },
        new Object[]{ "com.example.component_ref.ParametersModel", "@ComponentRef target must not have any parameters" },

        new Object[]{ "com.example.context_ref.FinalModel", "@ContextRef target must not be final" },
        new Object[]{ "com.example.context_ref.StaticModel", "@ContextRef target must not be static" },
        new Object[]{ "com.example.context_ref.PrivateModel", "@ContextRef target must not be private" },
        new Object[]{ "com.example.context_ref.VoidModel", "@ContextRef target must return a value" },
        new Object[]{ "com.example.context_ref.BadTypeModel",
                      "Method annotated with @ContextRef must return an instance of org.realityforge.arez.ArezContext" },
        new Object[]{ "com.example.context_ref.ThrowsExceptionModel",
                      "@ContextRef target must not throw any exceptions" },
        new Object[]{ "com.example.context_ref.DuplicateModel",
                      "@ContextRef target duplicates existing method named getContext" },
        new Object[]{ "com.example.context_ref.ParametersModel", "@ContextRef target must not have any parameters" },

        new Object[]{ "com.example.observable_ref.BadNameModel",
                      "Method annotated with @ObservableRef specified invalid name -ace" },
        new Object[]{ "com.example.observable_ref.BadReturnTypeModel",
                      "Method annotated with @ObservableRef must return an instance of org.realityforge.arez.Observable" },
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
                      "Method annotated with @ComputedValueRef specified invalid name -ace" },
        new Object[]{ "com.example.computed_value_ref.BadReturnTypeModel",
                      "Method annotated with @ComputedValueRef must return an instance of org.realityforge.arez.ComputedValue" },
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
                      "Method annotated with @ObserverRef specified invalid name -ace" },
        new Object[]{ "com.example.observer_ref.BadReturnTypeModel",
                      "Method annotated with @ObserverRef must return an instance of org.realityforge.arez.Observer" },
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
                      "Method annotated with @ObserverRef must return an instance of org.realityforge.arez.Observer" },

        new Object[]{ "com.example.name_duplicates.ActionDuplicatesObservableNameModel",
                      "Method annotated with @Action specified name field that duplicates @Observable defined by method getField" },
        new Object[]{ "com.example.on_activate.OnActivateNoComputedModel",
                      "@OnActivate exists but there is no corresponding @Computed" },
        new Object[]{ "com.example.on_activate.OnActivateBadNameModel",
                      "@OnActivate as does not match on[Name]Activate pattern. Please specify name." },
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
        new Object[]{ "com.example.on_deactivate.OnDeactivateNoComputedModel",
                      "@OnDeactivate exists but there is no corresponding @Computed" },
        new Object[]{ "com.example.on_deactivate.OnDeactivateBadNameModel",
                      "@OnDeactivate as does not match on[Name]Deactivate pattern. Please specify name." },
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
        new Object[]{ "com.example.on_stale.OnStaleNoComputedModel",
                      "@OnStale exists but there is no corresponding @Computed" },
        new Object[]{ "com.example.on_stale.OnStaleBadNameModel",
                      "@OnStale as does not match on[Name]Stale pattern. Please specify name." },
        new Object[]{ "com.example.on_stale.OnStalePrivateModel", "@OnStale target must not be private" },
        new Object[]{ "com.example.on_stale.OnStaleStaticModel", "@OnStale target must not be static" },
        new Object[]{ "com.example.on_stale.OnStaleParametersModel", "@OnStale target must not have any parameters" },
        new Object[]{ "com.example.on_stale.OnStaleReturnValueModel", "@OnStale target must not return a value" },
        new Object[]{ "com.example.on_stale.OnStaleThrowsExceptionModel",
                      "@OnStale target must not throw any exceptions" },
        new Object[]{ "com.example.on_stale.OnStaleDuplicateModel",
                      "@OnStale target duplicates existing method named foo" },
        new Object[]{ "com.example.on_dispose.OnDisposeNoComputedModel",
                      "@OnDispose exists but there is no corresponding @Computed" },
        new Object[]{ "com.example.on_dispose.OnDisposeBadNameModel",
                      "@OnDispose as does not match on[Name]Dispose pattern. Please specify name." },
        new Object[]{ "com.example.on_dispose.OnDisposePrivateModel", "@OnDispose target must not be private" },
        new Object[]{ "com.example.on_dispose.OnDisposeStaticModel", "@OnDispose target must not be static" },
        new Object[]{ "com.example.on_dispose.OnDisposeParametersModel",
                      "@OnDispose target must not have any parameters" },
        new Object[]{ "com.example.on_dispose.OnDisposeReturnValueModel", "@OnDispose target must not return a value" },
        new Object[]{ "com.example.on_dispose.OnDisposeThrowsExceptionModel",
                      "@OnDispose target must not throw any exceptions" },
        new Object[]{ "com.example.on_dispose.OnDisposeDuplicateModel",
                      "@OnDispose target duplicates existing method named foo" },
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
        new Object[]{ "com.example.tracked.TrackedFinalModel", "@Track target must not be final" },
        new Object[]{ "com.example.tracked.TrackedNotStaticModel", "@Track target must not be static" },
        new Object[]{ "com.example.tracked.TrackedNotPrivateModel", "@Track target must not be private" },
        new Object[]{ "com.example.tracked.TrackedMissingOnDepsChanged",
                      "@Track target has no corresponding @OnDepsChanged that could be automatically determined" },
        new Object[]{ "com.example.tracked.TrackedBadNameModel",
                      "Method annotated with @Track specified invalid name -ace" },
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
                      "Method annotated with @OnDepsChanged specified invalid name -ace" },
        new Object[]{ "com.example.on_deps_updated.OnDepsChangedNoTracked",
                      "@OnDepsChanged target has no corresponding @Track that could be automatically determined" },
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
                      "Method annotated with @Observable specified invalid name -ace" },
        new Object[]{ "com.example.observable.BadObservableName2Model",
                      "Method annotated with @Observable specified invalid name ace-" },
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
                      "Class annotated with @Repository defined an extension that has a non default method. Extension: com.example.repository.RepositoryExtensionHasAbstractMethod.Foo Method: other(int)" },
        new Object[]{ "com.example.repository.RepositoryBadName",
                      "Class annotated with @Repository specified an invalid name -abc" }
      };
  }

  @Test( dataProvider = "failedCompiles" )
  public void processFailedCompile( @Nonnull final String classname, @Nonnull final String errorMessageFragment )
    throws Exception
  {
    assertFailedCompile( classname, errorMessageFragment );
  }
}
