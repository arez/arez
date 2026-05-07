package arez.processor;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.ExecutableType;

/**
 * The class that represents the parsed state of @RequiresTransaction methods on a
 * @ArezComponent annotated class.
 */
final class RequiresTransactionDescriptor
{
  @Nonnull
  private final ComponentDescriptor _component;
  @Nonnull
  private final String _mode;
  @Nonnull
  private final String _tracking;
  @Nonnull
  private final ExecutableElement _method;
  @Nonnull
  private final ExecutableType _methodType;

  RequiresTransactionDescriptor( @Nonnull final ComponentDescriptor component,
                                 @Nonnull final String mode,
                                 @Nonnull final String tracking,
                                 @Nonnull final ExecutableElement method,
                                 @Nonnull final ExecutableType methodType )
  {
    _component = Objects.requireNonNull( component );
    _mode = Objects.requireNonNull( mode );
    _tracking = Objects.requireNonNull( tracking );
    _method = Objects.requireNonNull( method );
    _methodType = Objects.requireNonNull( methodType );
  }

  @Nonnull
  ComponentDescriptor getComponent()
  {
    return _component;
  }

  @Nonnull
  String getMode()
  {
    return _mode;
  }

  boolean isAnyMode()
  {
    return "ANY".equals( _mode );
  }

  boolean isReadOnlyMode()
  {
    return "READ_ONLY".equals( _mode );
  }

  @Nonnull
  String getTracking()
  {
    return _tracking;
  }

  boolean isAnyTracking()
  {
    return "ANY".equals( _tracking );
  }

  boolean isTrackingTransactionRequired()
  {
    return "TRACKING".equals( _tracking );
  }

  @Nonnull
  ExecutableElement getMethod()
  {
    return _method;
  }

  @Nonnull
  ExecutableType getMethodType()
  {
    return _methodType;
  }
}
