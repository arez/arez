package arez.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;
import org.realityforge.proton.MemberChecks;
import org.realityforge.proton.ProcessorException;

final class MemoizeContextParameterDescriptor
{
  @Nonnull
  private final ComponentDescriptor _component;
  @Nonnull
  private final String _name;
  @Nullable
  private String _patternText;
  @Nullable
  private Pattern _pattern;
  private boolean _allowEmpty;
  @Nullable
  private ExecutableElement _initial;
  @Nullable
  private MemoizeContextParameterMethodType _initialMethodType;
  @Nullable
  private TypeMirror _initialValueType;
  @Nullable
  private ExecutableElement _capture;
  @Nullable
  private ExecutableType _captureType;
  @Nullable
  private ExecutableElement _push;
  @Nullable
  private ExecutableType _pushType;
  @Nullable
  private ExecutableElement _pop;
  @Nullable
  private ExecutableType _popType;
  @Nonnull
  private final List<MemoizeDescriptor> _matchedMemoizeDescriptors = new ArrayList<>();

  MemoizeContextParameterDescriptor( @Nonnull final ComponentDescriptor component, @Nonnull final String name )
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

  @Nonnull
  Pattern pattern()
  {
    assert null != _pattern;
    return _pattern;
  }

  @Nonnull
  String patternText()
  {
    assert null != _patternText;
    return _patternText;
  }

  boolean hasInitial()
  {
    return null != _initial;
  }

  @Nonnull
  ExecutableElement initial()
  {
    assert null != _initial;
    return _initial;
  }

  @Nonnull
  MemoizeContextParameterMethodType initialMethodType()
  {
    assert null != _initialMethodType;
    return _initialMethodType;
  }

  @Nonnull
  TypeMirror initialValueType()
  {
    assert null != _initialValueType;
    return _initialValueType;
  }

  void setPattern( @Nonnull final String patternText, @Nonnull final Pattern pattern )
  {
    assert null == _pattern;
    assert null == _patternText;
    _patternText = Objects.requireNonNull( patternText );
    _pattern = Objects.requireNonNull( pattern );
  }

  void setAllowEmpty( final boolean allowEmpty )
  {
    _allowEmpty = allowEmpty;
  }

  boolean allowEmpty()
  {
    return _allowEmpty;
  }

  @Nonnull
  List<MemoizeDescriptor> getMatchedMemoizeDescriptors()
  {
    return _matchedMemoizeDescriptors;
  }

  boolean tryMatchMemoizeDescriptor( @Nonnull final MemoizeDescriptor memoizeDescriptor )
  {
    if ( _pattern.matcher( memoizeDescriptor.getName() ).matches() )
    {
      _matchedMemoizeDescriptors.add( memoizeDescriptor );
      memoizeDescriptor.addContextParameter( this );
      return true;
    }
    else
    {
      return false;
    }

  }

  boolean hasCapture()
  {
    return null != _capture;
  }

  @Nonnull
  ExecutableElement getCapture()
    throws ProcessorException
  {
    assert null != _capture;
    return _capture;
  }

  @Nonnull
  ExecutableType getCaptureType()
  {
    assert null != _captureType;
    return _captureType;
  }

  void compareInitial( @Nonnull final ExecutableElement method,
                       final boolean allowEmpty,
                       @Nonnull final String patternText )
  {
    if ( hasInitial() )
    {
      if ( allowEmpty() != allowEmpty )
      {
        throw new ProcessorException( "@MemoizeContextParameter target defined with the same name as the " +
                                      "@MemoizeContextParameter annotated method " +
                                      initial().getSimpleName().toString() + " does not have a matching " +
                                      "allowEmpty parameter. The target defines the value as " + allowEmpty +
                                      " while the " + initialMethodType() + " method named " +
                                      initial().getSimpleName().toString() + " specifies allowEntry as " +
                                      allowEmpty() + ". Please ensure that allowEntry is the same value " +
                                      "for both methods.", method );
      }
      else if ( !patternText.equals( patternText() ) )
      {
        throw new ProcessorException( "@MemoizeContextParameter target defined with the same name as the " +
                                      "@MemoizeContextParameter annotated method " +
                                      initial().getSimpleName().toString() + " does not have a matching " +
                                      "pattern parameter. The target defines the value as '" + patternText +
                                      "' while the " + initialMethodType() + " method named " +
                                      initial().getSimpleName().toString() + " specifies pattern as '" +
                                      patternText() + "'. Please ensure that allowEntry is the same value " +
                                      "in both methods.", method );
      }
    }
  }

  void setCapture( @Nonnull final ExecutableElement method,
                   @Nonnull final ExecutableType methodType,
                   final boolean allowEmpty,
                   @Nonnull final String patternText,
                   @Nonnull final Pattern pattern )
  {
    doSetCapture( method, methodType );

    final TypeMirror returnType = method.getReturnType();
    if ( null == _initial )
    {
      _initialMethodType = MemoizeContextParameterMethodType.Capture;
      _initial = Objects.requireNonNull( method );
      _initialValueType = Objects.requireNonNull( returnType );
      setAllowEmpty( allowEmpty );
      setPattern( patternText, pattern );
    }
    else
    {
      compareInitial( method, allowEmpty, patternText );
    }
  }

  void linkUnAnnotatedCapture( @Nonnull final ExecutableElement method, @Nonnull final ExecutableType methodType )
  {
    doSetCapture( method, methodType );
  }

  private void doSetCapture( final @Nonnull ExecutableElement method, final @Nonnull ExecutableType methodType )
  {
    MemberChecks.mustReturnAValue( Constants.MEMOIZE_CONTEXT_PARAMETER_CLASSNAME, method );
    MemberChecks.mustNotThrowAnyExceptions( Constants.MEMOIZE_CONTEXT_PARAMETER_CLASSNAME, method );
    MemberChecks.mustNotHaveAnyParameters( Constants.MEMOIZE_CONTEXT_PARAMETER_CLASSNAME, method );
    MemberChecks.mustNotHaveAnyTypeParameters( Constants.MEMOIZE_CONTEXT_PARAMETER_CLASSNAME, method );
    MemberChecks.mustNotBeAbstract( Constants.MEMOIZE_CONTEXT_PARAMETER_CLASSNAME, method );
    MemberChecks.mustBeSubclassCallable( _component.getElement(),
                                         Constants.COMPONENT_CLASSNAME,
                                         Constants.MEMOIZE_CONTEXT_PARAMETER_CLASSNAME, method );
    _capture = Objects.requireNonNull( method );
    _captureType = Objects.requireNonNull( methodType );
  }

  boolean hasPush()
  {
    return null != _push;
  }

  @Nonnull
  ExecutableElement getPush()
    throws ProcessorException
  {
    assert null != _push;
    return _push;
  }

  @Nonnull
  ExecutableType getPushType()
  {
    assert null != _pushType;
    return _pushType;
  }

  void linkUnAnnotatedPush( @Nonnull final ExecutableElement method, @Nonnull final ExecutableType methodType )
  {
    doSetPush( method, methodType );
  }

  void setPush( @Nonnull final ExecutableElement method,
                @Nonnull final ExecutableType methodType,
                final boolean allowEmpty,
                @Nonnull final String patternText,
                @Nonnull final Pattern pattern )
  {
    doSetPush( method, methodType );

    final TypeMirror valueType = method.getParameters().get( 0 ).asType();
    if ( null == _initial )
    {
      _initialMethodType = MemoizeContextParameterMethodType.Push;
      _initial = Objects.requireNonNull( method );
      _initialValueType = Objects.requireNonNull( valueType );
      setAllowEmpty( allowEmpty );
      setPattern( patternText, pattern );
    }
    else
    {
      compareInitial( method, allowEmpty, patternText );
    }
  }

  private void doSetPush( @Nonnull final ExecutableElement method, @Nonnull final ExecutableType methodType )
  {
    MemberChecks.mustNotReturnAnyValue( Constants.MEMOIZE_CONTEXT_PARAMETER_CLASSNAME, method );
    MemberChecks.mustNotThrowAnyExceptions( Constants.MEMOIZE_CONTEXT_PARAMETER_CLASSNAME, method );
    MemberChecks.mustNotHaveAnyTypeParameters( Constants.MEMOIZE_CONTEXT_PARAMETER_CLASSNAME, method );
    MemberChecks.mustNotBeAbstract( Constants.MEMOIZE_CONTEXT_PARAMETER_CLASSNAME, method );
    MemberChecks.mustBeSubclassCallable( _component.getElement(),
                                         Constants.COMPONENT_CLASSNAME,
                                         Constants.MEMOIZE_CONTEXT_PARAMETER_CLASSNAME, method );
    if ( 1 != method.getParameters().size() )
    {
      throw new ProcessorException( "@MemoizeContextParameter target on push method should accept a single parameter",
                                    method );
    }
    _push = Objects.requireNonNull( method );
    _pushType = Objects.requireNonNull( methodType );
  }

  boolean hasPop()
  {
    return null != _pop;
  }

  @Nonnull
  ExecutableElement getPop()
    throws ProcessorException
  {
    assert null != _pop;
    return _pop;
  }

  @Nonnull
  ExecutableType getPopType()
  {
    assert null != _popType;
    return _popType;
  }

  void linkUnAnnotatedPop( @Nonnull final ExecutableElement method, @Nonnull final ExecutableType methodType )
  {
    doSetPop( method, methodType );
  }

  void setPop( @Nonnull final ExecutableElement method,
               @Nonnull final ExecutableType methodType,
               final boolean allowEmpty,
               @Nonnull final String patternText,
               @Nonnull final Pattern pattern )
  {
    doSetPop( method, methodType );

    final TypeMirror valueType = method.getParameters().get( 0 ).asType();
    if ( null == _initial )
    {
      _initialMethodType = MemoizeContextParameterMethodType.Pop;
      _initial = Objects.requireNonNull( method );
      _initialValueType = Objects.requireNonNull( valueType );
      setAllowEmpty( allowEmpty );
      setPattern( patternText, pattern );
    }
    else
    {
      compareInitial( method, allowEmpty, patternText );
    }
  }

  private void doSetPop( @Nonnull final ExecutableElement method, @Nonnull final ExecutableType methodType )
  {
    MemberChecks.mustNotReturnAnyValue( Constants.MEMOIZE_CONTEXT_PARAMETER_CLASSNAME, method );
    MemberChecks.mustNotThrowAnyExceptions( Constants.MEMOIZE_CONTEXT_PARAMETER_CLASSNAME, method );
    MemberChecks.mustNotHaveAnyTypeParameters( Constants.MEMOIZE_CONTEXT_PARAMETER_CLASSNAME, method );
    MemberChecks.mustNotBeAbstract( Constants.MEMOIZE_CONTEXT_PARAMETER_CLASSNAME, method );
    MemberChecks.mustBeSubclassCallable( _component.getElement(),
                                         Constants.COMPONENT_CLASSNAME,
                                         Constants.MEMOIZE_CONTEXT_PARAMETER_CLASSNAME, method );
    if ( 1 != method.getParameters().size() )
    {
      throw new ProcessorException( "@MemoizeContextParameter target on pop method should accept a single parameter",
                                    method );
    }
    _pop = Objects.requireNonNull( method );
    _popType = Objects.requireNonNull( methodType );
  }

  void validate( @Nonnull final ProcessingEnvironment processingEnv )
  {
    assert null != _initial;
    if ( null == _capture )
    {
      throw new ProcessorException( "@MemoizeContextParameter target has no associated capture method.", _initial );
    }
    if ( null == _push )
    {
      throw new ProcessorException( "@MemoizeContextParameter target has no associated push method.", _initial );
    }
    if ( null == _pop )
    {
      throw new ProcessorException( "@MemoizeContextParameter target has no associated pop method.", _initial );
    }

    if ( !_allowEmpty && _matchedMemoizeDescriptors.isEmpty() )
    {
      assert null != _pattern;
      throw new ProcessorException( "@MemoizeContextParameter target has not specified allowEmpty = true but there " +
                                    "are no @Memoize annotated methods that match the pattern '" +
                                    _pattern.pattern() + "'.", _initial );
    }

    if ( !processingEnv.getTypeUtils().isSameType( initialValueType(), getCapture().getReturnType() ) &&
         !initialValueType().toString().equals( getCapture().getReturnType().toString() ) )
    {
      throw new ProcessorException( "@MemoizeContextParameter target defines a capture method with a different " +
                                    "type (" + getCapture().getReturnType() + ") from the matching " +
                                    "@MemoizeContextParameter " + initialMethodType() + " method named " +
                                    initial().getSimpleName() + "that defines the type " + initialValueType() + ".",
                                    getCapture() );
    }

    final TypeMirror pushValueType = getPush().getParameters().get( 0 ).asType();
    if ( !processingEnv.getTypeUtils().isSameType( initialValueType(), pushValueType ) &&
         !initialValueType().toString().equals( pushValueType.toString() ) )
    {
      throw new ProcessorException( "@MemoizeContextParameter target defines a push method with a different " +
                                    "type (" + pushValueType + ") from the matching @MemoizeContextParameter " +
                                    initialMethodType() + " method named " + initial().getSimpleName() +
                                    " that defines the type " + initialValueType() + ".", getPush() );
    }

    final TypeMirror popValueType = getPop().getParameters().get( 0 ).asType();
    if ( !processingEnv.getTypeUtils().isSameType( initialValueType(), popValueType ) &&
         !initialValueType().toString().equals( popValueType.toString() ) )
    {
      throw new ProcessorException( "@MemoizeContextParameter target defines a pop method with a different " +
                                    "type (" + popValueType + ") from the matching @MemoizeContextParameter " +
                                    initialMethodType() + " method named " + initial().getSimpleName() +
                                    " that defines the type " + initialValueType() + ".", getPop() );
    }
  }
}
