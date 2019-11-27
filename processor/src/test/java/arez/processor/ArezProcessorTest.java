package arez.processor;

import com.google.testing.compile.JavaSourcesSubjectFactory;
import java.util.Arrays;
import java.util.Collections;
import javax.annotation.Nonnull;
import javax.tools.JavaFileObject;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static com.google.common.truth.Truth.*;

public class ArezProcessorTest
  extends AbstractArezProcessorTest
{
  @DataProvider( name = "successfulCompiles" )
  public Object[][] successfulCompiles()
  {
    return new Object[][]
      {
        new Object[]{ "com.example.SubpackageModel", false, false, false, false },
        new Object[]{ "com.example.action.ActionTypeParametersModel", false, false, false, false },
        new Object[]{ "com.example.action.NewTypeParametersModel", false, false, false, false },
        new Object[]{ "com.example.action.NoReportParametersModel", false, false, false, false },
        new Object[]{ "com.example.action.FunctionActionThrowsRuntimeExceptionModel", false, false, false, false },
        new Object[]{ "com.example.action.FunctionActionThrowsThrowableModel", false, false, false, false },
        new Object[]{ "com.example.action.MultiThrowAction", false, false, false, false },
        new Object[]{ "com.example.action.NonStandardNameActionModel", false, false, false, false },
        new Object[]{ "com.example.action.UnsafeSpecificFunctionActionModel", false, false, false, false },
        new Object[]{ "com.example.action.UnsafeSpecificProcedureActionModel", false, false, false, false },
        new Object[]{ "com.example.action.UnsafeFunctionActionModel", false, false, false, false },
        new Object[]{ "com.example.action.UnsafeProcedureActionModel", false, false, false, false },
        new Object[]{ "com.example.action.NoReportResultActionModel", false, false, false, false },
        new Object[]{ "com.example.action.NoVerifyActionModel", false, false, false, false },
        new Object[]{ "com.example.action.ReadOnlyActionModel", false, false, false, false },
        new Object[]{ "com.example.action.RequiresNewTxTypeActionModel", false, false, false, false },
        new Object[]{ "com.example.action.RequiresTxTypeActionModel", false, false, false, false },
        new Object[]{ "com.example.action.BasicFunctionActionModel", false, false, false, false },
        new Object[]{ "com.example.action.BasicActionModel", false, false, false, false },
        new Object[]{ "com.example.cascade_dispose.ComponentCascadeDisposeModel", false, false, false, false },
        new Object[]{ "com.example.cascade_dispose.ComponentCascadeDisposeMethodModel", false, false, false, false },
        new Object[]{ "com.example.cascade_dispose.DisposableCascadeDisposeModel", false, false, false, false },
        new Object[]{ "com.example.cascade_dispose.DisposableCascadeDisposeMethodModel", false, false, false, false },
        new Object[]{ "com.example.cascade_dispose.NonStandardNameCascadeDisposeMethodModel",
                      false,
                      false,
                      false,
                      false },
        new Object[]{ "com.example.cascade_dispose.NonStandardNameDisposableCascadeDisposeModel",
                      false,
                      false,
                      false,
                      false },
        new Object[]{ "com.example.cascade_dispose.ObservableCascadeDisposeModel", false, false, false, false },
        new Object[]{ "com.example.component.DeprecatedModel", false, false, false, false },
        new Object[]{ "com.example.component.PublicCtorNonPublicModel", false, false, false, false },
        new Object[]{ "com.example.component.Suppressed1UnnecessaryDefaultPriorityPresentComponent", false, false, false, false },
        new Object[]{ "com.example.component.Suppressed2UnnecessaryDefaultPriorityPresentComponent", false, false, false, false },
        new Object[]{ "com.example.collections.AbstractCollectionObservableModel", false, false, false, false },
        new Object[]{ "com.example.collections.AbstractListObservableModel", false, false, false, false },
        new Object[]{ "com.example.collections.AbstractMapObservableModel", false, false, false, false },
        new Object[]{ "com.example.collections.AbstractNonnullCollectionObservableModel", false, false, false, false },
        new Object[]{ "com.example.collections.AbstractNonnullListObservableModel", false, false, false, false },
        new Object[]{ "com.example.collections.AbstractNonnullMapObservableModel", false, false, false, false },
        new Object[]{ "com.example.collections.AbstractNonnullSetObservableModel", false, false, false, false },
        new Object[]{ "com.example.collections.AbstractSetObservableModel", false, false, false, false },
        new Object[]{ "com.example.collections.MemoizeCollectionModel", false, false, false, false },
        new Object[]{ "com.example.collections.MemoizeCollectionWithHooksModel", false, false, false, false },
        new Object[]{ "com.example.collections.MemoizeKeepAliveListModel", false, false, false, false },
        new Object[]{ "com.example.collections.MemoizeListModel", false, false, false, false },
        new Object[]{ "com.example.collections.MemoizeMapModel", false, false, false, false },
        new Object[]{ "com.example.collections.MemoizeNonnullCollectionModel", false, false, false, false },
        new Object[]{ "com.example.collections.MemoizeNonnullListModel", false, false, false, false },
        new Object[]{ "com.example.collections.MemoizeNonnullMapModel", false, false, false, false },
        new Object[]{ "com.example.collections.MemoizeNonnullSetModel", false, false, false, false },
        new Object[]{ "com.example.collections.MemoizeSetModel", false, false, false, false },
        new Object[]{ "com.example.collections.ObservableCollectionModel", false, false, false, false },
        new Object[]{ "com.example.collections.ObservableListModel", false, false, false, false },
        new Object[]{ "com.example.collections.ObservableMapModel", false, false, false, false },
        new Object[]{ "com.example.collections.ObservableNonnullCollectionModel", false, false, false, false },
        new Object[]{ "com.example.collections.ObservableNonnullListModel", false, false, false, false },
        new Object[]{ "com.example.collections.ObservableNonnullMapModel", false, false, false, false },
        new Object[]{ "com.example.collections.ObservableNonnullSetModel", false, false, false, false },
        new Object[]{ "com.example.collections.ObservableNoSettersModel", false, false, false, false },
        new Object[]{ "com.example.collections.ObservableSetModel", false, false, false, false },
        new Object[]{ "com.example.component.DisposeOnDeactivateModel", false, false, false, false },
        new Object[]{ "com.example.component.GeneratedNonEmptyComponent", false, false, false, false },
        new Object[]{ "com.example.component.NoRequireEqualsModel", false, false, false, false },
        new Object[]{ "com.example.component.NotObservableModel", false, false, false, false },
        new Object[]{ "com.example.component.ObservableModel", false, false, false, false },
        new Object[]{ "com.example.component_id.BooleanComponentId", false, false, false, false },
        new Object[]{ "com.example.component_id.BooleanComponentIdRequireEquals", false, false, false, false },
        new Object[]{ "com.example.component_id.ByteComponentId", false, false, false, false },
        new Object[]{ "com.example.component_id.ByteComponentIdRequireEquals", false, false, false, false },
        new Object[]{ "com.example.component_id.CharComponentId", false, false, false, false },
        new Object[]{ "com.example.component_id.CharComponentIdRequireEquals", false, false, false, false },
        new Object[]{ "com.example.component_id.ComponentIdOnModel", false, false, false, false },
        new Object[]{ "com.example.component_id.DoubleComponentId", false, false, false, false },
        new Object[]{ "com.example.component_id.DoubleComponentIdRequireEquals", false, false, false, false },
        new Object[]{ "com.example.component_id.FloatComponentId", false, false, false, false },
        new Object[]{ "com.example.component_id.FloatComponentIdRequireEquals", false, false, false, false },
        new Object[]{ "com.example.component_id.IntComponentId", false, false, false, false },
        new Object[]{ "com.example.component_id.IntComponentIdRequireEquals", false, false, false, false },
        new Object[]{ "com.example.component_id.LongComponentId", false, false, false, false },
        new Object[]{ "com.example.component_id.LongComponentIdRequireEquals", false, false, false, false },
        new Object[]{ "com.example.component_id.NonStandardNameComponentId", false, false, false, false },
        new Object[]{ "com.example.component_id.ObjectComponentId", false, false, false, false },
        new Object[]{ "com.example.component_id.ObjectComponentIdRequireEquals", false, false, false, false },
        new Object[]{ "com.example.component_id.ShortComponentId", false, false, false, false },
        new Object[]{ "com.example.component_id.ShortComponentIdRequireEquals", false, false, false, false },
        new Object[]{ "com.example.component_id.ComponentIdOnSingletonModel", false, false, false, false },

        new Object[]{ "com.example.component_id_ref.BasicComponentIdRefModel", false, false, false, false },
        new Object[]{ "com.example.component_id_ref.ComponentIdPresentComponentIdRefModel",
                      false,
                      false,
                      false,
                      false },
        new Object[]{ "com.example.component_id_ref.NonIntTypeComponentIdRefModel", false, false, false, false },
        new Object[]{ "com.example.component_id_ref.NonStandardNameComponentIdRefModel", false, false, false, false },
        new Object[]{ "com.example.component_id_ref.PackageAccessComponentIdRefModel", false, false, false, false },
        new Object[]{ "com.example.component_id_ref.ProtectedAccessComponentIdRefModel", false, false, false, false },
        new Object[]{ "com.example.component_id_ref.PublicAccessComponentIdRefModel", false, false, false, false },
        new Object[]{ "com.example.component_id_ref.RawTypeComponentIdRefModel", false, false, false, false },

        new Object[]{ "com.example.component_name_ref.BasicComponentNameRefModel", false, false, false, false },
        new Object[]{ "com.example.component_name_ref.NonStandardMethodNameComponentNameRefModel",
                      false,
                      false,
                      false,
                      false },
        new Object[]{ "com.example.component_name_ref.PackageAccessComponentNameRefModel", false, false, false, false },
        new Object[]{ "com.example.component_name_ref.SingletonComponentNameRefModel", false, false, false, false },
        new Object[]{ "com.example.component_name_ref.Suppressed1ProtectedAccessComponentNameRefModel",
                      false,
                      false,
                      false,
                      false },
        new Object[]{ "com.example.component_name_ref.Suppressed1PublicAccessComponentNameRefModel",
                      false,
                      false,
                      false,
                      false },
        new Object[]{ "com.example.component_name_ref.Suppressed2PublicAccessComponentNameRefModel",
                      false,
                      false,
                      false,
                      false },

        new Object[]{ "com.example.component_ref.BasicComponentRefModel", false, false, false, false },
        new Object[]{ "com.example.component_ref.NonStandardNameComponentRefModel", false, false, false, false },
        new Object[]{ "com.example.component_ref.PackageAccessComponentRefModel", false, false, false, false },
        new Object[]{ "com.example.component_ref.Suppressed1ProtectedAccessComponentRefModel",
                      false,
                      false,
                      false,
                      false },
        new Object[]{ "com.example.component_ref.Suppressed1PublicAccessComponentRefModel",
                      false,
                      false,
                      false,
                      false },
        new Object[]{ "com.example.component_ref.Suppressed2ProtectedAccessComponentRefModel",
                      false,
                      false,
                      false,
                      false },
        new Object[]{ "com.example.component_ref.Suppressed2PublicAccessComponentRefModel",
                      false,
                      false,
                      false,
                      false },

        new Object[]{ "com.example.component_state_ref.CompleteComponentStateRefModel", false, false, false, false },
        new Object[]{ "com.example.component_state_ref.ConstructedComponentStateRefModel", false, false, false, false },
        new Object[]{ "com.example.component_state_ref.DefaultComponentStateRefModel", false, false, false, false },
        new Object[]{ "com.example.component_state_ref.DisposingComponentStateRefModel", false, false, false, false },
        new Object[]{ "com.example.component_state_ref.MultipleComponentStateRefModel", false, false, false, false },
        new Object[]{ "com.example.component_state_ref.PackageAccessComponentStateRefModel",
                      false,
                      false,
                      false,
                      false },
        new Object[]{ "com.example.component_state_ref.ReadyComponentStateRefModel", false, false, false, false },
        new Object[]{ "com.example.component_state_ref.Suppressed1ProtectedAccessComponentStateRefModel",
                      false,
                      false,
                      false,
                      false },
        new Object[]{ "com.example.component_state_ref.Suppressed1PublicAccessComponentStateRefModel",
                      false,
                      false,
                      false,
                      false },
        new Object[]{ "com.example.component_state_ref.Suppressed2ProtectedAccessComponentStateRefModel",
                      false,
                      false,
                      false,
                      false },
        new Object[]{ "com.example.component_state_ref.Suppressed2PublicAccessComponentStateRefModel",
                      false,
                      false,
                      false,
                      false },

        new Object[]{ "com.example.component_type_name_ref.BasicComponentTypeNameRefModel",
                      false,
                      false,
                      false,
                      false },
        new Object[]{ "com.example.component_type_name_ref.NonStandardMethodNameComponentTypeNameRefModel",
                      false,
                      false,
                      false,
                      false },
        new Object[]{ "com.example.component_type_name_ref.SingletonComponentTypeNameRefModel",
                      false,
                      false,
                      false,
                      false },
        new Object[]{ "com.example.component_type_name_ref.PackageAccessComponentTypeNameRefModel",
                      false,
                      false,
                      false,
                      false },
        new Object[]{ "com.example.component_type_name_ref.Suppressed1ProtectedAccessComponentTypeNameRefModel",
                      false,
                      false,
                      false,
                      false },
        new Object[]{ "com.example.component_type_name_ref.Suppressed1PublicAccessComponentTypeNameRefModel",
                      false,
                      false,
                      false,
                      false },
        new Object[]{ "com.example.component_type_name_ref.Suppressed2ProtectedAccessComponentTypeNameRefModel",
                      false,
                      false,
                      false,
                      false },
        new Object[]{ "com.example.component_type_name_ref.Suppressed2PublicAccessComponentTypeNameRefModel",
                      false,
                      false,
                      false,
                      false },

        new Object[]{ "com.example.memoize.ArezOrNoneDependenciesModel", false, false, false, false },
        new Object[]{ "com.example.memoize.NameVariationsModel", false, false, false, false },
        new Object[]{ "com.example.memoize.HighestPriorityModel", false, false, false, false },
        new Object[]{ "com.example.memoize.HighPriorityModel", false, false, false, false },
        new Object[]{ "com.example.memoize.NormalPriorityModel", false, false, false, false },
        new Object[]{ "com.example.memoize.LowestPriorityModel", false, false, false, false },
        new Object[]{ "com.example.memoize.LowPriorityModel", false, false, false, false },
        new Object[]{ "com.example.memoize.NonArezDependenciesModel", false, false, false, false },
        new Object[]{ "com.example.memoize.NoReportResultModel", false, false, false, false },
        new Object[]{ "com.example.memoize.WithHooksModel", false, false, false, false },
        new Object[]{ "com.example.memoize.KeepAliveModel", false, false, false, false },
        new Object[]{ "com.example.memoize.ObserveLowerPriorityModel", false, false, false, false },
        new Object[]{ "com.example.memoize.OnActivateModel", false, false, false, false },
        new Object[]{ "com.example.memoize.OnActivateWithComputableValueParamModel", false, false, false, false },
        new Object[]{ "com.example.memoize.OnActivateWithRawComputableValueParamModel", false, false, false, false },
        new Object[]{ "com.example.memoize.OnActivateWithWildcardComputableValueParamModel",
                      false,
                      false,
                      false,
                      false },
        new Object[]{ "com.example.memoize.OnDeactivateModel", false, false, false, false },
        new Object[]{ "com.example.memoize.OnStaleModel", false, false, false, false },
        new Object[]{ "com.example.memoize.ReadOutsideTransactionModel", false, false, false, false },
        new Object[]{ "com.example.memoize.ScheduleDeferredKeepAliveModel", false, false, false, false },
        new Object[]{ "com.example.memoize.TypeParametersModel", false, false, false, false },
        new Object[]{ "com.example.computable_value_ref.BasicComputableValueRefModel", false, false, false, false },
        new Object[]{ "com.example.computable_value_ref.NonStandardName1ComputableValueRefModel",
                      false,
                      false,
                      false,
                      false },
        new Object[]{ "com.example.computable_value_ref.NonStandardName2ComputableValueRefModel",
                      false,
                      false,
                      false,
                      false },
        new Object[]{ "com.example.computable_value_ref.PackageAccessComputableValueRefModel",
                      false,
                      false,
                      false,
                      false },
        new Object[]{ "com.example.computable_value_ref.ParametersComputableValueRefModel",
                      false,
                      false,
                      false,
                      false },
        new Object[]{ "com.example.computable_value_ref.RawComputableValueRefModel", false, false, false, false },
        new Object[]{ "com.example.computable_value_ref.RawWithParamsComputableValueRefModel",
                      false,
                      false,
                      false,
                      false },
        new Object[]{ "com.example.computable_value_ref.Suppressed1ProtectedAccessComputableValueRefModel",
                      false,
                      false,
                      false,
                      false },
        new Object[]{ "com.example.computable_value_ref.Suppressed1PublicAccessComputableValueRefModel",
                      false,
                      false,
                      false,
                      false },
        new Object[]{ "com.example.computable_value_ref.Suppressed2ProtectedAccessComputableValueRefModel",
                      false,
                      false,
                      false,
                      false },
        new Object[]{ "com.example.computable_value_ref.Suppressed2PublicAccessComputableValueRefModel",
                      false,
                      false,
                      false,
                      false },

        new Object[]{ "com.example.context_ref.BasicContextRefModel", false, false, false, false },
        new Object[]{ "com.example.context_ref.NonStandardMethodNameContextRefModel", false, false, false, false },
        new Object[]{ "com.example.context_ref.Suppressed1ProtectedAccessContextRefModel", false, false, false, false },
        new Object[]{ "com.example.context_ref.Suppressed1PublicAccessContextRefModel", false, false, false, false },
        new Object[]{ "com.example.context_ref.Suppressed2ProtectedAccessContextRefModel", false, false, false, false },
        new Object[]{ "com.example.context_ref.Suppressed2PublicAccessContextRefModel", false, false, false, false },

        new Object[]{ "com.example.component_dependency.AbstractObservableDependency", false, false, false, false },
        new Object[]{ "com.example.component_dependency.ActAsComponentFieldDependencyModel",
                      false,
                      false,
                      false,
                      false },
        new Object[]{ "com.example.component_dependency.ActAsComponentMethodDependencyModel",
                      false,
                      false,
                      false,
                      false },
        new Object[]{ "com.example.component_dependency.BasicDependencyModel", false, false, false, false },
        new Object[]{ "com.example.component_dependency.BasicFieldDependencyModel", false, false, false, false },
        new Object[]{ "com.example.component_dependency.CascadeDependencyModel", false, false, false, false },
        new Object[]{ "com.example.component_dependency.CascadeFieldDependencyModel", false, false, false, false },
        new Object[]{ "com.example.component_dependency.ComplexDependencyModel", false, false, false, false },
        new Object[]{ "com.example.component_dependency.ComplexDependencyWithCustomNameMethodModel",
                      false,
                      false,
                      false,
                      false },
        new Object[]{ "com.example.component_dependency.ComponentDependencyModel", false, false, false, false },
        new Object[]{ "com.example.component_dependency.ComponentFieldDependencyModel", false, false, false, false },
        new Object[]{ "com.example.component_dependency.ConcreteObservablePairWithInitializerDependency",
                      false,
                      false,
                      false,
                      false },
        new Object[]{ "com.example.component_dependency.NonCascadeObservableDependency", false, false, false, false },
        new Object[]{ "com.example.component_dependency.NonnullAbstractObservableDependency",
                      false,
                      false,
                      false,
                      false },
        new Object[]{ "com.example.component_dependency.NonnullFieldDependencyModel", false, false, false, false },
        new Object[]{ "com.example.component_dependency.NonnullObservableDependency", false, false, false, false },
        new Object[]{ "com.example.component_dependency.NonStandardNameDependencyModel", false, false, false, false },
        new Object[]{ "com.example.component_dependency.NonStandardNameFieldDependencyModel",
                      false,
                      false,
                      false,
                      false },
        new Object[]{ "com.example.component_dependency.ObservableDependency", false, false, false, false },
        new Object[]{ "com.example.component_dependency.ObservablePairWithInitializerDependency",
                      false,
                      false,
                      false,
                      false },
        new Object[]{ "com.example.component_dependency.ObservablePairAnnotatedDependency",
                      false,
                      false,
                      false,
                      false },
        new Object[]{ "com.example.component_dependency.RuntimeTypeValidateDependency", false, false, false, false },
        new Object[]{ "com.example.component_dependency.RuntimeTypeValidateFieldDependency",
                      false,
                      false,
                      false,
                      false },
        new Object[]{ "com.example.component_dependency.ScheduleDeferredDependencyModel", false, false, false, false },
        new Object[]{ "com.example.component_dependency.SetNullObservableDependency", false, false, false, false },
        new Object[]{ "com.example.deprecated.DeprecatedActionModel", false, false, false, false },
        new Object[]{ "com.example.deprecated.DeprecatedObserveModel", false, false, false, false },
        new Object[]{ "com.example.deprecated.DeprecatedMemoizeModel1", false, false, false, false },
        new Object[]{ "com.example.deprecated.DeprecatedMemoizeModel2", false, false, false, false },
        new Object[]{ "com.example.deprecated.DeprecatedMemoizeModel3", false, false, false, false },
        new Object[]{ "com.example.deprecated.DeprecatedMemoizeModel4", false, false, false, false },
        new Object[]{ "com.example.deprecated.DeprecatedMemoize5Model", false, false, false, false },
        new Object[]{ "com.example.deprecated.DeprecatedObservableModel1", false, false, false, false },
        new Object[]{ "com.example.deprecated.DeprecatedObservableModel2", false, false, false, false },
        new Object[]{ "com.example.deprecated.DeprecatedPostConstructModel", false, false, false, false },
        new Object[]{ "com.example.deprecated.DeprecatedObserveModel1", false, false, false, false },
        new Object[]{ "com.example.deprecated.DeprecatedObserveModel2", false, false, false, false },
        new Object[]{ "com.example.deprecated.DeprecatedObserveModel3", false, false, false, false },
        new Object[]{ "com.example.deprecated.DeprecatedObserveModel4", false, false, false, false },
        new Object[]{ "com.example.dispose_notifier.DisposeNotifierModel", false, false, false, false },
        new Object[]{ "com.example.dispose_notifier.NoDisposeNotifierModel", false, false, false, false },
        new Object[]{ "com.example.id.ComponentIdExample", false, false, false, false },
        new Object[]{ "com.example.id.NonStandardNameModel", false, false, false, false },
        new Object[]{ "com.example.id.RepositoryExample", false, false, true, true },
        new Object[]{ "com.example.id.RequireIdDisable", false, false, false, false },
        new Object[]{ "com.example.id.RequireIdEnable", false, false, false, false },
        new Object[]{ "com.example.inject.BasicInjectModel", false, false, false, false },
        new Object[]{ "com.example.inject.CtorInjectModel", false, false, false, false },
        new Object[]{ "com.example.inject.CtorInjectSuppressRawTypeAtClassModel", false, false, false, false },
        new Object[]{ "com.example.inject.CtorInjectSuppressRawTypeAtCtorModel", false, false, false, false },
        new Object[]{ "com.example.inject.CtorInjectSuppressRawTypeAtParamModel", false, false, false, false },
        new Object[]{ "com.example.inject.CtorInjectWithFactoryModel", false, false, false, false },
        new Object[]{ "com.example.inject.CtorInjectWithObserveAndFactoryModel", false, false, false, false },
        new Object[]{ "com.example.inject.CtorInjectWithObserveModel", false, false, false, false },
        new Object[]{ "com.example.inject.CtorInjectWithPostConstructAndFactoryModel", false, false, false, false },
        new Object[]{ "com.example.inject.CtorInjectWithPostConstructModel", false, false, false, false },
        new Object[]{ "com.example.inject.CtorInjectWithTrackingObserveAndFactoryModel", false, false, false, false },
        new Object[]{ "com.example.inject.CtorInjectWithTrackingObserveModel", false, false, false, false },
        new Object[]{ "com.example.inject.DefaultCtorModel", false, false, false, false },
        new Object[]{ "com.example.inject.FactoryConsumerBasicModel", false, true, false, false },
        new Object[]{ "com.example.inject.FactoryConsumerWithInjectedModel", false, true, false, false },
        new Object[]{ "com.example.inject.FactoryConsumerWithAnnotatedInjectedModel", false, true, false, false },
        new Object[]{ "com.example.inject.FactoryConsumerAnnotatedPerInstanceModel", false, true, false, false },
        new Object[]{ "com.example.inject.FactoryConsumerWithRawTypeInjectedModel", false, true, false, false },
        new Object[]{ "com.example.inject.FactoryConsumerWithRawTypeInjectedSuppressedAtClassModel",
                      false,
                      true,
                      false,
                      false },
        new Object[]{ "com.example.inject.MultipleArgsModel", false, false, false, false },
        new Object[]{ "com.example.inject.NoInjectModel", false, false, false, false },
        new Object[]{ "com.example.inject.ScopedButNoDaggerModel", false, false, false, false },
        new Object[]{ "com.example.inject.ScopedInjectModel", true, false, false, false },
        new Object[]{ "com.example.inverse.CustomNamesInverseModel", false, false, false, false },
        new Object[]{ "com.example.inverse.DefaultMultiplicityInverseModel", false, false, false, false },
        new Object[]{ "com.example.inverse.DisableInverseModel", false, false, false, false },
        new Object[]{ "com.example.inverse.NonGetterInverseModel", false, false, false, false },
        new Object[]{ "com.example.inverse.NonObservableCollectionInverseModel", false, false, false, false },
        new Object[]{ "com.example.inverse.NonObservableNullableManyReferenceModel", false, false, false, false },
        new Object[]{ "com.example.inverse.NonObservableNullableOneReferenceModel", false, false, false, false },
        new Object[]{ "com.example.inverse.NonObservableNullableZeroOrOneReferenceModel", false, false, false, false },
        new Object[]{ "com.example.inverse.NonStandardNameModel", false, false, false, false },
        new Object[]{ "com.example.inverse.ObservableCollectionInverseModel", false, false, false, false },
        new Object[]{ "com.example.inverse.ObservableListInverseModel", false, false, false, false },
        new Object[]{ "com.example.inverse.ObservableManyReferenceModel", false, false, false, false },
        new Object[]{ "com.example.inverse.ObservableOneReferenceModel", false, false, false, false },
        new Object[]{ "com.example.inverse.ObservableReferenceInverseModel", false, false, false, false },
        new Object[]{ "com.example.inverse.ObservableSetInverseModel", false, false, false, false },
        new Object[]{ "com.example.inverse.ObservableZeroOrOneReferenceModel", false, false, false, false },
        new Object[]{ "com.example.inverse.OneMultiplicityInverseModel", false, false, false, false },
        new Object[]{ "com.example.inverse.ZeroOrOneMultiplicityInverseModel", false, false, false, false },
        new Object[]{ "com.example.memoize.BasicModel", false, false, false, false },
        new Object[]{ "com.example.memoize.CustomDepTypeModel", false, false, false, false },
        new Object[]{ "com.example.memoize.CustomPriorityModel", false, false, false, false },
        new Object[]{ "com.example.memoize.DefaultDefaultPriorityUnspecifiedLocalPriorityMemoizeModel",
                      false,
                      false,
                      false,
                      false },
        new Object[]{ "com.example.memoize.DefaultPriorityDefaultLocalPriorityMemoizeModel",
                      false,
                      false,
                      false,
                      false },
        new Object[]{ "com.example.memoize.DefaultPrioritySpecifiedLocalPriorityMemoizeModel",
                      false,
                      false,
                      false,
                      false },
        new Object[]{ "com.example.memoize.DefaultPriorityUnspecifiedLocalPriorityMemoizeModel",
                      false,
                      false,
                      false,
                      false },
        new Object[]{ "com.example.memoize.LocalTypeParamModel", false, false, false, false },
        new Object[]{ "com.example.memoize.NonStandardNameModel", false, false, false, false },
        new Object[]{ "com.example.memoize.TypeParamModel", false, false, false, false },
        new Object[]{ "com.example.observable.AbstractNonPrimitiveObservablesModel", false, false, false, false },
        new Object[]{ "com.example.observable.AbstractObservablesModel", false, false, false, false },
        new Object[]{ "com.example.observable.GenericObservableModel", false, false, false, false },
        new Object[]{ "com.example.observable.InitializerAndConstructorParamNameCollisionModel",
                      false,
                      false,
                      false,
                      false },
        new Object[]{ "com.example.observable.NonStandardNameModel", false, false, false, false },
        new Object[]{ "com.example.observable.NullableInitializerModel", false, false, false, false },
        new Object[]{ "com.example.observable.ObservableWithNoSetter", false, false, false, false },
        new Object[]{ "com.example.observable.ReadOutsideTransactionObservableModel", false, false, false, false },
        new Object[]{ "com.example.observable.SetterAlwaysMutatesFalseObjectValue", false, false, false, false },
        new Object[]{ "com.example.observable.SetterAlwaysMutatesFalsePrimitiveValue", false, false, false, false },
        new Object[]{ "com.example.observable.WildcardGenericObservableModel", false, false, false, false },
        new Object[]{ "com.example.observable.WriteOutsideTransactionObservablesModel", false, false, false, false },
        new Object[]{ "com.example.observable.WriteOutsideTransactionThrowingObservablesModel",
                      false,
                      false,
                      false,
                      false },
        new Object[]{ "com.example.observable.AbstractNonPrimitiveNonnullObservablesModel",
                      false,
                      false,
                      false,
                      false },
        new Object[]{ "com.example.observable.AbstractPrimitiveObservablesWithInitializerModel",
                      false,
                      false,
                      false,
                      false },

        new Object[]{ "com.example.observable_value_ref.BasicObservableValueRefModel", false, false, false, false },
        new Object[]{ "com.example.observable_value_ref.GenericObservableValueRefModel", false, false, false, false },
        new Object[]{ "com.example.observable_value_ref.NonStandardMethodName1ObservableValueRefModel",
                      false,
                      false,
                      false,
                      false },
        new Object[]{ "com.example.observable_value_ref.NonStandardMethodName2ObservableValueRefModel",
                      false,
                      false,
                      false,
                      false },
        new Object[]{ "com.example.observable_value_ref.PackageAccessObservableValueRefModel",
                      false,
                      false,
                      false,
                      false },
        new Object[]{ "com.example.observable_value_ref.RawObservableValueRefModel", false, false, false, false },
        new Object[]{ "com.example.observable_value_ref.Suppressed1ProtectedAccessObservableValueRefModel",
                      false,
                      false,
                      false,
                      false },
        new Object[]{ "com.example.observable_value_ref.Suppressed1PublicAccessObservableValueRefModel",
                      false,
                      false,
                      false,
                      false },
        new Object[]{ "com.example.observable_value_ref.Suppressed2ProtectedAccessObservableValueRefModel",
                      false,
                      false,
                      false,
                      false },
        new Object[]{ "com.example.observable_value_ref.Suppressed2PublicAccessObservableValueRefModel",
                      false,
                      false,
                      false,
                      false },
        new Object[]{ "com.example.observable_value_ref.WildcardObservableValueRefModel", false, false, false, false },

        new Object[]{ "com.example.observe.BasicObserveModel", false, false, false, false },
        new Object[]{ "com.example.observe.NestedActionsAllowedObserveModel", false, false, false, false },
        new Object[]{ "com.example.observe.HighestPriorityObserveModel", false, false, false, false },
        new Object[]{ "com.example.observe.HighPriorityObserveModel", false, false, false, false },
        new Object[]{ "com.example.observe.LowestPriorityObserveModel", false, false, false, false },
        new Object[]{ "com.example.observe.LowPriorityObserveModel", false, false, false, false },
        new Object[]{ "com.example.observe.NormalPriorityObserveModel", false, false, false, false },
        new Object[]{ "com.example.observe.ObserveLowerPriorityObserveModel", false, false, false, false },
        new Object[]{ "com.example.observe.ReadWriteObserveModel", false, false, false, false },
        new Object[]{ "com.example.observe.ScheduleAfterConstructedModel", false, false, false, false },
        new Object[]{ "com.example.observe.ScheduleDeferredModel", false, false, false, false },
        new Object[]{ "com.example.observe.ArezOrNoneDependenciesModel", false, false, false, false },
        new Object[]{ "com.example.observe.BasicTrackedModel", false, false, false, false },
        new Object[]{ "com.example.observe.BasicTrackedWithExceptionsModel", false, false, false, false },
        new Object[]{ "com.example.observe.DefaultDefaultPriorityUnspecifiedLocalPriorityObserveModel", false, false, false, false },
        new Object[]{ "com.example.observe.DefaultPriorityDefaultLocalPriorityObserveModel", false, false, false, false },
        new Object[]{ "com.example.observe.DefaultPrioritySpecifiedLocalPriorityObserveModel", false, false, false, false },
        new Object[]{ "com.example.observe.DefaultPriorityUnspecifiedLocalPriorityObserveModel", false, false, false, false },
        new Object[]{ "com.example.observe.NestedActionsAllowedTrackedModel", false, false, false, false },
        new Object[]{ "com.example.observe.NonArezDependenciesModel", false, false, false, false },
        new Object[]{ "com.example.observe.NonStandardNameTrackedModel", false, false, false, false },
        new Object[]{ "com.example.observe.DeriveFinalOnDepsChangeModel", false, false, false, false },
        new Object[]{ "com.example.observe.DeriveOnDepsChangeModel", false, false, false, false },
        new Object[]{ "com.example.observe.DeriveTrackedModel", false, false, false, false },
        new Object[]{ "com.example.observe.HighestPriorityTrackedModel", false, false, false, false },
        new Object[]{ "com.example.observe.HighPriorityTrackedModel", false, false, false, false },
        new Object[]{ "com.example.observe.NormalPriorityTrackedModel", false, false, false, false },
        new Object[]{ "com.example.observe.LowestPriorityTrackedModel", false, false, false, false },
        new Object[]{ "com.example.observe.LowPriorityTrackedModel", false, false, false, false },
        new Object[]{ "com.example.observe.NoReportParametersModel", false, false, false, false },
        new Object[]{ "com.example.observe.NoReportResultModel", false, false, false, false },
        new Object[]{ "com.example.observe.ObserveLowerPriorityTrackedModel", false, false, false, false },
        new Object[]{ "com.example.observe.ProtectedAccessTrackedModel", false, false, false, false },
        new Object[]{ "com.example.observe.TrackedAllTypesModel", false, false, false, false },
        new Object[]{ "com.example.observe.TrackedAndSchedulableModel", false, false, false, false },
        new Object[]{ "com.example.observe.TrackedImplicitOnDepsChangeAcceptsObserverModel",
                      false,
                      false,
                      false,
                      false },
        new Object[]{ "com.example.observe.TrackedNoOtherSchedulableModel", false, false, false, false },
        new Object[]{ "com.example.observe.TrackedOnDepsChangeAcceptsObserverModel", false, false, false, false },

        new Object[]{ "com.example.observer_ref.BasicObserverRefModel", false, false, false, false },
        new Object[]{ "com.example.observer_ref.CustomNameObserverRefModel", false, false, false, false },
        new Object[]{ "com.example.observer_ref.ExternalObserveObserverRefModel", false, false, false, false },
        new Object[]{ "com.example.observer_ref.NonStandardMethodNameObserverRefModel", false, false, false, false },
        new Object[]{ "com.example.observer_ref.PackageAccessObserverRefModel", false, false, false, false },
        new Object[]{ "com.example.observer_ref.Suppressed1ProtectedAccessObserverRefModel",
                      false,
                      false,
                      false,
                      false },
        new Object[]{ "com.example.observer_ref.Suppressed1PublicAccessObserverRefModel", false, false, false, false },
        new Object[]{ "com.example.observer_ref.Suppressed2ProtectedAccessObserverRefModel",
                      false,
                      false,
                      false,
                      false },
        new Object[]{ "com.example.observer_ref.Suppressed2PublicAccessObserverRefModel", false, false, false, false },

        new Object[]{ "com.example.overloaded_names.OverloadedActions", false, false, false, false },

        new Object[]{ "com.example.post_construct.BasicPostConstructModel", false, false, false, false },
        new Object[]{ "com.example.post_construct.NonStandardNamePostConstructModel", false, false, false, false },
        new Object[]{ "com.example.post_construct.PackageAccessPostConstructModel", false, false, false, false },
        new Object[]{ "com.example.post_construct.Suppressed1ProtectedAccessPostConstructModel", false, false, false, false },
        new Object[]{ "com.example.post_construct.Suppressed1PublicAccessPostConstructModel", false, false, false, false },
        new Object[]{ "com.example.post_construct.Suppressed2ProtectedAccessPostConstructModel", false, false, false, false },
        new Object[]{ "com.example.post_construct.Suppressed2PublicAccessPostConstructModel", false, false, false, false },

        new Object[]{ "com.example.post_dispose.PostDisposeModel", false, false, false, false },
        new Object[]{ "com.example.post_dispose.PostDisposeWithDisabledDisposeNotifierModel",
                      false,
                      false,
                      false,
                      false },
        new Object[]{ "com.example.reference.CascadeDisposeReferenceModel", false, false, false, false },
        new Object[]{ "com.example.reference.CustomNameReferenceModel2", false, false, false, false },
        new Object[]{ "com.example.reference.CustomNameReferenceModel", false, false, false, false },
        new Object[]{ "com.example.reference.EagerLoadNulableObservableReferenceModel", false, false, false, false },
        new Object[]{ "com.example.reference.EagerLoadObservableReferenceModel", false, false, false, false },
        new Object[]{ "com.example.reference.EagerLoadReferenceModel", false, false, false, false },
        new Object[]{ "com.example.reference.EagerObservableReadOutsideTransactionReferenceModel",
                      false,
                      false,
                      false,
                      false },
        new Object[]{ "com.example.reference.ExplicitLoadObservableReferenceModel", false, false, false, false },
        new Object[]{ "com.example.reference.ExplicitLoadReferenceModel", false, false, false, false },
        new Object[]{ "com.example.reference.LazyLoadObservableReferenceModel", false, false, false, false },
        new Object[]{ "com.example.reference.LazyLoadReferenceModel", false, false, false, false },
        new Object[]{ "com.example.reference.LazyObservableReadOutsideTransactionReferenceModel",
                      false,
                      false,
                      false,
                      false },
        new Object[]{ "com.example.reference.NonJavabeanNameReferenceModel", false, false, false, false },
        new Object[]{ "com.example.reference.NonnullLazyLoadReferenceModel", false, false, false, false },
        new Object[]{ "com.example.reference.NonObservableReferenceModel", false, false, false, false },
        new Object[]{ "com.example.reference.NullableLazyLoadReferenceModel", false, false, false, false },
        new Object[]{ "com.example.reference.ObservableReferenceModel", false, false, false, false },
        new Object[]{ "com.example.repository.DaggerDisabledRepository", false, false, true, false },
        new Object[]{ "com.example.repository.DaggerEnabledRepository", false, false, true, true },
        new Object[]{ "com.example.repository.InjectEnabledRepository", false, false, true, true },
        new Object[]{ "com.example.repository.InjectDisabledRepository", false, false, false, false },
        new Object[]{ "com.example.repository.RepositoryWithAttachOnly", false, false, true, true },
        new Object[]{ "com.example.repository.RepositoryWithCreateOnly", false, false, true, true },
        new Object[]{ "com.example.repository.RepositoryWithCreateOrAttach", false, false, true, true },
        new Object[]{ "com.example.repository.RepositoryPreDisposeHook", false, false, true, true },
        new Object[]{ "com.example.repository.RepositoryWithDestroyAndDetach", false, false, true, true },
        new Object[]{ "com.example.repository.RepositoryWithDetachNone", false, false, true, true },
        new Object[]{ "com.example.repository.RepositoryWithDetachOnly", false, false, true, true },
        new Object[]{ "com.example.repository.RepositoryWithExplicitId", false, false, true, true },
        new Object[]{ "com.example.repository.RepositoryWithExplicitNonStandardId", false, false, true, true },
        new Object[]{ "com.example.repository.RepositoryWithImplicitId", false, false, true, true },
        new Object[]{ "com.example.repository.RepositoryWithInitializerModel", false, false, true, true },
        new Object[]{ "com.example.repository.RepositoryWithInitializerNameCollisionModel", false, false, true, true },
        new Object[]{ "com.example.repository.RepositoryWithMultipleCtors", false, false, true, true },
        new Object[]{ "com.example.repository.RepositoryWithMultipleInitializersModel", false, false, true, true },
        new Object[]{ "com.example.repository.RepositoryWithProtectedConstructor", false, false, true, true },
        new Object[]{ "com.example.repository.RepositoryWithRawType", false, false, true, true },
        new Object[]{ "com.example.repository.RepositoryWithSingleton", false, false, true, true },
        new Object[]{ "com.example.reserved_names.NonReservedNameModel", false, false, false, false },
        new Object[]{ "com.example.to_string.NoToStringPresent", false, false, false, false },
        new Object[]{ "com.example.to_string.ToStringPresent", false, false, false, false },
        new Object[]{ "com.example.type_access_levels.ReduceAccessLevelModel", false, false, false, false },
        new Object[]{ "com.example.verifiable.DisableVerifyModel", false, false, false, false },
        new Object[]{ "com.example.verifiable.EnableVerifyModel", false, false, false, false },
        new Object[]{ "DisposingModel", false, false, false, false },
        new Object[]{ "ObservableTypeParametersModel", false, false, false, false },
        new Object[]{ "TypeParametersOnModel", false, false, false, false },
        new Object[]{ "ObservableGuessingModel", false, false, false, false },
        new Object[]{ "AnnotationsOnModel", false, false, false, false },
        new Object[]{ "ObservableWithAnnotatedCtorModel", false, false, false, false },
        new Object[]{ "ObservableModelWithUnconventionalNames", false, false, false, false },
        new Object[]{ "DifferentObservableTypesModel", false, false, false, false },
        new Object[]{ "ObservableWithExceptingCtorModel", false, false, false, false },
        new Object[]{ "OverrideNamesInModel", false, false, false, false },
        new Object[]{ "ImplicitSingletonModel", true, false, false, false },
        new Object[]{ "SingletonModel", false, false, false, false },
        new Object[]{ "SingletonWithIdModel", true, false, false, false },
        new Object[]{ "EmptyModel", false, false, false, false },
        new Object[]{ "BasicModelWithDifferentAccessLevels", false, false, false, false },
        new Object[]{ "ObservableWithCtorModel", false, false, false, false },
        new Object[]{ "ObservableWithSpecificExceptionModel", false, false, false, false },
        new Object[]{ "ObservableWithExceptionModel", false, false, false, false },
        new Object[]{ "BasicObservableModel", false, false, false, false }
      };
  }

  @Test( dataProvider = "successfulCompiles" )
  public void processSuccessfulCompile( @Nonnull final String classname,
                                        final boolean componentDaggerEnabled,
                                        final boolean daggerComponentExtensionExpected,
                                        final boolean repositoryEnabled,
                                        final boolean repositoryDaggerEnabled )
    throws Exception
  {
    assertSuccessfulCompile( classname,
                             componentDaggerEnabled,
                             daggerComponentExtensionExpected,
                             repositoryEnabled,
                             repositoryDaggerEnabled );
  }

  @Test
  public void protectedAccessComponentRef()
  {
    final String filename =
      toFilename( "input", "com.example.component_ref.ProtectedAccessComponentRefModel" );
    final String messageFragment =
      "@ComponentRef target should not be protected. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Arez:ProtectedRefMethod\" ) or @SuppressArezWarnings( \"Arez:ProtectedRefMethod\" )";
    assert_().about( JavaSourcesSubjectFactory.javaSources() ).
      that( Collections.singletonList( fixture( filename ) ) ).
      withCompilerOptions( "-Xlint:all,-processing", "-implicit:none", "-Aarez.defer.errors=false" ).
      processedWith( new ArezProcessor() ).
      compilesWithoutError().
      withWarningCount( 1 ).
      withWarningContaining( messageFragment );
  }

  @Test
  public void validProtectedAccessComponentRef()
    throws Exception
  {
    final String input1 =
      toFilename( "input", "com.example.component_ref.ProtectedAccessFromBaseComponentRefModel" );
    final String input2 =
      toFilename( "input", "com.example.component_ref.other.BaseProtectedAccessComponentRefModel" );
    final String output =
      toFilename( "expected",
                  "com.example.component_ref.Arez_ProtectedAccessFromBaseComponentRefModel" );
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void validPublicAccessViaInterfaceComponentRef()
    throws Exception
  {
    final String input1 =
      toFilename( "input", "com.example.component_ref.PublicAccessViaInterfaceComponentRefModel" );
    final String input2 =
      toFilename( "input", "com.example.component_ref.ComponentRefInterface" );
    final String output =
      toFilename( "expected", "com.example.component_ref.Arez_PublicAccessViaInterfaceComponentRefModel" );
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void protectedAccessComponentNameRef()
  {
    final String filename =
      toFilename( "input", "com.example.component_name_ref.ProtectedAccessComponentNameRefModel" );
    final String messageFragment =
      "@ComponentNameRef target should not be protected. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Arez:ProtectedRefMethod\" ) or @SuppressArezWarnings( \"Arez:ProtectedRefMethod\" )";
    assert_().about( JavaSourcesSubjectFactory.javaSources() ).
      that( Collections.singletonList( fixture( filename ) ) ).
      withCompilerOptions( "-Xlint:all,-processing", "-implicit:none", "-Aarez.defer.errors=false" ).
      processedWith( new ArezProcessor() ).
      compilesWithoutError().
      withWarningCount( 1 ).
      withWarningContaining( messageFragment );
  }

  @Test
  public void validProtectedAccessComponentNameRef()
    throws Exception
  {
    final String input1 =
      toFilename( "input", "com.example.component_name_ref.ProtectedAccessFromBaseComponentNameRefModel" );
    final String input2 =
      toFilename( "input", "com.example.component_name_ref.other.BaseProtectedAccessComponentNameRefModel" );
    final String output =
      toFilename( "expected",
                  "com.example.component_name_ref.Arez_ProtectedAccessFromBaseComponentNameRefModel" );
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void validPublicAccessViaInterfaceComponentNameRef()
    throws Exception
  {
    final String input1 =
      toFilename( "input", "com.example.component_name_ref.PublicAccessViaInterfaceComponentNameRefModel" );
    final String input2 =
      toFilename( "input", "com.example.component_name_ref.ComponentNameRefInterface" );
    final String output =
      toFilename( "expected", "com.example.component_name_ref.Arez_PublicAccessViaInterfaceComponentNameRefModel" );
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void protectedAccessComponentStateRef()
  {
    final String filename =
      toFilename( "input", "com.example.component_state_ref.ProtectedAccessComponentStateRefModel" );
    final String messageFragment =
      "@ComponentStateRef target should not be protected. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Arez:ProtectedRefMethod\" ) or @SuppressArezWarnings( \"Arez:ProtectedRefMethod\" )";
    assert_().about( JavaSourcesSubjectFactory.javaSources() ).
      that( Collections.singletonList( fixture( filename ) ) ).
      withCompilerOptions( "-Xlint:all,-processing", "-implicit:none", "-Aarez.defer.errors=false" ).
      processedWith( new ArezProcessor() ).
      compilesWithoutError().
      withWarningCount( 1 ).
      withWarningContaining( messageFragment );
  }

  @Test
  public void validProtectedAccessComponentStateRef()
    throws Exception
  {
    final String input1 =
      toFilename( "input", "com.example.component_state_ref.ProtectedAccessFromBaseComponentStateRefModel" );
    final String input2 =
      toFilename( "input", "com.example.component_state_ref.other.BaseProtectedAccessComponentStateRefModel" );
    final String output =
      toFilename( "expected",
                  "com.example.component_state_ref.Arez_ProtectedAccessFromBaseComponentStateRefModel" );
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void validPublicAccessViaInterfaceComponentStateRef()
    throws Exception
  {
    final String input1 =
      toFilename( "input", "com.example.component_state_ref.PublicAccessViaInterfaceComponentStateRefModel" );
    final String input2 =
      toFilename( "input", "com.example.component_state_ref.ComponentStateRefInterface" );
    final String output =
      toFilename( "expected", "com.example.component_state_ref.Arez_PublicAccessViaInterfaceComponentStateRefModel" );
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void protectedAccessComponentTypeNameRef()
  {
    final String filename =
      toFilename( "input", "com.example.component_type_name_ref.ProtectedAccessComponentTypeNameRefModel" );
    final String messageFragment =
      "@ComponentTypeNameRef target should not be protected. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Arez:ProtectedRefMethod\" ) or @SuppressArezWarnings( \"Arez:ProtectedRefMethod\" )";
    assert_().about( JavaSourcesSubjectFactory.javaSources() ).
      that( Collections.singletonList( fixture( filename ) ) ).
      withCompilerOptions( "-Xlint:all,-processing", "-implicit:none", "-Aarez.defer.errors=false" ).
      processedWith( new ArezProcessor() ).
      compilesWithoutError().
      withWarningCount( 1 ).
      withWarningContaining( messageFragment );
  }

  @Test
  public void validProtectedAccessComponentTypeNameRef()
    throws Exception
  {
    final String input1 =
      toFilename( "input", "com.example.component_type_name_ref.ProtectedAccessFromBaseComponentTypeNameRefModel" );
    final String input2 =
      toFilename( "input", "com.example.component_type_name_ref.other.BaseProtectedAccessComponentTypeNameRefModel" );
    final String output =
      toFilename( "expected",
                  "com.example.component_type_name_ref.Arez_ProtectedAccessFromBaseComponentTypeNameRefModel" );
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void validPublicAccessViaInterfaceComponentTypeNameRef()
    throws Exception
  {
    final String input1 =
      toFilename( "input", "com.example.component_type_name_ref.PublicAccessViaInterfaceComponentTypeNameRefModel" );
    final String input2 =
      toFilename( "input", "com.example.component_type_name_ref.ComponentTypeNameRefInterface" );
    final String output =
      toFilename( "expected",
                  "com.example.component_type_name_ref.Arez_PublicAccessViaInterfaceComponentTypeNameRefModel" );
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void protectedAccessComputableValueRef()
  {
    final String filename =
      toFilename( "input", "com.example.computable_value_ref.ProtectedAccessComputableValueRefModel" );
    final String messageFragment =
      "@ComputableValueRef target should not be protected. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Arez:ProtectedRefMethod\" ) or @SuppressArezWarnings( \"Arez:ProtectedRefMethod\" )";
    assert_().about( JavaSourcesSubjectFactory.javaSources() ).
      that( Collections.singletonList( fixture( filename ) ) ).
      withCompilerOptions( "-Xlint:all,-processing", "-implicit:none", "-Aarez.defer.errors=false" ).
      processedWith( new ArezProcessor() ).
      compilesWithoutError().
      withWarningCount( 1 ).
      withWarningContaining( messageFragment );
  }

  @Test
  public void validProtectedAccessComputableValueRef()
    throws Exception
  {
    final String input1 =
      toFilename( "input", "com.example.computable_value_ref.ProtectedAccessFromBaseComputableValueRefModel" );
    final String input2 =
      toFilename( "input", "com.example.computable_value_ref.other.BaseProtectedAccessComputableValueRefModel" );
    final String output =
      toFilename( "expected",
                  "com.example.computable_value_ref.Arez_ProtectedAccessFromBaseComputableValueRefModel" );
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void validPublicAccessViaInterfaceComputableValueRef()
    throws Exception
  {
    final String input1 =
      toFilename( "input", "com.example.computable_value_ref.PublicAccessViaInterfaceComputableValueRefModel" );
    final String input2 =
      toFilename( "input", "com.example.computable_value_ref.ComputableValueRefInterface" );
    final String output =
      toFilename( "expected", "com.example.computable_value_ref.Arez_PublicAccessViaInterfaceComputableValueRefModel" );
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void protectedAccessContextRef()
  {
    final String filename =
      toFilename( "input", "com.example.context_ref.ProtectedAccessContextRefModel" );
    final String messageFragment =
      "@ContextRef target should not be protected. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Arez:ProtectedRefMethod\" ) or @SuppressArezWarnings( \"Arez:ProtectedRefMethod\" )";
    assert_().about( JavaSourcesSubjectFactory.javaSources() ).
      that( Collections.singletonList( fixture( filename ) ) ).
      withCompilerOptions( "-Xlint:all,-processing", "-implicit:none", "-Aarez.defer.errors=false" ).
      processedWith( new ArezProcessor() ).
      compilesWithoutError().
      withWarningCount( 1 ).
      withWarningContaining( messageFragment );
  }

  @Test
  public void validProtectedAccessContextRef()
    throws Exception
  {
    final String input1 =
      toFilename( "input", "com.example.context_ref.ProtectedAccessFromBaseContextRefModel" );
    final String input2 =
      toFilename( "input", "com.example.context_ref.other.BaseProtectedAccessContextRefModel" );
    final String output =
      toFilename( "expected",
                  "com.example.context_ref.Arez_ProtectedAccessFromBaseContextRefModel" );
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void validPublicAccessViaInterfaceContextRef()
    throws Exception
  {
    final String input1 =
      toFilename( "input", "com.example.context_ref.PublicAccessViaInterfaceContextRefModel" );
    final String input2 =
      toFilename( "input", "com.example.context_ref.ContextRefInterface" );
    final String output =
      toFilename( "expected", "com.example.context_ref.Arez_PublicAccessViaInterfaceContextRefModel" );
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void protectedAccessObservableValueRef()
  {
    final String filename =
      toFilename( "input", "com.example.observable_value_ref.ProtectedAccessObservableValueRefModel" );
    final String messageFragment =
      "@ObservableValueRef target should not be protected. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Arez:ProtectedRefMethod\" ) or @SuppressArezWarnings( \"Arez:ProtectedRefMethod\" )";
    assert_().about( JavaSourcesSubjectFactory.javaSources() ).
      that( Collections.singletonList( fixture( filename ) ) ).
      withCompilerOptions( "-Xlint:all,-processing", "-implicit:none", "-Aarez.defer.errors=false" ).
      processedWith( new ArezProcessor() ).
      compilesWithoutError().
      withWarningCount( 1 ).
      withWarningContaining( messageFragment );
  }

  @Test
  public void validProtectedAccessObservableValueRef()
    throws Exception
  {
    final String input1 =
      toFilename( "input", "com.example.observable_value_ref.ProtectedAccessFromBaseObservableValueRefModel" );
    final String input2 =
      toFilename( "input", "com.example.observable_value_ref.other.BaseProtectedAccessObservableValueRefModel" );
    final String output =
      toFilename( "expected",
                  "com.example.observable_value_ref.Arez_ProtectedAccessFromBaseObservableValueRefModel" );
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void validPublicAccessViaInterfaceObservableValueRef()
    throws Exception
  {
    final String input1 =
      toFilename( "input", "com.example.observable_value_ref.PublicAccessViaInterfaceObservableValueRefModel" );
    final String input2 =
      toFilename( "input", "com.example.observable_value_ref.ObservableValueRefInterface" );
    final String output =
      toFilename( "expected", "com.example.observable_value_ref.Arez_PublicAccessViaInterfaceObservableValueRefModel" );
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void protectedAccessObserverRef()
  {
    final String filename =
      toFilename( "input", "com.example.observer_ref.ProtectedAccessObserverRefModel" );
    final String messageFragment =
      "@ObserverRef target should not be protected. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Arez:ProtectedRefMethod\" ) or @SuppressArezWarnings( \"Arez:ProtectedRefMethod\" )";
    assert_().about( JavaSourcesSubjectFactory.javaSources() ).
      that( Collections.singletonList( fixture( filename ) ) ).
      withCompilerOptions( "-Xlint:all,-processing", "-implicit:none", "-Aarez.defer.errors=false" ).
      processedWith( new ArezProcessor() ).
      compilesWithoutError().
      withWarningCount( 1 ).
      withWarningContaining( messageFragment );
  }

  @Test
  public void validProtectedAccessObserverRef()
    throws Exception
  {
    final String input1 =
      toFilename( "input", "com.example.observer_ref.ProtectedAccessFromBaseObserverRefModel" );
    final String input2 =
      toFilename( "input", "com.example.observer_ref.other.BaseProtectedAccessObserverRefModel" );
    final String output =
      toFilename( "expected",
                  "com.example.observer_ref.Arez_ProtectedAccessFromBaseObserverRefModel" );
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void validPublicAccessViaInterfaceObserverRef()
    throws Exception
  {
    final String input1 =
      toFilename( "input", "com.example.observer_ref.PublicAccessViaInterfaceObserverRefModel" );
    final String input2 =
      toFilename( "input", "com.example.observer_ref.ObserverRefInterface" );
    final String output =
      toFilename( "expected", "com.example.observer_ref.Arez_PublicAccessViaInterfaceObserverRefModel" );
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void protectedAccessPostConstruct()
  {
    final String filename =
      toFilename( "input", "com.example.post_construct.ProtectedAccessPostConstructModel" );
    final String messageFragment =
      "@PostConstruct target should not be protected. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Arez:ProtectedLifecycleMethod\" ) or @SuppressArezWarnings( \"Arez:ProtectedLifecycleMethod\" )";
    assert_().about( JavaSourcesSubjectFactory.javaSources() ).
      that( Collections.singletonList( fixture( filename ) ) ).
      withCompilerOptions( "-Xlint:all,-processing", "-implicit:none", "-Aarez.defer.errors=false" ).
      processedWith( new ArezProcessor() ).
      compilesWithoutError().
      withWarningCount( 1 ).
      withWarningContaining( messageFragment );
  }

  @Test
  public void publicAccessPostConstruct()
  {
    final String filename =
      toFilename( "input", "com.example.post_construct.PublicAccessPostConstructModel" );
    final String messageFragment =
      "@PostConstruct target should not be public. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Arez:PublicLifecycleMethod\" ) or @SuppressArezWarnings( \"Arez:PublicLifecycleMethod\" )";
    assert_().about( JavaSourcesSubjectFactory.javaSources() ).
      that( Collections.singletonList( fixture( filename ) ) ).
      withCompilerOptions( "-Xlint:all,-processing", "-implicit:none", "-Aarez.defer.errors=false" ).
      processedWith( new ArezProcessor() ).
      compilesWithoutError().
      withWarningCount( 1 ).
      withWarningContaining( messageFragment );
  }

  @Test
  public void validProtectedAccessPostConstruct()
    throws Exception
  {
    final String input1 =
      toFilename( "input", "com.example.post_construct.ProtectedAccessFromBasePostConstructModel" );
    final String input2 =
      toFilename( "input", "com.example.post_construct.other.BaseProtectedAccessPostConstructModel" );
    final String output =
      toFilename( "expected",
                  "com.example.post_construct.Arez_ProtectedAccessFromBasePostConstructModel" );
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
  }

  @Test
  public void validPublicAccessViaInterfacePostConstruct()
    throws Exception
  {
    final String input1 =
      toFilename( "input", "com.example.post_construct.PublicAccessViaInterfacePostConstructModel" );
    final String input2 =
      toFilename( "input", "com.example.post_construct.PostConstructInterface" );
    final String output =
      toFilename( "expected", "com.example.post_construct.Arez_PublicAccessViaInterfacePostConstructModel" );
    assertSuccessfulCompile( Arrays.asList( fixture( input1 ), fixture( input2 ) ),
                             Collections.singletonList( output ) );
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
  public void processSuccessfulRepositoryIncludingMultipleExtensions()
    throws Exception
  {
    final JavaFileObject source1 = fixture( "input/com/example/repository/MultiExtensionRepositoryExample.java" );
    final JavaFileObject source2 = fixture( "input/com/example/repository/Extension1.java" );
    final JavaFileObject source3 = fixture( "input/com/example/repository/Extension2.java" );
    final String output1 = "expected/com/example/repository/Arez_MultiExtensionRepositoryExample.java";
    final String output2 = "expected/com/example/repository/Arez_MultiExtensionRepositoryExampleRepository.java";
    final String output3 = "expected/com/example/repository/MultiExtensionRepositoryExampleRepository.java";
    final String output4 = "expected/com/example/repository/MultiExtensionRepositoryExampleRepositoryDaggerModule.java";
    assertSuccessfulCompile( Arrays.asList( source1, source2, source3 ),
                             Arrays.asList( output1, output2, output3, output4 ) );
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
  public void processSuccessfulBaseInterfaceInDifferentPackage()
    throws Exception
  {
    final JavaFileObject source1 =
      fixture( "input/com/example/inheritance/CompleteInterfaceModel.java" );
    final JavaFileObject source2 =
      fixture( "input/com/example/inheritance/other/BaseCompleteInterfaceModel.java" );
    final JavaFileObject source3 =
      fixture( "input/com/example/inheritance/other/OtherElement.java" );
    final String output = "expected/com/example/inheritance/Arez_CompleteInterfaceModel.java";
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
        new Object[]{ "com.example.action.BadActionName1Model",
                      "@Action target specified an invalid value 'assert' for the parameter name. The value must not be a java keyword" },
        new Object[]{ "com.example.action.BadActionName2Model",
                      "@Action target specified an invalid value 'ace-' for the parameter name. The value must be a valid java identifier" },
        new Object[]{ "com.example.action.DuplicateActionModel",
                      "Method annotated with @Action specified name ace that duplicates @Action defined by method setField" },
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
        new Object[]{ "com.example.cascade_dispose.ConcreteObservableMethodComponent",
                      "@CascadeDispose target must be abstract if the method is also annotated with the @Observable or @Reference annotation." },
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

        new Object[]{ "com.example.component.ConcreteComponent", "@ArezComponent target must be abstract" },
        new Object[]{ "com.example.component.DeferredButNoObserveModel",
                      "@ArezComponent target has specified the deferSchedule = true annotation parameter but has no methods annotated with @Observe" },
        new Object[]{ "com.example.component.ModelWithAbstractMethod",
                      "@ArezComponent target has an abstract method not implemented by framework. The method is named someMethod" },
        new Object[]{ "com.example.component.NonEmptyComponent",
                      "@ArezComponent target has specified allowEmpty = true but has methods annotated with @Action, @CascadeDispose, @Memoize, @Observable, @Inverse, @Reference, @ComponentDependency or @Observe" },
        new Object[]{ "com.example.component.BadTypeComponent",
                      "@ArezComponent target specified an invalid type ''. The type must be a valid java identifier." },
        new Object[]{ "com.example.component.BadTypeComponent2",
                      "@ArezComponent target specified an invalid type 'long'. The type must not be a java keyword." },
        new Object[]{ "com.example.component.EmptyComponent",
                      "@ArezComponent target has no methods annotated with @Action, @CascadeDispose, @Memoize, @Observable, @Inverse, @Reference, @ComponentDependency or @Observe" },
        new Object[]{ "com.example.component.EmptyTypeComponent",
                      "@ArezComponent target specified an invalid type ''. The type must be a valid java identifier." },
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
        new Object[]{ "com.example.component_id.ComponentIdNotFinalModel", "@ComponentId target must be final" },
        new Object[]{ "com.example.component_id.ComponentIdNotPrivateModel",
                      "@ComponentId target must not be private" },
        new Object[]{ "com.example.component_id.ComponentIdNotStaticModel", "@ComponentId target must not be static" },

        new Object[]{ "com.example.component_type_name.ComponentTypeNameDuplicateModel",
                      "@ComponentTypeNameRef target duplicates existing method named getTypeName" },
        new Object[]{ "com.example.component_type_name.ComponentTypeNameFinalModel",
                      "@ComponentTypeNameRef target must be abstract" },
        new Object[]{ "com.example.component_type_name.ComponentTypeNameMustNotHaveParametersModel",
                      "@ComponentTypeNameRef target must not have any parameters" },
        new Object[]{ "com.example.component_type_name.ComponentTypeNameMustReturnValueModel",
                      "@ComponentTypeNameRef target must return a value" },
        new Object[]{ "com.example.component_type_name.ComponentTypeNamePrivateModel",
                      "@ComponentTypeNameRef target must be abstract" },
        new Object[]{ "com.example.component_type_name.ComponentTypeNameReturnNonStringModel",
                      "@ComponentTypeNameRef target must return an instance of java.lang.String" },
        new Object[]{ "com.example.component_type_name.ComponentTypeNameStaticModel",
                      "@ComponentTypeNameRef target must be abstract" },
        new Object[]{ "com.example.component_type_name.ComponentTypeNameThrowsExceptionModel",
                      "@ComponentTypeNameRef target must not throw any exceptions" },

        new Object[]{ "com.example.component_id_ref.BadType1Model",
                      "@ComponentIdRef target has a return type java.lang.String but no @ComponentId annotated method. The type is expected to be of type int." },
        new Object[]{ "com.example.component_id_ref.BadType2Model",
                      "@ComponentIdRef target has a return type java.lang.String and a @ComponentId annotated method with a return type java.lang.String. The types must match." },
        new Object[]{ "com.example.component_id_ref.ConcreteModel", "@ComponentIdRef target must be abstract" },
        new Object[]{ "com.example.component_id_ref.DuplicateModel",
                      "@ComponentIdRef target duplicates existing method named getId" },
        new Object[]{ "com.example.component_id_ref.FinalModel", "@ComponentIdRef target must be abstract" },
        new Object[]{ "com.example.component_id_ref.NoReturnModel", "@ComponentIdRef target must return a value" },
        new Object[]{ "com.example.component_id_ref.ParametersModel",
                      "@ComponentIdRef target must not have any parameters" },
        new Object[]{ "com.example.component_id_ref.PrivateModel", "@ComponentIdRef target must be abstract" },
        new Object[]{ "com.example.component_id_ref.StaticModel", "@ComponentIdRef target must be abstract" },
        new Object[]{ "com.example.component_id_ref.ThrowsModel",
                      "@ComponentIdRef target must not throw any exceptions" },

        new Object[]{ "com.example.component_name_ref.ComponentNameRefDuplicateModel",
                      "@ComponentNameRef target duplicates existing method named getTypeName" },
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
        new Object[]{ "com.example.memoize.ThrowsExceptionModel",
                      "@Memoize target must not throw any exceptions" },
        new Object[]{ "com.example.memoize.DuplicateModel",
                      "Method annotated with @Memoize specified name ace that duplicates @Memoize defined by method getX" },
        new Object[]{ "com.example.memoize.FinalModel", "@Memoize target must not be final" },
        new Object[]{ "com.example.memoize.KeepAliveWithParametersModel",
                      "@Memoize target specified parameter keepAlive as true but has parameters." },
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
        new Object[]{ "com.example.component_ref.DuplicateModel",
                      "@ComponentRef target duplicates existing method named getComponent" },
        new Object[]{ "com.example.component_ref.ParametersModel",
                      "@ComponentRef target must not have any parameters" },

        new Object[]{ "com.example.component_state_ref.FinalModel", "@ComponentStateRef target must be abstract" },
        new Object[]{ "com.example.component_state_ref.StaticModel", "@ComponentStateRef target must be abstract" },
        new Object[]{ "com.example.component_state_ref.PrivateModel", "@ComponentStateRef target must be abstract" },
        new Object[]{ "com.example.component_state_ref.VoidModel", "@ComponentStateRef target must return a value" },
        new Object[]{ "com.example.component_state_ref.BadTypeModel",
                      "Method annotated with @ComponentStateRef must return a boolean" },
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
        new Object[]{ "com.example.context_ref.DuplicateModel",
                      "@ContextRef target duplicates existing method named getContext" },
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

        new Object[]{ "com.example.dispose_notifier.NoDisposeNotifierWithRepositoryModel",
                      "@ArezComponent target has specified the disposeNotifier = DISABLE annotation parameter but is also annotated with @Repository that requires disposeNotifier = ENABLE." },

        new Object[]{ "com.example.id.DisableIdAndComponentId",
                      "@ArezComponent target has specified the idRequired = DISABLE annotation parameter but also has annotated a method with @ComponentId that requires idRequired = ENABLE." },
        new Object[]{ "com.example.id.DisableIdAndComponentIdRef",
                      "@ArezComponent target has specified the idRequired = DISABLE annotation parameter but also has annotated a method with @ComponentIdRef that requires idRequired = ENABLE." },
        new Object[]{ "com.example.id.DisableIdAndRepository",
                      "@ArezComponent target has specified the idRequired = DISABLE annotation parameter but is also annotated with @Repository that requires idRequired = ENABLE." },

        new Object[]{ "com.example.inject.MultipleConstructorsModel",
                      "@ArezComponent specified inject parameter but has more than one constructor" },
        new Object[]{ "com.example.inject.DaggerEnableNonPublicModel",
                      "@ArezComponent target is not public but is configured as inject = PROVIDE using the dagger injection framework. Due to constraints within the dagger framework the type needs to made public." },
        new Object[]{ "com.example.inject.DaggerEnableInjectDisabledModel",
                      "@ArezComponent target has a dagger parameter that resolved to ENABLE but the inject parameter is set to NONE and this is not a valid combination of parameters." },
        new Object[]{ "com.example.inject.FieldInjectionModel",
                      "@Inject is not supported on fields in an Arez component. Use constructor injection instead." },
        new Object[]{ "com.example.inject.MethodInjectionModel",
                      "@Inject is not supported on methods in an Arez component. Use constructor injection instead." },
        new Object[]{ "com.example.inject.MultipleConstructorsScopedModel",
                      "@ArezComponent target has specified a scope annotation but has more than one constructor and thus is not a candidate for injection" },
        new Object[]{ "com.example.inject.MultipleScopesModel",
                      "@ArezComponent target has specified multiple scope annotations: [javax.inject.Singleton, com.example.inject.MultipleScopesModel.MyScope]" },
        new Object[]{ "com.example.inject.PerInstanceParamOnProvideModel",
                      "@ArezComponent target has specified at least one @PerInstance parameter on the constructor but has set inject parameter to PROVIDE. The component cannot be provided to other components if the invoker must supply per-instance parameters so either change the inject parameter to CONSUME or remove the @PerInstance parameter." },
        new Object[]{ "com.example.inject.PublicCtorModel",
                      "@ArezComponent target has a public constructor but the inject parameter does not resolve to NONE. Public constructors are not necessary when the instantiation of the component is managed by the injection framework." },
        new Object[]{ "com.example.inject.ScopePresentInjectDisabledModel",
                      "@ArezComponent target is annotated with scope annotation @javax.inject.Singleton but the inject parameter is set to NONE and this is not a valid scenario. Remove the scope annotation or change the inject parameter to a value other than NONE." },

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
        new Object[]{ "com.example.observable_value_ref.BadReturnTypeParameterModel",
                      "@ObservableValueRef target has a type parameter of java.lang.String but @Observable method returns type of long" },
        new Object[]{ "com.example.observable_value_ref.DuplicateRefMethodModel",
                      "Method annotated with @ObservableValueRef defines duplicate ref accessor for observable named time" },
        new Object[]{ "com.example.observable_value_ref.FinalModel", "@ObservableValueRef target must be abstract" },
        new Object[]{ "com.example.observable_value_ref.NonAbstractModel",
                      "@ObservableValueRef target must be abstract" },
        new Object[]{ "com.example.observable_value_ref.NonAlignedNameModel",
                      "Method annotated with @ObservableValueRef should specify name or be named according to the convention get[Name]Observable" },
        new Object[]{ "com.example.observable_value_ref.NoObservableModel",
                      "@ObservableValueRef target unable to be associated with an Observable property" },
        new Object[]{ "com.example.observable_value_ref.ParametersModel",
                      "@ObservableValueRef target must not have any parameters" },
        new Object[]{ "com.example.observable_value_ref.PrivateModel",
                      "@ObservableValueRef target must be abstract" },
        new Object[]{ "com.example.observable_value_ref.StaticModel", "@ObservableValueRef target must be abstract" },
        new Object[]{ "com.example.observable_value_ref.ThrowsExceptionModel",
                      "@ObservableValueRef target must not throw any exceptions" },

        new Object[]{ "com.example.computable_value_ref.BadNameModel",
                      "@ComputableValueRef target specified an invalid name '-ace'. The name must be a valid java identifier." },
        new Object[]{ "com.example.computable_value_ref.BadNameModel2",
                      "@ComputableValueRef target specified an invalid name 'private'. The name must not be a java keyword." },
        new Object[]{ "com.example.computable_value_ref.BadReturnTypeModel",
                      "Method annotated with @ComputableValueRef must return an instance of arez.ComputableValue" },
        new Object[]{ "com.example.computable_value_ref.BadReturnType2Model",
                      "@ComputableValueRef target has a type parameter of ? but @Memoize method returns type of long" },
        new Object[]{ "com.example.computable_value_ref.BadReturnType3Model",
                      "@ComputableValueRef target has a type parameter of java.lang.String but @Memoize method returns type of long" },
        new Object[]{ "com.example.computable_value_ref.DuplicateRefMethodModel",
                      "@ComputableValueRef target duplicates existing method named getTimeComputableValue" },
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
        new Object[]{ "com.example.observer_ref.DuplicateNameModel",
                      "Method annotated with @ObserverRef defines duplicate ref accessor for observer named doStuff" },
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

        new Object[]{ "com.example.name_duplicates.ActionDuplicatesObservableNameModel",
                      "Method annotated with @Action specified name field that duplicates @Observable defined by method getField" },
        new Object[]{ "com.example.on_activate.BadTypeParamComputableValueParamModel",
                      "@OnActivate target has a parameter of type ComputableValue with a type parameter of java.lang.String but the @Memoize method returns a type of long" },
        new Object[]{ "com.example.on_activate.MemoizeHasParametersModel",
                      "@OnActivate target associated with @Memoize method that has parameters." },
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
                      "@OnActivate target must not have any parameters or must have a single parameter of type arez.ComputableValue" },
        new Object[]{ "com.example.on_activate.OnActivateReturnValueModel",
                      "@OnActivate target must not return a value" },
        new Object[]{ "com.example.on_activate.OnActivateThrowsExceptionModel",
                      "@OnActivate target must not throw any exceptions" },
        new Object[]{ "com.example.on_activate.OnActivateDuplicateModel",
                      "@OnActivate target duplicates existing method named foo" },
        new Object[]{ "com.example.on_deactivate.MemoizeHasParametersModel",
                      "@OnDeactivate target associated with @Memoize method that has parameters." },
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
        new Object[]{ "com.example.on_stale.MemoizeHasParametersModel",
                      "@OnStale target associated with @Memoize method that has parameters." },
        new Object[]{ "com.example.on_stale.OnStaleAbstractModel", "@OnStale target must not be abstract" },
        new Object[]{ "com.example.on_stale.OnStaleNoMemoizeModel",
                      "@OnStale exists but there is no corresponding @Memoize" },
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
        new Object[]{ "com.example.name_duplicates.ObservableAndMemoizeMethodModel",
                      "Method can not be annotated with both @Observable and @Memoize" },
        new Object[]{ "com.example.name_duplicates.ObservableAndContainerIdMethodModel",
                      "Method can not be annotated with both @Observable and @ComponentId" },
        new Object[]{ "com.example.name_duplicates.ObservableAndOnActivateMethodModel",
                      "Method can not be annotated with both @Observable and @OnActivate" },
        new Object[]{ "com.example.name_duplicates.ObservableAndOnDeactivateMethodModel",
                      "Method can not be annotated with both @Observable and @OnDeactivate" },
        new Object[]{ "com.example.name_duplicates.ObservableAndOnStaleMethodModel",
                      "Method can not be annotated with both @Observable and @OnStale" },
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
        new Object[]{ "com.example.name_duplicates.ActionAndOnStaleMethodModel",
                      "Method can not be annotated with both @Action and @OnStale" },
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
        new Object[]{ "com.example.name_duplicates.MemoizeAndOnStaleMethodModel",
                      "Method can not be annotated with both @Memoize and @OnStale" },
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
  {
    assertFailedCompile( classname, errorMessageFragment );
  }

  @Test
  public void unmanagedDisposeNotifierReference()
  {
    final String filename =
      toFilename( "input", "com.example.component.UnmanagedDisposeNotifierReference" );
    final String messageFragment =
      "Field named 'time' has a type that is an implementation of DisposeNotifier but is not annotated with @arez.annotations.CascadeDispose or @arez.annotations.ComponentDependency. This scenario can cause Please annotate the field as appropriate or suppress the warning by annotating the field with @SuppressWarnings( \"Arez:UnmanagedComponentReference\" ) or @SuppressArezWarnings( \"Arez:UnmanagedComponentReference\" )";
    assert_().about( JavaSourcesSubjectFactory.javaSources() ).
      that( Collections.singletonList( fixture( filename ) ) ).
      withCompilerOptions( "-Xlint:-processing", "-implicit:class" ).
      processedWith( new ArezProcessor() ).
      compilesWithoutError().
      withWarningCount( 1 ).
      withWarningContaining( messageFragment );
  }

  @Test
  public void unmanagedActAsComponentReference()
  {
    final String filename =
      toFilename( "input", "com.example.component.UnmanagedActAsComponentReference" );
    final String messageFragment =
      "Field named '_myComponent' has a type that is annotated with @ActAsComponent but is not annotated with @arez.annotations.CascadeDispose or @arez.annotations.ComponentDependency. This scenario can cause Please annotate the field as appropriate or suppress the warning by annotating the field with @SuppressWarnings( \"Arez:UnmanagedComponentReference\" ) or @SuppressArezWarnings( \"Arez:UnmanagedComponentReference\" )";
    assert_().about( JavaSourcesSubjectFactory.javaSources() ).
      that( Collections.singletonList( fixture( filename ) ) ).
      withCompilerOptions( "-Xlint:-processing", "-implicit:class" ).
      processedWith( new ArezProcessor() ).
      compilesWithoutError().
      withWarningCount( 1 ).
      withWarningContaining( messageFragment );
  }

  @Test
  public void unmanagedComponentReference()
  {
    final String filename =
      toFilename( "input", "com.example.component.UnmanagedComponentReference" );
    final String messageFragment =
      "Field named '_myComponent' has a type that is an Arez component but is not annotated with @arez.annotations.CascadeDispose or @arez.annotations.ComponentDependency. This scenario can cause Please annotate the field as appropriate or suppress the warning by annotating the field with @SuppressWarnings( \"Arez:UnmanagedComponentReference\" ) or @SuppressArezWarnings( \"Arez:UnmanagedComponentReference\" )";
    assert_().about( JavaSourcesSubjectFactory.javaSources() ).
      that( Collections.singletonList( fixture( filename ) ) ).
      withCompilerOptions( "-Xlint:-processing", "-implicit:class" ).
      processedWith( new ArezProcessor() ).
      compilesWithoutError().
      withWarningCount( 1 ).
      withWarningContaining( messageFragment );
  }

  @Test
  public void unmanagedComponentReferenceViaInheritance()
  {
    final String filename =
      toFilename( "input", "com.example.component.UnmanagedComponentReferenceViaInheritance" );
    final String messageFragment =
      "Field named '_component' has a type that is an Arez component but is not annotated with @arez.annotations.CascadeDispose or @arez.annotations.ComponentDependency. This scenario can cause Please annotate the field as appropriate or suppress the warning by annotating the field with @SuppressWarnings( \"Arez:UnmanagedComponentReference\" ) or @SuppressArezWarnings( \"Arez:UnmanagedComponentReference\" )";
    assert_().about( JavaSourcesSubjectFactory.javaSources() ).
      that( Collections.singletonList( fixture( filename ) ) ).
      withCompilerOptions( "-Xlint:-processing", "-implicit:class" ).
      processedWith( new ArezProcessor() ).
      compilesWithoutError().
      withWarningCount( 1 ).
      withWarningContaining( messageFragment );
  }

  @Test
  public void unmanagedComponentReferenceSuppressed()
  {
    final String filename =
      toFilename( "input", "com.example.component.UnmanagedComponentReferenceSuppressed" );
    assert_().about( JavaSourcesSubjectFactory.javaSources() ).
      that( Collections.singletonList( fixture( filename ) ) ).
      withCompilerOptions( "-Xlint:-processing", "-implicit:class" ).
      processedWith( new ArezProcessor() ).
      compilesWithoutWarnings();
  }

  @Test
  public void unmanagedComponentReferenceSuppressedAtClass()
  {
    final String filename =
      toFilename( "input", "com.example.component.UnmanagedComponentReferenceSuppressedAtClass" );
    assert_().about( JavaSourcesSubjectFactory.javaSources() ).
      that( Collections.singletonList( fixture( filename ) ) ).
      withCompilerOptions( "-Xlint:-processing", "-implicit:class" ).
      processedWith( new ArezProcessor() ).
      compilesWithoutWarnings();
  }

  @Test
  public void unmanagedComponentReferenceToNonDisposeNotifier()
  {
    final String filename =
      toFilename( "input", "com.example.component.UnmanagedComponentReferenceToNonDisposeNotifier" );
    assert_().about( JavaSourcesSubjectFactory.javaSources() ).
      that( Collections.singletonList( fixture( filename ) ) ).
      withCompilerOptions( "-Xlint:-processing", "-implicit:class" ).
      processedWith( new ArezProcessor() ).
      compilesWithoutWarnings();
  }

  @Test
  public void unmanagedComponentReferenceToNonVerify()
  {
    final String filename =
      toFilename( "input", "com.example.component.UnmanagedComponentReferenceToNonVerify" );
    assert_().about( JavaSourcesSubjectFactory.javaSources() ).
      that( Collections.singletonList( fixture( filename ) ) ).
      withCompilerOptions( "-Xlint:-processing", "-implicit:class" ).
      processedWith( new ArezProcessor() ).
      compilesWithoutWarnings();
  }

  @Test
  public void unmanagedComponentReferenceToSingleton()
  {
    final String filename =
      toFilename( "input", "com.example.component.UnmanagedComponentReferenceToSingleton" );
    assert_().about( JavaSourcesSubjectFactory.javaSources() ).
      that( Collections.singletonList( fixture( filename ) ) ).
      withCompilerOptions( "-Xlint:-processing", "-implicit:class" ).
      processedWith( new ArezProcessor() ).
      compilesWithoutWarnings();
  }

  @Test
  public void unmanagedObservableComponentReference()
  {
    final String filename =
      toFilename( "input", "com.example.component.UnmanagedObservableComponentReference" );
    final String messageFragment =
      "Method named 'getMyComponent' has a return type that is an Arez component but is not annotated with @arez.annotations.CascadeDispose or @arez.annotations.ComponentDependency. This scenario can cause errors. Please annotate the method as appropriate or suppress the warning by annotating the method with @SuppressWarnings( \"Arez:UnmanagedComponentReference\" ) or @SuppressArezWarnings( \"Arez:UnmanagedComponentReference\" )";
    assert_().about( JavaSourcesSubjectFactory.javaSources() ).
      that( Collections.singletonList( fixture( filename ) ) ).
      withCompilerOptions( "-Xlint:-processing", "-implicit:class" ).
      processedWith( new ArezProcessor() ).
      compilesWithoutError().
      withWarningCount( 1 ).
      withWarningContaining( messageFragment );
  }

  @Test
  public void unmanagedObservableActAsComponentReference()
  {
    final String filename =
      toFilename( "input", "com.example.component.UnmanagedObservableActAsComponentReference" );
    final String messageFragment =
      "Method named 'getMyComponent' has a return type that is annotated with @ActAsComponent but is not annotated with @arez.annotations.CascadeDispose or @arez.annotations.ComponentDependency. This scenario can cause errors. Please annotate the method as appropriate or suppress the warning by annotating the method with @SuppressWarnings( \"Arez:UnmanagedComponentReference\" ) or @SuppressArezWarnings( \"Arez:UnmanagedComponentReference\" )";
    assert_().about( JavaSourcesSubjectFactory.javaSources() ).
      that( Collections.singletonList( fixture( filename ) ) ).
      withCompilerOptions( "-Xlint:-processing", "-implicit:class" ).
      processedWith( new ArezProcessor() ).
      compilesWithoutError().
      withWarningCount( 1 ).
      withWarningContaining( messageFragment );
  }

  @Test
  public void unmanagedObservableDisposeNotifierReference()
  {
    final String filename =
      toFilename( "input", "com.example.component.UnmanagedObservableDisposeNotifierReference" );
    final String messageFragment =
      "Method named 'getMyComponent' has a return type that is an implementation of DisposeNotifier but is not annotated with @arez.annotations.CascadeDispose or @arez.annotations.ComponentDependency. This scenario can cause errors. Please annotate the method as appropriate or suppress the warning by annotating the method with @SuppressWarnings( \"Arez:UnmanagedComponentReference\" ) or @SuppressArezWarnings( \"Arez:UnmanagedComponentReference\" )";
    assert_().about( JavaSourcesSubjectFactory.javaSources() ).
      that( Collections.singletonList( fixture( filename ) ) ).
      withCompilerOptions( "-Xlint:-processing", "-implicit:class" ).
      processedWith( new ArezProcessor() ).
      compilesWithoutError().
      withWarningCount( 1 ).
      withWarningContaining( messageFragment );
  }

  @Test
  public void unmanagedObservableComponentReferenceViaInheritance()
  {
    final String filename =
      toFilename( "input", "com.example.component.UnmanagedObservableComponentReferenceViaInheritance" );
    final String messageFragment =
      "Method named 'getMyComponent' has a return type that is an Arez component but is not annotated with @arez.annotations.CascadeDispose or @arez.annotations.ComponentDependency. This scenario can cause errors. Please annotate the method as appropriate or suppress the warning by annotating the method with @SuppressWarnings( \"Arez:UnmanagedComponentReference\" ) or @SuppressArezWarnings( \"Arez:UnmanagedComponentReference\" )";
    assert_().about( JavaSourcesSubjectFactory.javaSources() ).
      that( Collections.singletonList( fixture( filename ) ) ).
      withCompilerOptions( "-Xlint:-processing", "-implicit:class" ).
      processedWith( new ArezProcessor() ).
      compilesWithoutError().
      withWarningCount( 1 ).
      withWarningContaining( messageFragment );
  }

  @Test
  public void unmanagedObservableActAsComponentReferenceSuppressed()
  {
    final String filename =
      toFilename( "input", "com.example.component.UnmanagedObservableActAsComponentReferenceSuppressed" );
    assert_().about( JavaSourcesSubjectFactory.javaSources() ).
      that( Collections.singletonList( fixture( filename ) ) ).
      withCompilerOptions( "-Xlint:-processing", "-implicit:class" ).
      processedWith( new ArezProcessor() ).
      compilesWithoutWarnings();
  }

  @Test
  public void unmanagedObservableActAsComponentReferenceSuppressedOnClass()
  {
    final String filename =
      toFilename( "input", "com.example.component.UnmanagedObservableActAsComponentReferenceSuppressedOnClass" );
    assert_().about( JavaSourcesSubjectFactory.javaSources() ).
      that( Collections.singletonList( fixture( filename ) ) ).
      withCompilerOptions( "-Xlint:-processing", "-implicit:class" ).
      processedWith( new ArezProcessor() ).
      compilesWithoutWarnings();
  }

  @Test
  public void unmanagedObservableComponentReferenceViaInheritanceSuppressed()
  {
    final String filename =
      toFilename( "input", "com.example.component.UnmanagedObservableComponentReferenceViaInheritanceSuppressed" );
    assert_().about( JavaSourcesSubjectFactory.javaSources() ).
      that( Collections.singletonList( fixture( filename ) ) ).
      withCompilerOptions( "-Xlint:-processing", "-implicit:class" ).
      processedWith( new ArezProcessor() ).
      compilesWithoutWarnings();
  }

  @Test
  public void unmanagedObservableComponentReferenceViaInheritanceSuppressedOnBaseClass()
  {
    final String filename =
      toFilename( "input",
                  "com.example.component.UnmanagedObservableComponentReferenceViaInheritanceSuppressedOnBaseClass" );
    assert_().about( JavaSourcesSubjectFactory.javaSources() ).
      that( Collections.singletonList( fixture( filename ) ) ).
      withCompilerOptions( "-Xlint:-processing", "-implicit:class" ).
      processedWith( new ArezProcessor() ).
      compilesWithoutWarnings();
  }

  @Test
  public void unmanagedObservableActAsComponentReferenceSuppressedOnSetter()
  {
    final String filename =
      toFilename( "input", "com.example.component.UnmanagedObservableActAsComponentReferenceSuppressedOnSetter" );
    assert_().about( JavaSourcesSubjectFactory.javaSources() ).
      that( Collections.singletonList( fixture( filename ) ) ).
      withCompilerOptions( "-Xlint:-processing", "-implicit:class" ).
      processedWith( new ArezProcessor() ).
      compilesWithoutWarnings();
  }

  @Test
  public void unmanagedObservableComponentReferenceToNonVerify()
  {
    final String filename =
      toFilename( "input", "com.example.component.UnmanagedObservableComponentReferenceToNonVerify" );
    assert_().about( JavaSourcesSubjectFactory.javaSources() ).
      that( Collections.singletonList( fixture( filename ) ) ).
      withCompilerOptions( "-Xlint:-processing", "-implicit:class" ).
      processedWith( new ArezProcessor() ).
      compilesWithoutWarnings();
  }

  @Test
  public void unnecessaryDefaultPriorityPresent()
  {
    final String filename =
      toFilename( "input", "com.example.component.UnnecessaryDefaultPriorityPresentComponent" );
    final String messageFragment =
      "@ArezComponent target should not specify the defaultPriority parameter unless it contains methods annotated with either the @Memoize annotation or the @Observe annotation. This warning can be suppressed by annotating the element with @SuppressWarnings( \"Arez:UnnecessaryDefaultPriority\" ) or @SuppressArezWarnings( \"Arez:UnnecessaryDefaultPriority\" )";
    assert_().about( JavaSourcesSubjectFactory.javaSources() ).
      that( Collections.singletonList( fixture( filename ) ) ).
      withCompilerOptions( "-Xlint:-processing", "-implicit:class" ).
      processedWith( new ArezProcessor() ).
      compilesWithoutError().
      withWarningCount( 1 ).
      withWarningContaining( messageFragment );
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
        new Object[]{ "OnStale", "OnStale" },
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
    final JavaFileObject source1 =
      fixture( "bad_input/com/example/package_access/other/BaseActionModel.java" );
    final JavaFileObject source2 =
      fixture( "bad_input/PackageAccessActionModel.java" );
    assertFailedCompileResource( Arrays.asList( source1, source2 ),
                                 "@Action target must not be package access if " +
                                 "the method is in a different package from the type annotated with the " +
                                 "@ArezComponent annotation" );
  }

  @Test
  public void processFailedCompileInheritedPackageAccessInDifferentPackageObservable_Setter()
  {
    final JavaFileObject source1 =
      fixture( "bad_input/com/example/package_access/other/BaseObservable2Model.java" );
    final JavaFileObject source2 =
      fixture( "bad_input/com/example/package_access/Observable2Model.java" );
    assertFailedCompileResource( Arrays.asList( source1, source2 ),
                                 "@Observable target must not be package access if " +
                                 "the method is in a different package from the type annotated with the " +
                                 "@ArezComponent annotation" );
  }

  @Test
  public void processFailedCompileInheritedPackageAccessInDifferentPackageObservable_Getter()
  {
    final JavaFileObject source1 =
      fixture( "bad_input/com/example/package_access/other/BaseObservable3Model.java" );
    final JavaFileObject source2 =
      fixture( "bad_input/com/example/package_access/Observable3Model.java" );
    assertFailedCompileResource( Arrays.asList( source1, source2 ),
                                 "@Observable target must not be package access if " +
                                 "the method is in a different package from the type annotated with the " +
                                 "@ArezComponent annotation" );
  }
}
