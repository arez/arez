package arez.processor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.tools.JavaFileObject;
import org.realityforge.proton.qa.Compilation;
import org.realityforge.proton.qa.CompileTestUtil;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class ArezProcessorGeneratedOutputTest
  extends ArezProcessorTestSupport
{
  @DataProvider( name = "successfulCompiles" )
  @Nonnull
  public Object[][] successfulCompiles()
  {
    return new Object[][]
      {
        new Object[]{ "com.example.SubpackageModel" },
        new Object[]{ "com.example.action.ActionTypeParametersModel" },
        new Object[]{ "com.example.action.NewTypeParametersModel" },
        new Object[]{ "com.example.action.NoReportParametersModel" },
        new Object[]{ "com.example.action.FunctionActionThrowsRuntimeExceptionModel" },
        new Object[]{ "com.example.action.FunctionActionThrowsThrowableModel" },
        new Object[]{ "com.example.action.MultiThrowAction" },
        new Object[]{ "com.example.action.NonStandardNameActionModel" },
        new Object[]{ "com.example.action.UnsafeSpecificFunctionActionModel" },
        new Object[]{ "com.example.action.UnsafeSpecificProcedureActionModel" },
        new Object[]{ "com.example.action.UnsafeFunctionActionModel" },
        new Object[]{ "com.example.action.UnsafeProcedureActionModel" },
        new Object[]{ "com.example.action.NoReportResultActionModel" },
        new Object[]{ "com.example.action.NoVerifyActionModel" },
        new Object[]{ "com.example.action.SkipIfDisposedActionModel" },
        new Object[]{ "com.example.action.SkipIfDisposedFromDefaultActionModel" },
        new Object[]{ "com.example.action.SkipIfDisposedDisabledOverrideActionModel" },
        new Object[]{ "com.example.action.ReadOnlyActionModel" },
        new Object[]{ "com.example.action.RequiresNewTxTypeActionModel" },
        new Object[]{ "com.example.action.RequiresTxTypeActionModel" },
        new Object[]{ "com.example.requires_transaction.BasicRequiresTransactionModel" },
        new Object[]{ "com.example.requires_transaction.ConstrainedRequiresTransactionModel" },
        new Object[]{ "com.example.action.BasicFunctionActionModel" },
        new Object[]{ "com.example.action.BasicActionModel" },
        new Object[]{ "com.example.cascade_dispose.ComponentCascadeDisposeModel" },
        new Object[]{ "com.example.cascade_dispose.ComponentCascadeDisposeMethodModel" },
        new Object[]{ "com.example.cascade_dispose.ParameterizedComponentCascadeDisposeMethodModel" },
        new Object[]{ "com.example.cascade_dispose.DisposableCascadeDisposeModel" },
        new Object[]{ "com.example.cascade_dispose.DisposeNotifierDisabledCascadeDisposeModel" },
        new Object[]{ "com.example.cascade_dispose.DisposableCascadeDisposeMethodModel" },
        new Object[]{ "com.example.cascade_dispose.NonStandardNameCascadeDisposeMethodModel" },
        new Object[]{ "com.example.cascade_dispose.NonStandardNameDisposableCascadeDisposeModel" },
        new Object[]{ "com.example.cascade_dispose.ObservableCascadeDisposeModel" },
        new Object[]{ "com.example.component.DeprecatedModel" },
        new Object[]{ "com.example.component.DisposeOnDeactivateModel" },
        new Object[]{ "com.example.component.NoRequireEqualsModel" },
        new Object[]{ "com.example.component.NotObservableModel" },
        new Object[]{ "com.example.component.ObservableModel" },
        new Object[]{ "com.example.collections.AbstractCollectionObservableModel" },
        new Object[]{ "com.example.collections.AbstractListObservableModel" },
        new Object[]{ "com.example.collections.AbstractMapObservableModel" },
        new Object[]{ "com.example.collections.AbstractNonnullCollectionObservableModel" },
        new Object[]{ "com.example.collections.AbstractNonnullListObservableModel" },
        new Object[]{ "com.example.collections.AbstractNonnullMapObservableModel" },
        new Object[]{ "com.example.collections.AbstractNonnullSetObservableModel" },
        new Object[]{ "com.example.collections.AbstractSetObservableModel" },
        new Object[]{ "com.example.collections.MemoizeCollectionModel" },
        new Object[]{ "com.example.collections.MemoizeCollectionWithHooksModel" },
        new Object[]{ "com.example.collections.MemoizeKeepAliveListModel" },
        new Object[]{ "com.example.collections.MemoizeListModel" },
        new Object[]{ "com.example.collections.MemoizeMapModel" },
        new Object[]{ "com.example.collections.MemoizeNonnullCollectionModel" },
        new Object[]{ "com.example.collections.MemoizeNonnullListModel" },
        new Object[]{ "com.example.collections.MemoizeNonnullMapModel" },
        new Object[]{ "com.example.collections.MemoizeNonnullSetModel" },
        new Object[]{ "com.example.collections.MemoizeSetModel" },
        new Object[]{ "com.example.collections.ObservableCollectionModel" },
        new Object[]{ "com.example.collections.ObservableListModel" },
        new Object[]{ "com.example.collections.ObservableMapModel" },
        new Object[]{ "com.example.collections.ObservableNonnullCollectionModel" },
        new Object[]{ "com.example.collections.ObservableNonnullListModel" },
        new Object[]{ "com.example.collections.ObservableNonnullMapModel" },
        new Object[]{ "com.example.collections.ObservableNonnullSetModel" },
        new Object[]{ "com.example.collections.ObservableNoSettersModel" },
        new Object[]{ "com.example.collections.ObservableSetModel" },
        new Object[]{ "com.example.component_id.BooleanComponentId" },
        new Object[]{ "com.example.component_id.BooleanComponentIdRequireEquals" },
        new Object[]{ "com.example.component_id.ByteComponentId" },
        new Object[]{ "com.example.component_id.ByteComponentIdRequireEquals" },
        new Object[]{ "com.example.component_id.CharComponentId" },
        new Object[]{ "com.example.component_id.CharComponentIdRequireEquals" },
        new Object[]{ "com.example.component_id.ComponentIdOnModel" },
        new Object[]{ "com.example.component_id.DoubleComponentId" },
        new Object[]{ "com.example.component_id.DoubleComponentIdRequireEquals" },
        new Object[]{ "com.example.component_id.FloatComponentId" },
        new Object[]{ "com.example.component_id.FloatComponentIdRequireEquals" },
        new Object[]{ "com.example.component_id.IntComponentId" },
        new Object[]{ "com.example.component_id.IntComponentIdRequireEquals" },
        new Object[]{ "com.example.component_id.LongComponentId" },
        new Object[]{ "com.example.component_id.LongComponentIdRequireEquals" },
        new Object[]{ "com.example.component_id.NonStandardNameComponentId" },
        new Object[]{ "com.example.component_id.ObjectComponentId" },
        new Object[]{ "com.example.component_id.ObjectComponentIdRequireEquals" },
        new Object[]{ "com.example.component_id.ShortComponentId" },
        new Object[]{ "com.example.component_id.ShortComponentIdRequireEquals" },

        new Object[]{ "com.example.component_id_ref.BasicComponentIdRefModel" },
        new Object[]{ "com.example.component_id_ref.ComponentIdPresentComponentIdRefModel" },
        new Object[]{ "com.example.component_id_ref.MultiComponentIdRefModel" },
        new Object[]{ "com.example.component_id_ref.NonIntTypeComponentIdRefModel" },
        new Object[]{ "com.example.component_id_ref.NonStandardNameComponentIdRefModel" },
        new Object[]{ "com.example.component_id_ref.PackageAccessComponentIdRefModel" },
        new Object[]{ "com.example.component_id_ref.PublicAccessComponentIdRefModel" },
        new Object[]{ "com.example.component_id_ref.RawTypeComponentIdRefModel" },

        new Object[]{ "com.example.component_name_ref.BasicComponentNameRefModel" },
        new Object[]{ "com.example.component_name_ref.MultiComponentNameRefModel" },
        new Object[]{ "com.example.component_name_ref.NonStandardMethodNameComponentNameRefModel" },
        new Object[]{ "com.example.component_name_ref.PackageAccessComponentNameRefModel" },

        new Object[]{ "com.example.component_ref.BasicComponentRefModel" },
        new Object[]{ "com.example.component_ref.MultiComponentRefModel" },
        new Object[]{ "com.example.component_ref.NonStandardNameComponentRefModel" },
        new Object[]{ "com.example.component_ref.PackageAccessComponentRefModel" },

        new Object[]{ "com.example.component_state_ref.CompleteComponentStateRefModel" },
        new Object[]{ "com.example.component_state_ref.ConstructedComponentStateRefModel" },
        new Object[]{ "com.example.component_state_ref.DefaultComponentStateRefModel" },
        new Object[]{ "com.example.component_state_ref.DisposingComponentStateRefModel" },
        new Object[]{ "com.example.component_state_ref.MultipleComponentStateRefModel" },
        new Object[]{ "com.example.component_state_ref.PackageAccessComponentStateRefModel" },
        new Object[]{ "com.example.component_state_ref.ReadyComponentStateRefModel" },

        new Object[]{ "com.example.component_type_name_ref.BasicComponentTypeNameRefModel" },
        new Object[]{ "com.example.component_type_name_ref.MultiComponentTypeNameRefModel" },
        new Object[]{ "com.example.component_type_name_ref.NonStandardMethodNameComponentTypeNameRefModel" },
        new Object[]{ "com.example.component_type_name_ref.PackageAccessComponentTypeNameRefModel" },

        new Object[]{ "com.example.memoize.ArezOrNoneDependenciesModel" },
        new Object[]{ "com.example.memoize.NameVariationsModel" },
        new Object[]{ "com.example.memoize.HighestPriorityModel" },
        new Object[]{ "com.example.memoize.HighPriorityModel" },
        new Object[]{ "com.example.memoize.NormalPriorityModel" },
        new Object[]{ "com.example.memoize.LowestPriorityModel" },
        new Object[]{ "com.example.memoize.LowPriorityModel" },
        new Object[]{ "com.example.memoize.NonArezDependenciesModel" },
        new Object[]{ "com.example.memoize.NonArezDependenciesWithOnActivateParamModel" },
        new Object[]{ "com.example.memoize.NoReportResultModel" },
        new Object[]{ "com.example.memoize.WithHooksModel" },
        new Object[]{ "com.example.memoize.KeepAliveModel" },
        new Object[]{ "com.example.memoize.ObserveLowerPriorityModel" },
        new Object[]{ "com.example.memoize.ReadOutsideTransactionDisableMemoizeModel" },
        new Object[]{ "com.example.memoize.ReadOutsideTransactionEnabledMemoizeModel" },
        new Object[]{ "com.example.memoize.ReadOutsideTransactionFromDefaultDefaultMemoizeModel" },
        new Object[]{ "com.example.memoize.ReadOutsideTransactionFromDisabledDefaultMemoizeModel" },
        new Object[]{ "com.example.memoize.ReadOutsideTransactionFromEnabledDefaultMemoizeModel" },
        new Object[]{ "com.example.memoize.TypeParametersModel" },

        new Object[]{ "com.example.memoize_context_parameter.AllowEmptyModel" },
        new Object[]{ "com.example.memoize_context_parameter.BasicModel" },
        new Object[]{ "com.example.memoize_context_parameter.FinalMethodsModel" },
        new Object[]{ "com.example.memoize_context_parameter.FullyAnnotatedBasicModel" },
        new Object[]{ "com.example.memoize_context_parameter.ManyTypesModel" },
        new Object[]{ "com.example.memoize_context_parameter.NoCapturePrefixModel" },
        new Object[]{ "com.example.memoize_context_parameter.OverrideNameModel" },

        new Object[]{ "com.example.computable_value_ref.BasicComputableValueRefModel" },
        new Object[]{ "com.example.computable_value_ref.MultiComputableValueRefModel" },
        new Object[]{ "com.example.computable_value_ref.NonStandardName1ComputableValueRefModel" },
        new Object[]{ "com.example.computable_value_ref.NonStandardName2ComputableValueRefModel" },
        new Object[]{ "com.example.computable_value_ref.PackageAccessComputableValueRefModel" },
        new Object[]{ "com.example.computable_value_ref.ParametersComputableValueRefModel" },
        new Object[]{ "com.example.computable_value_ref.RawComputableValueRefModel" },
        new Object[]{ "com.example.computable_value_ref.RawWithParamsComputableValueRefModel" },
        new Object[]{ "com.example.computable_value_ref.WildcardComputableValueRefModel" },

        new Object[]{ "com.example.context_ref.BasicContextRefModel" },
        new Object[]{ "com.example.context_ref.MultiContextRefModel" },
        new Object[]{ "com.example.context_ref.NonStandardMethodNameContextRefModel" },

        new Object[]{ "com.example.auto_observe.BasicAutoObserveModel" },
        new Object[]{ "com.example.auto_observe.NonnullFieldAutoObserveModel" },
        new Object[]{ "com.example.auto_observe.NonnullRuntimeTypeValidateFieldAutoObserveModel" },
        new Object[]{ "com.example.auto_observe.NullableFieldAutoObserveModel" },
        new Object[]{ "com.example.auto_observe.NullableMethodAutoObserveModel" },
        new Object[]{ "com.example.auto_observe.NullableRuntimeTypeValidateFieldAutoObserveModel" },
        new Object[]{ "com.example.auto_observe.RuntimeTypeValidateFieldAutoObserveModel" },

        new Object[]{ "com.example.component_dependency.AbstractObservableDependency" },
        new Object[]{ "com.example.component_dependency.ArezComponentLikeFieldDependencyModel" },
        new Object[]{ "com.example.component_dependency.ArezComponentLikeMethodDependencyModel" },
        new Object[]{ "com.example.component_dependency.BasicDependencyModel" },
        new Object[]{ "com.example.component_dependency.BasicFieldDependencyModel" },
        new Object[]{ "com.example.component_dependency.CascadeDependencyModel" },
        new Object[]{ "com.example.component_dependency.CascadeFieldDependencyModel" },
        new Object[]{ "com.example.component_dependency.ComplexDependencyModel" },
        new Object[]{ "com.example.component_dependency.ComplexDependencyWithCustomNameMethodModel" },
        new Object[]{ "com.example.component_dependency.ComponentDependencyModel" },
        new Object[]{ "com.example.component_dependency.MultiComponentDependencyModel" },
        new Object[]{ "com.example.component_dependency.ComponentFieldDependencyModel" },
        new Object[]{ "com.example.component_dependency.ConcreteObservablePairWithInitializerDependency" },
        new Object[]{ "com.example.component_dependency.NonCascadeObservableDependency" },
        new Object[]{ "com.example.component_dependency.NonnullAbstractObservableDependency" },
        new Object[]{ "com.example.component_dependency.NonnullFieldDependencyModel" },
        new Object[]{ "com.example.component_dependency.NonnullObservableDependency" },
        new Object[]{ "com.example.component_dependency.NonStandardNameDependencyModel" },
        new Object[]{ "com.example.component_dependency.NonStandardNameFieldDependencyModel" },
        new Object[]{ "com.example.component_dependency.ObservableDependency" },
        new Object[]{ "com.example.component_dependency.ObservablePairWithInitializerDependency" },
        new Object[]{ "com.example.component_dependency.ObservablePairAnnotatedDependency" },
        new Object[]{ "com.example.component_dependency.RuntimeTypeValidateDependency" },
        new Object[]{ "com.example.component_dependency.RuntimeTypeValidateFieldDependency" },
        new Object[]{ "com.example.component_dependency.SetNullObservableDependency" },
        new Object[]{ "com.example.deprecated.DeprecatedActionModel" },
        new Object[]{ "com.example.deprecated.DeprecatedObserveModel" },
        new Object[]{ "com.example.deprecated.DeprecatedMemoizeModel1" },
        new Object[]{ "com.example.deprecated.DeprecatedMemoizeModel2" },
        new Object[]{ "com.example.deprecated.DeprecatedMemoizeModel3" },
        new Object[]{ "com.example.deprecated.DeprecatedMemoize5Model" },
        new Object[]{ "com.example.deprecated.DeprecatedObservableModel1" },
        new Object[]{ "com.example.deprecated.DeprecatedObservableModel2" },
        new Object[]{ "com.example.deprecated.DeprecatedPostConstructModel" },
        new Object[]{ "com.example.deprecated.DeprecatedObserveModel1" },
        new Object[]{ "com.example.deprecated.DeprecatedObserveModel2" },
        new Object[]{ "com.example.deprecated.DeprecatedObserveModel3" },
        new Object[]{ "com.example.deprecated.DeprecatedObserveModel4" },
        new Object[]{ "com.example.deprecation.DeprecationModel" },
        new Object[]{ "com.example.dispose_notifier.DisposeNotifierModel" },
        new Object[]{ "com.example.dispose_notifier.NoDisposeNotifierModel" },
        new Object[]{ "com.example.id.ComponentIdExample" },
        new Object[]{ "com.example.id.NonStandardNameModel" },
        new Object[]{ "com.example.id.RequireIdDisable" },
        new Object[]{ "com.example.id.RequireIdEnable" },
        new Object[]{ "com.example.inject.NoInjectModel" },
        new Object[]{ "com.example.inverse.CustomNamesInverseModel" },
        new Object[]{ "com.example.inverse.DefaultMultiplicityInverseModel" },
        new Object[]{ "com.example.inverse.DisableInverseModel" },
        new Object[]{ "com.example.inverse.NonGetterInverseModel" },
        new Object[]{ "com.example.inverse.NonObservableCollectionInverseModel" },
        new Object[]{ "com.example.inverse.NonObservableNullableManyReferenceModel" },
        new Object[]{ "com.example.inverse.NonObservableNullableOneReferenceModel" },
        new Object[]{ "com.example.inverse.NonObservableNullableZeroOrOneReferenceModel" },
        new Object[]{ "com.example.inverse.NonStandardNameModel" },
        new Object[]{ "com.example.inverse.ObservableCollectionInverseModel" },
        new Object[]{ "com.example.inverse.ObservableListInverseModel" },
        new Object[]{ "com.example.inverse.ObservableManyReferenceModel" },
        new Object[]{ "com.example.inverse.ObservableOneReferenceModel" },
        new Object[]{ "com.example.inverse.ObservableReferenceInverseModel" },
        new Object[]{ "com.example.inverse.ObservableSetInverseModel" },
        new Object[]{ "com.example.inverse.ObservableZeroOrOneReferenceModel" },
        new Object[]{ "com.example.inverse.OneMultiplicityInverseModel" },
        new Object[]{ "com.example.inverse.ZeroOrOneMultiplicityInverseModel" },
        new Object[]{ "com.example.memoize.AnnotatedModel" },
        new Object[]{ "com.example.memoize.BasicModel" },
        new Object[]{ "com.example.memoize.CustomDepTypeModel" },
        new Object[]{ "com.example.memoize.CustomPriorityModel" },
        new Object[]{ "com.example.memoize.DefaultEqualityComparatorModel" },
        new Object[]{ "com.example.memoize.DefaultDefaultPriorityUnspecifiedLocalPriorityMemoizeModel" },
        new Object[]{ "com.example.memoize.DefaultPriorityDefaultLocalPriorityMemoizeModel" },
        new Object[]{ "com.example.memoize.DefaultPrioritySpecifiedLocalPriorityMemoizeModel" },
        new Object[]{ "com.example.memoize.DefaultPriorityUnspecifiedLocalPriorityMemoizeModel" },
        new Object[]{ "com.example.memoize.LocalTypeParamModel" },
        new Object[]{ "com.example.memoize.NoArgEqualityComparatorModel" },
        new Object[]{ "com.example.memoize.ParameterizedEqualityComparatorModel" },
        new Object[]{ "com.example.memoize.NonStandardNameModel" },
        new Object[]{ "com.example.memoize.TypeParamModel" },
        new Object[]{ "com.example.observable.AbstractNonPrimitiveObservablesModel" },
        new Object[]{ "com.example.observable.AbstractObservablesModel" },
        new Object[]{ "com.example.observable.DefaultEqualityComparatorModel" },
        new Object[]{ "com.example.observable.GenericObservableModel" },
        new Object[]{ "com.example.observable.InitializerAndConstructorParamNameCollisionModel" },
        new Object[]{ "com.example.observable.NonStandardNameModel" },
        new Object[]{ "com.example.observable.NullableInitializerModel" },
        new Object[]{ "com.example.observable.ObservableWithNoSetter" },
        new Object[]{ "com.example.observable.RawCollectionObservableModel" },
        new Object[]{ "com.example.observable.RawObservableModel" },
        new Object[]{ "com.example.observable.ReadOutsideTransactionDisabledObservableModel" },
        new Object[]{ "com.example.observable.ReadOutsideTransactionEnabledObservableModel" },
        new Object[]{ "com.example.observable.ReadOutsideTransactionFromDefaultDefaultObservableModel" },
        new Object[]{ "com.example.observable.ReadOutsideTransactionFromDisabledDefaultObservableModel" },
        new Object[]{ "com.example.observable.ReadOutsideTransactionFromEnabledDefaultObservableModel" },
        new Object[]{ "com.example.observable.SetterAlwaysMutatesFalseObjectValue" },
        new Object[]{ "com.example.observable.SetterAlwaysMutatesFalseDeepObjectValue" },
        new Object[]{ "com.example.observable.SetterAlwaysMutatesFalseCustomObjectValue" },
        new Object[]{ "com.example.observable.SetterAlwaysMutatesFalsePrimitiveValue" },
        new Object[]{ "com.example.observable.UnannotatedObservableModel" },
        new Object[]{ "com.example.observable.WildcardGenericObservableModel" },
        new Object[]{ "com.example.observable.WriteOutsideTransactionDisabledObservableModel" },
        new Object[]{ "com.example.observable.WriteOutsideTransactionEnabledObservableModel" },
        new Object[]{ "com.example.observable.WriteOutsideTransactionFromDefaultDefaultObservableModel" },
        new Object[]{ "com.example.observable.WriteOutsideTransactionFromDisabledDefaultObservableModel" },
        new Object[]{ "com.example.observable.WriteOutsideTransactionFromEnabledDefaultObservableModel" },
        new Object[]{ "com.example.observable.WriteOutsideTransactionThrowingObservablesModel" },
        new Object[]{ "com.example.observable.AbstractNonPrimitiveNonnullObservablesModel" },
        new Object[]{ "com.example.observable.AbstractPrimitiveObservablesWithInitializerModel" },
        new Object[]{ "com.example.observable.ObservableInitialModel" },

        new Object[]{ "com.example.observable_value_ref.BasicObservableValueRefModel" },
        new Object[]{ "com.example.observable_value_ref.MultiObservableValueRefModel" },
        new Object[]{ "com.example.observable_value_ref.GenericObservableValueRefModel" },
        new Object[]{ "com.example.observable_value_ref.NonStandardMethodName1ObservableValueRefModel" },
        new Object[]{ "com.example.observable_value_ref.NonStandardMethodName2ObservableValueRefModel" },
        new Object[]{ "com.example.observable_value_ref.PackageAccessObservableValueRefModel" },
        new Object[]{ "com.example.observable_value_ref.RawObservableValueRefModel" },

        new Object[]{ "com.example.observable_value_ref.WildcardObservableValueRefModel" },

        new Object[]{ "com.example.observe.BasicObserveModel" },
        new Object[]{ "com.example.observe.NestedActionsAllowedObserveModel" },
        new Object[]{ "com.example.observe.HighestPriorityObserveModel" },
        new Object[]{ "com.example.observe.HighPriorityObserveModel" },
        new Object[]{ "com.example.observe.LowestPriorityObserveModel" },
        new Object[]{ "com.example.observe.LowPriorityObserveModel" },
        new Object[]{ "com.example.observe.NormalPriorityObserveModel" },
        new Object[]{ "com.example.observe.ObserveLowerPriorityObserveModel" },
        new Object[]{ "com.example.observe.ReadWriteObserveModel" },
        new Object[]{ "com.example.observe.ScheduleAfterConstructedModel" },
        new Object[]{ "com.example.observe.ArezOrNoneDependenciesModel" },
        new Object[]{ "com.example.observe.BasicTrackedModel" },
        new Object[]{ "com.example.observe.BasicTrackedWithExceptionsModel" },
        new Object[]{ "com.example.observe.DefaultDefaultPriorityUnspecifiedLocalPriorityObserveModel" },
        new Object[]{ "com.example.observe.DefaultPriorityDefaultLocalPriorityObserveModel" },
        new Object[]{ "com.example.observe.DefaultPrioritySpecifiedLocalPriorityObserveModel" },
        new Object[]{ "com.example.observe.DefaultPriorityUnspecifiedLocalPriorityObserveModel" },
        new Object[]{ "com.example.observe.NestedActionsAllowedTrackedModel" },
        new Object[]{ "com.example.observe.NonArezDependenciesModel" },
        new Object[]{ "com.example.observe.NonStandardNameTrackedModel" },
        new Object[]{ "com.example.observe.DeriveTrackedModel" },
        new Object[]{ "com.example.observe.HighestPriorityTrackedModel" },
        new Object[]{ "com.example.observe.HighPriorityTrackedModel" },
        new Object[]{ "com.example.observe.NormalPriorityTrackedModel" },
        new Object[]{ "com.example.observe.LowestPriorityTrackedModel" },
        new Object[]{ "com.example.observe.LowPriorityTrackedModel" },
        new Object[]{ "com.example.observe.NoReportParametersModel" },
        new Object[]{ "com.example.observe.NoReportResultModel" },
        new Object[]{ "com.example.observe.ObserveLowerPriorityTrackedModel" },
        new Object[]{ "com.example.observe.MultiInternalOnDepsChangeModel" },
        new Object[]{ "com.example.observe.MultiOnDepsChangeModel" },
        new Object[]{ "com.example.observe.TrackedAllTypesModel" },
        new Object[]{ "com.example.observe.TrackedAndSchedulableModel" },
        new Object[]{ "com.example.observe.TrackedImplicitOnDepsChangeAcceptsObserverModel" },
        new Object[]{ "com.example.observe.TrackedNoOtherSchedulableModel" },
        new Object[]{ "com.example.observe.TrackedOnDepsChangeAcceptsObserverModel" },

        new Object[]{ "com.example.observer_ref.BasicObserverRefModel" },
        new Object[]{ "com.example.observer_ref.MultiObserverRefModel" },
        new Object[]{ "com.example.observer_ref.CustomNameObserverRefModel" },
        new Object[]{ "com.example.observer_ref.ExternalObserveObserverRefModel" },
        new Object[]{ "com.example.observer_ref.NonStandardMethodNameObserverRefModel" },
        new Object[]{ "com.example.observer_ref.PackageAccessObserverRefModel" },

        new Object[]{ "com.example.on_activate.BasicOnActivateModel" },
        new Object[]{ "com.example.on_activate.PackageAccessOnActivateModel" },

        new Object[]{ "com.example.on_deactivate.BasicOnDeactivateModel" },
        new Object[]{ "com.example.on_deactivate.PackageAccessOnDeactivateModel" },

        new Object[]{ "com.example.on_deps_change.BasicOnDepsChangeModel" },
        new Object[]{ "com.example.on_deps_change.DeriveOnDepsChangeModel" },
        new Object[]{ "com.example.on_deps_change.OnDepsChangeDuplicatedModel" },
        new Object[]{ "com.example.on_deps_change.PackageAccessOnDepsChangeModel" },

        new Object[]{ "com.example.overloaded_names.OverloadedActions" },

        new Object[]{ "com.example.post_construct.ActionPostConstructModel" },
        new Object[]{ "com.example.post_construct.BasicPostConstructModel" },
        new Object[]{ "com.example.post_construct.MultiPostConstructModel" },
        new Object[]{ "com.example.post_construct.NonStandardNamePostConstructModel" },
        new Object[]{ "com.example.post_construct.PackageAccessPostConstructModel" },

        new Object[]{ "com.example.post_dispose.BasicPostDisposeModel" },
        new Object[]{ "com.example.post_dispose.MultiPostDisposeModel" },
        new Object[]{ "com.example.post_dispose.PackageAccessPostDisposeModel" },
        new Object[]{ "com.example.post_dispose.PostDisposeWithDisabledDisposeNotifierModel" },

        new Object[]{ "com.example.post_inverse_add.BasicPostInverseAddModel" },
        new Object[]{ "com.example.post_inverse_add.MultiPostInverseAddModel" },
        new Object[]{ "com.example.post_inverse_add.PackageAccessPostInverseAddModel" },
        new Object[]{ "com.example.post_inverse_add.SingularInversePostInverseAddModel" },

        new Object[]{ "com.example.pre_dispose.BasicPreDisposeModel" },
        new Object[]{ "com.example.pre_dispose.MultiPreDisposeModel" },
        new Object[]{ "com.example.pre_dispose.MultiPreDisposeNotDisposeNotifierModel" },
        new Object[]{ "com.example.pre_dispose.PackageAccessPreDisposeModel" },

        new Object[]{ "com.example.pre_inverse_remove.BasicPreInverseRemoveModel" },
        new Object[]{ "com.example.pre_inverse_remove.MultiPreInverseRemoveModel" },
        new Object[]{ "com.example.pre_inverse_remove.PackageAccessPreInverseRemoveModel" },
        new Object[]{ "com.example.pre_inverse_remove.SingularInversePreInverseRemoveModel" },

        new Object[]{ "com.example.reference.CascadeDisposeReferenceModel" },
        new Object[]{ "com.example.reference.CustomNameReferenceModel2" },
        new Object[]{ "com.example.reference.CustomNameReferenceModel" },
        new Object[]{ "com.example.reference.EagerLoadNulableObservableReferenceModel" },
        new Object[]{ "com.example.reference.EagerLoadObservableReferenceModel" },
        new Object[]{ "com.example.reference.EagerLoadReferenceModel" },
        new Object[]{ "com.example.reference.EagerObservableReadOutsideTransactionReferenceModel" },
        new Object[]{ "com.example.reference.ExplicitLoadObservableReferenceModel" },
        new Object[]{ "com.example.reference.ExplicitLoadReferenceModel" },
        new Object[]{ "com.example.reference.LazyLoadObservableReferenceModel" },
        new Object[]{ "com.example.reference.LazyLoadReferenceModel" },
        new Object[]{ "com.example.reference.LazyObservableReadOutsideTransactionReferenceModel" },
        new Object[]{ "com.example.reference.NonJavabeanNameReferenceModel" },
        new Object[]{ "com.example.reference.NonnullLazyLoadReferenceModel" },
        new Object[]{ "com.example.reference.NonObservableReferenceModel" },
        new Object[]{ "com.example.reference.NullableLazyLoadReferenceModel" },
        new Object[]{ "com.example.reference.ObservableReferenceModel" },
        new Object[]{ "com.example.reserved_names.NonReservedNameModel" },
        new Object[]{ "com.example.sting.BasicStingModel" },
        new Object[]{ "com.example.sting.EagerStingModel" },
        new Object[]{ "com.example.sting.MultipleArgsStingModel" },
        new Object[]{ "com.example.sting.NamedArgStingModel" },
        new Object[]{ "com.example.sting.NamedStingModel" },
        new Object[]{ "com.example.sting.ServiceViaEagerStingModel" },
        new Object[]{ "com.example.sting.ServiceViaNamedStingModel" },
        new Object[]{ "com.example.sting.ServiceViaTypedStingModel" },
        new Object[]{ "com.example.sting.EmptyTypedStingModel" },
        new Object[]{ "com.example.sting.TypedStingModel" },
        new Object[]{ "com.example.to_string.NoToStringPresent" },
        new Object[]{ "com.example.to_string.ToStringPresent" },
        new Object[]{ "com.example.type_access_levels.ReduceAccessLevelModel" },
        new Object[]{ "com.example.verifiable.DisableVerifyModel" },
        new Object[]{ "com.example.verifiable.EnableVerifyModel" },
        new Object[]{ "DisposingModel" },
        new Object[]{ "ObservableTypeParametersModel" },
        new Object[]{ "TypeParametersOnModel" },
        new Object[]{ "ObservableGuessingModel" },
        new Object[]{ "AnnotationsOnModel" },
        new Object[]{ "ObservableWithAnnotatedCtorModel" },
        new Object[]{ "ObservableModelWithUnconventionalNames" },
        new Object[]{ "DifferentObservableTypesModel" },
        new Object[]{ "ObservableWithExceptingCtorModel" },
        new Object[]{ "OverrideNamesInModel" },
        new Object[]{ "EmptyModel" },
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
    assertSuccessfulFixtureCompile( classname );
  }

  @Test
  public void memoizeContextParameterInherit()
    throws Exception
  {
    final String classname = "com.example.memoize_context_parameter.inherit.ConcreteModel";
    final String[] expectedOutputResources = deriveExpectedOutputs( classname );
    final JavaFileObject input1 = fixture( "input/" + toFilename( classname ) );
    final JavaFileObject input2 =
      fixture( "input/" + toFilename( "com.example.memoize_context_parameter.inherit.subpkg.AbstractModel" ) );
    assertSuccessfulFixtureCompile( Arrays.asList( input1, input2 ), Arrays.asList( expectedOutputResources ) );
  }

  @Test
  public void memoizeContextParameterInterface()
    throws Exception
  {
    final String classname = "com.example.memoize_context_parameter.intf.ConcreteModel";
    final String[] expectedOutputResources = deriveExpectedOutputs( classname );
    final JavaFileObject input1 = fixture( "input/" + toFilename( classname ) );
    final JavaFileObject input2 =
      fixture( "input/" + toFilename( "com.example.memoize_context_parameter.intf.subpkg.MyInterfaceModelBase" ) );
    assertSuccessfulFixtureCompile( Arrays.asList( input1, input2 ), Arrays.asList( expectedOutputResources ) );
  }

  @Test
  public void deprecatedUsageModel()
    throws Exception
  {
    // Use deprecated types, but arez should suppress the warnings and generate code that has no warnings...
    final String classname = "com.example.deprecated.DeprecatedUsageModel";
    final String[] expectedOutputResources = deriveExpectedOutputs( classname );
    final JavaFileObject input1 = fixture( "input/" + toFilename( classname ) );
    final JavaFileObject input2 = fixture( "input/" + toFilename( "com.example.deprecated.MyDeprecatedEntity" ) );
    assertSuccessfulFixtureCompile( Arrays.asList( input1, input2 ), Arrays.asList( expectedOutputResources ) );
  }

  @Test
  public void deprecatedTypeParameterModel()
    throws Exception
  {
    // Use deprecated types, but arez should suppress the warnings and generate code that has no warnings...
    final String classname = "com.example.deprecated.DeprecatedTypeParameterModel";
    final String[] expectedOutputResources = deriveExpectedOutputs( classname );
    final JavaFileObject input1 = fixture( "input/" + toFilename( classname ) );
    final JavaFileObject input2 = fixture( "input/" + toFilename( "com.example.deprecated.MyDeprecatedEntity" ) );
    assertSuccessfulFixtureCompile( Arrays.asList( input1, input2 ), Arrays.asList( expectedOutputResources ) );
  }

  @Test
  public void deprecatedParameterModelModel()
    throws Exception
  {
    // Use deprecated types, but arez should suppress the warnings and generate code that has no warnings...
    final String classname = "com.example.deprecated.DeprecatedParameterModel";
    final String[] expectedOutputResources = deriveExpectedOutputs( classname );
    final JavaFileObject input1 = fixture( "input/" + toFilename( classname ) );
    final JavaFileObject input2 = fixture( "input/" + toFilename( "com.example.deprecated.MyDeprecatedEntity" ) );
    assertSuccessfulFixtureCompile( Arrays.asList( input1, input2 ), Arrays.asList( expectedOutputResources ) );
  }

  @Test
  public void deprecatedViaInterfaceModel()
    throws Exception
  {
    // Use deprecated types, but arez should suppress the warnings and generate code that has no warnings...
    final String classname = "com.example.deprecated.DeprecatedViaInterfaceModel";
    final String[] expectedOutputResources = deriveExpectedOutputs( classname );
    final JavaFileObject input1 = fixture( "input/" + toFilename( classname ) );
    final JavaFileObject input2 = fixture( "input/" + toFilename( "com.example.deprecated.DeprecatedInterface" ) );
    final JavaFileObject input3 = fixture( "input/" + toFilename( "com.example.deprecated.DeprecatedBaseInterface" ) );
    assertSuccessfulFixtureCompile( Arrays.asList( input1, input2, input3 ), Arrays.asList( expectedOutputResources ) );
  }

  @Test
  public void rawTypesUsageModel()
    throws Exception
  {
    // Use deprecated types but arez should suppress the warnings and generate code that has no warnings...
    final String classname = "com.example.raw_types.RawTypesUsageModel";
    final String[] expectedOutputResources = deriveExpectedOutputs( classname );
    final JavaFileObject input1 = fixture( "input/" + toFilename( classname ) );
    assertSuccessfulFixtureCompile( Collections.singletonList( input1 ), Arrays.asList( expectedOutputResources ) );
  }

  @Test
  public void validProtectedAccessComponentRef()
    throws Exception
  {
    final String input1 =
      "input/" + toFilename( "com.example.component_ref.ProtectedAccessFromBaseComponentRefModel" );
    final String input2 =
      "input/" + toFilename( "com.example.component_ref.other.BaseProtectedAccessComponentRefModel" );
    final String output =
      toFilename( "com.example.component_ref.Arez_ProtectedAccessFromBaseComponentRefModel" );
    assertSuccessfulFixtureCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void validProtectedAccessFieldAutoObserve()
    throws Exception
  {
    final String input1 =
      "input/" + toFilename( "com.example.auto_observe.ProtectedAccessFromBaseFieldAutoObserveModel" );
    final String input2 =
      "input/" + toFilename( "com.example.auto_observe.other.BaseProtectedAccessFieldAutoObserveModel" );
    final String output =
      toFilename( "com.example.auto_observe.Arez_ProtectedAccessFromBaseFieldAutoObserveModel" );
    assertSuccessfulFixtureCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void validProtectedAccessFieldCascadeDispose()
    throws Exception
  {
    final String input1 =
      "input/" + toFilename( "com.example.cascade_dispose.ProtectedAccessFromBaseFieldCascadeDisposeModel" );
    final String input2 =
      "input/" + toFilename( "com.example.cascade_dispose.other.BaseProtectedAccessFieldCascadeDisposeModel" );
    final String output =
      toFilename( "com.example.cascade_dispose.Arez_ProtectedAccessFromBaseFieldCascadeDisposeModel" );
    assertSuccessfulFixtureCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void validProtectedAccessFieldComponentDependency()
    throws Exception
  {
    final String input1 =
      "input/" + toFilename( "com.example.component_dependency.ProtectedAccessFromBaseFieldDependencyModel" );
    final String input2 =
      "input/" + toFilename( "com.example.component_dependency.other.BaseProtectedAccessFieldDependencyModel" );
    final String output =
      toFilename( "com.example.component_dependency.Arez_ProtectedAccessFromBaseFieldDependencyModel" );
    assertSuccessfulFixtureCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void validPublicAccessViaInterfaceComponentRef()
    throws Exception
  {
    final String input1 =
      "input/" + toFilename( "com.example.component_ref.PublicAccessViaInterfaceComponentRefModel" );
    final String input2 =
      "input/" + toFilename( "com.example.component_ref.ComponentRefInterface" );
    final String output =
      toFilename( "com.example.component_ref.Arez_PublicAccessViaInterfaceComponentRefModel" );
    assertSuccessfulFixtureCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void validProtectedAccessComponentNameRef()
    throws Exception
  {
    final String input1 =
      "input/" + toFilename( "com.example.component_name_ref.ProtectedAccessFromBaseComponentNameRefModel" );
    final String input2 =
      "input/" + toFilename( "com.example.component_name_ref.other.BaseProtectedAccessComponentNameRefModel" );
    final String output =
      toFilename( "com.example.component_name_ref.Arez_ProtectedAccessFromBaseComponentNameRefModel" );
    assertSuccessfulFixtureCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void validPublicAccessViaInterfaceComponentNameRef()
    throws Exception
  {
    final String input1 =
      "input/" + toFilename( "com.example.component_name_ref.PublicAccessViaInterfaceComponentNameRefModel" );
    final String input2 =
      "input/" + toFilename( "com.example.component_name_ref.ComponentNameRefInterface" );
    final String output =
      toFilename( "com.example.component_name_ref.Arez_PublicAccessViaInterfaceComponentNameRefModel" );
    assertSuccessfulFixtureCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void validProtectedAccessComponentStateRef()
    throws Exception
  {
    final String input1 =
      "input/" + toFilename( "com.example.component_state_ref.ProtectedAccessFromBaseComponentStateRefModel" );
    final String input2 =
      "input/" + toFilename( "com.example.component_state_ref.other.BaseProtectedAccessComponentStateRefModel" );
    final String output =
      toFilename( "com.example.component_state_ref.Arez_ProtectedAccessFromBaseComponentStateRefModel" );
    assertSuccessfulFixtureCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void validPublicAccessViaInterfaceComponentStateRef()
    throws Exception
  {
    final String input1 =
      "input/" + toFilename( "com.example.component_state_ref.PublicAccessViaInterfaceComponentStateRefModel" );
    final String input2 =
      "input/" + toFilename( "com.example.component_state_ref.ComponentStateRefInterface" );
    final String output =
      toFilename( "com.example.component_state_ref.Arez_PublicAccessViaInterfaceComponentStateRefModel" );
    assertSuccessfulFixtureCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void validProtectedAccessComponentTypeNameRef()
    throws Exception
  {
    final String input1 =
      "input/" + toFilename( "com.example.component_type_name_ref.ProtectedAccessFromBaseComponentTypeNameRefModel" );
    final String input2 =
      "input/" + toFilename( "com.example.component_type_name_ref.other.BaseProtectedAccessComponentTypeNameRefModel" );
    final String output =
      toFilename(
        "com.example.component_type_name_ref.Arez_ProtectedAccessFromBaseComponentTypeNameRefModel" );
    assertSuccessfulFixtureCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void validPublicAccessViaInterfaceComponentTypeNameRef()
    throws Exception
  {
    final String input1 =
      "input/" + toFilename( "com.example.component_type_name_ref.PublicAccessViaInterfaceComponentTypeNameRefModel" );
    final String input2 =
      "input/" + toFilename( "com.example.component_type_name_ref.ComponentTypeNameRefInterface" );
    final String output =
      toFilename(
        "com.example.component_type_name_ref.Arez_PublicAccessViaInterfaceComponentTypeNameRefModel" );
    assertSuccessfulFixtureCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void validProtectedAccessComputableValueRef()
    throws Exception
  {
    final String input1 =
      "input/" + toFilename( "com.example.computable_value_ref.ProtectedAccessFromBaseComputableValueRefModel" );
    final String input2 =
      "input/" + toFilename( "com.example.computable_value_ref.other.BaseProtectedAccessComputableValueRefModel" );
    final String output =
      toFilename( "com.example.computable_value_ref.Arez_ProtectedAccessFromBaseComputableValueRefModel" );
    assertSuccessfulFixtureCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void validPublicAccessViaInterfaceComputableValueRef()
    throws Exception
  {
    final String input1 =
      "input/" + toFilename( "com.example.computable_value_ref.PublicAccessViaInterfaceComputableValueRefModel" );
    final String input2 =
      "input/" + toFilename( "com.example.computable_value_ref.ComputableValueRefInterface" );
    final String output =
      toFilename( "com.example.computable_value_ref.Arez_PublicAccessViaInterfaceComputableValueRefModel" );
    assertSuccessfulFixtureCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void validProtectedAccessContextRef()
    throws Exception
  {
    final String input1 =
      "input/" + toFilename( "com.example.context_ref.ProtectedAccessFromBaseContextRefModel" );
    final String input2 =
      "input/" + toFilename( "com.example.context_ref.other.BaseProtectedAccessContextRefModel" );
    final String output =
      toFilename( "com.example.context_ref.Arez_ProtectedAccessFromBaseContextRefModel" );
    assertSuccessfulFixtureCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void validPublicAccessViaInterfaceContextRef()
    throws Exception
  {
    final String input1 =
      "input/" + toFilename( "com.example.context_ref.PublicAccessViaInterfaceContextRefModel" );
    final String input2 =
      "input/" + toFilename( "com.example.context_ref.ContextRefInterface" );
    final String output =
      toFilename( "com.example.context_ref.Arez_PublicAccessViaInterfaceContextRefModel" );
    assertSuccessfulFixtureCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void validProtectedAccessObservableValueRef()
    throws Exception
  {
    final String input1 =
      "input/" + toFilename( "com.example.observable_value_ref.ProtectedAccessFromBaseObservableValueRefModel" );
    final String input2 =
      "input/" + toFilename( "com.example.observable_value_ref.other.BaseProtectedAccessObservableValueRefModel" );
    final String output =
      toFilename( "com.example.observable_value_ref.Arez_ProtectedAccessFromBaseObservableValueRefModel" );
    assertSuccessfulFixtureCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void validPublicAccessViaInterfaceObservableValueRef()
    throws Exception
  {
    final String input1 =
      "input/" + toFilename( "com.example.observable_value_ref.PublicAccessViaInterfaceObservableValueRefModel" );
    final String input2 =
      "input/" + toFilename( "com.example.observable_value_ref.ObservableValueRefInterface" );
    final String output =
      toFilename( "com.example.observable_value_ref.Arez_PublicAccessViaInterfaceObservableValueRefModel" );
    assertSuccessfulFixtureCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void validProtectedAccessObserverRef()
    throws Exception
  {
    final String input1 =
      "input/" + toFilename( "com.example.observer_ref.ProtectedAccessFromBaseObserverRefModel" );
    final String input2 =
      "input/" + toFilename( "com.example.observer_ref.other.BaseProtectedAccessObserverRefModel" );
    final String output =
      toFilename( "com.example.observer_ref.Arez_ProtectedAccessFromBaseObserverRefModel" );
    assertSuccessfulFixtureCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void validPublicAccessViaInterfaceObserverRef()
    throws Exception
  {
    final String input1 =
      "input/" + toFilename( "com.example.observer_ref.PublicAccessViaInterfaceObserverRefModel" );
    final String input2 =
      "input/" + toFilename( "com.example.observer_ref.ObserverRefInterface" );
    final String output =
      toFilename( "com.example.observer_ref.Arez_PublicAccessViaInterfaceObserverRefModel" );
    assertSuccessfulFixtureCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void validProtectedAccessOnActivate()
    throws Exception
  {
    final String input1 =
      "input/" + toFilename( "com.example.on_activate.ProtectedAccessFromBaseOnActivateModel" );
    final String input2 =
      "input/" + toFilename( "com.example.on_activate.other.BaseProtectedAccessOnActivateModel" );
    final String output =
      toFilename( "com.example.on_activate.Arez_ProtectedAccessFromBaseOnActivateModel" );
    assertSuccessfulFixtureCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void validPublicAccessViaInterfaceOnActivate()
    throws Exception
  {
    final String input1 =
      "input/" + toFilename( "com.example.on_activate.PublicAccessViaInterfaceOnActivateModel" );
    final String input2 =
      "input/" + toFilename( "com.example.on_activate.OnActivateInterface" );
    final String output =
      toFilename( "com.example.on_activate.Arez_PublicAccessViaInterfaceOnActivateModel" );
    assertSuccessfulFixtureCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void validProtectedAccessOnDeactivate()
    throws Exception
  {
    final String input1 =
      "input/" + toFilename( "com.example.on_deactivate.ProtectedAccessFromBaseOnDeactivateModel" );
    final String input2 =
      "input/" + toFilename( "com.example.on_deactivate.other.BaseProtectedAccessOnDeactivateModel" );
    final String output =
      toFilename( "com.example.on_deactivate.Arez_ProtectedAccessFromBaseOnDeactivateModel" );
    assertSuccessfulFixtureCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void validPublicAccessViaInterfaceOnDeactivate()
    throws Exception
  {
    final String input1 =
      "input/" + toFilename( "com.example.on_deactivate.PublicAccessViaInterfaceOnDeactivateModel" );
    final String input2 =
      "input/" + toFilename( "com.example.on_deactivate.OnDeactivateInterface" );
    final String output =
      toFilename( "com.example.on_deactivate.Arez_PublicAccessViaInterfaceOnDeactivateModel" );
    assertSuccessfulFixtureCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void validProtectedAccessOnDepsChange()
    throws Exception
  {
    final String input1 =
      "input/" + toFilename( "com.example.on_deps_change.ProtectedAccessFromBaseOnDepsChangeModel" );
    final String input2 =
      "input/" + toFilename( "com.example.on_deps_change.other.BaseProtectedAccessOnDepsChangeModel" );
    final String output =
      toFilename( "com.example.on_deps_change.Arez_ProtectedAccessFromBaseOnDepsChangeModel" );
    assertSuccessfulFixtureCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void validPublicAccessViaInterfaceOnDepsChange()
    throws Exception
  {
    final String input1 =
      "input/" + toFilename( "com.example.on_deps_change.PublicAccessViaInterfaceOnDepsChangeModel" );
    final String input2 =
      "input/" + toFilename( "com.example.on_deps_change.OnDepsChangeInterface" );
    final String output =
      toFilename( "com.example.on_deps_change.Arez_PublicAccessViaInterfaceOnDepsChangeModel" );
    assertSuccessfulFixtureCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void validProtectedAccessPostConstruct()
    throws Exception
  {
    final String input1 =
      "input/" + toFilename( "com.example.post_construct.ProtectedAccessFromBasePostConstructModel" );
    final String input2 =
      "input/" + toFilename( "com.example.post_construct.other.BaseProtectedAccessPostConstructModel" );
    final String output =
      toFilename( "com.example.post_construct.Arez_ProtectedAccessFromBasePostConstructModel" );
    assertSuccessfulFixtureCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void validPublicAccessViaInterfacePostConstruct()
    throws Exception
  {
    final String input1 =
      "input/" + toFilename( "com.example.post_construct.PublicAccessViaInterfacePostConstructModel" );
    final String input2 =
      "input/" + toFilename( "com.example.post_construct.PostConstructInterface" );
    final String output =
      toFilename( "com.example.post_construct.Arez_PublicAccessViaInterfacePostConstructModel" );
    assertSuccessfulFixtureCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void multiViaInheritancePostConstruct()
    throws Exception
  {
    final String input1 =
      "input/" + toFilename( "com.example.post_construct.MultiViaInheritanceChainPostConstructModel" );
    final String input2 = "input/" + toFilename( "com.example.post_construct.other.AbstractMultiModel" );
    final String input3 = "input/" + toFilename( "com.example.post_construct.other.MiddleMultiModel" );
    final String input4 = "input/" + toFilename( "com.example.post_construct.other.MultiModelInterface1" );
    final String input5 = "input/" + toFilename( "com.example.post_construct.other.MultiModelInterface2" );
    final String input6 = "input/" + toFilename( "com.example.post_construct.other.MultiModelInterface3" );
    final String output =
      toFilename( "com.example.post_construct.Arez_MultiViaInheritanceChainPostConstructModel" );
    assertSuccessfulFixtureCompile( Arrays.asList( fixture( input1 ),
                                            fixture( input2 ),
                                            fixture( input3 ),
                                            fixture( input4 ),
                                            fixture( input5 ),
                                            fixture( input6 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void validProtectedAccessPostDispose()
    throws Exception
  {
    final String input1 =
      "input/" + toFilename( "com.example.post_dispose.ProtectedAccessFromBasePostDisposeModel" );
    final String input2 =
      "input/" + toFilename( "com.example.post_dispose.other.BaseProtectedAccessPostDisposeModel" );
    final String output =
      toFilename( "com.example.post_dispose.Arez_ProtectedAccessFromBasePostDisposeModel" );
    assertSuccessfulFixtureCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void validPublicAccessViaInterfacePostDispose()
    throws Exception
  {
    final String input1 =
      "input/" + toFilename( "com.example.post_dispose.PublicAccessViaInterfacePostDisposeModel" );
    final String input2 =
      "input/" + toFilename( "com.example.post_dispose.PostDisposeInterface" );
    final String output =
      toFilename( "com.example.post_dispose.Arez_PublicAccessViaInterfacePostDisposeModel" );
    assertSuccessfulFixtureCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void multiViaInheritancePostDispose()
    throws Exception
  {
    final String pkg = "com.example.post_dispose";
    final String output =
      toFilename( pkg + ".Arez_MultiViaInheritanceChainPostDisposeModel" );
    assertSuccessfulFixtureCompile( inputs( pkg + ".MultiViaInheritanceChainPostDisposeModel",
                                     pkg + ".other.AbstractMultiModel",
                                     pkg + ".other.MiddleMultiModel",
                                     pkg + ".other.MultiModelInterface1",
                                     pkg + ".other.MultiModelInterface2",
                                     pkg + ".other.MultiModelInterface3" ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void validProtectedAccessPostInverseAdd()
    throws Exception
  {
    final String input1 =
      "input/" + toFilename( "com.example.post_inverse_add.ProtectedAccessFromBasePostInverseAddModel" );
    final String input2 =
      "input/" + toFilename( "com.example.post_inverse_add.other.BaseProtectedAccessPostInverseAddModel" );
    final String output1 =
      toFilename( "com.example.post_inverse_add.Arez_ProtectedAccessFromBasePostInverseAddModel" );
    final String output2 =
      toFilename( "com.example.post_inverse_add.other.BaseProtectedAccessPostInverseAddModel_Arez_Element" );
    assertSuccessfulFixtureCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Arrays.asList( output1, output2 ) );
  }

  @Test
  public void validPublicAccessViaInterfacePostInverseAdd()
    throws Exception
  {
    final String input1 =
      "input/" + toFilename( "com.example.post_inverse_add.PublicAccessViaInterfacePostInverseAddModel" );
    final String input2 =
      "input/" + toFilename( "com.example.post_inverse_add.PostInverseAddInterface" );
    final String output1 =
      toFilename( "com.example.post_inverse_add.Arez_PublicAccessViaInterfacePostInverseAddModel" );
    final String output2 =
      toFilename( "com.example.post_inverse_add.PublicAccessViaInterfacePostInverseAddModel_Arez_Element" );
    assertSuccessfulFixtureCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Arrays.asList( output1, output2 ) );
  }

  @Test
  public void validProtectedAccessPreDispose()
    throws Exception
  {
    final String input1 =
      "input/" + toFilename( "com.example.pre_dispose.ProtectedAccessFromBasePreDisposeModel" );
    final String input2 =
      "input/" + toFilename( "com.example.pre_dispose.other.BaseProtectedAccessPreDisposeModel" );
    final String output =
      toFilename( "com.example.pre_dispose.Arez_ProtectedAccessFromBasePreDisposeModel" );
    assertSuccessfulFixtureCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void validPublicAccessViaInterfacePreDispose()
    throws Exception
  {
    final String input1 =
      "input/" + toFilename( "com.example.pre_dispose.PublicAccessViaInterfacePreDisposeModel" );
    final String input2 =
      "input/" + toFilename( "com.example.pre_dispose.PreDisposeInterface" );
    final String output =
      toFilename( "com.example.pre_dispose.Arez_PublicAccessViaInterfacePreDisposeModel" );
    assertSuccessfulFixtureCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void multiViaInheritancePreDispose()
    throws Exception
  {
    final String input1 =
      "input/" + toFilename( "com.example.pre_dispose.MultiViaInheritanceChainPreDisposeModel" );
    final String input2 = "input/" + toFilename( "com.example.pre_dispose.other.AbstractMultiModel" );
    final String input3 = "input/" + toFilename( "com.example.pre_dispose.other.MiddleMultiModel" );
    final String input4 = "input/" + toFilename( "com.example.pre_dispose.other.MultiModelInterface1" );
    final String input5 = "input/" + toFilename( "com.example.pre_dispose.other.MultiModelInterface2" );
    final String input6 = "input/" + toFilename( "com.example.pre_dispose.other.MultiModelInterface3" );
    final String output =
      toFilename( "com.example.pre_dispose.Arez_MultiViaInheritanceChainPreDisposeModel" );
    assertSuccessfulFixtureCompile( Arrays.asList( fixture( input1 ),
                                            fixture( input2 ),
                                            fixture( input3 ),
                                            fixture( input4 ),
                                            fixture( input5 ),
                                            fixture( input6 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void validProtectedAccessPreInverseRemove()
    throws Exception
  {
    final String input1 =
      "input/" + toFilename( "com.example.pre_inverse_remove.ProtectedAccessFromBasePreInverseRemoveModel" );
    final String input2 =
      "input/" + toFilename( "com.example.pre_inverse_remove.other.BaseProtectedAccessPreInverseRemoveModel" );
    final String output1 =
      toFilename( "com.example.pre_inverse_remove.Arez_ProtectedAccessFromBasePreInverseRemoveModel" );
    final String output2 =
      toFilename( "com.example.pre_inverse_remove.other.BaseProtectedAccessPreInverseRemoveModel_Arez_Element" );
    assertSuccessfulFixtureCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Arrays.asList( output1, output2 ) );
  }

  @Test
  public void validPublicAccessViaInterfacePreInverseRemove()
    throws Exception
  {
    final String input1 =
      "input/" + toFilename( "com.example.pre_inverse_remove.PublicAccessViaInterfacePreInverseRemoveModel" );
    final String input2 =
      "input/" + toFilename( "com.example.pre_inverse_remove.PreInverseRemoveInterface" );
    final String output1 =
      toFilename( "com.example.pre_inverse_remove.Arez_PublicAccessViaInterfacePreInverseRemoveModel" );
    final String output2 =
      toFilename( "com.example.pre_inverse_remove.PublicAccessViaInterfacePreInverseRemoveModel_Arez_Element" );
    assertSuccessfulFixtureCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Arrays.asList( output1, output2 ) );
  }

  @Test
  public void processSuccessfulMultipleInverseWithSameTarget()
    throws Exception
  {
    final JavaFileObject source1 =
      fixture( "input/com/example/inverse/MultipleReferenceWithInverseWithSameTarget.java" );
    final String output1 = "com/example/inverse/MultipleReferenceWithInverseWithSameTarget_Arez_RoleType.java";
    final String output2 =
      "com/example/inverse/MultipleReferenceWithInverseWithSameTarget_Arez_RoleTypeGeneralisation.java";
    assertSuccessfulFixtureCompile( Collections.singletonList( source1 ), Arrays.asList( output1, output2 ) );
  }

  @Test
  public void processSuccessfulInheritedProtectedAccessInDifferentPackage()
    throws Exception
  {
    final JavaFileObject source1 =
      fixture( "input/com/example/observe/InheritProtectedAccessTrackedModel.java" );
    final JavaFileObject source2 =
      fixture( "input/com/example/observe/other/BaseModelProtectedAccess.java" );
    final String output = "com/example/observe/Arez_InheritProtectedAccessTrackedModel.java";
    assertSuccessfulFixtureCompile( Arrays.asList( source1, source2 ), Collections.singletonList( output ) );
  }

  @Test
  public void processSuccessfulInheritedMultiOnDepsChangeModel()
    throws Exception
  {
    final JavaFileObject source1 =
      fixture( "input/com/example/on_deps_change/InheritedMultiOnDepsChangeModel.java" );
    final JavaFileObject source2 =
      fixture( "input/com/example/on_deps_change/other/BaseInheritedMultiOnDepsChangeModel.java" );
    final String output = "com/example/on_deps_change/Arez_InheritedMultiOnDepsChangeModel.java";
    assertSuccessfulFixtureCompile( Arrays.asList( source1, source2 ), Collections.singletonList( output ) );
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
      "com/example/component_dependency/Arez_TransitivelyDisposeTrackableDependencyModel.java";
    assertSuccessfulFixtureCompile( Arrays.asList( source1, source2 ), Collections.singletonList( output ) );
  }

  @Test
  public void processSuccessfulDependencyThatIsParameterizedInParentClass()
    throws Exception
  {
    final String pkg = "com.example.component_dependency";
    final String output = toFilename( pkg + ".Arez_ParameterizedFieldDependencyInParentModel" );
    assertSuccessfulFixtureCompile( inputs( pkg + ".BaseParameterizedFieldDependencyInParentModel",
                                     pkg + ".ParameterizedFieldDependencyInParentModel" ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void processSuccessfulDependencyThatIsParameterizedInParentClassAndPartiallyResolved()
    throws Exception
  {
    final String pkg = "com.example.component_dependency";
    final String output =
      toFilename( pkg + ".Arez_PartiallyResolvedParameterizedFieldDependencyInParentModel" );
    assertSuccessfulFixtureCompile( inputs( pkg + ".BaseParameterizedFieldDependencyInParentModel",
                                     pkg + ".PartiallyResolvedParameterizedFieldDependencyInParentModel" ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void processSuccessfulDependencyThatIsParameterizedInComponent()
    throws Exception
  {
    final String pkg = "com.example.component_dependency";
    final String output = toFilename( pkg + ".Arez_ParameterizedFieldDependencyModel" );
    assertSuccessfulFixtureCompile( inputs( pkg + ".ParameterizedFieldDependencyModel" ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void processSuccessfulBaseInterfaceInDifferentPackage()
    throws Exception
  {
    final JavaFileObject source1 =
      fixture( "input/com/example/inheritance/CompleteInterfaceModel.java" );
    final JavaFileObject source2 =
      fixture( "input/com/example/inheritance/other/BaseCompleteInterfaceModel.java" );
    final JavaFileObject source3 =
      fixture( "input/com/example/inheritance/other/OtherElement.java" );
    final String output1 = "com/example/inheritance/Arez_CompleteInterfaceModel.java";
    final String output2 = "com/example/inheritance/other/Arez_OtherElement.java";
    assertSuccessfulFixtureCompile( Arrays.asList( source1, source2, source3 ), Arrays.asList( output1, output2 ) );
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
    final String output1 = "com/example/inheritance/Arez_CompleteModel.java";
    final String output2 = "com/example/inheritance/other/Arez_Element.java";
    assertSuccessfulFixtureCompile( Arrays.asList( source1, source2, source3 ), Arrays.asList( output1, output2 ) );
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
    final String output = "com/example/override_generics/Arez_MyArezReactComponent_.java";
    assertSuccessfulFixtureCompile( Arrays.asList( source1, source2, source3, source4 ), Collections.singletonList( output ) );
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
    final String output = "com/example/override_generics/Arez_LeafModel.java";
    assertSuccessfulFixtureCompile( Arrays.asList( source1, source2, source3 ), Collections.singletonList( output ) );
  }

  @Test
  public void processSuccessfulInverseInDifferentPackage()
    throws Exception
  {
    final JavaFileObject source1 =
      fixture( "input/com/example/inverse/PackageAccessWithDifferentPackageInverseModel.java" );
    final JavaFileObject source2 =
      fixture( "input/com/example/inverse/other/Element.java" );
    final String output1 = "com/example/inverse/Arez_PackageAccessWithDifferentPackageInverseModel.java";
    final String output2 = "com/example/inverse/other/Arez_Element.java";
    assertSuccessfulFixtureCompile( Arrays.asList( source1, source2 ), Arrays.asList( output1, output2 ) );
  }

  @Test
  public void processSuccessfulToStringInPresent()
    throws Exception
  {
    final JavaFileObject source1 =
      fixture( "input/com/example/to_string/ToStringPresentInParent.java" );
    final JavaFileObject source2 = fixture( "input/com/example/to_string/ParentType.java" );
    final String output = "com/example/to_string/Arez_ToStringPresentInParent.java";
    assertSuccessfulFixtureCompile( Arrays.asList( source1, source2 ), Collections.singletonList( output ) );
  }

  @Test
  public void processSuccessfulNestedCompile()
    throws Exception
  {
    assertSuccessfulFixtureCompile( "NestedModel", "NestedModel_Arez_BasicActionModel.java" );
  }

  @Test
  public void processSuccessfulNestedNestedCompile()
    throws Exception
  {
    assertSuccessfulFixtureCompile( "NestedNestedModel", "NestedNestedModel_Something_Arez_BasicActionModel.java" );
  }

  @Test
  public void processSuccessfulWhereAnnotationsSourcedFromInterface()
    throws Exception
  {
    final JavaFileObject source1 = fixture( "input/DefaultMethodsModel.java" );
    final JavaFileObject source2 = fixture( "input/MyAnnotatedInterface.java" );
    final String output1 = "Arez_DefaultMethodsModel.java";
    assertSuccessfulFixtureCompile( Arrays.asList( source1, source2 ), Collections.singletonList( output1 ) );
  }

  @Test
  public void processSuccessfulRequiresTransactionInheritedFromBaseClass()
    throws Exception
  {
    final String classname = "com.example.requires_transaction.InheritedRequiresTransactionModel";
    final String[] expectedOutputResources = deriveExpectedOutputs( classname );
    final JavaFileObject input1 = fixture( "input/" + toFilename( classname ) );
    final JavaFileObject input2 =
      fixture( "input/" + toFilename( "com.example.requires_transaction.BaseInheritedRequiresTransactionModel" ) );
    assertSuccessfulFixtureCompile( Arrays.asList( input1, input2 ), Arrays.asList( expectedOutputResources ) );
  }

  @Test
  public void processSuccessfulRequiresTransactionSourcedFromInterfaceDefaultMethod()
    throws Exception
  {
    final String classname = "com.example.requires_transaction.DefaultMethodRequiresTransactionModel";
    final String[] expectedOutputResources = deriveExpectedOutputs( classname );
    final JavaFileObject input1 = fixture( "input/" + toFilename( classname ) );
    final JavaFileObject input2 =
      fixture( "input/" + toFilename( "com.example.requires_transaction.DefaultRequiresTransactionMethods" ) );
    assertSuccessfulFixtureCompile( Arrays.asList( input1, input2 ), Arrays.asList( expectedOutputResources ) );

  }

  @Test
  public void processSuccessfulWhereTypeResolvedInInheritanceHierarchy()
    throws Exception
  {
    final JavaFileObject source1 = fixture( "input/com/example/type_params/AbstractModel.java" );
    final JavaFileObject source2 = fixture( "input/com/example/type_params/MiddleModel.java" );
    final JavaFileObject source3 = fixture( "input/com/example/type_params/ConcreteModel.java" );
    final String output1 = "com/example/type_params/Arez_ConcreteModel.java";
    assertSuccessfulFixtureCompile( Arrays.asList( source1, source2, source3 ), Collections.singletonList( output1 ) );
  }

  @Test
  public void processResolvedParameterizedType()
    throws Exception
  {
    final JavaFileObject source1 =
      fixture( "input/com/example/parameterized_type/ParentModel.java" );
    final JavaFileObject source2 =
      fixture( "input/com/example/parameterized_type/ResolvedModel.java" );
    final String output1 = "com/example/parameterized_type/Arez_ResolvedModel.java";
    assertSuccessfulFixtureCompile( Arrays.asList( source1, source2 ), Collections.singletonList( output1 ) );
  }

  @Test
  public void processUnresolvedParameterizedType()
    throws Exception
  {
    final JavaFileObject source1 =
      fixture( "input/com/example/parameterized_type/ParentModel.java" );
    final JavaFileObject source2 =
      fixture( "input/com/example/parameterized_type/UnresolvedModel.java" );
    final String output1 = "com/example/parameterized_type/Arez_UnresolvedModel.java";
    assertSuccessfulFixtureCompile( Arrays.asList( source1, source2 ), Collections.singletonList( output1 ) );
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
    final String output1 = "com/example/override_generics/Arez_GenericsModel.java";
    assertSuccessfulFixtureCompile( Arrays.asList( source1, source2, source3 ), Collections.singletonList( output1 ) );
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
    final String output1 = "com/example/inheritance/Arez_MyModel.java";
    assertSuccessfulFixtureCompile( Arrays.asList( source1, source2, source3, source4, source5 ),
                             Collections.singletonList( output1 ) );
  }

  @Test
  public void processSuccessfulWhereTraceInheritanceChainInInterfaces()
    throws Exception
  {
    final JavaFileObject source1 =
      fixture( "input/com/example/inheritance/interface_inheritance/MyBaseInterface.java" );
    final JavaFileObject source2 = fixture( "input/com/example/inheritance/interface_inheritance/MyInterface.java" );
    final JavaFileObject source3 = fixture( "input/com/example/inheritance/interface_inheritance/MyModel.java" );
    final String output1 = "com/example/inheritance/interface_inheritance/Arez_MyModel.java";
    assertSuccessfulFixtureCompile( Arrays.asList( source1, source2, source3 ),
                             Collections.singletonList( output1 ) );
  }

  private void assertSuccessfulFixtureCompile( @Nonnull final String classname )
    throws Exception
  {
    assertSuccessfulFixtureCompile( inputs( classname ), Arrays.asList( deriveExpectedOutputs( classname ) ) );
  }

  private void assertSuccessfulFixtureCompile( @Nonnull final String classname,
                                               @Nonnull final String... expectedOutputResources )
    throws Exception
  {
    assertSuccessfulFixtureCompile( inputs( classname ), Arrays.asList( expectedOutputResources ) );
  }

  private void assertSuccessfulFixtureCompile( @Nonnull final List<JavaFileObject> inputs,
                                               @Nonnull final List<String> expectedOutputs )
    throws Exception
  {
    final List<String> unformattedOptions = new ArrayList<>( getOptions() );
    unformattedOptions.add( "-A" + getOptionPrefix() + ".format_generated_source=false" );
    assertSuccessfulFixtureCompile( inputs, expectedOutputs, "expected", unformattedOptions );
    assertSuccessfulFixtureCompile( inputs, expectedOutputs, "expectedFormatted", getOptions() );
  }

  private void assertSuccessfulFixtureCompile( @Nonnull final List<JavaFileObject> inputs,
                                               @Nonnull final List<String> expectedOutputs,
                                               @Nonnull final String expectedDirectory,
                                               @Nonnull final List<String> options )
    throws Exception
  {
    final Compilation compilation =
      CompileTestUtil.assertCompilesWithoutWarnings( inputs, options, processors(), Collections.emptyList() );
    try
    {
      outputFilesIfEnabled( compilation, expectedDirectory, this::emitGeneratedFile );

      final List<String> actualOutputs =
        Stream.concat( compilation.sourceOutputFilenames().stream(), compilation.classOutputFilenames().stream() ).
          filter( this::emitGeneratedFile ).
          map( this::normalizeOutputPath ).
          sorted().
          toList();
      final List<String> normalizedExpectedOutputs =
        expectedOutputs.stream().map( this::normalizeOutputPath ).sorted().toList();
      assertEquals( new TreeSet<>( actualOutputs ).size(),
                    actualOutputs.size(),
                    "Generated output paths must be unique" );
      assertEquals( new TreeSet<>( normalizedExpectedOutputs ).size(),
                    normalizedExpectedOutputs.size(),
                    "Expected output paths must be unique" );
      assertEquals( actualOutputs,
                    normalizedExpectedOutputs,
                    "Generated outputs differ from declared outputs for " + expectedDirectory );

      for ( final String output : normalizedExpectedOutputs )
      {
        final Path expected = fixtureDir().resolve( expectedDirectory ).resolve( output );
        assertTrue( Files.exists( expected ), "Missing expected output " + output );
        final Path sourceOutput = compilation.sourceOutput().resolve( output );
        final Path classOutput = compilation.classOutput().resolve( output );
        final Path actual = Files.exists( sourceOutput ) ? sourceOutput : classOutput;
        assertTrue( Files.exists( actual ), "Missing generated output " + output );
        CompileTestUtil.assertSourceMatchesTarget( expected, actual );
      }
    }
    finally
    {
      deleteDir( compilation.sourceOutput() );
      deleteDir( compilation.classOutput() );
    }
  }

  @Nonnull
  private String normalizeOutputPath( @Nonnull final String path )
  {
    return path.replace( '\\', '/' );
  }

  @SuppressWarnings( "ResultOfMethodCallIgnored" )
  private void deleteDir( @Nonnull final Path directory )
  {
    try ( var paths = Files.walk( directory ) )
    {
      paths.sorted( Comparator.reverseOrder() ).map( Path::toFile ).forEach( File::delete );
    }
    catch ( final IOException e )
    {
      throw new IllegalStateException( "Failure to delete directory: " + directory, e );
    }
  }

  @Nonnull
  private String[] deriveExpectedOutputs( @Nonnull final String classname )
  {
    final List<String> expectedOutputs = new ArrayList<>();
    expectedOutputs.add( toFilename( classname, "Arez_", ".java" ) );
    switch ( classname )
    {
      case "com.example.cascade_dispose.ComponentCascadeDisposeModel":
        expectedOutputs.add( "com/example/cascade_dispose/ComponentCascadeDisposeModel_Arez_MyComponent.java" );
        break;
      case "com.example.cascade_dispose.ComponentCascadeDisposeMethodModel":
        expectedOutputs.add( "com/example/cascade_dispose/ComponentCascadeDisposeMethodModel_Arez_MyComponent.java" );
        break;
      case "com.example.cascade_dispose.ParameterizedComponentCascadeDisposeMethodModel":
        expectedOutputs.add( "com/example/cascade_dispose/ParameterizedComponentCascadeDisposeMethodModel_Arez_EventDrivenValue.java" );
        break;
      case "com.example.cascade_dispose.ObservableCascadeDisposeModel":
        expectedOutputs.add( "com/example/cascade_dispose/ObservableCascadeDisposeModel_Arez_MyComponent.java" );
        break;
      case "com.example.component_dependency.ComponentDependencyModel":
        expectedOutputs.add( "com/example/component_dependency/ComponentDependencyModel_Arez_Foo.java" );
        break;
      case "com.example.component_dependency.MultiComponentDependencyModel":
        expectedOutputs.add( "com/example/component_dependency/MultiComponentDependencyModel_Arez_Foo.java" );
        break;
      case "com.example.component_dependency.ComponentFieldDependencyModel":
        expectedOutputs.add( "com/example/component_dependency/ComponentFieldDependencyModel_Arez_Foo.java" );
        break;
      case "com.example.inverse.CustomNamesInverseModel":
      case "com.example.inverse.DefaultMultiplicityInverseModel":
      case "com.example.inverse.DisableInverseModel":
      case "com.example.inverse.NonGetterInverseModel":
      case "com.example.inverse.NonObservableCollectionInverseModel":
      case "com.example.inverse.NonObservableNullableManyReferenceModel":
      case "com.example.inverse.NonObservableNullableOneReferenceModel":
      case "com.example.inverse.NonObservableNullableZeroOrOneReferenceModel":
      case "com.example.inverse.NonStandardNameModel":
      case "com.example.inverse.ObservableCollectionInverseModel":
      case "com.example.inverse.ObservableListInverseModel":
      case "com.example.inverse.ObservableManyReferenceModel":
      case "com.example.inverse.ObservableOneReferenceModel":
      case "com.example.inverse.ObservableReferenceInverseModel":
      case "com.example.inverse.ObservableSetInverseModel":
      case "com.example.inverse.ObservableZeroOrOneReferenceModel":
      case "com.example.inverse.OneMultiplicityInverseModel":
      case "com.example.inverse.ZeroOrOneMultiplicityInverseModel":
      case "com.example.post_inverse_add.BasicPostInverseAddModel":
      case "com.example.post_inverse_add.MultiPostInverseAddModel":
      case "com.example.post_inverse_add.PackageAccessPostInverseAddModel":
      case "com.example.post_inverse_add.SingularInversePostInverseAddModel":
      case "com.example.pre_inverse_remove.BasicPreInverseRemoveModel":
      case "com.example.pre_inverse_remove.MultiPreInverseRemoveModel":
      case "com.example.pre_inverse_remove.PackageAccessPreInverseRemoveModel":
      case "com.example.pre_inverse_remove.SingularInversePreInverseRemoveModel":
        expectedOutputs.add( toFilename( classname, "", "_Arez_Element.java" ) );
        break;
      case "com.example.reference.CascadeDisposeReferenceModel":
        expectedOutputs.add( "com/example/reference/CascadeDisposeReferenceModel_Arez_MyEntity.java" );
        break;
      default:
        break;
    }
    return expectedOutputs.toArray( new String[ 0 ] );
  }
}
