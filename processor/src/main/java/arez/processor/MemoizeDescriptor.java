package arez.processor;

import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.WildcardTypeName;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;
import org.realityforge.proton.MemberChecks;
import org.realityforge.proton.ProcessorException;

/**
 * The class that represents the parsed state of @Memoize methods on a @ArezComponent annotated class.
 */
final class MemoizeDescriptor
{
  @Nonnull
  private final ComponentDescriptor _component;
  @Nonnull
  private final String _name;
  @Nullable
  private ExecutableElement _method;
  @Nullable
  private ExecutableType _methodType;
  private boolean _keepAlive;
  private Priority _priority;
  private boolean _reportResult;
  private boolean _observeLowerPriorityDependencies;
  private String _readOutsideTransaction;
  private String _depType;
  @Nullable
  private ExecutableElement _onActivate;
  @Nullable
  private ExecutableElement _onDeactivate;
  @Nonnull
  private final List<CandidateMethod> _refMethods = new ArrayList<>();
  @Nonnull
  private final List<MemoizeContextParameterDescriptor> _contextParameters = new ArrayList<>();

  MemoizeDescriptor( @Nonnull final ComponentDescriptor component, @Nonnull final String name )
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

  boolean hasMemoize()
  {
    return null != _method;
  }

  boolean isKeepAlive()
  {
    return _keepAlive;
  }

  @Nonnull
  Priority getPriority()
  {
    return _priority;
  }

  @Nonnull
  ExecutableElement getMethod()
  {
    return Objects.requireNonNull( _method );
  }

  @Nonnull
  ExecutableType getMethodType()
  {
    return Objects.requireNonNull( _methodType );
  }

  @Nullable
  ExecutableElement getOnActivate()
  {
    return _onActivate;
  }

  @Nullable
  ExecutableElement getOnDeactivate()
  {
    return _onDeactivate;
  }

  void setMemoize( @Nonnull final ExecutableElement method,
                   @Nonnull final ExecutableType methodType,
                   final boolean keepAlive,
                   @Nonnull final Priority priority,
                   final boolean reportResult,
                   final boolean observeLowerPriorityDependencies,
                   @Nonnull final String readOutsideTransaction,
                   @Nonnull final String depType )
    throws ProcessorException
  {
    //The caller already verified that no duplicate computable have been defined
    assert null == _method;
    MemberChecks.mustBeWrappable( _component.getElement(),
                                  Constants.COMPONENT_CLASSNAME,
                                  Constants.MEMOIZE_CLASSNAME,
                                  method );
    MemberChecks.mustReturnAValue( Constants.MEMOIZE_CLASSNAME, method );
    MemberChecks.mustNotThrowAnyExceptions( Constants.MEMOIZE_CLASSNAME, method );

    _method = Objects.requireNonNull( method );
    _methodType = Objects.requireNonNull( methodType );
    _keepAlive = keepAlive;
    _priority = Objects.requireNonNull( priority );
    _reportResult = reportResult;
    _observeLowerPriorityDependencies = observeLowerPriorityDependencies;
    _readOutsideTransaction = readOutsideTransaction;
    _depType = Objects.requireNonNull( depType );

    if ( ComponentGenerator.isMethodReturnType( getMethod(), Stream.class ) )
    {
      throw new ProcessorException( "@Memoize target must not return a value of type java.util.stream.Stream " +
                                    "as the type is single use and thus does not make sense to cache as a " +
                                    "computable value", method );
    }
  }

  void addRefMethod( @Nonnull final ExecutableElement method, @Nonnull final ExecutableType methodType )
    throws ProcessorException
  {
    _refMethods.add( new CandidateMethod( method, methodType ) );
  }

  @Nonnull
  List<CandidateMethod> getRefMethods()
  {
    return _refMethods;
  }

  void addContextParameter( @Nonnull final MemoizeContextParameterDescriptor parameter )
    throws ProcessorException
  {
    _contextParameters.add( parameter );
  }

  @Nonnull
  List<MemoizeContextParameterDescriptor> getContextParameters()
  {
    return _contextParameters;
  }

  void setOnActivate( @Nonnull final ExecutableElement method )
    throws ProcessorException
  {
    if ( null != _onActivate )
    {
      throw new ProcessorException( "@OnActivate target duplicates existing method named " +
                                    _onActivate.getSimpleName(), method );
    }
    else
    {
      _onActivate = Objects.requireNonNull( method );
    }
  }

  void setOnDeactivate( @Nonnull final ExecutableElement method )
    throws ProcessorException
  {
    if ( null != _onDeactivate )
    {
      throw new ProcessorException( "@OnDeactivate target duplicates existing method named " +
                                    _onDeactivate.getSimpleName(),
                                    method );
    }
    else
    {
      _onDeactivate = Objects.requireNonNull( method );
    }
  }

  void validate( @Nonnull final ProcessingEnvironment processingEnv )
    throws ProcessorException
  {
    if ( null == _method )
    {
      if ( null != _onActivate )
      {
        throw new ProcessorException( "@OnActivate exists but there is no corresponding @Memoize",
                                      _onActivate );
      }
      else if ( null != _onDeactivate )
      {
        throw new ProcessorException( "@OnDeactivate exists but there is no corresponding @Memoize",
                                      _onDeactivate );
      }
      else
      {
        throw new ProcessorException( "@ComputableValueRef exists but there is no corresponding @Memoize",
                                      _refMethods.get( 0 ).getMethod() );
      }
    }
    if ( _keepAlive )
    {
      if ( !_method.getParameters().isEmpty() )
      {
        throw new ProcessorException( "@Memoize target specified parameter keepAlive as true but has parameters.",
                                      _method );
      }
      else if ( !getContextParameters().isEmpty() )
      {
        throw new ProcessorException( "@Memoize target specified parameter keepAlive as true but has " +
                                      "matching context parameters.", _method );
      }
      else if ( null != _onActivate )
      {
        throw new ProcessorException( "@OnActivate exists for @Memoize property that specified parameter " +
                                      "keepAlive as true.", _onActivate );
      }
      else if ( null != _onDeactivate )
      {
        throw new ProcessorException( "@OnDeactivate exists for @Memoize property that specified parameter " +
                                      "keepAlive as true.", _onDeactivate );
      }
    }
    // One day we may want to support parameters here.
    // But rather than put the effort into correct code generation when there is no
    // current use case, let's throw an exception.
    // When a use case pops up, then we can implement the functionality.
    if ( !_method.getParameters().isEmpty() )
    {
      if ( null != _onActivate )
      {
        throw new ProcessorException( "@OnActivate target associated with @Memoize method that has parameters.",
                                      _onActivate );
      }
      else if ( null != _onDeactivate )
      {
        throw new ProcessorException( "@OnDeactivate target associated with @Memoize method that has parameters.",
                                      _onDeactivate );
      }
    }
    // One day we may want to support context parameters here.
    // But rather than put the effort into correct code generation when there is no
    // current use case, let's throw an exception.
    // When a use case pops up, then we can implement the functionality.
    if ( !getContextParameters().isEmpty() )
    {
      if ( null != _onActivate )
      {
        throw new ProcessorException( "@OnActivate target associated with @Memoize method that has " +
                                      "matching context parameters.", _onActivate );
      }
      else if ( null != _onDeactivate )
      {
        throw new ProcessorException( "@OnDeactivate target associated with @Memoize method that has " +
                                      "matching context parameters.", _onDeactivate );
      }
    }

    if ( null != _onActivate && null != _method )
    {
      final List<? extends VariableElement> parameters = _onActivate.getParameters();
      if ( 1 == parameters.size() )
      {
        final TypeName typeName = TypeName.get( parameters.get( 0 ).asType() );
        if ( typeName instanceof final ParameterizedTypeName parameterizedTypeName )
        {
          final TypeName paramType = parameterizedTypeName.typeArguments.get( 0 );
          if ( !( paramType instanceof WildcardTypeName ) )
          {
            final TypeName actual = TypeName.get( _method.getReturnType() );
            if ( !actual.box().toString().equals( paramType.toString() ) )
            {
              throw new ProcessorException( "@OnActivate target has a parameter of type ComputableValue with a " +
                                            "type parameter of " + paramType + " but the @Memoize method " +
                                            "returns a type of " + actual, _onActivate );
            }
          }
        }
      }
    }

    for ( final CandidateMethod refMethod : _refMethods )
    {
      final TypeName typeName = TypeName.get( refMethod.getMethod().getReturnType() );
      if ( typeName instanceof final ParameterizedTypeName parameterizedTypeName )
      {
        final TypeName expectedType = parameterizedTypeName.typeArguments.get( 0 );
        if ( !( expectedType instanceof WildcardTypeName ) )
        {
          final TypeName actual = TypeName.get( _method.getReturnType() );
          if ( !actual.box().toString().equals( expectedType.toString() ) )
          {
            throw new ProcessorException( "@ComputableValueRef target has a type parameter of " + expectedType +
                                          " but @Memoize method returns type of " + actual, refMethod.getMethod() );
          }
        }
      }

      assert null != _methodType;
      final List<? extends TypeMirror> parameterTypes = _methodType.getParameterTypes();
      final List<? extends TypeMirror> refParameterTypes = refMethod.getMethodType().getParameterTypes();

      final boolean sizeMatch = parameterTypes.size() == refParameterTypes.size();
      boolean typesMatch = true;
      if ( sizeMatch )
      {
        for ( int i = 0; i < parameterTypes.size(); i++ )
        {
          final TypeMirror typeMirror = parameterTypes.get( i );
          final TypeMirror typeMirror2 = refParameterTypes.get( i );
          if ( !processingEnv.getTypeUtils().isSameType( typeMirror, typeMirror2 ) )
          {
            typesMatch = false;
            break;
          }
        }
      }
      if ( !sizeMatch || !typesMatch )
      {
        throw new ProcessorException( "@ComputableValueRef target and the associated @Memoize " +
                                      "target do not have the same parameters.", _method );
      }
    }
    if ( _refMethods.isEmpty() && getDepType().equals( "AREZ_OR_EXTERNAL" ) )
    {
      assert null != _method;
      throw new ProcessorException( "@Memoize target specified depType = AREZ_OR_EXTERNAL but " +
                                    "there is no associated @ComputableValueRef method.", _method );
    }
  }

  boolean hasHooks()
  {
    return null != _onActivate || null != _onDeactivate;
  }

  boolean isReportResult()
  {
    return _reportResult;
  }

  boolean isObserveLowerPriorityDependencies()
  {
    return _observeLowerPriorityDependencies;
  }

  boolean canReadOutsideTransaction()
  {
    return "ENABLE".equals( _readOutsideTransaction ) ||
           ( "AUTODETECT".equals( _readOutsideTransaction ) && getComponent().defaultReadOutsideTransaction() );
  }

  String getDepType()
  {
    return _depType;
  }
}
