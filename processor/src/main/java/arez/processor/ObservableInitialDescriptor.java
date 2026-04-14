package arez.processor;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;

/**
 * The class that represents the parsed state of @ObservableInitial annotated elements.
 */
final class ObservableInitialDescriptor
{
  @Nonnull
  private final String _name;
  @Nonnull
  private final Element _element;
  @Nullable
  private final VariableElement _field;
  @Nullable
  private final ExecutableElement _method;
  @Nullable
  private final ExecutableType _methodType;

  ObservableInitialDescriptor( @Nonnull final String name, @Nonnull final VariableElement field )
  {
    _name = Objects.requireNonNull( name );
    _element = Objects.requireNonNull( field );
    _field = field;
    _method = null;
    _methodType = null;
  }

  ObservableInitialDescriptor( @Nonnull final String name,
                               @Nonnull final ExecutableElement method,
                               @Nonnull final ExecutableType methodType )
  {
    _name = Objects.requireNonNull( name );
    _element = Objects.requireNonNull( method );
    _field = null;
    _method = method;
    _methodType = Objects.requireNonNull( methodType );
  }

  @Nonnull
  String getName()
  {
    return _name;
  }

  boolean isField()
  {
    return null != _field;
  }

  @Nonnull
  VariableElement getField()
  {
    assert null != _field;
    return _field;
  }

  boolean isMethod()
  {
    return null != _method;
  }

  @Nonnull
  ExecutableElement getMethod()
  {
    assert null != _method;
    return _method;
  }

  @Nonnull
  TypeMirror getType()
  {
    return isField() ? getField().asType() : getMethodType().getReturnType();
  }

  @Nonnull
  ExecutableType getMethodType()
  {
    assert null != _methodType;
    return _methodType;
  }

  @Nonnull
  Element getElement()
  {
    return _element;
  }
}
