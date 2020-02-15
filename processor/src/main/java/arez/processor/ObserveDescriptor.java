package arez.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeKind;
import org.realityforge.proton.MemberChecks;
import org.realityforge.proton.ProcessorException;

/**
 * The class that represents the parsed state of @Observe methods on a @ArezComponent annotated class.
 */
final class ObserveDescriptor
{
  @Nonnull
  private final ComponentDescriptor _component;
  @Nonnull
  private final String _name;
  private boolean _mutation;
  private Priority _priority;
  private boolean _internalExecutor;
  private boolean _reportParameters;
  private boolean _reportResult;
  private String _depType;
  private boolean _observeLowerPriorityDependencies;
  private boolean _nestedActionsAllowed;
  @Nullable
  private ExecutableElement _method;
  @Nullable
  private ExecutableType _methodType;
  @Nullable
  private ExecutableElement _onDepsChange;
  @Nonnull
  private final List<ExecutableElement> _refMethods = new ArrayList<>();

  ObserveDescriptor( @Nonnull final ComponentDescriptor component, @Nonnull final String name )
  {
    _component = Objects.requireNonNull( component );
    _name = Objects.requireNonNull( name );
  }

  @Nonnull
  ComponentDescriptor getComponent()
  {
    return _component;
  }

  @Nonnull
  String getName()
  {
    return _name;
  }

  void addRefMethod( @Nonnull final ExecutableElement method )
  {
    getRefMethods().add( Objects.requireNonNull( method ) );
  }

  @Nonnull
  List<ExecutableElement> getRefMethods()
  {
    return _refMethods;
  }

  @Nonnull
  ExecutableElement getMethod()
  {
    assert null != _method;
    return _method;
  }

  @Nonnull
  ExecutableType getMethodType()
  {
    assert null != _methodType;
    return _methodType;
  }

  boolean hasObserve()
  {
    return null != _method;
  }

  void setObserveMethod( final boolean mutation,
                         @Nonnull final Priority priority,
                         final boolean internalExecutor,
                         final boolean reportParameters,
                         final boolean reportResult,
                         @Nonnull final String depType,
                         final boolean observeLowerPriorityDependencies,
                         final boolean nestedActionsAllowed,
                         @Nonnull final ExecutableElement method,
                         @Nonnull final ExecutableType trackedMethodType )
  {
    MemberChecks.mustBeWrappable( _component.getElement(),
                                  Constants.COMPONENT_CLASSNAME,
                                  Constants.OBSERVE_CLASSNAME,
                                  method );

    if ( internalExecutor )
    {
      if ( !method.getParameters().isEmpty() )
      {
        throw new ProcessorException( "@Observe target must not have any parameters when executor=INTERNAL",
                                      method );
      }
      if ( !method.getThrownTypes().isEmpty() )
      {
        throw new ProcessorException( "@Observe target must not throw any exceptions when executor=INTERNAL",
                                      method );
      }
      if ( TypeKind.VOID != method.getReturnType().getKind() )
      {
        throw new ProcessorException( "@Observe target must not return a value when executor=INTERNAL", method );
      }
      if ( _component.isClassType() && method.getModifiers().contains( Modifier.PUBLIC ) )
      {
        throw new ProcessorException( "@Observe target must not be public when executor=INTERNAL", method );
      }
      if ( !reportParameters )
      {
        throw new ProcessorException( "@Observe target must not specify reportParameters parameter " +
                                      "when executor=INTERNAL", method );
      }
      if ( !reportResult )
      {
        throw new ProcessorException( "@Observe target must not specify reportResult parameter " +
                                      "when executor=INTERNAL", method );
      }
    }

    if ( hasObserve() )
    {
      throw new ProcessorException( "@Observe target duplicates existing method named " +
                                    getMethod().getSimpleName(), method );
    }
    else
    {
      _mutation = mutation;
      _priority = Objects.requireNonNull( priority );
      _internalExecutor = internalExecutor;
      _reportParameters = reportParameters;
      _reportResult = reportResult;
      _depType = Objects.requireNonNull( depType );
      _observeLowerPriorityDependencies = observeLowerPriorityDependencies;
      _nestedActionsAllowed = nestedActionsAllowed;
      _method = Objects.requireNonNull( method );
      _methodType = Objects.requireNonNull( trackedMethodType );
    }
  }

  boolean isInternalExecutor()
  {
    return _internalExecutor;
  }

  @Nonnull
  ExecutableElement getOnDepsChange()
  {
    assert null != _onDepsChange;
    return _onDepsChange;
  }

  boolean hasOnDepsChange()
  {
    return null != _onDepsChange;
  }

  void setOnDepsChange( @Nonnull final ExecutableElement method )
  {
    if ( null != _onDepsChange )
    {
      throw new ProcessorException( "@OnDepsChange target duplicates existing method named " +
                                    _onDepsChange.getSimpleName(), method );

    }
    else
    {
      _onDepsChange = Objects.requireNonNull( method );
    }
  }

  @Nonnull
  String getFieldName()
  {
    return ComponentGenerator.FIELD_PREFIX + getName();
  }

  void validate()
  {
    if ( isInternalExecutor() &&
         hasOnDepsChange() &&
         getRefMethods().isEmpty() &&
         _onDepsChange.getParameters().isEmpty() )
    {
      assert hasObserve();
      throw new ProcessorException( "@Observe target with parameter executor=INTERNAL defined an @OnDepsChange " +
                                    "method but has not defined an @ObserverRef method nor does the " +
                                    "@OnDepsChange annotated method have an arez.Observer parameter. This results " +
                                    "in an impossible to schedule observer.", getMethod() );
    }
    if ( !isInternalExecutor() && !hasOnDepsChange() )
    {
      assert hasObserve();
      throw new ProcessorException( "@Observe target defined parameter executor=EXTERNAL but does not " +
                                    "specify an @OnDepsChange method.", getMethod() );
    }
    if ( "AREZ_OR_EXTERNAL".equals( getDepType() ) && getRefMethods().isEmpty() )
    {
      assert hasObserve();
      throw new ProcessorException( "@Observe target with parameter depType=AREZ_OR_EXTERNAL has not " +
                                    "defined an @ObserverRef method and thus can not invoke reportStale().",
                                    getMethod() );
    }
  }

  boolean isMutation()
  {
    return _mutation;
  }

  Priority getPriority()
  {
    return _priority;
  }

  boolean isReportParameters()
  {
    return _reportParameters;
  }

  boolean isReportResult()
  {
    return _reportResult;
  }

  String getDepType()
  {
    return _depType;
  }

  boolean isObserveLowerPriorityDependencies()
  {
    return _observeLowerPriorityDependencies;
  }

  boolean isNestedActionsAllowed()
  {
    return _nestedActionsAllowed;
  }
}
