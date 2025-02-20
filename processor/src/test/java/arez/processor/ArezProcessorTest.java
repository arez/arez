package arez.processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.processing.Processor;
import javax.tools.JavaFileObject;
import org.realityforge.proton.qa.AbstractProcessorTest;
import org.realityforge.proton.qa.Compilation;
import org.realityforge.proton.qa.CompileTestUtil;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public final class ArezProcessorTest
  extends AbstractProcessorTest
{
  @DataProvider( name = "successfulCompiles" )
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
        new Object[]{ "com.example.action.ReadOnlyActionModel" },
        new Object[]{ "com.example.action.RequiresNewTxTypeActionModel" },
        new Object[]{ "com.example.action.RequiresTxTypeActionModel" },
        new Object[]{ "com.example.action.BasicFunctionActionModel" },
        new Object[]{ "com.example.action.BasicActionModel" },
        new Object[]{ "com.example.cascade_dispose.ComponentCascadeDisposeModel" },
        new Object[]{ "com.example.cascade_dispose.ComponentCascadeDisposeMethodModel" },
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

        new Object[]{ "com.example.component_dependency.AbstractObservableDependency" },
        new Object[]{ "com.example.component_dependency.ActAsComponentFieldDependencyModel" },
        new Object[]{ "com.example.component_dependency.ActAsComponentMethodDependencyModel" },
        new Object[]{ "com.example.component_dependency.BasicDependencyModel" },
        new Object[]{ "com.example.component_dependency.BasicFieldDependencyModel" },
        new Object[]{ "com.example.component_dependency.CascadeDependencyModel" },
        new Object[]{ "com.example.component_dependency.CascadeFieldDependencyModel" },
        new Object[]{ "com.example.component_dependency.ComplexDependencyModel" },
        new Object[]{ "com.example.component_dependency.ComplexDependencyWithCustomNameMethodModel" },
        new Object[]{ "com.example.component_dependency.ComponentDependencyModel" },
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
        new Object[]{ "com.example.memoize.DefaultDefaultPriorityUnspecifiedLocalPriorityMemoizeModel" },
        new Object[]{ "com.example.memoize.DefaultPriorityDefaultLocalPriorityMemoizeModel" },
        new Object[]{ "com.example.memoize.DefaultPrioritySpecifiedLocalPriorityMemoizeModel" },
        new Object[]{ "com.example.memoize.DefaultPriorityUnspecifiedLocalPriorityMemoizeModel" },
        new Object[]{ "com.example.memoize.LocalTypeParamModel" },
        new Object[]{ "com.example.memoize.NonStandardNameModel" },
        new Object[]{ "com.example.memoize.TypeParamModel" },
        new Object[]{ "com.example.observable.AbstractNonPrimitiveObservablesModel" },
        new Object[]{ "com.example.observable.AbstractObservablesModel" },
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
    assertSuccessfulCompile( classname );
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
    assertSuccessfulCompile( Arrays.asList( input1, input2 ), Arrays.asList( expectedOutputResources ) );
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
    assertSuccessfulCompile( Arrays.asList( input1, input2 ), Arrays.asList( expectedOutputResources ) );
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
    assertSuccessfulCompile( Arrays.asList( input1, input2 ), Arrays.asList( expectedOutputResources ) );
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
    assertSuccessfulCompile( Arrays.asList( input1, input2 ), Arrays.asList( expectedOutputResources ) );
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
    assertSuccessfulCompile( Arrays.asList( input1, input2 ), Arrays.asList( expectedOutputResources ) );
  }

  @Test
  public void rawTypesUsageModel()
    throws Exception
  {
    // Use deprecated types but arez should suppress the warnings and generate code that has no warnings...
    final String classname = "com.example.raw_types.RawTypesUsageModel";
    final String[] expectedOutputResources = deriveExpectedOutputs( classname );
    final JavaFileObject input1 = fixture( "input/" + toFilename( classname ) );
    assertSuccessfulCompile( Collections.singletonList( input1 ), Arrays.asList( expectedOutputResources ) );
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
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
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
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
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
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
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
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
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
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
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
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
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
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
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
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
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
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
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
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
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
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
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
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
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
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
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
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
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
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
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
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
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
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
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
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
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
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
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
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
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
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
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
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
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
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
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
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
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
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ),
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
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
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
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void multiViaInheritancePostDispose()
    throws Exception
  {
    final String pkg = "com.example.post_dispose";
    final String output =
      toFilename( pkg + ".Arez_MultiViaInheritanceChainPostDisposeModel" );
    assertSuccessfulCompile( inputs( pkg + ".MultiViaInheritanceChainPostDisposeModel",
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
    final String output =
      toFilename( "com.example.post_inverse_add.Arez_ProtectedAccessFromBasePostInverseAddModel" );
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void validPublicAccessViaInterfacePostInverseAdd()
    throws Exception
  {
    final String input1 =
      "input/" + toFilename( "com.example.post_inverse_add.PublicAccessViaInterfacePostInverseAddModel" );
    final String input2 =
      "input/" + toFilename( "com.example.post_inverse_add.PostInverseAddInterface" );
    final String output =
      toFilename( "com.example.post_inverse_add.Arez_PublicAccessViaInterfacePostInverseAddModel" );
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
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
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
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
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
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
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ),
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
    final String output =
      toFilename( "com.example.pre_inverse_remove.Arez_ProtectedAccessFromBasePreInverseRemoveModel" );
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void validPublicAccessViaInterfacePreInverseRemove()
    throws Exception
  {
    final String input1 =
      "input/" + toFilename( "com.example.pre_inverse_remove.PublicAccessViaInterfacePreInverseRemoveModel" );
    final String input2 =
      "input/" + toFilename( "com.example.pre_inverse_remove.PreInverseRemoveInterface" );
    final String output =
      toFilename( "com.example.pre_inverse_remove.Arez_PublicAccessViaInterfacePreInverseRemoveModel" );
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
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
    assertSuccessfulCompile( Collections.singletonList( source1 ), Arrays.asList( output1, output2 ) );
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
      "com/example/component_dependency/Arez_TransitivelyDisposeTrackableDependencyModel.java";
    assertSuccessfulCompile( Arrays.asList( source1, source2 ), Collections.singletonList( output ) );
  }

  @Test
  public void processSuccessfulDependencyThatIsParameterizedInParentClass()
    throws Exception
  {
    final String pkg = "com.example.component_dependency";
    final String output = toFilename( pkg + ".Arez_ParameterizedFieldDependencyInParentModel" );
    assertSuccessfulCompile( inputs( pkg + ".BaseParameterizedFieldDependencyInParentModel",
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
    assertSuccessfulCompile( inputs( pkg + ".BaseParameterizedFieldDependencyInParentModel",
                                     pkg + ".PartiallyResolvedParameterizedFieldDependencyInParentModel" ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void processSuccessfulDependencyThatIsParameterizedInComponent()
    throws Exception
  {
    final String pkg = "com.example.component_dependency";
    final String output = toFilename( pkg + ".Arez_ParameterizedFieldDependencyModel" );
    assertSuccessfulCompile( inputs( pkg + ".ParameterizedFieldDependencyModel" ),
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
    final String output = "com/example/inheritance/Arez_CompleteInterfaceModel.java";
    assertSuccessfulCompile( Arrays.asList( source1, source2, source3 ), Collections.singletonList( output ) );
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
    final String output = "com/example/inheritance/Arez_CompleteModel.java";
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
    final String output = "com/example/override_generics/Arez_MyArezReactComponent_.java";
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
    final String output = "com/example/override_generics/Arez_LeafModel.java";
    assertSuccessfulCompile( Arrays.asList( source1, source2, source3 ), Collections.singletonList( output ) );
  }

  @Test
  public void processSuccessfulInverseInDifferentPackage()
    throws Exception
  {
    final JavaFileObject source1 =
      fixture( "input/com/example/inverse/PackageAccessWithDifferentPackageInverseModel.java" );
    final JavaFileObject source2 =
      fixture( "input/com/example/inverse/other/Element.java" );
    final String output = "com/example/inverse/Arez_PackageAccessWithDifferentPackageInverseModel.java";
    assertSuccessfulCompile( Arrays.asList( source1, source2 ), Collections.singletonList( output ) );
  }

  @Test
  public void processSuccessfulToStringInPresent()
    throws Exception
  {
    final JavaFileObject source1 =
      fixture( "input/com/example/to_string/ToStringPresentInParent.java" );
    final JavaFileObject source2 = fixture( "input/com/example/to_string/ParentType.java" );
    final String output = "com/example/to_string/Arez_ToStringPresentInParent.java";
    assertSuccessfulCompile( Arrays.asList( source1, source2 ), Collections.singletonList( output ) );
  }

  @Test
  public void processSuccessfulServiceViaContributeToStingModel()
    throws Exception
  {
    final String pkg = "com.example.sting.autofragment";

    final List<JavaFileObject> inputs =
      inputs( pkg + ".ServiceViaContributeToStingModel",
              pkg + ".MyAutoFragment",

              // The following input exists so that the synthesizing processor has types to "process"
              pkg + ".MyFramework",
              pkg + ".MyFrameworkModel" );

    // This one is just used to keep synthesizer running
    final Processor synthesizingProcessor1 =
      newSynthesizingProcessor( "input", pkg + ".MyFrameworkModelImpl", 1 );
    // this synthesizer produces java file that we are using in test
    final Processor synthesizingProcessor2 =
      newSynthesizingProcessor( "input", pkg + ".OtherModel", 2 );

    final Compilation compilation =
      CompileTestUtil.compile( inputs,
                               getOptions(),
                               Arrays.asList( synthesizingProcessor1, synthesizingProcessor2, processor() ),
                               Collections.emptyList() );
    outputFilesIfEnabled( compilation,
                          compilation.sourceOutputFilenames().stream().filter( this::emitGeneratedFile ).toList(),
                          compilation.classOutputFilenames().stream().filter( this::emitGeneratedFile ).toList() );
    assertCompilationSuccessful( compilation );
  }

  @Test
  public void processSuccessfulNestedCompile()
    throws Exception
  {
    assertSuccessfulCompile( "NestedModel", "NestedModel_Arez_BasicActionModel.java" );
  }

  @Test
  public void processSuccessfulNestedNestedCompile()
    throws Exception
  {
    assertSuccessfulCompile( "NestedNestedModel", "NestedNestedModel_Something_Arez_BasicActionModel.java" );
  }

  @Test
  public void processSuccessfulWhereAnnotationsSourcedFromInterface()
    throws Exception
  {
    final JavaFileObject source1 = fixture( "input/DefaultMethodsModel.java" );
    final JavaFileObject source2 = fixture( "input/MyAnnotatedInterface.java" );
    final String output1 = "Arez_DefaultMethodsModel.java";
    assertSuccessfulCompile( Arrays.asList( source1, source2 ), Collections.singletonList( output1 ) );
  }

  @Test
  public void processSuccessfulWhereTypeResolvedInInheritanceHierarchy()
    throws Exception
  {
    final JavaFileObject source1 = fixture( "input/com/example/type_params/AbstractModel.java" );
    final JavaFileObject source2 = fixture( "input/com/example/type_params/MiddleModel.java" );
    final JavaFileObject source3 = fixture( "input/com/example/type_params/ConcreteModel.java" );
    final String output1 = "com/example/type_params/Arez_ConcreteModel.java";
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
    final String output1 = "com/example/parameterized_type/Arez_ResolvedModel.java";
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
    final String output1 = "com/example/parameterized_type/Arez_UnresolvedModel.java";
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
    final String output1 = "com/example/override_generics/Arez_GenericsModel.java";
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
    final String output1 = "com/example/inheritance/Arez_MyModel.java";
    assertSuccessfulCompile( Arrays.asList( source1, source2, source3, source4, source5 ),
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
    assertSuccessfulCompile( Arrays.asList( source1, source2, source3 ),
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
        new Object[]{ "com.example.action.BadActionName1Model",
                      "@Action target specified an invalid value 'assert' for the parameter name. The value must not be a java keyword" },
        new Object[]{ "com.example.action.BadActionName2Model",
                      "@Action target specified an invalid value 'ace-' for the parameter name. The value must be a valid java identifier" },
        new Object[]{ "com.example.action.DuplicateActionModel",
                      "Method annotated with @Action specified name ace that duplicates @Action defined by method setField" },
        new Object[]{ "com.example.action.JaxWsActionModel",
                      "@javax.xml.ws.Action annotation not supported in components annotated with @ArezComponent, use the @arez.annotations.Action annotation instead." },
        new Object[]{ "com.example.action.PrivateActionModel", "@Action target must not be private" },
        new Object[]{ "com.example.action.StaticActionModel", "@Action target must not be static" },

        new Object[]{ "com.example.observe.ApplicationExecutorButNoOnDepsChangeModel",
                      "@Observe target defined parameter executor=EXTERNAL but does not specify an @OnDepsChange method." },
        new Object[]{ "com.example.observe.ArezExecutorOnDepsChangeButNoObserverRefModel",
                      "@Observe target with parameter executor=INTERNAL defined an @OnDepsChange method but has not defined an @ObserverRef method nor does the @OnDepsChange annotated method have an arez.Observer parameter. This results in an impossible to schedule observer." },
        new Object[]{ "com.example.observe.NonArezDependenciesButNoObserverRefModel",
                      "@Observe target with parameter depType=AREZ_OR_EXTERNAL has not defined an @ObserverRef method and thus can not invoke reportStale()." },
        new Object[]{ "com.example.observe.ObserveAbstractModel", "@Observe target must not be abstract" },
        new Object[]{ "com.example.observe.ObserveBadNameModel",
                      "@Observe target specified an invalid name '-ace'. The name must be a valid java identifier." },
        new Object[]{ "com.example.observe.ObserveBadNameModel2",
                      "@Observe target specified an invalid name 'float'. The name must not be a java keyword." },
        new Object[]{ "com.example.observe.ObserveDuplicateModel",
                      "@Observe target duplicates existing method named foo" },
        new Object[]{ "com.example.observe.ObserveDuplicateModel2",
                      "Method annotated with @Memoize specified name doStuff that duplicates @Observe defined by method foo" },
        new Object[]{ "com.example.observe.ObserveFinalModel", "@Observe target must not be final" },
        new Object[]{ "com.example.observe.ObserveParametersModel", "@Observe target must not have any parameters" },
        new Object[]{ "com.example.observe.ObservePrivateModel", "@Observe target must not be private" },
        new Object[]{ "com.example.observe.ObservePublicModel", "@Observe target must not be public" },
        new Object[]{ "com.example.observe.ObserveReturnsValueModel", "@Observe target must not return a value" },
        new Object[]{ "com.example.observe.ObserveStaticModel", "@Observe target must not be static" },
        new Object[]{ "com.example.observe.ObserveThrowsExceptionModel",
                      "@Observe target must not throw any exceptions" },
        new Object[]{ "com.example.observe.ReportResultArezExecutorModel",
                      "@Observe target must not specify reportResult parameter when executor=INTERNAL" },
        new Object[]{ "com.example.observe.ReportParametersArezExecutorModel",
                      "@Observe target must not specify reportParameters parameter when executor=INTERNAL" },

        new Object[]{ "com.example.cascade_dispose.AbstractMethodComponent",
                      "@CascadeDispose target must not be abstract unless the method is also annotated with the @Observable or @Reference annotation." },
        new Object[]{ "com.example.cascade_dispose.BadType1Component",
                      "@CascadeDispose target must be assignable to arez.Disposable or a type annotated with the @ArezComponent annotation where the disposeNotifier does not resolve to DISABLE" },
        new Object[]{ "com.example.cascade_dispose.BadType1MethodComponent",
                      "@CascadeDispose target must return a type assignable to arez.Disposable or a type annotated with @ArezComponent" },
        new Object[]{ "com.example.cascade_dispose.BadType2Component",
                      "@CascadeDispose target must be assignable to arez.Disposable or a type annotated with the @ArezComponent annotation where the disposeNotifier does not resolve to DISABLE" },
        new Object[]{ "com.example.cascade_dispose.BadType2MethodComponent",
                      "@CascadeDispose target must return a type assignable to arez.Disposable or a type annotated with @ArezComponent" },
        new Object[]{ "com.example.cascade_dispose.BadType3Component",
                      "@CascadeDispose target must be assignable to arez.Disposable or a type annotated with the @ArezComponent annotation where the disposeNotifier does not resolve to DISABLE" },
        new Object[]{ "com.example.cascade_dispose.BadType3MethodComponent",
                      "@CascadeDispose target must return a type assignable to arez.Disposable or a type annotated with @ArezComponent" },
        new Object[]{ "com.example.cascade_dispose.ConcreteObservableMethodComponent",
                      "@CascadeDispose target must be abstract if the method is also annotated with the @Observable or @Reference annotation." },
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

        new Object[]{ "com.example.component.ConcreteComponent", "@ArezComponent target must be abstract" },
        new Object[]{ "com.example.component.ModelWithAbstractMethod",
                      "@ArezComponent target has an abstract method not implemented by framework. The method is named someMethod" },
        new Object[]{ "com.example.component.BadTypeComponent",
                      "@ArezComponent target specified an invalid name ''. The name must be a valid java identifier." },
        new Object[]{ "com.example.component.BadTypeComponent2",
                      "@ArezComponent target specified an invalid name 'long'. The name must not be a java keyword." },
        new Object[]{ "com.example.component.EmptyComponent",
                      "@ArezComponent target has no methods annotated with @Action, @CascadeDispose, @Memoize, @Observable, @Inverse, @Reference, @ComponentDependency or @Observe" },
        new Object[]{ "com.example.component.EmptyTypeComponent",
                      "@ArezComponent target specified an invalid name ''. The name must be a valid java identifier." },
        new Object[]{ "com.example.component.EnumModel", "@ArezComponent target must be a class or an interface" },
        new Object[]{ "com.example.component.FinalModel", "@ArezComponent target must not be final" },
        new Object[]{ "com.example.component.NonObservableWithDisposeOnDeactivateModel",
                      "@ArezComponent target has specified observable = DISABLE and disposeOnDeactivate = true which is not a valid combination" },
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
        new Object[]{ "com.example.component_id.ComponentIdNotPrivateModel",
                      "@ComponentId target must not be private" },
        new Object[]{ "com.example.component_id.ComponentIdNotStaticModel", "@ComponentId target must not be static" },

        new Object[]{ "com.example.component_type_name_ref.BadReturnTypeComponentTypeNameRefModel",
                      "@ComponentTypeNameRef target must return an instance of java.lang.String" },
        new Object[]{ "com.example.component_type_name_ref.FinalComponentTypeNameRefModel",
                      "@ComponentTypeNameRef target must be abstract" },
        new Object[]{ "com.example.component_type_name_ref.ParameterizedComponentTypeNameRefModel",
                      "@ComponentTypeNameRef target must not have any parameters" },
        new Object[]{ "com.example.component_type_name_ref.PrivateComponentTypeNameRefModel",
                      "@ComponentTypeNameRef target must be abstract" },
        new Object[]{ "com.example.component_type_name_ref.StaticComponentTypeNameRefModel",
                      "@ComponentTypeNameRef target must be abstract" },
        new Object[]{ "com.example.component_type_name_ref.ThrowsComponentTypeNameRefModel",
                      "@ComponentTypeNameRef target must not throw any exceptions" },
        new Object[]{ "com.example.component_type_name_ref.VoidComponentTypeNameRefModel",
                      "@ComponentTypeNameRef target must return a value" },

        new Object[]{ "com.example.component_id_ref.BadType1Model",
                      "@ComponentIdRef target has a return type java.lang.String but no @ComponentId annotated method. The type is expected to be of type int." },
        new Object[]{ "com.example.component_id_ref.BadType2Model",
                      "@ComponentIdRef target has a return type java.lang.String and a @ComponentId annotated method with a return type java.lang.String. The types must match." },
        new Object[]{ "com.example.component_id_ref.ConcreteModel", "@ComponentIdRef target must be abstract" },
        new Object[]{ "com.example.component_id_ref.FinalModel", "@ComponentIdRef target must be abstract" },
        new Object[]{ "com.example.component_id_ref.NoReturnModel", "@ComponentIdRef target must return a value" },
        new Object[]{ "com.example.component_id_ref.ParametersModel",
                      "@ComponentIdRef target must not have any parameters" },
        new Object[]{ "com.example.component_id_ref.PrivateModel", "@ComponentIdRef target must be abstract" },
        new Object[]{ "com.example.component_id_ref.StaticModel", "@ComponentIdRef target must be abstract" },
        new Object[]{ "com.example.component_id_ref.ThrowsModel",
                      "@ComponentIdRef target must not throw any exceptions" },

        new Object[]{ "com.example.component_name_ref.ComponentNameRefFinalModel",
                      "@ComponentNameRef target must be abstract" },
        new Object[]{ "com.example.component_name_ref.ComponentNameRefMustNotHaveParametersModel",
                      "@ComponentNameRef target must not have any parameters" },
        new Object[]{ "com.example.component_name_ref.ComponentNameRefMustReturnValueModel",
                      "@ComponentNameRef target must return a value" },
        new Object[]{ "com.example.component_name_ref.ComponentNameRefPrivateModel",
                      "@ComponentNameRef target must be abstract" },
        new Object[]{ "com.example.component_name_ref.ComponentNameRefStaticModel",
                      "@ComponentNameRef target must be abstract" },
        new Object[]{ "com.example.component_name_ref.ComponentNameRefThrowsExceptionModel",
                      "@ComponentNameRef target must not throw any exceptions" },

        new Object[]{ "com.example.memoize.AbstractModel", "@Memoize target must not be abstract" },
        new Object[]{ "com.example.memoize.BadName2Model",
                      "@Memoize target specified an invalid name 'public'. The name must not be a java keyword." },
        new Object[]{ "com.example.memoize.BadName1Model",
                      "@Memoize target specified an invalid name '-ace'. The name must be a valid java identifier." },
        new Object[]{ "com.example.memoize.BadStreamTypeModel",
                      "@Memoize target must not return a value of type java.util.stream.Stream as the type is single use and thus does not make sense to cache as a computable value" },
        new Object[]{ "com.example.memoize.ThrowsExceptionModel", "@Memoize target must not throw any exceptions" },
        new Object[]{ "com.example.memoize.DuplicateModel",
                      "Method annotated with @Memoize specified name ace that duplicates @Memoize defined by method getX" },
        new Object[]{ "com.example.memoize.FinalModel", "@Memoize target must not be final" },
        new Object[]{ "com.example.memoize.KeepAliveWithParametersModel",
                      "@Memoize target specified parameter keepAlive as true but has parameters." },
        new Object[]{ "com.example.memoize.KeepAliveWithContextParametersModel",
                      "@Memoize target specified parameter keepAlive as true but has matching context parameters." },
        new Object[]{ "com.example.memoize.MissingComputableValueRefModel",
                      "@Memoize target specified depType = AREZ_OR_EXTERNAL but there is no associated @ComputableValueRef method." },
        new Object[]{ "com.example.memoize.PrivateModel", "@Memoize target must not be private" },
        new Object[]{ "com.example.memoize.StaticModel", "@Memoize target must not be static" },
        new Object[]{ "com.example.memoize.ReturnVoidModel", "@Memoize target must return a value" },

        new Object[]{ "com.example.component_ref.FinalModel", "@ComponentRef target must be abstract" },
        new Object[]{ "com.example.component_ref.StaticModel", "@ComponentRef target must be abstract" },
        new Object[]{ "com.example.component_ref.PrivateModel", "@ComponentRef target must be abstract" },
        new Object[]{ "com.example.component_ref.VoidModel", "@ComponentRef target must return a value" },
        new Object[]{ "com.example.component_ref.BadTypeModel",
                      "@ComponentRef target must return an instance of arez.Component" },
        new Object[]{ "com.example.component_ref.ThrowsExceptionModel",
                      "@ComponentRef target must not throw any exceptions" },
        new Object[]{ "com.example.component_ref.ParametersModel",
                      "@ComponentRef target must not have any parameters" },

        new Object[]{ "com.example.component_state_ref.FinalModel", "@ComponentStateRef target must be abstract" },
        new Object[]{ "com.example.component_state_ref.StaticModel", "@ComponentStateRef target must be abstract" },
        new Object[]{ "com.example.component_state_ref.PrivateModel", "@ComponentStateRef target must be abstract" },
        new Object[]{ "com.example.component_state_ref.VoidModel", "@ComponentStateRef target must return a value" },
        new Object[]{ "com.example.component_state_ref.BadTypeModel",
                      "@ComponentStateRef target must return a boolean" },
        new Object[]{ "com.example.component_state_ref.ThrowsExceptionModel",
                      "@ComponentStateRef target must not throw any exceptions" },
        new Object[]{ "com.example.component_state_ref.ParametersModel",
                      "@ComponentStateRef target must not have any parameters" },

        new Object[]{ "com.example.context_ref.FinalModel", "@ContextRef target must be abstract" },
        new Object[]{ "com.example.context_ref.StaticModel", "@ContextRef target must be abstract" },
        new Object[]{ "com.example.context_ref.PrivateModel", "@ContextRef target must be abstract" },
        new Object[]{ "com.example.context_ref.VoidModel", "@ContextRef target must return a value" },
        new Object[]{ "com.example.context_ref.BadTypeModel",
                      "@ObserverRef target must return an instance of arez.ArezContext" },
        new Object[]{ "com.example.context_ref.ThrowsExceptionModel",
                      "@ContextRef target must not throw any exceptions" },
        new Object[]{ "com.example.context_ref.ParametersModel", "@ContextRef target must not have any parameters" },

        new Object[]{ "com.example.component_dependency.AbstractDependency",
                      "@ArezComponent target has an abstract method not implemented by framework. The method is named getTime" },
        new Object[]{ "com.example.component_dependency.BadTypeDependency",
                      "@ComponentDependency target must return an instance compatible with arez.component.DisposeNotifier or a type annotated with @ArezComponent(disposeNotifier=ENABLE) or @ActAsComponent" },
        new Object[]{ "com.example.component_dependency.BadTypeFieldDependency",
                      "@ComponentDependency target must be an instance compatible with arez.component.DisposeNotifier or a type annotated with @ArezComponent(disposeNotifier=ENABLE) or @ActAsComponent" },
        new Object[]{ "com.example.component_dependency.CascadeDisposeAndFieldDependency",
                      "Method can not be annotated with both @ComponentDependency and @CascadeDispose" },
        new Object[]{ "com.example.component_dependency.MemoizeDependency",
                      "Method can not be annotated with both @Memoize and @ComponentDependency" },
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

        new Object[]{ "com.example.id.DisableIdAndComponentId",
                      "@ArezComponent target has specified the idRequired = DISABLE annotation parameter but also has annotated a method with @ComponentId that requires idRequired = ENABLE." },
        new Object[]{ "com.example.id.DisableIdAndComponentIdRef",
                      "@ArezComponent target has specified the idRequired = DISABLE annotation parameter but also has annotated a method with @ComponentIdRef that requires idRequired = ENABLE." },
        new Object[]{ "com.example.id.DisableIdAndInverseModel",
                      "@ArezComponent target has specified the idRequired = DISABLE annotation parameter but also has annotated a method with @Inverse that requires idRequired = ENABLE." },

        new Object[]{ "com.example.sting.MultipleConstructorsModel",
                      "@ArezComponent target must not enable sting integration and have multiple constructors" },
        new Object[]{ "com.example.sting.InjectArrayTypeStingModel",
                      "@ArezComponent target must not enable sting integration and contain a constructor with a parameter that contains an array type" },
        new Object[]{ "com.example.sting.InjectParameterizedTypeStingModel",
                      "@ArezComponent target must not enable sting integration and contain a constructor with a parameter that contains a parameterized type" },
        new Object[]{ "com.example.sting.InjectRawTypeStingModel",
                      "@ArezComponent target must not enable sting integration and contain a constructor with a parameter that contains a raw type" },
        new Object[]{ "com.example.sting.InjectWildcardTypeStingModel",
                      "@ArezComponent target must not enable sting integration and contain a constructor with a parameter that contains a wildcard type" },
        new Object[]{ "com.example.sting.StingNamedConstructorParameterNonStingModel",
                      "@ArezComponent target must not disable sting integration and contain a constructor with a parameter that is annotated with the sting.Named annotation" },
        new Object[]{ "com.example.sting.ParameterizedTypeStingModel",
                      "@ArezComponent target must not enable sting integration and be a parameterized type" },

        new Object[]{ "com.example.inverse.BadCollectionTypeInverseModel",
                      "@Inverse target expected to return a type annotated with arez.annotations.ArezComponent" },
        new Object[]{ "com.example.inverse.BadInverseName1InverseModel",
                      "@Reference target specified an invalid inverseName '-sxkw'. The inverseName must be a valid java identifier." },
        new Object[]{ "com.example.inverse.BadInverseName2InverseModel",
                      "@Reference target specified an invalid inverseName 'byte'. The inverseName must not be a java keyword." },
        new Object[]{ "com.example.inverse.BadInverseType1InverseModel",
                      "@Inverse target expected to find an associated @Reference annotation with a target type equal to com.example.inverse.BadInverseType1InverseModel but the actual target type is com.example.inverse.BadInverseType1InverseModel.MyEntity" },
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
                      "@Reference target expected to return a type annotated with @ArezComponent if there is an inverse reference" },
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
        new Object[]{ "com.example.observable_value_ref.BadReturnTypeParameterModel",
                      "@ObservableValueRef target has a type parameter of java.lang.String but @Observable method returns type of long" },
        new Object[]{ "com.example.observable_value_ref.FinalModel", "@ObservableValueRef target must be abstract" },
        new Object[]{ "com.example.observable_value_ref.NonAbstractModel",
                      "@ObservableValueRef target must be abstract" },
        new Object[]{ "com.example.observable_value_ref.NonAlignedNameModel",
                      "Method annotated with @ObservableValueRef should specify name or be named according to the convention get[Name]Observable" },
        new Object[]{ "com.example.observable_value_ref.NoObservableModel",
                      "@ObservableValueRef target unable to be associated with an Observable property" },
        new Object[]{ "com.example.observable_value_ref.ParametersModel",
                      "@ObservableValueRef target must not have any parameters" },
        new Object[]{ "com.example.observable_value_ref.PrivateModel", "@ObservableValueRef target must be abstract" },
        new Object[]{ "com.example.observable_value_ref.StaticModel", "@ObservableValueRef target must be abstract" },
        new Object[]{ "com.example.observable_value_ref.ThrowsExceptionModel",
                      "@ObservableValueRef target must not throw any exceptions" },

        new Object[]{ "com.example.computable_value_ref.BadNameModel",
                      "@ComputableValueRef target specified an invalid name '-ace'. The name must be a valid java identifier." },
        new Object[]{ "com.example.computable_value_ref.BadNameModel2",
                      "@ComputableValueRef target specified an invalid name 'private'. The name must not be a java keyword." },
        new Object[]{ "com.example.computable_value_ref.BadReturnTypeModel",
                      "Method annotated with @ComputableValueRef must return an instance of arez.ComputableValue" },
        new Object[]{ "com.example.computable_value_ref.BadReturnType3Model",
                      "@ComputableValueRef target has a type parameter of java.lang.String but @Memoize method returns type of long" },
        new Object[]{ "com.example.computable_value_ref.FinalModel", "@ComputableValueRef target must be abstract" },
        new Object[]{ "com.example.computable_value_ref.MemoizeHasDifferentParameters1Model",
                      "@ComputableValueRef target and the associated @Memoize target do not have the same parameters." },
        new Object[]{ "com.example.computable_value_ref.MemoizeHasDifferentParameters2Model",
                      "@ComputableValueRef target and the associated @Memoize target do not have the same parameters." },
        new Object[]{ "com.example.computable_value_ref.MemoizeHasDifferentParameters3Model",
                      "@ComputableValueRef target and the associated @Memoize target do not have the same parameters." },
        new Object[]{ "com.example.computable_value_ref.MemoizeHasDifferentParameters4Model",
                      "@ComputableValueRef target and the associated @Memoize target do not have the same parameters." },
        new Object[]{ "com.example.computable_value_ref.NoMemoizeModel",
                      "@ComputableValueRef exists but there is no corresponding @Memoize" },
        new Object[]{ "com.example.computable_value_ref.NonAlignedNameModel",
                      "Method annotated with @ComputableValueRef should specify name or be named according to the convention get[Name]ComputableValue" },
        new Object[]{ "com.example.computable_value_ref.PrivateModel", "@ComputableValueRef target must be abstract" },
        new Object[]{ "com.example.computable_value_ref.StaticModel", "@ComputableValueRef target must be abstract" },
        new Object[]{ "com.example.computable_value_ref.ThrowsExceptionModel",
                      "@ComputableValueRef target must not throw any exceptions" },

        new Object[]{ "com.example.observer_ref.BadNameModel",
                      "@ObserverRef target specified an invalid name '-ace'. The name must be a valid java identifier." },
        new Object[]{ "com.example.observer_ref.BadNameModel2",
                      "@ObserverRef target specified an invalid name 'int'. The name must not be a java keyword." },
        new Object[]{ "com.example.observer_ref.BadReturnTypeModel",
                      "@ObserverRef target must return an instance of arez.Observer" },
        new Object[]{ "com.example.observer_ref.ExceptionModel", "@ObserverRef target must not throw any exceptions" },
        new Object[]{ "com.example.observer_ref.FinalModel", "@ObserverRef target must be abstract" },
        new Object[]{ "com.example.observer_ref.NoNameModel",
                      "Method annotated with @ObserverRef should specify name or be named according to the convention get[Name]Observer" },
        new Object[]{ "com.example.observer_ref.ParametersModel", "@ObserverRef target must not have any parameters" },
        new Object[]{ "com.example.observer_ref.PrivateModel", "@ObserverRef target must be abstract" },
        new Object[]{ "com.example.observer_ref.RefOnNeitherModel",
                      "@ObserverRef target defined observer named 'render' but no @Observe method with that name exists" },
        new Object[]{ "com.example.observer_ref.StaticModel", "@ObserverRef target must be abstract" },
        new Object[]{ "com.example.observer_ref.VoidReturnModel", "@ObserverRef target must return a value" },

        new Object[]{ "com.example.memoize_context_parameter.capture.AbstractCaptureModel",
                      "@MemoizeContextParameter target must not be abstract" },
        new Object[]{ "com.example.memoize_context_parameter.capture.BadTypeCaptureModel",
                      "@MemoizeContextParameter target defines a push method with a different type (java.lang.String) from the matching @MemoizeContextParameter Capture method named captureMyContextVar that defines the type int." },
        new Object[]{ "com.example.memoize_context_parameter.capture.PrivateCaptureModel",
                      "@MemoizeContextParameter target must not be private" },
        new Object[]{ "com.example.memoize_context_parameter.capture.StaticCaptureModel",
                      "@MemoizeContextParameter target must not be static" },
        new Object[]{ "com.example.memoize_context_parameter.capture.ThrowsCaptureModel",
                      "@MemoizeContextParameter target must not throw any exceptions" },
        new Object[]{ "com.example.memoize_context_parameter.capture.TypeParamCaptureModel",
                      "@MemoizeContextParameter target must not have any type parameters" },
        new Object[]{ "com.example.memoize_context_parameter.capture.VoidCaptureModel",
                      "@MemoizeContextParameter target must return a value" },

        new Object[]{ "com.example.memoize_context_parameter.pop.AbstractPopModel",
                      "@MemoizeContextParameter target must not be abstract" },
        new Object[]{ "com.example.memoize_context_parameter.pop.BadTypePopModel",
                      "@MemoizeContextParameter target defines a pop method with a different type (int) from the matching @MemoizeContextParameter Capture method named captureMyContextVar that defines the type java.lang.String." },
        new Object[]{ "com.example.memoize_context_parameter.pop.MultipleParameterPopModel",
                      "@MemoizeContextParameter target on pop method should accept a single parameter" },
        new Object[]{ "com.example.memoize_context_parameter.pop.PrivatePopModel",
                      "@MemoizeContextParameter target must not be private" },
        new Object[]{ "com.example.memoize_context_parameter.pop.ReturningPopModel",
                      "@MemoizeContextParameter target must not return a value" },
        new Object[]{ "com.example.memoize_context_parameter.pop.StaticPopModel",
                      "@MemoizeContextParameter target must not be static" },
        new Object[]{ "com.example.memoize_context_parameter.pop.ThrowsPopModel",
                      "@MemoizeContextParameter target must not throw any exceptions" },
        new Object[]{ "com.example.memoize_context_parameter.pop.TypeParamPopModel",
                      "@MemoizeContextParameter target must not have any type parameters" },

        new Object[]{ "com.example.memoize_context_parameter.push.AbstractPushModel",
                      "@MemoizeContextParameter target must not be abstract" },
        new Object[]{ "com.example.memoize_context_parameter.push.BadTypePushModel",
                      "@MemoizeContextParameter target defines a push method with a different type (int) from the matching @MemoizeContextParameter Capture method named captureMyContextVar that defines the type java.lang.String." },
        new Object[]{ "com.example.memoize_context_parameter.push.MultipleParameterPushModel",
                      "@MemoizeContextParameter target on push method should accept a single parameter" },
        new Object[]{ "com.example.memoize_context_parameter.push.PrivatePushModel",
                      "@MemoizeContextParameter target must not be private" },
        new Object[]{ "com.example.memoize_context_parameter.push.ReturningPushModel",
                      "@MemoizeContextParameter target must not return a value" },
        new Object[]{ "com.example.memoize_context_parameter.push.StaticPushModel",
                      "@MemoizeContextParameter target must not be static" },
        new Object[]{ "com.example.memoize_context_parameter.push.ThrowsPushModel",
                      "@MemoizeContextParameter target must not throw any exceptions" },
        new Object[]{ "com.example.memoize_context_parameter.push.TypeParamPushModel",
                      "@MemoizeContextParameter target must not have any type parameters" },

        new Object[]{ "com.example.name_duplicates.ActionDuplicatesObservableNameModel",
                      "Method annotated with @Action specified name field that duplicates @Observable defined by method getField" },
        new Object[]{ "com.example.on_activate.MemoizeHasParametersModel",
                      "@OnActivate target associated with @Memoize method that has parameters." },
        new Object[]{ "com.example.on_activate.MemoizeHasContextParametersModel",
                      "@OnActivate target associated with @Memoize method that has matching context parameters." },
        new Object[]{ "com.example.on_activate.OnActivateAbstractModel", "@OnActivate target must not be abstract" },
        new Object[]{ "com.example.on_activate.OnActivateNoMemoizeModel",
                      "@OnActivate exists but there is no corresponding @Memoize" },
        new Object[]{ "com.example.on_activate.OnActivateBadNameModel",
                      "@OnActivate as does not match on[Name]Activate pattern. Please specify name." },
        new Object[]{ "com.example.on_activate.OnActivateBadNameModel2",
                      "@OnActivate target specified an invalid name 'final'. The name must not be a java keyword." },
        new Object[]{ "com.example.on_activate.OnActivateBadNameModel3",
                      "@OnActivate target specified an invalid name '-f-f-'. The name must be a valid java identifier." },
        new Object[]{ "com.example.on_activate.OnActivatePrivateModel", "@OnActivate target must not be private" },
        new Object[]{ "com.example.on_activate.OnActivateStaticModel", "@OnActivate target must not be static" },
        new Object[]{ "com.example.on_activate.OnActivateOnKeepAliveModel",
                      "@OnActivate exists for @Memoize property that specified parameter keepAlive as true." },
        new Object[]{ "com.example.on_activate.OnActivateParametersModel",
                      "@OnActivate target must not have any parameters" },
        new Object[]{ "com.example.on_activate.OnActivateReturnValueModel",
                      "@OnActivate target must not return a value" },
        new Object[]{ "com.example.on_activate.OnActivateThrowsExceptionModel",
                      "@OnActivate target must not throw any exceptions" },
        new Object[]{ "com.example.on_activate.OnActivateDuplicateModel",
                      "@OnActivate target duplicates existing method named foo" },
        new Object[]{ "com.example.on_deactivate.MemoizeHasParametersModel",
                      "@OnDeactivate target associated with @Memoize method that has parameters." },
        new Object[]{ "com.example.on_deactivate.MemoizeHasContextParametersModel",
                      "@OnDeactivate target associated with @Memoize method that has matching context parameters." },
        new Object[]{ "com.example.on_deactivate.OnDeactivateAbstractModel",
                      "@OnDeactivate target must not be abstract" },
        new Object[]{ "com.example.on_deactivate.OnDeactivateNoMemoizeModel",
                      "@OnDeactivate exists but there is no corresponding @Memoize" },
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
                      "@OnDeactivate exists for @Memoize property that specified parameter keepAlive as true." },
        new Object[]{ "com.example.on_deactivate.OnDeactivateParametersModel",
                      "@OnDeactivate target must not have any parameters" },
        new Object[]{ "com.example.on_deactivate.OnDeactivateReturnValueModel",
                      "@OnDeactivate target must not return a value" },
        new Object[]{ "com.example.on_deactivate.OnDeactivateThrowsExceptionModel",
                      "@OnDeactivate target must not throw any exceptions" },
        new Object[]{ "com.example.on_deactivate.OnDeactivateDuplicateModel",
                      "@OnDeactivate target duplicates existing method named foo" },

        new Object[]{ "com.example.pre_dispose.PreDisposeAbstractModel", "@PreDispose target must not be abstract" },
        new Object[]{ "com.example.pre_dispose.PreDisposePrivateModel", "@PreDispose target must not be private" },
        new Object[]{ "com.example.pre_dispose.PreDisposeStaticModel", "@PreDispose target must not be static" },
        new Object[]{ "com.example.pre_dispose.PreDisposeParametersModel",
                      "@PreDispose target must not have any parameters" },
        new Object[]{ "com.example.pre_dispose.PreDisposeReturnValueModel",
                      "@PreDispose target must not return a value" },
        new Object[]{ "com.example.pre_dispose.PreDisposeThrowsExceptionModel",
                      "@PreDispose target must not throw any exceptions" },

        new Object[]{ "com.example.post_dispose.PostDisposeAbstractModel", "@PostDispose target must not be abstract" },
        new Object[]{ "com.example.post_dispose.PostDisposePrivateModel", "@PostDispose target must not be private" },
        new Object[]{ "com.example.post_dispose.PostDisposeStaticModel", "@PostDispose target must not be static" },
        new Object[]{ "com.example.post_dispose.PostDisposeParametersModel",
                      "@PostDispose target must not have any parameters" },
        new Object[]{ "com.example.post_dispose.PostDisposeReturnValueModel",
                      "@PostDispose target must not return a value" },
        new Object[]{ "com.example.post_dispose.PostDisposeThrowsExceptionModel",
                      "@PostDispose target must not throw any exceptions" },

        new Object[]{ "com.example.post_inverse_add.AbstractPostInverseAddModel",
                      "@PostInverseAdd target must not be abstract" },
        new Object[]{ "com.example.post_inverse_add.BadName1PostInverseAddModel",
                      "@PostInverseAdd target has not specified a name and does not follow the convention \"post[Name]Add\"" },
        new Object[]{ "com.example.post_inverse_add.BadName2PostInverseAddModel",
                      "@PostInverseAdd target specified an invalid name '-ace'. The name must be a valid java identifier" },
        new Object[]{ "com.example.post_inverse_add.BadName3PostInverseAddModel",
                      "@PostInverseAdd target specified an invalid name 'int'. The name must not be a java keyword" },
        new Object[]{ "com.example.post_inverse_add.BadParamCount1PostInverseAddModel",
                      "@PostInverseAdd target must have exactly 1 parameter" },
        new Object[]{ "com.example.post_inverse_add.BadParamCount2PostInverseAddModel",
                      "@PostInverseAdd target must have exactly 1 parameter" },
        new Object[]{ "com.example.post_inverse_add.BadParamTypePostInverseAddModel",
                      "@PostInverseAdd target has a parameter that is not the expected type. Actual type: java.lang.String Expected Type: com.example.post_inverse_add.BadParamTypePostInverseAddModel.Element" },
        new Object[]{ "com.example.post_inverse_add.MissingInversePostInverseAddModel",
                      "@PostInverseAdd target with name 'element' is not associated an @Inverse annotated method with the same name" },
        new Object[]{ "com.example.post_inverse_add.PrivatePostInverseAddModel",
                      "@PostInverseAdd target must not be private" },
        new Object[]{ "com.example.post_inverse_add.ReturnsPostInverseAddModel",
                      "@PostInverseAdd target must not return a value" },
        new Object[]{ "com.example.post_inverse_add.StaticPostInverseAddModel",
                      "@PostInverseAdd target must not be static" },
        new Object[]{ "com.example.post_inverse_add.ThrowsPostInverseAddModel",
                      "@PostInverseAdd target must not throw any exceptions" },

        new Object[]{ "com.example.pre_inverse_remove.AbstractPreInverseRemoveModel",
                      "@PreInverseRemove target must not be abstract" },
        new Object[]{ "com.example.pre_inverse_remove.BadName1PreInverseRemoveModel",
                      "@PreInverseRemove target specified an invalid name '-e-e-'. The name must be a valid java identifier" },
        new Object[]{ "com.example.pre_inverse_remove.BadName2PreInverseRemoveModel",
                      "@PreInverseRemove target specified an invalid name 'double'. The name must not be a java keyword" },
        new Object[]{ "com.example.pre_inverse_remove.BadName3PreInverseRemoveModel",
                      "@PreInverseRemove target has not specified a name and does not follow the convention \"pre[Name]Remove\"" },
        new Object[]{ "com.example.pre_inverse_remove.BadParamCount1PreInverseRemoveModel",
                      "@PreInverseRemove target must have exactly 1 parameter" },
        new Object[]{ "com.example.pre_inverse_remove.BadParamCount2PreInverseRemoveModel",
                      "@PreInverseRemove target must have exactly 1 parameter" },
        new Object[]{ "com.example.pre_inverse_remove.BadParamTypePreInverseRemoveModel",
                      "@PreInverseRemove target has a parameter that is not the expected type. Actual type: java.lang.String Expected Type: com.example.pre_inverse_remove.BadParamTypePreInverseRemoveModel.Element" },
        new Object[]{ "com.example.pre_inverse_remove.MissingInversePreInverseRemoveModel",
                      "@PreInverseRemove target with name 'element' is not associated an @Inverse annotated method with the same name" },
        new Object[]{ "com.example.pre_inverse_remove.PrivatePreInverseRemoveModel",
                      "@PreInverseRemove target must not be private" },
        new Object[]{ "com.example.pre_inverse_remove.ReturnsPreInverseRemoveModel",
                      "@PreInverseRemove target must not return a value" },
        new Object[]{ "com.example.pre_inverse_remove.StaticPreInverseRemoveModel",
                      "@PreInverseRemove target must not be static" },
        new Object[]{ "com.example.pre_inverse_remove.ThrowsPreInverseRemoveModel",
                      "@PreInverseRemove target must not throw any exceptions" },

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
        new Object[]{ "com.example.name_duplicates.ActionAndMemoizeSameNameModel",
                      "Method annotated with @Action specified name x that duplicates @Memoize defined by method m1" },
        new Object[]{ "com.example.name_duplicates.ActionAndObservableSameNameModel",
                      "Method annotated with @Observable specified name x that duplicates @Action defined by method m1" },
        new Object[]{ "com.example.name_duplicates.ActionAndObservableSameNameNoGetterYetModel",
                      "Method annotated with @Action specified name x that duplicates @Observable defined by method setTime" },
        new Object[]{ "com.example.name_duplicates.MemoizeAndObservableSameNameModel",
                      "Method annotated with @Observable specified name x that duplicates @Memoize defined by method m1" },

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

        new Object[]{ "com.example.on_deps_change.OnDepsChangeNotAbstractModel",
                      "@OnDepsChange target must not be abstract" },
        new Object[]{ "com.example.on_deps_change.OnDepsChangeDuplicatedModel",
                      "@OnDepsChange target duplicates existing method named onRenderDepsChange" },
        new Object[]{ "com.example.on_deps_change.OnDepsChangeNotStaticModel",
                      "@OnDepsChange target must not be static" },
        new Object[]{ "com.example.on_deps_change.OnDepsChangeNotPrivateModel",
                      "@OnDepsChange target must not be private" },
        new Object[]{ "com.example.on_deps_change.OnDepsChangeMustNotHaveParametersModel",
                      "@OnDepsChange target must not have any parameters" },
        new Object[]{ "com.example.on_deps_change.OnDepsChangeMustNotReturnValueModel",
                      "@OnDepsChange target must not return a value" },
        new Object[]{ "com.example.on_deps_change.OnDepsChangeThrowsExceptionModel",
                      "@OnDepsChange target must not throw any exceptions" },
        new Object[]{ "com.example.on_deps_change.OnDepsChangeBadName",
                      "@OnDepsChange target specified an invalid name '-ace'. The name must be a valid java identifier." },
        new Object[]{ "com.example.on_deps_change.OnDepsChangeBadName2",
                      "@OnDepsChange target specified an invalid name 'class'. The name must not be a java keyword." },
        new Object[]{ "com.example.on_deps_change.OnDepsChangeBadName3",
                      "@OnDepsChange target specified an invalid name '-ace-'. The name must be a valid java identifier." },
        new Object[]{ "com.example.on_deps_change.OnDepsChangeNoTracked",
                      "@OnDepsChange target has no corresponding @Observe that could be automatically determined" },
        new Object[]{ "com.example.post_construct.EjbPostConstructModel",
                      "@javax.annotation.PostConstruct annotation not supported in components annotated with @ArezComponent, use the @arez.annotations.PostConstruct annotation instead." },
        new Object[]{ "com.example.post_construct.PostConstructAbstractModel",
                      "@PostConstruct target must not be abstract" },
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
        new Object[]{ "com.example.name_duplicates.ObservableAndMemoizeMethodModel",
                      "Method can not be annotated with both @Observable and @Memoize" },
        new Object[]{ "com.example.name_duplicates.ObservableAndContainerIdMethodModel",
                      "Method can not be annotated with both @Observable and @ComponentId" },
        new Object[]{ "com.example.name_duplicates.ObservableAndOnActivateMethodModel",
                      "Method can not be annotated with both @Observable and @OnActivate" },
        new Object[]{ "com.example.name_duplicates.ObservableAndOnDeactivateMethodModel",
                      "Method can not be annotated with both @Observable and @OnDeactivate" },
        new Object[]{ "com.example.name_duplicates.ActionAndObserveMethodModel",
                      "Method can not be annotated with both @Action and @Observe" },
        new Object[]{ "com.example.name_duplicates.ActionAndMemoizeMethodModel",
                      "Method can not be annotated with both @Action and @Memoize" },
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
        new Object[]{ "com.example.name_duplicates.MemoizeAndContainerIdMethodModel",
                      "Method can not be annotated with both @Memoize and @ComponentId" },
        new Object[]{ "com.example.name_duplicates.MemoizeAndPreDisposeMethodModel",
                      "Method can not be annotated with both @Memoize and @PreDispose" },
        new Object[]{ "com.example.name_duplicates.MemoizeAndObserveMethodModel",
                      "Method can not be annotated with both @Observe and @Memoize" },
        new Object[]{ "com.example.name_duplicates.MemoizeAndPostDisposeMethodModel",
                      "Method can not be annotated with both @Memoize and @PostDispose" },
        new Object[]{ "com.example.name_duplicates.MemoizeAndOnActivateMethodModel",
                      "Method can not be annotated with both @Memoize and @OnActivate" },
        new Object[]{ "com.example.name_duplicates.MemoizeAndOnDeactivateMethodModel",
                      "Method can not be annotated with both @Memoize and @OnDeactivate" },
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

        new Object[]{ "com.example.sting.ContributeToButNoStingModel",
                      "@ArezComponent target must not disable sting integration and be annotated with sting.ContributeTo" },
        new Object[]{ "com.example.sting.EagerButNoStingModel",
                      "@ArezComponent target must not disable sting integration and be annotated with sting.Eager" },
        new Object[]{ "com.example.sting.NamedArgButNoStingModel",
                      "@ArezComponent target must not disable sting integration and contain a constructor with a parameter that is annotated with the sting.Named annotation" },
        new Object[]{ "com.example.sting.NamedButNoStingModel",
                      "@ArezComponent target must not disable sting integration and be annotated with sting.Named" },
        new Object[]{ "com.example.sting.TypedButNoStingModel",
                      "@ArezComponent target must not disable sting integration and be annotated with sting.Typed" }
      };
  }

  @Test( dataProvider = "failedCompiles" )
  public void processFailedCompile( @Nonnull final String classname, @Nonnull final String messageFragment )
  {
    assertFailedCompile( classname, messageFragment );
  }

  @DataProvider( name = "compileWithWarnings" )
  public Object[][] compileWithWarnings()
  {
    return new Object[][]
      {
        new Object[]{ "com.example.component.AllowEmptyOnNonEmptyComponent",
                      "@ArezComponent target has specified allowEmpty = true but has methods annotated with @Action, @CascadeDispose, @Memoize, @Observable, @Inverse, @Reference, @ComponentDependency or @Observe" },
        new Object[]{ "com.example.component.PublicCtorModel",
                      "@ArezComponent target should not have a public constructor. It is recommended that a static create method be added to the component that is responsible for instantiating the arez implementation class. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Arez:PublicConstructor\" ) or @SuppressArezWarnings( \"Arez:PublicConstructor\" )" },
        new Object[]{ "com.example.component.PublicCtorNonPublicModel",
                      "@ArezComponent target should not have a public constructor. It is recommended that a static create method be added to the component that is responsible for instantiating the arez implementation class. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Arez:PublicConstructor\" ) or @SuppressArezWarnings( \"Arez:PublicConstructor\" )" },
        new Object[]{ "com.example.component.UnmanagedComponentReferencePassedInConstructor",
                      "Field named '_myComponent' has a type that is an Arez component but is not annotated with @arez.annotations.CascadeDispose or @arez.annotations.ComponentDependency and was not injected into the constructor. This scenario can cause errors if the value is disposed. Please annotate the field as appropriate or suppress the warning by annotating the field with @SuppressWarnings( \"Arez:UnmanagedComponentReference\" ) or @SuppressArezWarnings( \"Arez:UnmanagedComponentReference\" )" },
        new Object[]{ "com.example.component.UnmanagedActAsComponentReference",
                      "Field named '_myComponent' has a type that is annotated with @ActAsComponent but is not annotated with @arez.annotations.CascadeDispose or @arez.annotations.ComponentDependency and was not injected into the constructor. This scenario can cause errors if the value is disposed. Please annotate the field as appropriate or suppress the warning by annotating the field with @SuppressWarnings( \"Arez:UnmanagedComponentReference\" ) or @SuppressArezWarnings( \"Arez:UnmanagedComponentReference\" )" },
        new Object[]{ "com.example.component.UnmanagedComponentReference",
                      "Field named '_myComponent' has a type that is an Arez component but is not annotated with @arez.annotations.CascadeDispose or @arez.annotations.ComponentDependency and was not injected into the constructor. This scenario can cause errors if the value is disposed. Please annotate the field as appropriate or suppress the warning by annotating the field with @SuppressWarnings( \"Arez:UnmanagedComponentReference\" ) or @SuppressArezWarnings( \"Arez:UnmanagedComponentReference\" )" },
        new Object[]{ "com.example.component.UnmanagedComponentReferenceViaInheritance",
                      "Field named '_component' has a type that is an Arez component but is not annotated with @arez.annotations.CascadeDispose or @arez.annotations.ComponentDependency and was not injected into the constructor. This scenario can cause errors if the value is disposed. Please annotate the field as appropriate or suppress the warning by annotating the field with @SuppressWarnings( \"Arez:UnmanagedComponentReference\" ) or @SuppressArezWarnings( \"Arez:UnmanagedComponentReference\" )" },
        new Object[]{ "com.example.component.UnmanagedDisposeNotifierReference",
                      "Field named 'time' has a type that is an implementation of DisposeNotifier but is not annotated with @arez.annotations.CascadeDispose or @arez.annotations.ComponentDependency and was not injected into the constructor. This scenario can cause errors if the value is disposed. Please annotate the field as appropriate or suppress the warning by annotating the field with @SuppressWarnings( \"Arez:UnmanagedComponentReference\" ) or @SuppressArezWarnings( \"Arez:UnmanagedComponentReference\" )" },
        new Object[]{ "com.example.component.UnmanagedObservableComponentReference",
                      "Method named 'getMyComponent' has a return type that is an Arez component but is not annotated with @arez.annotations.CascadeDispose or @arez.annotations.ComponentDependency. This scenario can cause errors. Please annotate the method as appropriate or suppress the warning by annotating the method with @SuppressWarnings( \"Arez:UnmanagedComponentReference\" ) or @SuppressArezWarnings( \"Arez:UnmanagedComponentReference\" )" },
        new Object[]{ "com.example.component.UnmanagedObservableActAsComponentReference",
                      "Method named 'getMyComponent' has a return type that is annotated with @ActAsComponent but is not annotated with @arez.annotations.CascadeDispose or @arez.annotations.ComponentDependency. This scenario can cause errors. Please annotate the method as appropriate or suppress the warning by annotating the method with @SuppressWarnings( \"Arez:UnmanagedComponentReference\" ) or @SuppressArezWarnings( \"Arez:UnmanagedComponentReference\" )" },
        new Object[]{ "com.example.component.UnmanagedObservableDisposeNotifierReference",
                      "Method named 'getMyComponent' has a return type that is an implementation of DisposeNotifier but is not annotated with @arez.annotations.CascadeDispose or @arez.annotations.ComponentDependency. This scenario can cause errors. Please annotate the method as appropriate or suppress the warning by annotating the method with @SuppressWarnings( \"Arez:UnmanagedComponentReference\" ) or @SuppressArezWarnings( \"Arez:UnmanagedComponentReference\" )" },
        new Object[]{ "com.example.component.UnmanagedObservableComponentReferenceViaInheritance",
                      "Method named 'getMyComponent' has a return type that is an Arez component but is not annotated with @arez.annotations.CascadeDispose or @arez.annotations.ComponentDependency. This scenario can cause errors. Please annotate the method as appropriate or suppress the warning by annotating the method with @SuppressWarnings( \"Arez:UnmanagedComponentReference\" ) or @SuppressArezWarnings( \"Arez:UnmanagedComponentReference\" )" },
        new Object[]{ "com.example.component.UnnecessaryDefaultPriorityPresentComponent",
                      "@ArezComponent target should not specify the defaultPriority parameter unless it contains methods annotated with either the @Memoize annotation or the @Observe annotation. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Arez:UnnecessaryDefaultPriority\" ) or @SuppressArezWarnings( \"Arez:UnnecessaryDefaultPriority\" )" },
        new Object[]{ "com.example.component.UnnecessaryDefaultReadOutsideTransactionComponentModel",
                      "@ArezComponent target has specified a value for the defaultReadOutsideTransaction parameter but does not contain any methods annotated with either @Memoize or @Observable. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Arez:UnnecessaryDefault\" ) or @SuppressArezWarnings( \"Arez:UnnecessaryDefault\" )" },
        new Object[]{ "com.example.component.UnnecessaryDefaultWriteOutsideTransactionComponentModel",
                      "@ArezComponent target has specified a value for the defaultWriteOutsideTransaction parameter but does not contain any methods annotated with @Observable. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Arez:UnnecessaryDefault\" ) or @SuppressArezWarnings( \"Arez:UnnecessaryDefault\" )" },
        new Object[]{ "com.example.component.ProtectedCtorModel",
                      "@ArezComponent target should have a package access constructor. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Arez:ProtectedConstructor\" ) or @SuppressArezWarnings( \"Arez:ProtectedConstructor\" )" },
        new Object[]{ "com.example.component.ExtendsComponentModel",
                      "@ArezComponent target should not extend a class annotated with the arez.annotations.ArezComponent annotation. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Arez:ExtendsComponent\" ) or @SuppressArezWarnings( \"Arez:ExtendsComponent\" )" },

        new Object[]{ "com.example.component_ref.ProtectedAccessComponentRefModel",
                      "@ArezComponent target should not declare a protected method. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Arez:ProtectedMethod\" ) or @SuppressArezWarnings( \"Arez:ProtectedMethod\" )" },
        new Object[]{ "com.example.component_ref.PublicAccessComponentRefModel",
                      "@ComponentRef target should not be public. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Arez:PublicRefMethod\" ) or @SuppressArezWarnings( \"Arez:PublicRefMethod\" )" },

        new Object[]{ "com.example.component_name_ref.ProtectedAccessComponentNameRefModel",
                      "@ArezComponent target should not declare a protected method. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Arez:ProtectedMethod\" ) or @SuppressArezWarnings( \"Arez:ProtectedMethod\" )" },
        new Object[]{ "com.example.component_name_ref.PublicAccessComponentNameRefModel",
                      "@ComponentNameRef target should not be public. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Arez:PublicRefMethod\" ) or @SuppressArezWarnings( \"Arez:PublicRefMethod\" )" },

        new Object[]{ "com.example.component_state_ref.ProtectedAccessComponentStateRefModel",
                      "@ArezComponent target should not declare a protected method. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Arez:ProtectedMethod\" ) or @SuppressArezWarnings( \"Arez:ProtectedMethod\" )" },
        new Object[]{ "com.example.component_state_ref.PublicAccessComponentStateRefModel",
                      "@ComponentStateRef target should not be public. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Arez:PublicRefMethod\" ) or @SuppressArezWarnings( \"Arez:PublicRefMethod\" )" },

        new Object[]{ "com.example.component_type_name_ref.ProtectedAccessComponentTypeNameRefModel",
                      "@ArezComponent target should not declare a protected method. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Arez:ProtectedMethod\" ) or @SuppressArezWarnings( \"Arez:ProtectedMethod\" )" },
        new Object[]{ "com.example.component_type_name_ref.PublicAccessComponentTypeNameRefModel",
                      "@ComponentTypeNameRef target should not be public. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Arez:PublicRefMethod\" ) or @SuppressArezWarnings( \"Arez:PublicRefMethod\" )" },

        new Object[]{ "com.example.computable_value_ref.ProtectedAccessComputableValueRefModel",
                      "@ArezComponent target should not declare a protected method. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Arez:ProtectedMethod\" ) or @SuppressArezWarnings( \"Arez:ProtectedMethod\" )" },
        new Object[]{ "com.example.computable_value_ref.PublicAccessComputableValueRefModel",
                      "@ComputableValueRef target should not be public. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Arez:PublicRefMethod\" ) or @SuppressArezWarnings( \"Arez:PublicRefMethod\" )" },

        new Object[]{ "com.example.context_ref.ProtectedAccessContextRefModel",
                      "@ArezComponent target should not declare a protected method. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Arez:ProtectedMethod\" ) or @SuppressArezWarnings( \"Arez:ProtectedMethod\" )" },
        new Object[]{ "com.example.context_ref.PublicAccessContextRefModel",
                      "@ContextRef target should not be public. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Arez:PublicRefMethod\" ) or @SuppressArezWarnings( \"Arez:PublicRefMethod\" )" },

        new Object[]{ "com.example.observable_value_ref.ProtectedAccessObservableValueRefModel",
                      "@ArezComponent target should not declare a protected method. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Arez:ProtectedMethod\" ) or @SuppressArezWarnings( \"Arez:ProtectedMethod\" )" },
        new Object[]{ "com.example.observable_value_ref.PublicAccessObservableValueRefModel",
                      "@ObservableValueRef target should not be public. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Arez:PublicRefMethod\" ) or @SuppressArezWarnings( \"Arez:PublicRefMethod\" )" },

        new Object[]{ "com.example.observer_ref.ProtectedAccessObserverRefModel",
                      "@ArezComponent target should not declare a protected method. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Arez:ProtectedMethod\" ) or @SuppressArezWarnings( \"Arez:ProtectedMethod\" )" },
        new Object[]{ "com.example.observer_ref.PublicAccessObserverRefModel",
                      "@ObserverRef target should not be public. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Arez:PublicRefMethod\" ) or @SuppressArezWarnings( \"Arez:PublicRefMethod\" )" },

        new Object[]{ "com.example.on_activate.ProtectedAccessOnActivateModel",
                      "@ArezComponent target should not declare a protected method. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Arez:ProtectedMethod\" ) or @SuppressArezWarnings( \"Arez:ProtectedMethod\" )" },
        new Object[]{ "com.example.on_activate.PublicAccessOnActivateModel",
                      "@OnActivate target should not be public. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Arez:PublicHookMethod\" ) or @SuppressArezWarnings( \"Arez:PublicHookMethod\" )" },

        new Object[]{ "com.example.on_deactivate.ProtectedAccessOnDeactivateModel",
                      "@ArezComponent target should not declare a protected method. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Arez:ProtectedMethod\" ) or @SuppressArezWarnings( \"Arez:ProtectedMethod\" )" },
        new Object[]{ "com.example.on_deactivate.PublicAccessOnDeactivateModel",
                      "@OnDeactivate target should not be public. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Arez:PublicHookMethod\" ) or @SuppressArezWarnings( \"Arez:PublicHookMethod\" )" },

        new Object[]{ "com.example.on_deps_change.ProtectedAccessOnDepsChangeModel",
                      "@ArezComponent target should not declare a protected method. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Arez:ProtectedMethod\" ) or @SuppressArezWarnings( \"Arez:ProtectedMethod\" )" },
        new Object[]{ "com.example.on_deps_change.PublicAccessOnDepsChangeModel",
                      "@OnDepsChange target should not be public. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Arez:PublicHookMethod\" ) or @SuppressArezWarnings( \"Arez:PublicHookMethod\" )" },

        new Object[]{ "com.example.post_construct.ProtectedAccessPostConstructModel",
                      "@ArezComponent target should not declare a protected method. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Arez:ProtectedMethod\" ) or @SuppressArezWarnings( \"Arez:ProtectedMethod\" )" },
        new Object[]{ "com.example.post_construct.PublicAccessPostConstructModel",
                      "@PostConstruct target should not be public. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Arez:PublicLifecycleMethod\" ) or @SuppressArezWarnings( \"Arez:PublicLifecycleMethod\" )" },

        new Object[]{ "com.example.post_dispose.ProtectedAccessPostDisposeModel",
                      "@ArezComponent target should not declare a protected method. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Arez:ProtectedMethod\" ) or @SuppressArezWarnings( \"Arez:ProtectedMethod\" )" },
        new Object[]{ "com.example.post_dispose.PublicAccessPostDisposeModel",
                      "@PostDispose target should not be public. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Arez:PublicLifecycleMethod\" ) or @SuppressArezWarnings( \"Arez:PublicLifecycleMethod\" )" },

        new Object[]{ "com.example.post_inverse_add.ProtectedAccessPostInverseAddModel",
                      "@ArezComponent target should not declare a protected method. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Arez:ProtectedMethod\" ) or @SuppressArezWarnings( \"Arez:ProtectedMethod\" )" },
        new Object[]{ "com.example.post_inverse_add.PublicAccessPostInverseAddModel",
                      "@PostInverseAdd target should not be public. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Arez:PublicHookMethod\" ) or @SuppressArezWarnings( \"Arez:PublicHookMethod\" )" },

        new Object[]{ "com.example.pre_dispose.ProtectedAccessPreDisposeModel",
                      "@ArezComponent target should not declare a protected method. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Arez:ProtectedMethod\" ) or @SuppressArezWarnings( \"Arez:ProtectedMethod\" )" },
        new Object[]{ "com.example.pre_dispose.PublicAccessPreDisposeModel",
                      "@PreDispose target should not be public. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Arez:PublicLifecycleMethod\" ) or @SuppressArezWarnings( \"Arez:PublicLifecycleMethod\" )" },

        new Object[]{ "com.example.pre_inverse_remove.ProtectedAccessPreInverseRemoveModel",
                      "@ArezComponent target should not declare a protected method. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Arez:ProtectedMethod\" ) or @SuppressArezWarnings( \"Arez:ProtectedMethod\" )" },
        new Object[]{ "com.example.pre_inverse_remove.PublicAccessPreInverseRemoveModel",
                      "@PreInverseRemove target should not be public. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Arez:PublicHookMethod\" ) or @SuppressArezWarnings( \"Arez:PublicHookMethod\" )" },
        new Object[]{ "com.example.sting.PublicCtorStingModel",
                      "@ArezComponent target should not have a public constructor. The type is instantiated by the sting injection framework and should have a package-access constructor. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Arez:PublicConstructor\" ) or @SuppressArezWarnings( \"Arez:PublicConstructor\" )" }
      };
  }

  @Test( dataProvider = "compileWithWarnings" )
  public void processCompileWithWarnings( @Nonnull final String classname, @Nonnull final String messageFragment )
  {
    assertCompilesWithSingleWarning( classname, messageFragment );
  }

  @DataProvider( name = "compileWithoutWarnings" )
  public Object[][] compileWithoutWarnings()
  {
    return new Object[][]
      {
        new Object[]{ "com.example.component.UnmanagedComponentReferenceStingInjected" },
        new Object[]{ "com.example.component.UnmanagedComponentReferenceSuppressed" },
        new Object[]{ "com.example.component.UnmanagedComponentReferenceSuppressedAtClass" },
        new Object[]{ "com.example.component.UnmanagedComponentReferenceToNonDisposeNotifier" },
        new Object[]{ "com.example.component.UnmanagedComponentReferenceToNonVerify" },
        new Object[]{ "com.example.component.UnmanagedObservableActAsComponentReferenceSuppressed" },
        new Object[]{ "com.example.component.UnmanagedObservableActAsComponentReferenceSuppressedOnClass" },
        new Object[]{ "com.example.component.UnmanagedObservableComponentReferenceViaInheritanceSuppressed" },
        new Object[]{ "com.example.component.UnmanagedObservableComponentReferenceViaInheritanceSuppressedOnBaseClass" },
        new Object[]{ "com.example.component.UnmanagedObservableActAsComponentReferenceSuppressedOnSetter" },
        new Object[]{ "com.example.component.UnmanagedObservableComponentReferenceToNonVerify" },
        new Object[]{ "com.example.component_name_ref.Suppressed1ProtectedAccessComponentNameRefModel" },
        new Object[]{ "com.example.component_name_ref.Suppressed1PublicAccessComponentNameRefModel" },
        new Object[]{ "com.example.component_name_ref.Suppressed2PublicAccessComponentNameRefModel" },
        new Object[]{ "com.example.component.Suppressed1ExtendsComponentModel" },
        new Object[]{ "com.example.component.Suppressed1ProtectedCtorModel" },
        new Object[]{ "com.example.component.Suppressed1UnnecessaryAllowEmptyPresentComponent" },
        new Object[]{ "com.example.component.Suppressed1UnnecessaryDefaultPriorityPresentComponent" },
        new Object[]{ "com.example.component.Suppressed2ExtendsComponentModel" },
        new Object[]{ "com.example.component.Suppressed2ProtectedCtorModel" },
        new Object[]{ "com.example.component.Suppressed2UnnecessaryAllowEmptyPresentComponent" },
        new Object[]{ "com.example.component.Suppressed2UnnecessaryDefaultPriorityPresentComponent" },
        new Object[]{ "com.example.component_ref.Suppressed1ProtectedAccessComponentRefModel" },
        new Object[]{ "com.example.component_ref.Suppressed1PublicAccessComponentRefModel" },
        new Object[]{ "com.example.component_ref.Suppressed2ProtectedAccessComponentRefModel" },
        new Object[]{ "com.example.component_ref.Suppressed2PublicAccessComponentRefModel" },
        new Object[]{ "com.example.component_state_ref.Suppressed1ProtectedAccessComponentStateRefModel" },
        new Object[]{ "com.example.component_state_ref.Suppressed1PublicAccessComponentStateRefModel" },
        new Object[]{ "com.example.component_state_ref.Suppressed2ProtectedAccessComponentStateRefModel" },
        new Object[]{ "com.example.component_state_ref.Suppressed2PublicAccessComponentStateRefModel" },
        new Object[]{ "com.example.component_type_name_ref.Suppressed1ProtectedAccessComponentTypeNameRefModel" },
        new Object[]{ "com.example.component_type_name_ref.Suppressed1PublicAccessComponentTypeNameRefModel" },
        new Object[]{ "com.example.component_type_name_ref.Suppressed2ProtectedAccessComponentTypeNameRefModel" },
        new Object[]{ "com.example.component_type_name_ref.Suppressed2PublicAccessComponentTypeNameRefModel" },
        new Object[]{ "com.example.computable_value_ref.Suppressed1ProtectedAccessComputableValueRefModel" },
        new Object[]{ "com.example.computable_value_ref.Suppressed1PublicAccessComputableValueRefModel" },
        new Object[]{ "com.example.computable_value_ref.Suppressed2ProtectedAccessComputableValueRefModel" },
        new Object[]{ "com.example.computable_value_ref.Suppressed2PublicAccessComputableValueRefModel" },
        new Object[]{ "com.example.context_ref.Suppressed1ProtectedAccessContextRefModel" },
        new Object[]{ "com.example.context_ref.Suppressed1PublicAccessContextRefModel" },
        new Object[]{ "com.example.context_ref.Suppressed2ProtectedAccessContextRefModel" },
        new Object[]{ "com.example.context_ref.Suppressed2PublicAccessContextRefModel" },
        new Object[]{ "com.example.observable_value_ref.Suppressed1ProtectedAccessObservableValueRefModel" },
        new Object[]{ "com.example.observable_value_ref.Suppressed1PublicAccessObservableValueRefModel" },
        new Object[]{ "com.example.observable_value_ref.Suppressed2ProtectedAccessObservableValueRefModel" },
        new Object[]{ "com.example.observable_value_ref.Suppressed2PublicAccessObservableValueRefModel" },
        new Object[]{ "com.example.observer_ref.Suppressed1ProtectedAccessObserverRefModel" },
        new Object[]{ "com.example.observer_ref.Suppressed1PublicAccessObserverRefModel" },
        new Object[]{ "com.example.observer_ref.Suppressed2ProtectedAccessObserverRefModel" },
        new Object[]{ "com.example.observer_ref.Suppressed2PublicAccessObserverRefModel" },
        new Object[]{ "com.example.on_activate.Suppressed1ProtectedAccessOnActivateModel" },
        new Object[]{ "com.example.on_activate.Suppressed1PublicAccessOnActivateModel" },
        new Object[]{ "com.example.on_activate.Suppressed2ProtectedAccessOnActivateModel" },
        new Object[]{ "com.example.on_activate.Suppressed2PublicAccessOnActivateModel" },
        new Object[]{ "com.example.on_deactivate.Suppressed1ProtectedAccessOnDeactivateModel" },
        new Object[]{ "com.example.on_deactivate.Suppressed1PublicAccessOnDeactivateModel" },
        new Object[]{ "com.example.on_deactivate.Suppressed2ProtectedAccessOnDeactivateModel" },
        new Object[]{ "com.example.on_deactivate.Suppressed2PublicAccessOnDeactivateModel" },
        new Object[]{ "com.example.on_deps_change.Suppressed1ProtectedAccessOnDepsChangeModel" },
        new Object[]{ "com.example.on_deps_change.Suppressed1PublicAccessOnDepsChangeModel" },
        new Object[]{ "com.example.on_deps_change.Suppressed2ProtectedAccessOnDepsChangeModel" },
        new Object[]{ "com.example.on_deps_change.Suppressed2PublicAccessOnDepsChangeModel" },
        new Object[]{ "com.example.post_construct.Suppressed1ProtectedAccessPostConstructModel" },
        new Object[]{ "com.example.post_construct.Suppressed1PublicAccessPostConstructModel" },
        new Object[]{ "com.example.post_construct.Suppressed2ProtectedAccessPostConstructModel" },
        new Object[]{ "com.example.post_construct.Suppressed2PublicAccessPostConstructModel" },
        new Object[]{ "com.example.post_dispose.Suppressed1ProtectedAccessPostDisposeModel" },
        new Object[]{ "com.example.post_dispose.Suppressed1PublicAccessPostDisposeModel" },
        new Object[]{ "com.example.post_dispose.Suppressed2ProtectedAccessPostDisposeModel" },
        new Object[]{ "com.example.post_dispose.Suppressed2PublicAccessPostDisposeModel" },
        new Object[]{ "com.example.post_inverse_add.Suppressed1ProtectedAccessPostInverseAddModel" },
        new Object[]{ "com.example.post_inverse_add.Suppressed1PublicAccessPostInverseAddModel" },
        new Object[]{ "com.example.post_inverse_add.Suppressed2ProtectedAccessPostInverseAddModel" },
        new Object[]{ "com.example.post_inverse_add.Suppressed2PublicAccessPostInverseAddModel" },
        new Object[]{ "com.example.pre_dispose.Suppressed1ProtectedAccessPreDisposeModel" },
        new Object[]{ "com.example.pre_dispose.Suppressed1PublicAccessPreDisposeModel" },
        new Object[]{ "com.example.pre_dispose.Suppressed2ProtectedAccessPreDisposeModel" },
        new Object[]{ "com.example.pre_dispose.Suppressed2PublicAccessPreDisposeModel" },
        new Object[]{ "com.example.pre_inverse_remove.Suppressed1ProtectedAccessPreInverseRemoveModel" },
        new Object[]{ "com.example.pre_inverse_remove.Suppressed1PublicAccessPreInverseRemoveModel" },
        new Object[]{ "com.example.pre_inverse_remove.Suppressed2ProtectedAccessPreInverseRemoveModel" },
        new Object[]{ "com.example.pre_inverse_remove.Suppressed2PublicAccessPreInverseRemoveModel" }
      };
  }

  @Test( dataProvider = "compileWithoutWarnings" )
  public void processCompileWithoutWarnings( @Nonnull final String classname )
  {
    assertCompilesWithoutWarnings( classname );
  }

  @DataProvider( name = "packageAccessElementInDifferentPackage" )
  public Object[][] packageAccessElementInDifferentPackage()
  {
    return new Object[][]
      {
        new Object[]{ "Action", "Action" },
        new Object[]{ "Observe", "Observe" },
        new Object[]{ "CascadeDispose", "CascadeDisposeMethod" },
        new Object[]{ "ComponentId", "ComponentId" },
        new Object[]{ "ComponentNameRef", "ComponentNameRef" },
        new Object[]{ "ComponentRef", "ComponentRef" },
        new Object[]{ "Memoize", "Memoize" },
        new Object[]{ "OnActivate", "OnActivate" },
        new Object[]{ "OnDeactivate", "OnDeactivate" },
        new Object[]{ "ComputableValueRef", "ComputableValueRef" },
        new Object[]{ "ContextRef", "ContextRef" },
        new Object[]{ "ComponentDependency", "ComponentDependency" },
        new Object[]{ "Observable", "Observable" },
        new Object[]{ "ObservableValueRef", "ObservableValueRef" },
        new Object[]{ "ObserverRef", "ObserverRef" },
        new Object[]{ "PostConstruct", "PostConstruct" },
        new Object[]{ "OnDepsChange", "OnDepsChange" }
      };
  }

  @Test( dataProvider = "packageAccessElementInDifferentPackage" )
  public void processFailedCompileInheritedPackageAccessInDifferentPackage( @Nonnull final String annotation,
                                                                            @Nonnull final String name )
  {
    final JavaFileObject source1 =
      fixture( "bad_input/com/example/package_access/other/Base" + name + "Model.java" );
    final JavaFileObject source2 =
      fixture( "bad_input/com/example/package_access/" + name + "Model.java" );
    assertFailedCompileResource( Arrays.asList( source1, source2 ),
                                 "@" + annotation + " target must not be package access if " +
                                 "the method is in a different package from the type annotated with the " +
                                 "@ArezComponent annotation" );
  }

  @DataProvider( name = "packageAccessFieldInDifferentPackage" )
  public Object[][] packageAccessFieldInDifferentPackage()
  {
    return new Object[][]
      {
        new Object[]{ "CascadeDispose", "CascadeDispose" }
      };
  }

  @Test( dataProvider = "packageAccessFieldInDifferentPackage" )
  public void processFailedCompileInheritedPackageAccessFieldInDifferentPackage( @Nonnull final String annotation,
                                                                                 @Nonnull final String name )
  {
    final JavaFileObject source1 =
      fixture( "bad_input/com/example/package_access/other/Base" + name + "Model.java" );
    final JavaFileObject source2 =
      fixture( "bad_input/com/example/package_access/" + name + "Model.java" );
    assertFailedCompileResource( Arrays.asList( source1, source2 ),
                                 "@" + annotation + " target must not be package access if " +
                                 "the field is in a different package from the type annotated with the " +
                                 "@ArezComponent annotation" );
  }

  @Test
  public void processFailedCompileInheritedPackageAccessInDifferentPackageWhenInRoot()
  {
    final JavaFileObject source1 = fixture( "bad_input/com/example/package_access/other/BaseActionModel.java" );
    final JavaFileObject source2 = fixture( "bad_input/PackageAccessActionModel.java" );
    assertFailedCompileResource( Arrays.asList( source1, source2 ),
                                 "@Action target must not be package access if " +
                                 "the method is in a different package from the type annotated with the " +
                                 "@ArezComponent annotation" );
  }

  @Test
  public void processFailedCompileInheritedPackageAccessInDifferentPackageObservable_Setter()
  {
    final JavaFileObject source1 = fixture( "bad_input/com/example/package_access/other/BaseObservable2Model.java" );
    final JavaFileObject source2 = fixture( "bad_input/com/example/package_access/Observable2Model.java" );
    assertFailedCompileResource( Arrays.asList( source1, source2 ),
                                 "@Observable target must not be package access if " +
                                 "the method is in a different package from the type annotated with the " +
                                 "@ArezComponent annotation" );
  }

  @Test
  public void processFailedCompileInheritedPackageAccessInDifferentPackageObservable_Getter()
  {
    final JavaFileObject source1 = fixture( "bad_input/com/example/package_access/other/BaseObservable3Model.java" );
    final JavaFileObject source2 = fixture( "bad_input/com/example/package_access/Observable3Model.java" );
    assertFailedCompileResource( Arrays.asList( source1, source2 ),
                                 "@Observable target must not be package access if " +
                                 "the method is in a different package from the type annotated with the " +
                                 "@ArezComponent annotation" );
  }

  @Test
  public void unresolvedComponent()
  {
    final JavaFileObject source1 = fixture( "unresolved/com/example/component/UnresolvedComponent.java" );
    final List<JavaFileObject> inputs = Collections.singletonList( source1 );
    assertFailedCompileResource( inputs,
                                 "ArezProcessor unable to process com.example.component.UnresolvedComponent because not all of its dependencies could be resolved. Check for compilation errors or a circular dependency with generated code." );

    final String optionPrefix = getOptionPrefix();
    CompileTestUtil.assertCompilesWithoutWarnings( inputs,
                                                   Arrays.asList( "-Xlint:all,-processing",
                                                                  "-implicit:none",
                                                                  "-A" + optionPrefix + ".defer.errors=false",
                                                                  "-A" + optionPrefix + ".defer.unresolved=false" ),
                                                   List.of( processor() ),
                                                   Collections.emptyList() );
  }

  private void assertSuccessfulCompile( @Nonnull final String classname )
    throws Exception
  {
    assertSuccessfulCompile( classname, deriveExpectedOutputs( classname ) );
  }

  @Nonnull
  private String[] deriveExpectedOutputs( @Nonnull final String classname )
  {
    final List<String> expectedOutputs = new ArrayList<>();
    expectedOutputs.add( toFilename( classname, "Arez_", ".java" ) );
    return expectedOutputs.toArray( new String[ 0 ] );
  }

  @Nonnull
  @Override
  protected String getOptionPrefix()
  {
    return "arez";
  }

  @Nonnull
  @Override
  protected ArezProcessor processor()
  {
    return new ArezProcessor();
  }
}
