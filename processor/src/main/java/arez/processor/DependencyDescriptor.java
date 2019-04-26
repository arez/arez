package arez.processor;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;

/**
 * Declaration of a component's dependency.
 * This dependency can be an <code>@Observable</code> method, a un-annotated getter method or a field.
 */
final class DependencyDescriptor
{
  @Nonnull
  private final ComponentDescriptor _componentDescriptor;
  @Nullable
  private final ExecutableElement _method;
  @Nullable
  private final VariableElement _field;
  private final boolean _cascade;
  @Nullable
  private ObservableDescriptor _observable;

  DependencyDescriptor( @Nonnull final ComponentDescriptor componentDescriptor,
                        @Nonnull final ExecutableElement method,
                        final boolean cascade )
  {
    _componentDescriptor = Objects.requireNonNull( componentDescriptor );
    _method = Objects.requireNonNull( method );
    _field = null;
    _cascade = cascade;
  }

  DependencyDescriptor( @Nonnull final ComponentDescriptor componentDescriptor, @Nonnull final VariableElement field )
  {
    _componentDescriptor = Objects.requireNonNull( componentDescriptor );
    _method = null;
    _field = Objects.requireNonNull( field );
    _cascade = true;
  }

  boolean shouldCascadeDispose()
  {
    return _cascade;
  }

  boolean isMethodDependency()
  {
    return null != _method;
  }

  @Nonnull
  Element getElement()
  {
    return null != _method ? _method : Objects.requireNonNull( _field );
  }

  @Nonnull
  ExecutableElement getMethod()
  {
    assert null != _method;
    return _method;
  }

  @Nonnull
  VariableElement getField()
  {
    assert null != _field;
    return _field;
  }

  void setObservable( @Nonnull final ObservableDescriptor observable )
  {
    _observable = Objects.requireNonNull( observable );
    _observable.setDependencyDescriptor( this );
  }

  @Nonnull
  ObservableDescriptor getObservable()
  {
    assert null != _observable;
    return _observable;
  }

  void buildInitializer( @Nonnull final MethodSpec.Builder builder )
  {
    if ( null != _field )
    {
      assert _cascade;
      final String fieldName = _field.getSimpleName().toString();
      final boolean isNonnull =
        null != ProcessorUtil.findAnnotationByType( _field, Constants.NONNULL_ANNOTATION_CLASSNAME );
      if ( isNonnull )
      {
        builder.addStatement( "$T.asDisposeNotifier( this.$N ).addOnDisposeListener( this, this::dispose )",
                              Generator.DISPOSE_TRACKABLE_CLASSNAME,
                              fieldName );
      }
      else
      {
        final CodeBlock.Builder listenerBlock = CodeBlock.builder();
        listenerBlock.beginControlFlow( "if ( null != this.$N )", fieldName );
        listenerBlock.addStatement( "$T.asDisposeNotifier( this.$N ).addOnDisposeListener( this, this::dispose )",
                                    Generator.DISPOSE_TRACKABLE_CLASSNAME,
                                    _field.getSimpleName() );
        listenerBlock.endControlFlow();
        builder.addCode( listenerBlock.build() );
      }
    }
    else
    {
      final ExecutableElement method = getMethod();
      final String methodName = method.getSimpleName().toString();
      final boolean abstractObservables = method.getModifiers().contains( Modifier.ABSTRACT );
      final boolean isNonnull =
        null != ProcessorUtil.findAnnotationByType( method, Constants.NONNULL_ANNOTATION_CLASSNAME );
      if ( abstractObservables )
      {
        if ( isNonnull )
        {
          assert _cascade;
          builder.addStatement( "$T.asDisposeNotifier( $N ).addOnDisposeListener( this, this::dispose )",
                                Generator.DISPOSE_TRACKABLE_CLASSNAME,
                                getObservable().getDataFieldName() );
        }
        // Abstract methods that do not require initializer have no chance to be non-null in the constructor
        // so there is no need to try and add listener as this can not occur
        else if ( getObservable().requireInitializer() )
        {
          final String varName = Generator.VARIABLE_PREFIX + methodName + "_dependency";
          builder.addStatement( "final $T $N = this.$N",
                                getMethod().getReturnType(),
                                varName,
                                getObservable().getDataFieldName() );
          final CodeBlock.Builder listenerBlock = CodeBlock.builder();
          listenerBlock.beginControlFlow( "if ( null != $N )", varName );
          if ( _cascade )
          {
            listenerBlock.addStatement( "$T.asDisposeNotifier( $N ).addOnDisposeListener( this, this::dispose )",
                                        Generator.DISPOSE_TRACKABLE_CLASSNAME,
                                        getObservable().getDataFieldName() );
          }
          else
          {
            listenerBlock.addStatement( "$T.asDisposeNotifier( $N ).addOnDisposeListener( this, () -> $N( null ) )",
                                        Generator.DISPOSE_TRACKABLE_CLASSNAME,
                                        getObservable().getDataFieldName(),
                                        getObservable().getSetter().getSimpleName().toString() );
          }
          listenerBlock.endControlFlow();
          builder.addCode( listenerBlock.build() );
        }
      }
      else
      {
        if ( isNonnull )
        {
          assert _cascade;
          if ( _componentDescriptor.isClassType() )
          {
            builder.addStatement( "$T.asDisposeNotifier( super.$N() ).addOnDisposeListener( this, this::dispose )",
                                  Generator.DISPOSE_TRACKABLE_CLASSNAME,
                                  method.getSimpleName().toString() );
          }
          else
          {
            builder.addStatement( "$T.asDisposeNotifier( $T.super.$N() ).addOnDisposeListener( this, this::dispose )",
                                  Generator.DISPOSE_TRACKABLE_CLASSNAME,
                                  _componentDescriptor.getClassName(),
                                  method.getSimpleName().toString() );
          }
        }
        else
        {
          final String varName = Generator.VARIABLE_PREFIX + methodName + "_dependency";
          if ( _componentDescriptor.isClassType() )
          {
            builder.addStatement( "final $T $N = super.$N()",
                                  getMethod().getReturnType(),
                                  varName,
                                  method.getSimpleName().toString() );
          }
          else
          {
            builder.addStatement( "final $T $N = $T.super.$N()",
                                  getMethod().getReturnType(),
                                  varName,
                                  _componentDescriptor.getClassName(),
                                  method.getSimpleName().toString() );
          }
          final CodeBlock.Builder listenerBlock = CodeBlock.builder();
          listenerBlock.beginControlFlow( "if ( null != $N )", varName );
          if ( _cascade )
          {
            if ( _componentDescriptor.isClassType() )
            {
              listenerBlock.addStatement( "$T.asDisposeNotifier( super.$N() )." +
                                          "addOnDisposeListener( this, this::dispose )",
                                          Generator.DISPOSE_TRACKABLE_CLASSNAME,
                                          method.getSimpleName() );
            }
            else
            {
              listenerBlock.addStatement( "$T.asDisposeNotifier( $T.super.$N() )." +
                                          "addOnDisposeListener( this, this::dispose )",
                                          Generator.DISPOSE_TRACKABLE_CLASSNAME,
                                          _componentDescriptor.getClassName(),
                                          method.getSimpleName() );
            }
          }
          else
          {
            if ( _componentDescriptor.isClassType() )
            {
              listenerBlock.addStatement( "$T.asDisposeNotifier( super.$N() )." +
                                          "addOnDisposeListener( this, () -> $N( null ) )",
                                          Generator.DISPOSE_TRACKABLE_CLASSNAME,
                                          method.getSimpleName(),
                                          getObservable().getSetter().getSimpleName().toString() );
            }
            else
            {
              listenerBlock.addStatement( "$T.asDisposeNotifier( $T.super.$N() )." +
                                          "addOnDisposeListener( this, () -> $N( null ) )",
                                          Generator.DISPOSE_TRACKABLE_CLASSNAME,
                                          _componentDescriptor.getClassName(),
                                          method.getSimpleName(),
                                          getObservable().getSetter().getSimpleName().toString() );
            }
          }
          listenerBlock.endControlFlow();
          builder.addCode( listenerBlock.build() );
        }
      }
    }
  }

  void validate()
  {
    assert null == _observable || null == _field;
    if ( null == _observable && null != _method )
    {
      MethodChecks.mustBeFinal( Constants.COMPONENT_DEPENDENCY_ANNOTATION_CLASSNAME, _method );
    }
    if ( !_cascade && null != _method )
    {
      if ( null == _observable )
      {
        throw new ArezProcessorException( "@ComponentDependency target defined an action of 'SET_NULL' but the " +
                                          "dependency is not an observable so the annotation processor does not " +
                                          "know how to set the value to null.", _method );
      }
      else if ( !_observable.hasSetter() )
      {
        throw new ArezProcessorException( "@ComponentDependency target defined an action of 'SET_NULL' but the " +
                                          "dependency is an observable with no setter defined so the annotation " +
                                          "processor does not know how to set the value to null.", _method );
      }
      else if ( null != ProcessorUtil.findAnnotationByType( _observable.getSetter().getParameters().get( 0 ),
                                                            Constants.NONNULL_ANNOTATION_CLASSNAME ) )
      {
        throw new ArezProcessorException( "@ComponentDependency target defined an action of 'SET_NULL' but the " +
                                          "setter is annotated with @javax.annotation.Nonnull.", _method );
      }
    }
  }
}
