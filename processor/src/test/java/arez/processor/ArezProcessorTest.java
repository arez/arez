package arez.processor;

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
        new Object[]{ "com.example.action.NoReportResultActionModel", false, false, false },
        new Object[]{ "com.example.action.NoVerifyActionModel", false, false, false },
        new Object[]{ "com.example.action.ReadOnlyActionModel", false, false, false },
        new Object[]{ "com.example.action.RequireEnvironmentActionModel", false, false, false },
        new Object[]{ "com.example.action.RequiresNewTxTypeActionModel", false, false, false },
        new Object[]{ "com.example.action.RequiresTxTypeActionModel", false, false, false },
        new Object[]{ "com.example.action.BasicFunctionActionModel", false, false, false },
        new Object[]{ "com.example.action.BasicActionModel", false, false, false },
        new Object[]{ "com.example.cascade_dispose.ComponentCascadeDisposeModel", false, false, false },
        new Object[]{ "com.example.cascade_dispose.ComponentCascadeDisposeMethodModel", false, false, false },
        new Object[]{ "com.example.cascade_dispose.DisposableCascadeDisposeModel", false, false, false },
        new Object[]{ "com.example.cascade_dispose.DisposableCascadeDisposeMethodModel", false, false, false },
        new Object[]{ "com.example.component.AnnotatedConcreteModel", false, false, false },
        new Object[]{ "com.example.component.PublicCtorNonPublicModel", false, false, false },
        new Object[]{ "com.example.collections.AbstractCollectionObservableModel", false, false, false },
        new Object[]{ "com.example.collections.AbstractListObservableModel", false, false, false },
        new Object[]{ "com.example.collections.AbstractMapObservableModel", false, false, false },
        new Object[]{ "com.example.collections.AbstractNonnullCollectionObservableModel", false, false, false },
        new Object[]{ "com.example.collections.AbstractNonnullListObservableModel", false, false, false },
        new Object[]{ "com.example.collections.AbstractNonnullMapObservableModel", false, false, false },
        new Object[]{ "com.example.collections.AbstractNonnullSetObservableModel", false, false, false },
        new Object[]{ "com.example.collections.AbstractSetObservableModel", false, false, false },
        new Object[]{ "com.example.collections.ComputedCollectionModel", false, false, false },
        new Object[]{ "com.example.collections.ComputedCollectionWithHooksModel", false, false, false },
        new Object[]{ "com.example.collections.ComputedKeepAliveListModel", false, false, false },
        new Object[]{ "com.example.collections.ComputedListModel", false, false, false },
        new Object[]{ "com.example.collections.ComputedMapModel", false, false, false },
        new Object[]{ "com.example.collections.ComputedNonnullCollectionModel", false, false, false },
        new Object[]{ "com.example.collections.ComputedNonnullListModel", false, false, false },
        new Object[]{ "com.example.collections.ComputedNonnullMapModel", false, false, false },
        new Object[]{ "com.example.collections.ComputedNonnullSetModel", false, false, false },
        new Object[]{ "com.example.collections.ComputedSetModel", false, false, false },
        new Object[]{ "com.example.collections.ObservableCollectionModel", false, false, false },
        new Object[]{ "com.example.collections.ObservableListModel", false, false, false },
        new Object[]{ "com.example.collections.ObservableMapModel", false, false, false },
        new Object[]{ "com.example.collections.ObservableNonnullCollectionModel", false, false, false },
        new Object[]{ "com.example.collections.ObservableNonnullListModel", false, false, false },
        new Object[]{ "com.example.collections.ObservableNonnullMapModel", false, false, false },
        new Object[]{ "com.example.collections.ObservableNonnullSetModel", false, false, false },
        new Object[]{ "com.example.collections.ObservableNoSettersModel", false, false, false },
        new Object[]{ "com.example.collections.ObservableSetModel", false, false, false },
        new Object[]{ "com.example.component.DisposeOnDeactivateModel", false, false, false },
        new Object[]{ "com.example.component.NoRequireEqualsModel", false, false, false },
        new Object[]{ "com.example.component.NotObservableModel", false, false, false },
        new Object[]{ "com.example.component_id.BooleanComponentId", false, false, false },
        new Object[]{ "com.example.component_id.BooleanComponentIdRequireEquals", false, false, false },
        new Object[]{ "com.example.component_id.ByteComponentId", false, false, false },
        new Object[]{ "com.example.component_id.ByteComponentIdRequireEquals", false, false, false },
        new Object[]{ "com.example.component_id.CharComponentId", false, false, false },
        new Object[]{ "com.example.component_id.CharComponentIdRequireEquals", false, false, false },
        new Object[]{ "com.example.component_id.ComponentIdOnModel", false, false, false },
        new Object[]{ "com.example.component_id.DoubleComponentId", false, false, false },
        new Object[]{ "com.example.component_id.DoubleComponentIdRequireEquals", false, false, false },
        new Object[]{ "com.example.component_id.FloatComponentId", false, false, false },
        new Object[]{ "com.example.component_id.FloatComponentIdRequireEquals", false, false, false },
        new Object[]{ "com.example.component_id.IntComponentId", false, false, false },
        new Object[]{ "com.example.component_id.IntComponentIdRequireEquals", false, false, false },
        new Object[]{ "com.example.component_id.LongComponentId", false, false, false },
        new Object[]{ "com.example.component_id.LongComponentIdRequireEquals", false, false, false },
        new Object[]{ "com.example.component_id.ObjectComponentId", false, false, false },
        new Object[]{ "com.example.component_id.ObjectComponentIdRequireEquals", false, false, false },
        new Object[]{ "com.example.component_id.ShortComponentId", false, false, false },
        new Object[]{ "com.example.component_id.ShortComponentIdRequireEquals", false, false, false },
        new Object[]{ "com.example.component_id.ComponentIdOnSingletonModel", false, false, false },
        new Object[]{ "com.example.component_id_ref.BasicModel", false, false, false },
        new Object[]{ "com.example.component_id_ref.CombinedWithComponentIdModel", false, false, false },
        new Object[]{ "com.example.component_id_ref.NonIntTypeModel", false, false, false },
        new Object[]{ "com.example.component_name_ref.ComponentNameModel", false, false, false },
        new Object[]{ "com.example.component_name_ref.ComponentTypeNameModel", false, false, false },
        new Object[]{ "com.example.component_name_ref.ComponentTypeNameAloneOnSingletonModel", false, false, false },
        new Object[]{ "com.example.component_name_ref.ComponentNameOnSingletonModel", false, false, false },
        new Object[]{ "com.example.component_ref.AnnotatedComponent", false, false, false },
        new Object[]{ "com.example.component_ref.SimpleComponent", false, false, false },
        new Object[]{ "com.example.component_ref.ProtectedAccessComponent", false, false, false },
        new Object[]{ "com.example.computed.ArezOrNoneDependenciesComputedModel", false, false, false },
        new Object[]{ "com.example.computed.ComputedWithNameVariationsModel", false, false, false },
        new Object[]{ "com.example.computed.HighestPriorityComputedModel", false, false, false },
        new Object[]{ "com.example.computed.HighPriorityComputedModel", false, false, false },
        new Object[]{ "com.example.computed.NormalPriorityComputedModel", false, false, false },
        new Object[]{ "com.example.computed.LowestPriorityComputedModel", false, false, false },
        new Object[]{ "com.example.computed.LowPriorityComputedModel", false, false, false },
        new Object[]{ "com.example.computed.NonArezDependenciesComputedModel", false, false, false },
        new Object[]{ "com.example.computed.ComputedWithHooksModel", false, false, false },
        new Object[]{ "com.example.computed.BasicComputedModel", false, false, false },
        new Object[]{ "com.example.computed.KeepAliveComputedModel", false, false, false },
        new Object[]{ "com.example.computed.ObserveLowerPriorityComputedModel", false, false, false },
        new Object[]{ "com.example.computed.RequireEnvironmentComputedModel", false, false, false },
        new Object[]{ "com.example.computed.ScheduleDeferredKeepAliveComputedModel", false, false, false },
        new Object[]{ "com.example.computed.TypeParametersModel", false, false, false },
        new Object[]{ "com.example.computed_value_ref.DefaultRefNameModel", false, false, false },
        new Object[]{ "com.example.computed_value_ref.NonStandardNameModel", false, false, false },
        new Object[]{ "com.example.computed_value_ref.RawComputedValueModel", false, false, false },
        new Object[]{ "com.example.context_ref.AnnotatedComponent", false, false, false },
        new Object[]{ "com.example.context_ref.SimpleComponent", false, false, false },
        new Object[]{ "com.example.context_ref.ProtectedAccessComponent", false, false, false },
        new Object[]{ "com.example.component_dependency.AbstractObservableDependency", false, false, false },
        new Object[]{ "com.example.component_dependency.BasicDependencyModel", false, false, false },
        new Object[]{ "com.example.component_dependency.BasicFieldDependencyModel", false, false, false },
        new Object[]{ "com.example.component_dependency.CascadeDependencyModel", false, false, false },
        new Object[]{ "com.example.component_dependency.CascadeFieldDependencyModel", false, false, false },
        new Object[]{ "com.example.component_dependency.ComplexDependencyModel", false, false, false },
        new Object[]{ "com.example.component_dependency.ComplexDependencyWithCustomNameMethodModel",
                      false,
                      false,
                      false },
        new Object[]{ "com.example.component_dependency.ComponentDependencyModel", false, false, false },
        new Object[]{ "com.example.component_dependency.ComponentFieldDependencyModel", false, false, false },
        new Object[]{ "com.example.component_dependency.NonnullAbstractObservableDependency", false, false, false },
        new Object[]{ "com.example.component_dependency.NonnullFieldDependencyModel", false, false, false },
        new Object[]{ "com.example.component_dependency.NonnullObservableDependency", false, false, false },
        new Object[]{ "com.example.component_dependency.ObservableDependency", false, false, false },
        new Object[]{ "com.example.component_dependency.ObservablePairAnnotatedDependency", false, false, false },
        new Object[]{ "com.example.component_dependency.ScheduleDeferredDependencyModel", false, false, false },
        new Object[]{ "com.example.component_dependency.SetNullObservableDependency", false, false, false },
        new Object[]{ "com.example.deprecated.DeprecatedActionModel", false, false, false },
        new Object[]{ "com.example.deprecated.DeprecatedObservedModel", false, false, false },
        new Object[]{ "com.example.deprecated.DeprecatedComputedModel1", false, false, false },
        new Object[]{ "com.example.deprecated.DeprecatedComputedModel2", false, false, false },
        new Object[]{ "com.example.deprecated.DeprecatedComputedModel3", false, false, false },
        new Object[]{ "com.example.deprecated.DeprecatedComputedModel4", false, false, false },
        new Object[]{ "com.example.deprecated.DeprecatedMemoizeModel", false, false, false },
        new Object[]{ "com.example.deprecated.DeprecatedObservableModel1", false, false, false },
        new Object[]{ "com.example.deprecated.DeprecatedObservableModel2", false, false, false },
        new Object[]{ "com.example.deprecated.DeprecatedPostConstructModel", false, false, false },
        new Object[]{ "com.example.deprecated.DeprecatedObservedModel1", false, false, false },
        new Object[]{ "com.example.deprecated.DeprecatedObservedModel2", false, false, false },
        new Object[]{ "com.example.deprecated.DeprecatedObservedModel3", false, false, false },
        new Object[]{ "com.example.deprecated.DeprecatedObservedModel4", false, false, false },
        new Object[]{ "com.example.dispose_trackable.DisposeTrackableModel", false, false, false },
        new Object[]{ "com.example.dispose_trackable.NoDisposeTrackableModel", false, false, false },
        new Object[]{ "com.example.id.ComponentIdExample", false, false, false },
        new Object[]{ "com.example.id.RepositoryExample", false, true, true },
        new Object[]{ "com.example.id.RequireIdDisable", false, false, false },
        new Object[]{ "com.example.id.RequireIdEnable", false, false, false },
        new Object[]{ "com.example.inject.BasicInjectModel", false, false, false },
        new Object[]{ "com.example.inject.DefaultCtorModel", false, false, false },
        new Object[]{ "com.example.inject.MultipleArgsModel", false, false, false },
        new Object[]{ "com.example.inject.NoInjectModel", false, false, false },
        new Object[]{ "com.example.inject.ScopedButNoDaggerModel", false, false, false },
        new Object[]{ "com.example.inject.ScopedInjectModel", true, false, false },
        new Object[]{ "com.example.inverse.CustomNamesInverseModel", false, false, false },
        new Object[]{ "com.example.inverse.DefaultMultiplicityInverseModel", false, false, false },
        new Object[]{ "com.example.inverse.DisableInverseModel", false, false, false },
        new Object[]{ "com.example.inverse.NonGetterInverseModel", false, false, false },
        new Object[]{ "com.example.inverse.NonObservableCollectionInverseModel", false, false, false },
        new Object[]{ "com.example.inverse.NonObservableNullableManyReferenceModel", false, false, false },
        new Object[]{ "com.example.inverse.NonObservableNullableOneReferenceModel", false, false, false },
        new Object[]{ "com.example.inverse.NonObservableNullableZeroOrOneReferenceModel", false, false, false },
        new Object[]{ "com.example.inverse.ObservableCollectionInverseModel", false, false, false },
        new Object[]{ "com.example.inverse.ObservableListInverseModel", false, false, false },
        new Object[]{ "com.example.inverse.ObservableManyReferenceModel", false, false, false },
        new Object[]{ "com.example.inverse.ObservableOneReferenceModel", false, false, false },
        new Object[]{ "com.example.inverse.ObservableReferenceInverseModel", false, false, false },
        new Object[]{ "com.example.inverse.ObservableSetInverseModel", false, false, false },
        new Object[]{ "com.example.inverse.ObservableZeroOrOneReferenceModel", false, false, false },
        new Object[]{ "com.example.inverse.OneMultiplicityInverseModel", false, false, false },
        new Object[]{ "com.example.inverse.ZeroOrOneMultiplicityInverseModel", false, false, false },
        new Object[]{ "com.example.memoize.BasicMemoizeModel", false, false, false },
        new Object[]{ "com.example.memoize.CustomDepTypeMemoizeModel", false, false, false },
        new Object[]{ "com.example.memoize.CustomPriorityMemoizeModel", false, false, false },
        new Object[]{ "com.example.memoize.LocalTypeParamMemoizeModel", false, false, false },
        new Object[]{ "com.example.memoize.TypeParamMemoizeModel", false, false, false },
        new Object[]{ "com.example.observable.AbstractNonPrimitiveObservablesModel", false, false, false },
        new Object[]{ "com.example.observable.AbstractObservablesModel", false, false, false },
        new Object[]{ "com.example.observable.GenericObservableModel", false, false, false },
        new Object[]{ "com.example.observable.InitializerAndConstructorParamNameCollisionModel", false, false, false },
        new Object[]{ "com.example.observable.NullableInitializerModel", false, false, false },
        new Object[]{ "com.example.observable.ObservableWithNoSetter", false, false, false },
        new Object[]{ "com.example.observable.ReadOutsideTransactionObservableModel", false, false, false },
        new Object[]{ "com.example.observable.SetterAlwaysMutatesFalseObjectValue", false, false, false },
        new Object[]{ "com.example.observable.SetterAlwaysMutatesFalsePrimitiveValue", false, false, false },
        new Object[]{ "com.example.observable.WildcardGenericObservableModel", false, false, false },
        new Object[]{ "com.example.observable.WriteOutsideTransactionObservablesModel", false, false, false },
        new Object[]{ "com.example.observable.WriteOutsideTransactionThrowingObservablesModel", false, false, false },
        new Object[]{ "com.example.observable.AbstractNonPrimitiveNonnullObservablesModel", false, false, false },
        new Object[]{ "com.example.observable.AbstractPrimitiveObservablesWithInitializerModel", false, false, false },
        new Object[]{ "com.example.observable_value_ref.DefaultRefNameModel", false, false, false },
        new Object[]{ "com.example.observable_value_ref.GenericObservableRefModel", false, false, false },
        new Object[]{ "com.example.observable_value_ref.NonStandardNameModel", false, false, false },
        new Object[]{ "com.example.observable_value_ref.RawObservableModel", false, false, false },
        new Object[]{ "com.example.observe.BasicObserveModel", false, false, false },
        new Object[]{ "com.example.observe.NestedActionsAllowedObserveModel", false, false, false },
        new Object[]{ "com.example.observe.HighestPriorityObserveModel", false, false, false },
        new Object[]{ "com.example.observe.HighPriorityObserveModel", false, false, false },
        new Object[]{ "com.example.observe.LowestPriorityObserveModel", false, false, false },
        new Object[]{ "com.example.observe.LowPriorityObserveModel", false, false, false },
        new Object[]{ "com.example.observe.NormalPriorityObserveModel", false, false, false },
        new Object[]{ "com.example.observe.ObserveLowerPriorityObserveModel", false, false, false },
        new Object[]{ "com.example.observe.ReadWriteObserveModel", false, false, false },
        new Object[]{ "com.example.observe.ScheduleAfterConstructedModel", false, false, false },
        new Object[]{ "com.example.observe.ScheduleDeferredModel", false, false, false },
        new Object[]{ "com.example.observe.ArezOrNoneDependenciesModel", false, false, false },
        new Object[]{ "com.example.observe.BasicTrackedModel", false, false, false },
        new Object[]{ "com.example.observe.BasicTrackedWithExceptionsModel", false, false, false },
        new Object[]{ "com.example.observe.NestedActionsAllowedTrackedModel", false, false, false },
        new Object[]{ "com.example.observe.NonArezDependenciesModel", false, false, false },
        new Object[]{ "com.example.observe.DeriveFinalOnDepsChangedModel", false, false, false },
        new Object[]{ "com.example.observe.DeriveOnDepsChangedModel", false, false, false },
        new Object[]{ "com.example.observe.DeriveTrackedModel", false, false, false },
        new Object[]{ "com.example.observe.EnvironmentRequiredTrackedModel", false, false, false },
        new Object[]{ "com.example.observe.HighestPriorityTrackedModel", false, false, false },
        new Object[]{ "com.example.observe.HighPriorityTrackedModel", false, false, false },
        new Object[]{ "com.example.observe.NormalPriorityTrackedModel", false, false, false },
        new Object[]{ "com.example.observe.LowestPriorityTrackedModel", false, false, false },
        new Object[]{ "com.example.observe.LowPriorityTrackedModel", false, false, false },
        new Object[]{ "com.example.observe.NoReportParametersModel", false, false, false },
        new Object[]{ "com.example.observe.NoReportResultModel", false, false, false },
        new Object[]{ "com.example.observe.ObserveLowerPriorityTrackedModel", false, false, false },
        new Object[]{ "com.example.observe.ProtectedAccessTrackedModel", false, false, false },
        new Object[]{ "com.example.observe.TrackedAllTypesModel", false, false, false },
        new Object[]{ "com.example.observer_ref.CustomNameRefOnObservedModel1", false, false, false },
        new Object[]{ "com.example.observer_ref.CustomNameRefOnObservedModel2", false, false, false },
        new Object[]{ "com.example.observer_ref.RefOnObservedModel1", false, false, false },
        new Object[]{ "com.example.observer_ref.RefOnBothModel", false, false, false },
        new Object[]{ "com.example.observer_ref.RefOnObservedModel2", false, false, false },
        new Object[]{ "com.example.overloaded_names.OverloadedActions", false, false, false },
        new Object[]{ "com.example.post_construct.PostConstructModel", false, false, false },
        new Object[]{ "com.example.reference.CustomNameReferenceModel", false, false, false },
        new Object[]{ "com.example.reference.EagerLoadNulableObservableReferenceModel", false, false, false },
        new Object[]{ "com.example.reference.EagerLoadObservableReferenceModel", false, false, false },
        new Object[]{ "com.example.reference.EagerLoadReferenceModel", false, false, false },
        new Object[]{ "com.example.reference.EagerObservableReadOutsideTransactionReferenceModel",
                      false,
                      false,
                      false },
        new Object[]{ "com.example.reference.ExplicitLoadObservableReferenceModel", false, false, false },
        new Object[]{ "com.example.reference.ExplicitLoadReferenceModel", false, false, false },
        new Object[]{ "com.example.reference.LazyLoadObservableReferenceModel", false, false, false },
        new Object[]{ "com.example.reference.LazyLoadReferenceModel", false, false, false },
        new Object[]{ "com.example.reference.LazyObservableReadOutsideTransactionReferenceModel", false, false, false },
        new Object[]{ "com.example.reference.NonJavabeanNameReferenceModel", false, false, false },
        new Object[]{ "com.example.reference.NonnullLazyLoadReferenceModel", false, false, false },
        new Object[]{ "com.example.reference.NonObservableReferenceModel", false, false, false },
        new Object[]{ "com.example.reference.NullableLazyLoadReferenceModel", false, false, false },
        new Object[]{ "com.example.reference.ObservableReferenceModel", false, false, false },
        new Object[]{ "com.example.repository.DaggerDisabledRepository", false, true, false },
        new Object[]{ "com.example.repository.DaggerEnabledRepository", false, true, true },
        new Object[]{ "com.example.repository.InjectEnabledRepository", false, true, true },
        new Object[]{ "com.example.repository.InjectDisabledRepository", false, false, false },
        new Object[]{ "com.example.repository.RepositoryWithAttachOnly", false, true, true },
        new Object[]{ "com.example.repository.RepositoryWithCreateOnly", false, true, true },
        new Object[]{ "com.example.repository.RepositoryWithCreateOrAttach", false, true, true },
        new Object[]{ "com.example.repository.RepositoryPreDisposeHook", false, true, true },
        new Object[]{ "com.example.repository.RepositoryWithDestroyAndDetach", false, true, true },
        new Object[]{ "com.example.repository.RepositoryWithDetachNone", false, true, true },
        new Object[]{ "com.example.repository.RepositoryWithDetachOnly", false, true, true },
        new Object[]{ "com.example.repository.RepositoryWithExplicitId", false, true, true },
        new Object[]{ "com.example.repository.RepositoryWithExplicitNonStandardId", false, true, true },
        new Object[]{ "com.example.repository.RepositoryWithImplicitId", false, true, true },
        new Object[]{ "com.example.repository.RepositoryWithInitializerModel", false, true, true },
        new Object[]{ "com.example.repository.RepositoryWithInitializerNameCollisionModel", false, true, true },
        new Object[]{ "com.example.repository.RepositoryWithMultipleCtors", false, true, true },
        new Object[]{ "com.example.repository.RepositoryWithMultipleInitializersModel", false, true, true },
        new Object[]{ "com.example.repository.RepositoryWithProtectedConstructor", false, true, true },
        new Object[]{ "com.example.repository.RepositoryWithSingleton", false, true, true },
        new Object[]{ "com.example.reserved_names.NonReservedNameModel", false, false, false },
        new Object[]{ "com.example.to_string.NoToStringPresent", false, false, false },
        new Object[]{ "com.example.to_string.ToStringPresent", false, false, false },
        new Object[]{ "com.example.type_access_levels.ReduceAccessLevelModel", false, false, false },
        new Object[]{ "com.example.verifiable.DisableVerifyModel", false, false, false },
        new Object[]{ "com.example.verifiable.EnableVerifyModel", false, false, false },
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
  public void processSuccessfulMultipleInverseWithSameTarget()
    throws Exception
  {
    final JavaFileObject source1 =
      fixture( "input/com/example/inverse/MultipleReferenceWithInverseWithSameTarget.java" );
    final String output1 = "expected/com/example/inverse/MultipleReferenceWithInverseWithSameTarget_Arez_RoleType.java";
    final String output2 =
      "expected/com/example/inverse/MultipleReferenceWithInverseWithSameTarget_Arez_RoleTypeGeneralisation.java";
    assertSuccessfulCompile( Collections.singletonList( source1 ), Arrays.asList( output1, output2 ) );
  }

  @Test
  public void processSuccessfulRepositoryIncludingExtension()
    throws Exception
  {
    final JavaFileObject source1 = fixture( "input/com/example/repository/CompleteRepositoryExample.java" );
    final JavaFileObject source2 =
      fixture( "input/com/example/repository/CompleteRepositoryExampleRepositoryExtension.java" );
    final String output1 = "expected/com/example/repository/Arez_CompleteRepositoryExample.java";
    final String output2 = "expected/com/example/repository/Arez_CompleteRepositoryExampleRepository.java";
    final String output3 = "expected/com/example/repository/CompleteRepositoryExampleRepository.java";
    final String output4 = "expected/com/example/repository/CompleteRepositoryExampleRepositoryDaggerModule.java";
    assertSuccessfulCompile( Arrays.asList( source1, source2 ), Arrays.asList( output1, output2, output3, output4 ) );
  }

  @Test
  public void processSuccessfulPackageAccessRepositoryIncludingExtension()
    throws Exception
  {
    final JavaFileObject source1 = fixture( "input/com/example/repository/PackageAccessRepositoryExample.java" );
    final JavaFileObject source2 =
      fixture( "input/com/example/repository/PackageAccessRepositoryExampleRepositoryExtension.java" );
    final String output1 = "expected/com/example/repository/Arez_PackageAccessRepositoryExample.java";
    final String output2 = "expected/com/example/repository/Arez_PackageAccessRepositoryExampleRepository.java";
    final String output3 = "expected/com/example/repository/PackageAccessRepositoryExampleRepository.java";
    final String output4 = "expected/com/example/repository/PackageAccessRepositoryExampleRepositoryDaggerModule.java";
    assertSuccessfulCompile( Arrays.asList( source1, source2 ), Arrays.asList( output1, output2, output3, output4 ) );
  }

  @Test
  public void processSuccessfulInheritedProtectedAccessInDifferentPackage()
    throws Exception
  {
    final JavaFileObject source1 =
      fixture( "input/com/example/observe/InheritProtectedAccessTrackedModel.java" );
    final JavaFileObject source2 =
      fixture( "input/com/example/observe/other/BaseModelProtectedAccess.java" );
    final String output = "expected/com/example/observe/Arez_InheritProtectedAccessTrackedModel.java";
    assertSuccessfulCompile( Arrays.asList( source1, source2 ), Collections.singletonList( output ) );
  }

  @Test
  public void processSuccessfulDependencyThatIsTransitivelyDisposeTrackable()
    throws Exception
  {
    final JavaFileObject source1 =
      fixture( "input/com/example/component_dependency/TransitivelyDisposeTrackableDependencyModel.java" );
    final JavaFileObject source2 =
      fixture( "input/com/example/component_dependency/MyDependentValue.java" );
    final String output =
      "expected/com/example/component_dependency/Arez_TransitivelyDisposeTrackableDependencyModel.java";
    assertSuccessfulCompile( Arrays.asList( source1, source2 ), Collections.singletonList( output ) );
  }

  @Test
  public void processSuccessfulBaseClassInDifferentPackage()
    throws Exception
  {
    final JavaFileObject source1 =
      fixture( "input/com/example/inheritance/CompleteModel.java" );
    final JavaFileObject source2 =
      fixture( "input/com/example/inheritance/other/BaseCompleteModel.java" );
    final JavaFileObject source3 =
      fixture( "input/com/example/inheritance/other/Element.java" );
    final String output = "expected/com/example/inheritance/Arez_CompleteModel.java";
    assertSuccessfulCompile( Arrays.asList( source1, source2, source3 ), Collections.singletonList( output ) );
  }

  @Test
  public void processSuccessfulReactArezGenericsScenario()
    throws Exception
  {
    final JavaFileObject source1 =
      fixture( "input/com/example/override_generics/BaseReactComponent.java" );
    final JavaFileObject source2 =
      fixture( "input/com/example/override_generics/ArezReactComponent.java" );
    final JavaFileObject source3 =
      fixture( "input/com/example/override_generics/MyArezReactComponent.java" );
    final JavaFileObject source4 =
      fixture( "input/com/example/override_generics/MyArezReactComponent_.java" );
    final String output = "expected/com/example/override_generics/Arez_MyArezReactComponent_.java";
    assertSuccessfulCompile( Arrays.asList( source1, source2, source3, source4 ), Collections.singletonList( output ) );
  }

  @Test
  public void processSuccessfulWhereAbstractMethodWithGenericParameterIsRefinedInMiddleComponent()
    throws Exception
  {
    final JavaFileObject source1 =
      fixture( "input/com/example/override_generics/BaseModel.java" );
    final JavaFileObject source2 =
      fixture( "input/com/example/override_generics/MiddleModel.java" );
    final JavaFileObject source3 =
      fixture( "input/com/example/override_generics/LeafModel.java" );
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
  public void processSuccessfulInverseInDifferentPackage()
    throws Exception
  {
    final JavaFileObject source1 =
      fixture( "input/com/example/inverse/PackageAccessWithDifferentPackageInverseModel.java" );
    final JavaFileObject source2 =
      fixture( "input/com/example/inverse/other/Element.java" );
    final String output = "expected/com/example/inverse/Arez_PackageAccessWithDifferentPackageInverseModel.java";
    assertSuccessfulCompile( Arrays.asList( source1, source2 ), Collections.singletonList( output ) );
  }

  @Test
  public void processSuccessfulToStringInPresent()
    throws Exception
  {
    final JavaFileObject source1 =
      fixture( "input/com/example/to_string/ToStringPresentInParent.java" );
    final JavaFileObject source2 = fixture( "input/com/example/to_string/ParentType.java" );
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
    final JavaFileObject source1 = fixture( "input/DefaultMethodsModel.java" );
    final JavaFileObject source2 = fixture( "input/MyAnnotatedInterface.java" );
    final String output1 = "expected/Arez_DefaultMethodsModel.java";
    assertSuccessfulCompile( Arrays.asList( source1, source2 ), Collections.singletonList( output1 ) );
  }

  @Test
  public void processSuccessfulWhereTypeResolvedInInheritanceHierarchy()
    throws Exception
  {
    final JavaFileObject source1 = fixture( "input/com/example/type_params/AbstractModel.java" );
    final JavaFileObject source2 = fixture( "input/com/example/type_params/MiddleModel.java" );
    final JavaFileObject source3 = fixture( "input/com/example/type_params/ConcreteModel.java" );
    final String output1 = "expected/com/example/type_params/Arez_ConcreteModel.java";
    assertSuccessfulCompile( Arrays.asList( source1, source2, source3 ), Collections.singletonList( output1 ) );
  }

  @Test
  public void processResolvedParameterizedType()
    throws Exception
  {
    final JavaFileObject source1 =
      fixture( "input/com/example/parameterized_type/ParentModel.java" );
    final JavaFileObject source2 =
      fixture( "input/com/example/parameterized_type/ResolvedModel.java" );
    final String output1 = "expected/com/example/parameterized_type/Arez_ResolvedModel.java";
    assertSuccessfulCompile( Arrays.asList( source1, source2 ), Collections.singletonList( output1 ) );
  }

  @Test
  public void processUnresolvedParameterizedType()
    throws Exception
  {
    final JavaFileObject source1 =
      fixture( "input/com/example/parameterized_type/ParentModel.java" );
    final JavaFileObject source2 =
      fixture( "input/com/example/parameterized_type/UnresolvedModel.java" );
    final String output1 = "expected/com/example/parameterized_type/Arez_UnresolvedModel.java";
    assertSuccessfulCompile( Arrays.asList( source1, source2 ), Collections.singletonList( output1 ) );
  }

  @Test
  public void processSuccessfulWhereGenericsRefinedAndActionsOverriddenHierarchy()
    throws Exception
  {
    final JavaFileObject source1 =
      fixture( "input/com/example/override_generics/GenericsBaseModel.java" );
    final JavaFileObject source2 =
      fixture( "input/com/example/override_generics/GenericsMiddleModel.java" );
    final JavaFileObject source3 =
      fixture( "input/com/example/override_generics/GenericsModel.java" );
    final String output1 = "expected/com/example/override_generics/Arez_GenericsModel.java";
    assertSuccessfulCompile( Arrays.asList( source1, source2, source3 ), Collections.singletonList( output1 ) );
  }

  @Test
  public void processSuccessfulWhereTraceInheritanceChain()
    throws Exception
  {
    final JavaFileObject source1 = fixture( "input/com/example/inheritance/BaseModel.java" );
    final JavaFileObject source2 = fixture( "input/com/example/inheritance/ParentModel.java" );
    final JavaFileObject source3 = fixture( "input/com/example/inheritance/MyModel.java" );
    final JavaFileObject source4 = fixture( "input/com/example/inheritance/MyInterface1.java" );
    final JavaFileObject source5 = fixture( "input/com/example/inheritance/MyInterface2.java" );
    final String output1 = "expected/com/example/inheritance/Arez_MyModel.java";
    assertSuccessfulCompile( Arrays.asList( source1, source2, source3, source4, source5 ),
                             Collections.singletonList( output1 ) );
  }

  @DataProvider( name = "failedCompiles" )
  public Object[][] failedCompiles()
  {
    return new Object[][]
      {
        new Object[]{ "com.example.reserved_names.DataPrefixReservedNameModel",
                      "Method defined on a class annotated by @ArezComponent uses a name with a prefix reserved by Arez" },
        new Object[]{ "com.example.reserved_names.DisposeReservedNameModel",
                      "Method defined on a class annotated by @ArezComponent uses a name reserved by Arez" },
        new Object[]{ "com.example.reserved_names.FieldPrefixReservedNameModel",
                      "Method defined on a class annotated by @ArezComponent uses a name with a prefix reserved by Arez" },
        new Object[]{ "com.example.reserved_names.FrameworkPrefixReservedNameModel",
                      "Method defined on a class annotated by @ArezComponent uses a name with a prefix reserved by Arez" },
        new Object[]{ "com.example.reserved_names.GetArezIdReservedNameModel",
                      "Method defined on a class annotated by @ArezComponent uses a name reserved by Arez" },
        new Object[]{ "com.example.reserved_names.IsDisposedReservedNameModel",
                      "Method defined on a class annotated by @ArezComponent uses a name reserved by Arez" },
        new Object[]{ "com.example.reserved_names.ObserveReservedNameModel",
                      "Method defined on a class annotated by @ArezComponent uses a name reserved by Arez" },

        new Object[]{ "com.example.action.AbstractActionModel", "@Action target must not be abstract" },
        new Object[]{ "com.example.action.BadActionName2Model",
                      "@Action target specified an invalid name 'ace-'. The name must be a valid java identifier." },
        new Object[]{ "com.example.action.BadActionNameModel",
                      "@Action target specified an invalid name 'assert'. The name must not be a java keyword." },
        new Object[]{ "com.example.action.DuplicateActionModel",
                      "Method annotated with @Action specified name ace that duplicates @Action defined by method setField" },
        new Object[]{ "com.example.action.PrivateActionModel", "@Action target must not be private" },
        new Object[]{ "com.example.action.StaticActionModel", "@Action target must not be static" },

        new Object[]{ "com.example.observed.ApplicationExecutorButNoOnDepsChangedModel",
                      "@Observe target defined parameter executor=APPLICATION but does not specify an @OnDepsChanged method." },
        new Object[]{ "com.example.observed.ArezExecutorOnDepsChangedButNoObserverRefModel",
                      "@Observe target with parameter executor=AREZ defined an @OnDepsChanged method but has not defined an @ObserverRef method and thus can neverschedule observer." },
        new Object[]{ "com.example.observed.NonArezDependenciesButNoObserverRefModel",
                      "@Observe target with parameter depType=AREZ_OR_EXTERNAL has not defined an @ObserverRef method and thus can not invoke reportStale()." },
        new Object[]{ "com.example.observed.ObservedAbstractModel", "@Observe target must not be abstract" },
        new Object[]{ "com.example.observed.ObservedBadNameModel",
                      "@Observe target specified an invalid name '-ace'. The name must be a valid java identifier." },
        new Object[]{ "com.example.observed.ObservedBadNameModel2",
                      "@Observe target specified an invalid name 'float'. The name must not be a java keyword." },
        new Object[]{ "com.example.observed.ObservedDuplicateModel",
                      "@Observe target duplicates existing method named foo" },
        new Object[]{ "com.example.observed.ObservedDuplicateModel2",
                      "Method annotated with @Computed specified name doStuff that duplicates @Observe defined by method foo" },
        new Object[]{ "com.example.observed.ObservedFinalModel", "@Observe target must not be final" },
        new Object[]{ "com.example.observed.ObservedParametersModel", "@Observe target must not have any parameters" },
        new Object[]{ "com.example.observed.ObservedPrivateModel", "@Observe target must not be private" },
        new Object[]{ "com.example.observed.ObservedPublicModel", "@Observe target must not be public" },
        new Object[]{ "com.example.observed.ObservedReturnsValueModel", "@Observe target must not return a value" },
        new Object[]{ "com.example.observed.ObservedStaticModel", "@Observe target must not be static" },
        new Object[]{ "com.example.observed.ObservedThrowsExceptionModel",
                      "@Observe target must not throw any exceptions" },
        new Object[]{ "com.example.observed.ReportResultArezExecutorModel",
                      "@Observe target must not specify reportResult parameter when executor=AREZ" },
        new Object[]{ "com.example.observed.ReportParametersArezExecutorModel",
                      "@Observe target must not specify reportParameters parameter when executor=AREZ" },

        new Object[]{ "com.example.cascade_dispose.AbstractMethodComponent",
                      "@CascadeDispose target must not be abstract" },
        new Object[]{ "com.example.cascade_dispose.BadType1Component",
                      "@CascadeDispose target must be assignable to arez.Disposable or a type annotated with @ArezComponent" },
        new Object[]{ "com.example.cascade_dispose.BadType1MethodComponent",
                      "@CascadeDispose target must return a type assignable to arez.Disposable or a type annotated with @ArezComponent" },
        new Object[]{ "com.example.cascade_dispose.BadType2Component",
                      "@CascadeDispose target must be assignable to arez.Disposable or a type annotated with @ArezComponent" },
        new Object[]{ "com.example.cascade_dispose.BadType2MethodComponent",
                      "@CascadeDispose target must return a type assignable to arez.Disposable or a type annotated with @ArezComponent" },
        new Object[]{ "com.example.cascade_dispose.BadType3Component",
                      "@CascadeDispose target must be assignable to arez.Disposable or a type annotated with @ArezComponent" },
        new Object[]{ "com.example.cascade_dispose.BadType3MethodComponent",
                      "@CascadeDispose target must return a type assignable to arez.Disposable or a type annotated with @ArezComponent" },
        new Object[]{ "com.example.cascade_dispose.NonFinalMethodComponent", "@CascadeDispose target must be final" },
        new Object[]{ "com.example.cascade_dispose.ParametersMethodComponent",
                      "@CascadeDispose target must not have any parameters" },
        new Object[]{ "com.example.cascade_dispose.PrivateComponent", "@CascadeDispose target must not be private" },
        new Object[]{ "com.example.cascade_dispose.PrivateMethodComponent",
                      "@CascadeDispose target must not be private" },
        new Object[]{ "com.example.cascade_dispose.StaticComponent", "@CascadeDispose target must not be static" },
        new Object[]{ "com.example.cascade_dispose.StaticMethodComponent",
                      "@CascadeDispose target must not be static" },
        new Object[]{ "com.example.cascade_dispose.ThrowsMethodComponent",
                      "@CascadeDispose target must not throw any exceptions" },

        new Object[]{ "com.example.component.ConcreteComponent",
                      "@ArezComponent target must be abstract unless the allowConcrete parameter is set to true" },
        new Object[]{ "com.example.component.DeferredButNoObservedModel",
                      "@ArezComponent target has specified the deferSchedule = true annotation parameter but has no methods annotated with @Observe" },
        new Object[]{ "com.example.component.ModelWithAbstractMethod",
                      "@ArezComponent target has an abstract method not implemented by framework. The method is named someMethod" },
        new Object[]{ "com.example.component.NonEmptyComponent",
                      "@ArezComponent target has specified allowEmpty = true but has methods annotated with @Action, @CascadeDispose, @Computed, @Memoize, @Observable, @Inverse, @Reference, @ComponentDependency or @Observe" },
        new Object[]{ "com.example.component.BadTypeComponent",
                      "@ArezComponent target specified an invalid type ''. The type must be a valid java identifier." },
        new Object[]{ "com.example.component.BadTypeComponent2",
                      "@ArezComponent target specified an invalid type 'long'. The type must not be a java keyword." },
        new Object[]{ "com.example.component.EmptyComponent",
                      "@ArezComponent target has no methods annotated with @Action, @CascadeDispose, @Computed, @Memoize, @Observable, @Inverse, @Reference, @ComponentDependency or @Observe" },
        new Object[]{ "com.example.component.EmptyTypeComponent",
                      "@ArezComponent target specified an invalid type ''. The type must be a valid java identifier." },
        new Object[]{ "com.example.component.EnumModel", "@ArezComponent target must be a class" },
        new Object[]{ "com.example.component.FinalModel", "@ArezComponent target must not be final" },
        new Object[]{ "com.example.component.InterfaceModel", "@ArezComponent target must be a class" },
        new Object[]{ "com.example.component.NonObservableWithDisposeOnDeactivateModel",
                      "@ArezComponent target has specified observable = DISABLE and disposeOnDeactivate = true which is not a valid combination" },
        new Object[]{ "com.example.component.NonStaticNestedModel",
                      "@ArezComponent target must not be a non-static nested class" },
        new Object[]{ "com.example.component.UnexpectedAbstractComponent",
                      "@ArezComponent target must be concrete if the allowConcrete parameter is set to true" },

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

        new Object[]{ "com.example.component_id_ref.BadType1Model",
                      "@ComponentIdRef target has a return type java.lang.String but no @ComponentId annotated method. The type is expected to be of type int." },
        new Object[]{ "com.example.component_id_ref.BadType2Model",
                      "@ComponentIdRef target has a return type java.lang.String and a @ComponentId annotated method with a return type java.lang.String. The types must match." },
        new Object[]{ "com.example.component_id_ref.ConcreteModel", "@ComponentIdRef target must be abstract" },
        new Object[]{ "com.example.component_id_ref.DuplicateModel",
                      "@ComponentIdRef target duplicates existing method named getId" },
        new Object[]{ "com.example.component_id_ref.FinalModel", "@ComponentIdRef target must not be final" },
        new Object[]{ "com.example.component_id_ref.NoReturnModel", "@ComponentIdRef target must return a value" },
        new Object[]{ "com.example.component_id_ref.ParametersModel",
                      "@ComponentIdRef target must not have any parameters" },
        new Object[]{ "com.example.component_id_ref.PrivateModel", "@ComponentIdRef target must not be private" },
        new Object[]{ "com.example.component_id_ref.StaticModel", "@ComponentIdRef target must not be static" },
        new Object[]{ "com.example.component_id_ref.ThrowsModel",
                      "@ComponentIdRef target must not throw any exceptions" },

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
        new Object[]{ "com.example.computed.BadStreamTypeModel",
                      "@Computed target must not return a value of type java.util.stream.Stream as the type is single use and thus does not make sense to cache as a computed value" },
        new Object[]{ "com.example.computed.ComputedThrowsExceptionModel",
                      "@Computed target must not throw any exceptions" },
        new Object[]{ "com.example.computed.DuplicateComputedModel",
                      "Method annotated with @Computed specified name ace that duplicates @Computed defined by method getX" },
        new Object[]{ "com.example.computed.FinalComputedModel", "@Computed target must not be final" },
        new Object[]{ "com.example.computed.MissingComputedValueRefModel",
                      "@Computed target specified depType = AREZ_OR_EXTERNAL but there is no associated @ComputedValueRef method." },
        new Object[]{ "com.example.computed.ParameterizedComputedModel",
                      "@Computed target must not have any parameters" },
        new Object[]{ "com.example.computed.PrivateComputedModel", "@Computed target must not be private" },
        new Object[]{ "com.example.computed.StaticComputedModel", "@Computed target must not be static" },
        new Object[]{ "com.example.computed.VoidComputedModel", "@Computed target must return a value" },

        new Object[]{ "com.example.memoized.AbstractMemoizeModel", "@Memoize target must not be abstract" },
        new Object[]{ "com.example.memoized.BadDepTypeMemoizeModel",
                      "@Memoize target specified an invalid depType od AREZ_OR_EXTERNAL." },
        new Object[]{ "com.example.memoized.BadName2MemoizeModel",
                      "@Memoize target specified an invalid name 'protected'. The name must not be a java keyword." },
        new Object[]{ "com.example.memoized.BadNameMemoizeModel",
                      "@Memoize target specified an invalid name '-ace'. The name must be a valid java identifier." },
        new Object[]{ "com.example.memoized.ThrowsExceptionMemoizeModel",
                      "@Memoize target must not throw any exceptions" },
        new Object[]{ "com.example.memoized.DuplicateMemoizeModel",
                      "Method annotated with @Memoize specified name method1 that duplicates @Memoize defined by method method1" },
        new Object[]{ "com.example.memoized.FinalMemoizeModel", "@Memoize target must not be final" },
        new Object[]{ "com.example.memoized.NoParamMemoizeModel", "@Memoize target must have parameters" },
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

        new Object[]{ "com.example.component_dependency.AbstractDependency",
                      "@ArezComponent target has an abstract method not implemented by framework. The method is named getTime" },
        new Object[]{ "com.example.component_dependency.BadTypeDependency",
                      "@ComponentDependency target must return an instance compatible with arez.component.DisposeTrackable or a type annotated with @ArezComponent(disposeTrackable=ENABLE)" },
        new Object[]{ "com.example.component_dependency.BadTypeFieldDependency",
                      "@ComponentDependency target must be an instance compatible with arez.component.DisposeTrackable or a type annotated with @ArezComponent(disposeTrackable=ENABLE)" },
        new Object[]{ "com.example.component_dependency.CascadeDisposeAndFieldDependency",
                      "Method can not be annotated with both @ComponentDependency and @CascadeDispose" },
        new Object[]{ "com.example.component_dependency.ComputedDependency",
                      "Method can not be annotated with both @Computed and @ComponentDependency" },
        new Object[]{ "com.example.component_dependency.NonFinalDependency",
                      "@ComponentDependency target must be final" },
        new Object[]{ "com.example.component_dependency.NonFinalFieldDependency",
                      "@ComponentDependency target must be final" },
        new Object[]{ "com.example.component_dependency.ParametersDependency",
                      "@ComponentDependency target must not have any parameters" },
        new Object[]{ "com.example.component_dependency.PrimitiveFieldDependency",
                      "@ComponentDependency target must be a non-primitive value" },
        new Object[]{ "com.example.component_dependency.PrimitiveReturnDependency",
                      "@ComponentDependency target must return a non-primitive value" },
        new Object[]{ "com.example.component_dependency.PrivateDependency",
                      "@ComponentDependency target must not be private" },
        new Object[]{ "com.example.component_dependency.SetNullBasicDependency",
                      "@ComponentDependency target defined an action of 'SET_NULL' but the dependency is not an observable so the annotation processor does not know how to set the value to null." },
        new Object[]{ "com.example.component_dependency.SetNullFieldDependency",
                      "@ComponentDependency target defined an action of 'SET_NULL' but the dependency is on a final field and can not be set to null." },
        new Object[]{ "com.example.component_dependency.SetNullObservableNoSetterDependency",
                      "@ComponentDependency target defined an action of 'SET_NULL' but the dependency is an observable with no setter defined so the annotation processor does not know how to set the value to null." },
        new Object[]{ "com.example.component_dependency.SetNullOnNonnullDependency",
                      "@ComponentDependency target defined an action of 'SET_NULL' but the setter is annotated with @javax.annotation.Nonnull." },
        new Object[]{ "com.example.component_dependency.StaticDependency",
                      "@ComponentDependency target must not be static" },
        new Object[]{ "com.example.component_dependency.StaticFieldDependency",
                      "@ComponentDependency target must not be static" },
        new Object[]{ "com.example.component_dependency.ThrowsDependency",
                      "@ComponentDependency target must not throw any exceptions" },
        new Object[]{ "com.example.component_dependency.VoidReturnDependency",
                      "@ComponentDependency target must return a value" },

        new Object[]{ "com.example.dispose_trackable.NoDisposeTrackableWithRepositoryModel",
                      "@ArezComponent target has specified the disposeTrackable = DISABLE annotation parameter but is also annotated with @Repository that requires disposeTrackable = ENABLE." },

        new Object[]{ "com.example.id.DisableIdAndComponentId",
                      "@ArezComponent target has specified the idRequired = DISABLE annotation parameter but also has annotated a method with @ComponentId that requires idRequired = ENABLE." },
        new Object[]{ "com.example.id.DisableIdAndComponentIdRef",
                      "@ArezComponent target has specified the idRequired = DISABLE annotation parameter but also has annotated a method with @ComponentIdRef that requires idRequired = ENABLE." },
        new Object[]{ "com.example.id.DisableIdAndRepository",
                      "@ArezComponent target has specified the idRequired = DISABLE annotation parameter but is also annotated with @Repository that requires idRequired = ENABLE." },

        new Object[]{ "com.example.inject.MultipleConstructorsModel",
                      "@ArezComponent specified inject parameter but has more than one constructor" },
        new Object[]{ "com.example.inject.MultipleConstructorsScopedModel",
                      "@ArezComponent target has specified a scope annotation but has more than one constructor and thus is not a candidate for injection" },
        new Object[]{ "com.example.inject.MultipleScopesModel",
                      "@ArezComponent target has specified multiple scope annotations: [javax.inject.Singleton, com.example.inject.MultipleScopesModel.MyScope]" },

        new Object[]{ "com.example.inverse.BadCollectionTypeInverseModel",
                      "@Inverse target expected to return a type annotated with arez.annotations.ArezComponent" },
        new Object[]{ "com.example.inverse.BadInverseName1InverseModel",
                      "@Reference target specified an invalid inverseName '-sxkw'. The inverseName must be a valid java identifier." },
        new Object[]{ "com.example.inverse.BadInverseName2InverseModel",
                      "@Reference target specified an invalid inverseName 'byte'. The inverseName must not be a java keyword." },
        new Object[]{ "com.example.inverse.BadInverseType1InverseModel",
                      "@Inverse target expected to find an associated @Reference annotation with a target type equal to com.example.inverse.BadInverseType1InverseModel.OtherEntity but the actual target type is com.example.inverse.BadInverseType1InverseModel.MyEntity" },
        new Object[]{ "com.example.inverse.BadMultiplicity1InverseModel",
                      "@Inverse target has a multiplicity of MANY but that associated @Reference has a multiplicity of ONE. The multiplicity must align." },
        new Object[]{ "com.example.inverse.BadMultiplicity2InverseModel",
                      "@Inverse target has a multiplicity of MANY but that associated @Reference has a multiplicity of ZERO_OR_ONE. The multiplicity must align." },
        new Object[]{ "com.example.inverse.BadMultiplicity3InverseModel",
                      "@Inverse target has a multiplicity of ONE but that associated @Reference has a multiplicity of ZERO_OR_ONE. The multiplicity must align." },
        new Object[]{ "com.example.inverse.BadMultiplicity4InverseModel",
                      "@Inverse target has a multiplicity of ONE but that associated @Reference has a multiplicity of MANY. The multiplicity must align." },
        new Object[]{ "com.example.inverse.BadMultiplicity5InverseModel",
                      "@Inverse target has a multiplicity of ZERO_OR_ONE but that associated @Reference has a multiplicity of ONE. The multiplicity must align." },
        new Object[]{ "com.example.inverse.BadMultiplicity6InverseModel",
                      "@Inverse target has a multiplicity of ZERO_OR_ONE but that associated @Reference has a multiplicity of MANY. The multiplicity must align." },
        new Object[]{ "com.example.inverse.BadName1InverseModel",
                      "@Inverse target specified an invalid name '-sss'. The name must be a valid java identifier." },
        new Object[]{ "com.example.inverse.BadName2InverseModel",
                      "@Inverse target specified an invalid name 'long'. The name must not be a java keyword." },
        new Object[]{ "com.example.inverse.BadReferenceMultiplicity1InverseModel",
                      "@Reference target has an inverseMultiplicity of ONE but that associated @Inverse has a multiplicity of MANY. The multiplicity must align." },
        new Object[]{ "com.example.inverse.BadReferenceMultiplicity2InverseModel",
                      "@Reference target has an inverseMultiplicity of ZERO_OR_ONE but that associated @Inverse has a multiplicity of MANY. The multiplicity must align." },
        new Object[]{ "com.example.inverse.BadReferenceMultiplicity3InverseModel",
                      "@Reference target has an inverseMultiplicity of MANY but that associated @Inverse has a multiplicity of ONE. The multiplicity must align." },
        new Object[]{ "com.example.inverse.BadReferenceMultiplicity4InverseModel",
                      "@Reference target has an inverseMultiplicity of ZERO_OR_ONE but that associated @Inverse has a multiplicity of ONE. The multiplicity must align." },
        new Object[]{ "com.example.inverse.BadReferenceMultiplicity5InverseModel",
                      "@Reference target has an inverseMultiplicity of MANY but that associated @Inverse has a multiplicity of ZERO_OR_ONE. The multiplicity must align." },
        new Object[]{ "com.example.inverse.BadReferenceMultiplicity6InverseModel",
                      "@Reference target has an inverseMultiplicity of ONE but that associated @Inverse has a multiplicity of ZERO_OR_ONE. The multiplicity must align." },
        new Object[]{ "com.example.inverse.BadReferenceName1InverseModel",
                      "@Inverse target specified an invalid referenceName '-sxkw'. The name must be a valid java identifier." },
        new Object[]{ "com.example.inverse.BadReferenceName2InverseModel",
                      "@Inverse target specified an invalid referenceName 'long'. The name must not be a java keyword." },
        new Object[]{ "com.example.inverse.BadReferenceType2InverseModel",
                      "@Reference target expected to find an associated @Inverse annotation with a name parameter equal to 'badReferenceType2InverseModel' on class com.example.inverse.BadReferenceType2InverseModel.MyEntity but is unable to locate a matching method." },
        new Object[]{ "com.example.inverse.BadReferenceTypeInverseModel",
                      "@Reference target expected to find an associated @Inverse annotation with a target type equal to com.example.inverse.BadReferenceTypeInverseModel but the actual target type is com.example.inverse.BadReferenceTypeInverseModel.OtherEntity" },
        new Object[]{ "com.example.inverse.BadType1InverseModel",
                      "@Inverse target expected to return a type annotated with arez.annotations.ArezComponent" },
        new Object[]{ "com.example.inverse.BadType2InverseModel",
                      "@Inverse target expected to return a type annotated with arez.annotations.ArezComponent" },
        new Object[]{ "com.example.inverse.BadType3InverseModel",
                      "@Inverse target expected to return a type annotated with arez.annotations.ArezComponent" },
        new Object[]{ "com.example.inverse.BadType4InverseModel",
                      "@Inverse target expected to be annotated with either javax.annotation.Nullable or javax.annotation.Nonnull" },
        new Object[]{ "com.example.inverse.ConcreteInverseModel", "@Inverse target must be abstract" },
        new Object[]{ "com.example.inverse.DuplicateInverseModel",
                      "@Inverse target defines duplicate inverse for name 'myEntity'. The other inverse is getMyEntity2()" },
        new Object[]{ "com.example.inverse.InitializerWithInverseModel",
                      "@Inverse target also specifies @Observable(initializer=ENABLE) but it is not valid to define an initializer for an inverse." },
        new Object[]{ "com.example.inverse.MissingInverseOnReferenceModel",
                      "@Inverse target found an associated @Reference on the method 'getCar' on type 'com.example.inverse.MissingInverseOnReferenceModel.Wheel' but the annotation has not configured an inverse." },
        new Object[]{ "com.example.inverse.MissingInverseReferenceModel",
                      "@Reference target expected to find an associated @Inverse annotation with a name parameter equal to 'missingInverseReferenceModels' on class com.example.inverse.MissingInverseReferenceModel.MyEntity but is unable to locate a matching method." },
        new Object[]{ "com.example.inverse.MissingReference1InverseModel",
                      "@Inverse target expected to find an associated @Reference annotation with a name parameter equal to 'missingReference1InverseModel' on class com.example.inverse.MissingReference1InverseModel.MyEntity but is unable to locate a matching method." },
        new Object[]{ "com.example.inverse.NoReturnInverseModel", "@Inverse target must return a value" },
        new Object[]{ "com.example.inverse.ParametersInverseModel", "@Inverse target must not have any parameters" },
        new Object[]{ "com.example.inverse.PrivateInverseModel", "@Inverse target must not be private" },
        new Object[]{ "com.example.inverse.ReferenceSpecifiesNonComponentInverseModel",
                      "@Reference target expected to return a type annotated with arez.annotations.ArezComponent if there is an inverse reference." },
        new Object[]{ "com.example.inverse.StaticInverseModel", "@Inverse target must not be static" },
        new Object[]{ "com.example.inverse.ThrowsInverseModel", "@Inverse target must not throw any exceptions" },
        new Object[]{ "com.example.inverse.UnexpectedInverseForReferenceModel",
                      "@Reference target has not configured an inverse but there is an associated @Inverse annotated method named 'getWheels' on type 'com.example.inverse.UnexpectedInverseForReferenceModel.Car'." },

        new Object[]{ "com.example.observable_value_ref.BadNameModel",
                      "@ObservableValueRef target specified an invalid name '-ace'. The name must be a valid java identifier." },
        new Object[]{ "com.example.observable_value_ref.BadNameModel2",
                      "@ObservableValueRef target specified an invalid name 'const'. The name must not be a java keyword." },
        new Object[]{ "com.example.observable_value_ref.BadReturnTypeModel",
                      "Method annotated with @ObservableValueRef must return an instance of arez.ObservableValue" },
        new Object[]{ "com.example.observable_value_ref.BadReturnTypeParameter2Model",
                      "@ObservableValueRef target has a type parameter of ? but @Observable method returns type of long" },
        new Object[]{ "com.example.observable_value_ref.BadReturnTypeParameterModel",
                      "@ObservableValueRef target has a type parameter of java.lang.String but @Observable method returns type of long" },
        new Object[]{ "com.example.observable_value_ref.DuplicateRefMethodModel",
                      "Method annotated with @ObservableValueRef defines duplicate ref accessor for observable named time" },
        new Object[]{ "com.example.observable_value_ref.FinalModel", "@ObservableValueRef target must not be final" },
        new Object[]{ "com.example.observable_value_ref.NonAbstractModel",
                      "@ObservableValueRef target must be abstract" },
        new Object[]{ "com.example.observable_value_ref.NonAlignedNameModel",
                      "Method annotated with @ObservableValueRef should specify name or be named according to the convention get[Name]Observable" },
        new Object[]{ "com.example.observable_value_ref.NoObservableModel",
                      "@ObservableValueRef target unable to be associated with an Observable property" },
        new Object[]{ "com.example.observable_value_ref.ParametersModel",
                      "@ObservableValueRef target must not have any parameters" },
        new Object[]{ "com.example.observable_value_ref.PrivateModel",
                      "@ObservableValueRef target must not be private" },
        new Object[]{ "com.example.observable_value_ref.StaticModel", "@ObservableValueRef target must not be static" },
        new Object[]{ "com.example.observable_value_ref.ThrowsExceptionModel",
                      "@ObservableValueRef target must not throw any exceptions" },

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
        new Object[]{ "com.example.observer_ref.ParametersModel", "@ObserverRef target must not have any parameters" },
        new Object[]{ "com.example.observer_ref.PrivateModel", "@ObserverRef target must not be private" },
        new Object[]{ "com.example.observer_ref.RefOnNeitherModel",
                      "@ObserverRef target defined observer named 'render' but no @Observe method with that name exists" },
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
        new Object[]{ "com.example.on_activate.OnActivateOnKeepAliveModel",
                      "@OnActivate exists for @Computed property that specified parameter keepAlive as true." },
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
        new Object[]{ "com.example.on_deactivate.OnDeactivateKeepAliveModel",
                      "@OnDeactivate exists for @Computed property that specified parameter keepAlive as true." },
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
        new Object[]{ "com.example.observable.SetterAlwaysMutatesFalseOnAbstractMethods",
                      "@Observable target defines setterAlwaysMutates = false but but has defined abstract getters and setters." },
        new Object[]{ "com.example.observable.SetterAlwaysMutatesFalseWhenNoSetter",
                      "@Observable target defines expectSetter = false setterAlwaysMutates = false but this is an invalid configuration." },
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

        new Object[]{ "com.example.reference.BadExpectSetterObservableReferenceModel",
                      "@ReferenceId added to @Observable method but expectSetter = false on property which is not compatible with @ReferenceId" },
        new Object[]{ "com.example.reference.BadName2ReferenceIdModel",
                      "@ReferenceId target specified an invalid name 'long'. The name must not be a java keyword." },
        new Object[]{ "com.example.reference.BadNameReferenceIdModel",
                      "@ReferenceId target specified an invalid name '-hello'. The name must be a valid java identifier." },
        new Object[]{ "com.example.reference.BadName3ReferenceIdModel",
                      "@ReferenceId target has not specified a name and does not follow the convention \"get[Name]Id\" or \"[name]Id\"" },
        new Object[]{ "com.example.reference.BadName2ReferenceModel",
                      "@Reference target specified an invalid name 'short'. The name must not be a java keyword." },
        new Object[]{ "com.example.reference.BadNameReferenceModel",
                      "@Reference target specified an invalid name '-meep-'. The name must be a valid java identifier." },
        new Object[]{ "com.example.reference.ConcreteReferenceModel", "@Reference target must be abstract" },
        new Object[]{ "com.example.reference.MissingReferenceIdReferenceModel",
                      "@Reference exists but there is no corresponding @ReferenceId" },
        new Object[]{ "com.example.reference.MissingReferenceReferenceIdModel",
                      "@ReferenceId exists but there is no corresponding @Reference" },
        new Object[]{ "com.example.reference.NoReturnReferenceIdModel", "@ReferenceId target must return a value" },
        new Object[]{ "com.example.reference.NoReturnReferenceModel", "@Reference target must return a value" },
        new Object[]{ "com.example.reference.ParameterReferenceIdModel",
                      "@ReferenceId target must not have any parameters" },
        new Object[]{ "com.example.reference.NoSetterObservableReferenceModel",
                      "@Reference exists but there is no corresponding @ReferenceId" },
        new Object[]{ "com.example.reference.ParameterReferenceModel",
                      "@Reference target must not have any parameters" },
        new Object[]{ "com.example.reference.PrivateReferenceIdModel", "@ReferenceId target must not be private" },
        new Object[]{ "com.example.reference.PrivateReferenceModel", "@Reference target must not be private" },
        new Object[]{ "com.example.reference.StaticReferenceIdModel", "@ReferenceId target must not be static" },
        new Object[]{ "com.example.reference.StaticReferenceModel", "@Reference target must not be static" },
        new Object[]{ "com.example.reference.ThrowsReferenceIdModel",
                      "@ReferenceId target must not throw any exceptions" },
        new Object[]{ "com.example.reference.ThrowsReferenceModel", "@Reference target must not throw any exceptions" },

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
                      "@OnDepsChanged target has no corresponding @Observe that could be automatically determined" },
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
        new Object[]{ "com.example.name_duplicates.ActionAndObservedMethodModel",
                      "Method can not be annotated with both @Action and @Observe" },
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
        new Object[]{ "com.example.name_duplicates.ComputedAndObservedMethodModel",
                      "Method can not be annotated with both @Observe and @Computed" },
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
                      "@Observable target defines expectSetter = false but is abstract. This is not compatible as there is no opportunity for the processor to generate the setter." },
        new Object[]{ "com.example.observable.AbstractGetterThrowsExceptionModel",
                      "@Observable property is abstract but the getter declares an exception." },
        new Object[]{ "com.example.observable.AbstractSetterThrowsExceptionModel",
                      "@Observable property is abstract but the setter declares an exception." },
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
                      "@Observable target should be a setter or getter" },
        new Object[]{ "com.example.observable.ExtraParameterGetterModel",
                      "@Observable target should be a setter or getter" },
        new Object[]{ "com.example.observable.DuplicateSetterModel",
                      "@Observable defines duplicate setter for observable named field" },
        new Object[]{ "com.example.observable.DuplicateGetterModel",
                      "@Observable defines duplicate getter for observable named field" },
        new Object[]{ "com.example.observable.StaticObservableGetterModel", "@Observable target must not be static" },
        new Object[]{ "com.example.observable.StaticObservableSetterModel", "@Observable target must not be static" },
        new Object[]{ "com.example.observable.InitializerOnConcreteGetterModel",
                      "@Observable target set initializer parameter to ENABLED but method is not abstract." },
        new Object[]{ "com.example.observable.InitializerOnConcreteSetterModel",
                      "@Observable target set initializer parameter to ENABLED but method is not abstract." },
        new Object[]{ "com.example.observable.InitializerParametersDisagreeModel",
                      "@Observable target set initializer parameter to value that differs from the paired observable method." },
        new Object[]{ "com.example.observable.FinalObservableGetterModel", "@Observable target must not be final" },
        new Object[]{ "com.example.observable.FinalObservableSetterModel", "@Observable target must not be final" },
        new Object[]{ "com.example.observable.TypeArgumentsOnObservableGetterModel",
                      "@Observable target defines type variables. Method level type parameters are not supported for observable values." },
        new Object[]{ "com.example.observable.TypeArgumentsOnObservableSetterModel",
                      "@Observable target defines type variables. Method level type parameters are not supported for observable values." },
        new Object[]{ "com.example.repository.RepositoryExtensionIsClass",
                      "Class annotated with @Repository defined an extension that is not an interface. Extension: com.example.repository.RepositoryExtensionIsClass.Foo" },
        new Object[]{ "com.example.repository.RepositoryExtensionNotInterface",
                      "Class annotated with @Repository defined an extension that is not an interface. Extension: com.example.repository.RepositoryExtensionNotInterface.Foo" },
        new Object[]{ "com.example.repository.RepositoryExtensionHasBadSelf",
                      "Class annotated with @Repository defined an extension that has a non default method. Extension: com.example.repository.RepositoryExtensionHasBadSelf.Foo Method: self(int)" },
        new Object[]{ "com.example.repository.RepositoryExtensionHasAbstractMethod",
                      "Class annotated with @Repository defined an extension that has a non default method. Extension: com.example.repository.RepositoryExtensionHasAbstractMethod.Foo Method: other(int)" },
        new Object[]{ "com.example.repository.RepositoryNotObservable",
                      "@ArezComponent target has specified observable = DISABLE and but is also annotated with the @Repository annotation which requires that the observable != DISABLE." },
        new Object[]{ "com.example.repository.SingletonAndRepository",
                      "@ArezComponent target is annotated with both the @arez.annotations.Repository annotation and the javax.inject.Singleton annotation which is an invalid combination." }
      };
  }

  @Test( dataProvider = "failedCompiles" )
  public void processFailedCompile( @Nonnull final String classname, @Nonnull final String errorMessageFragment )
    throws Exception
  {
    assertFailedCompile( classname, errorMessageFragment );
  }

  @DataProvider( name = "packageAccessElementInDifferentPackage" )
  public Object[][] packageAccessElementInDifferentPackage()
  {
    return new Object[][]
      {
        new Object[]{ "Action", "Action" },
        new Object[]{ "Observe", "Observe" },
        new Object[]{ "CascadeDispose", "CascadeDispose" },
        new Object[]{ "CascadeDispose", "CascadeDisposeMethod" },
        new Object[]{ "ComponentId", "ComponentId" },
        new Object[]{ "ComponentNameRef", "ComponentNameRef" },
        new Object[]{ "ComponentRef", "ComponentRef" },
        new Object[]{ "Computed", "Computed" },
        new Object[]{ "OnActivate", "OnActivate" },
        new Object[]{ "OnDeactivate", "OnDeactivate" },
        new Object[]{ "OnStale", "OnStale" },
        new Object[]{ "ComputedValueRef", "ComputedValueRef" },
        new Object[]{ "ContextRef", "ContextRef" },
        new Object[]{ "ComponentDependency", "ComponentDependency" },
        new Object[]{ "Observable", "Observable" },
        new Object[]{ "ObservableValueRef", "ObservableValueRef" },
        new Object[]{ "ObserverRef", "ObserverRef" },
        new Object[]{ "PostConstruct", "PostConstruct" },
        new Object[]{ "OnDepsChanged", "OnDepsChanged" }
      };
  }

  @Test( dataProvider = "packageAccessElementInDifferentPackage" )
  public void processFailedCompileInheritedPackageAccessInDifferentPackage( @Nonnull final String annotation,
                                                                            @Nonnull final String name )
    throws Exception
  {
    final JavaFileObject source1 =
      fixture( "bad_input/com/example/package_access/other/Base" + name + "Model.java" );
    final JavaFileObject source2 =
      fixture( "bad_input/com/example/package_access/" + name + "Model.java" );
    assertFailedCompileResource( Arrays.asList( source1, source2 ),
                                 "@" + annotation + " target must not be package access if " +
                                 "the method is in a different package from the @ArezComponent" );
  }

  @Test
  public void processFailedCompileInheritedPackageAccessInDifferentPackageWhenInRoot()
    throws Exception
  {
    final JavaFileObject source1 =
      fixture( "bad_input/com/example/package_access/other/BaseActionModel.java" );
    final JavaFileObject source2 =
      fixture( "bad_input/PackageAccessActionModel.java" );
    assertFailedCompileResource( Arrays.asList( source1, source2 ),
                                 "@Action target must not be package access if " +
                                 "the method is in a different package from the @ArezComponent" );
  }

  @Test
  public void processFailedCompileInheritedPackageAccessInDifferentPackageObservable_Setter()
    throws Exception
  {
    final JavaFileObject source1 =
      fixture( "bad_input/com/example/package_access/other/BaseObservable2Model.java" );
    final JavaFileObject source2 =
      fixture( "bad_input/com/example/package_access/Observable2Model.java" );
    assertFailedCompileResource( Arrays.asList( source1, source2 ),
                                 "@Observable target must not be package access if " +
                                 "the method is in a different package from the @ArezComponent" );
  }

  @Test
  public void processFailedCompileInheritedPackageAccessInDifferentPackageObservable_Getter()
    throws Exception
  {
    final JavaFileObject source1 =
      fixture( "bad_input/com/example/package_access/other/BaseObservable3Model.java" );
    final JavaFileObject source2 =
      fixture( "bad_input/com/example/package_access/Observable3Model.java" );
    assertFailedCompileResource( Arrays.asList( source1, source2 ),
                                 "@Observable target must not be package access if " +
                                 "the method is in a different package from the @ArezComponent" );
  }
}
