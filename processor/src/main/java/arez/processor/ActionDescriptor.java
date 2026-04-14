package arez.processor;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.ExecutableType;

/**
 * The class that represents the parsed state of @Action methods on a @ArezComponent annotated class.
 */
final class ActionDescriptor
{
  @Nonnull
  private final ComponentDescriptor _component;
  @Nonnull
  private final String _name;
  private final boolean _requireNewTransaction;
  private final boolean _mutation;
  private final boolean _verifyRequired;
  private final boolean _reportParameters;
  private final boolean _reportResult;
  private final boolean _skipIfDisposed;
  @Nonnull
  private final ExecutableElement _action;
  @Nonnull
  private final ExecutableType _actionType;

  ActionDescriptor( @Nonnull final ComponentDescriptor component,
                    @Nonnull final String name,
                    final boolean requireNewTransaction,
                    final boolean mutation,
                    final boolean verifyRequired,
                    final boolean reportParameters,
                    final boolean reportResult,
                    final boolean skipIfDisposed,
                    @Nonnull final ExecutableElement action,
                    @Nonnull final ExecutableType actionType )
  {
    _component = Objects.requireNonNull( component );
    _name = Objects.requireNonNull( name );
    _requireNewTransaction = requireNewTransaction;
    _mutation = mutation;
    _verifyRequired = verifyRequired;
    _reportParameters = reportParameters;
    _reportResult = reportResult;
    _skipIfDisposed = skipIfDisposed;
    _action = Objects.requireNonNull( action );
    _actionType = Objects.requireNonNull( actionType );
  }

  @Nonnull
  String getName()
  {
    return _name;
  }

  @Nonnull
  ExecutableElement getAction()
  {
    return _action;
  }

  @Nonnull
  ComponentDescriptor getComponent()
  {
    return _component;
  }

  boolean isRequireNewTransaction()
  {
    return _requireNewTransaction;
  }

  boolean isMutation()
  {
    return _mutation;
  }

  boolean isVerifyRequired()
  {
    return _verifyRequired;
  }

  boolean isReportParameters()
  {
    return _reportParameters;
  }

  boolean isReportResult()
  {
    return _reportResult;
  }

  boolean isSkipIfDisposed()
  {
    return _skipIfDisposed;
  }

  @Nonnull
  ExecutableType getActionType()
  {
    return _actionType;
  }
}
