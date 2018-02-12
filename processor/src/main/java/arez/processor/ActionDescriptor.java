package arez.processor;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

/**
 * The class that represents the parsed state of @Action methods on a @ArezComponent annotated class.
 */
@SuppressWarnings( "Duplicates" )
final class ActionDescriptor
{
  @Nonnull
  private final ComponentDescriptor _componentDescriptor;
  @Nonnull
  private final String _name;
  private final boolean _mutation;
  private final boolean _reportParameters;
  @Nonnull
  private final ExecutableElement _action;
  @Nonnull
  private final ExecutableType _actionType;

  ActionDescriptor( @Nonnull final ComponentDescriptor componentDescriptor,
                    @Nonnull final String name,
                    final boolean mutation,
                    final boolean reportParameters,
                    @Nonnull final ExecutableElement action,
                    @Nonnull final ExecutableType actionType )
  {
    _componentDescriptor = Objects.requireNonNull( componentDescriptor );
    _name = Objects.requireNonNull( name );
    _mutation = mutation;
    _reportParameters = reportParameters;
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

  void buildMethods( @Nonnull final TypeSpec.Builder builder )
    throws ArezProcessorException
  {
    builder.addMethod( buildAction() );
  }

  /**
   * Generate the action wrapper.
   */
  @Nonnull
  private MethodSpec buildAction()
    throws ArezProcessorException
  {
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( _action.getSimpleName().toString() );
    ProcessorUtil.copyAccessModifiers( _action, builder );
    ProcessorUtil.copyExceptions( _actionType, builder );
    ProcessorUtil.copyTypeParameters( _actionType, builder );
    ProcessorUtil.copyDocumentedAnnotations( _action, builder );
    builder.addAnnotation( Override.class );
    final TypeMirror returnType = _actionType.getReturnType();
    builder.returns( TypeName.get( returnType ) );

    final boolean isProcedure = returnType.getKind() == TypeKind.VOID;
    final List<? extends TypeMirror> thrownTypes = _action.getThrownTypes();
    final boolean isSafe = thrownTypes.isEmpty();

    final StringBuilder statement = new StringBuilder();
    final ArrayList<Object> parameterNames = new ArrayList<>();

    if ( !isProcedure )
    {
      statement.append( "return " );
    }
    statement.append( "$N()." );
    parameterNames.add( _componentDescriptor.getContextMethodName() );

    if ( isProcedure && isSafe )
    {
      statement.append( "safeAction" );
    }
    else if ( isProcedure )
    {
      statement.append( "action" );
    }
    else if ( isSafe )
    {
      statement.append( "safeAction" );
    }
    else
    {
      statement.append( "action" );
    }

    statement.append( "(" );

    statement.append( "$T.areNamesEnabled() ? $N() + $S : null" );
    parameterNames.add( GeneratorUtil.AREZ_CLASSNAME );
    parameterNames.add( _componentDescriptor.getComponentNameMethodName() );
    parameterNames.add( "." + getName() );

    statement.append( ", " );
    statement.append( _mutation );
    statement.append( ", () -> super." );
    statement.append( _action.getSimpleName() );
    statement.append( "(" );

    boolean firstParam = true;
    final List<? extends VariableElement> parameters = _action.getParameters();
    final int paramCount = parameters.size();
    for ( int i = 0; i < paramCount; i++ )
    {
      final VariableElement element = parameters.get( i );
      final TypeName parameterType = TypeName.get( _actionType.getParameterTypes().get( i ) );
      final ParameterSpec.Builder param =
        ParameterSpec.builder( parameterType, element.getSimpleName().toString(), Modifier.FINAL );
      ProcessorUtil.copyDocumentedAnnotations( element, param );
      builder.addParameter( param.build() );
      parameterNames.add( element.getSimpleName().toString() );
      if ( !firstParam )
      {
        statement.append( "," );
      }
      firstParam = false;
      statement.append( "$N" );
    }

    statement.append( ")" );
    if ( _reportParameters )
    {
      for ( final VariableElement parameter : parameters )
      {
        parameterNames.add( parameter.getSimpleName().toString() );
        statement.append( ", $N" );
      }
    }
    statement.append( " )" );

    GeneratorUtil.generateNotDisposedInvariant( _componentDescriptor, builder );

    final CodeBlock.Builder codeBlock = CodeBlock.builder();
    codeBlock.beginControlFlow( "try" );

    codeBlock.addStatement( statement.toString(), parameterNames.toArray() );

    if ( !thrownTypes.isEmpty() )
    {
      final ArrayList<Object> args = new ArrayList<>();
      args.addAll( thrownTypes );
      args.add( GeneratorUtil.CAUGHT_THROWABLE_NAME );

      codeBlock.nextControlFlow( "catch( final " +
                                 thrownTypes.stream().map( t -> "$T" ).collect( Collectors.joining( " | " ) ) +
                                 " $N )", args.toArray() );
      codeBlock.addStatement( "throw $N", GeneratorUtil.CAUGHT_THROWABLE_NAME );
    }

    if ( thrownTypes.stream().noneMatch( t -> t.toString().equals( "java.lang.Throwable" ) ) )
    {
      if ( thrownTypes.stream().noneMatch( t -> t.toString().equals( "java.lang.Exception" ) ) )
      {
        if ( thrownTypes.stream().noneMatch( t -> t.toString().equals( "java.lang.RuntimeException" ) ) )
        {
          codeBlock.nextControlFlow( "catch( final $T $N )",
                                     RuntimeException.class,
                                     GeneratorUtil.CAUGHT_THROWABLE_NAME );
          codeBlock.addStatement( "throw $N", GeneratorUtil.CAUGHT_THROWABLE_NAME );
        }
        codeBlock.nextControlFlow( "catch( final $T $N )", Exception.class, GeneratorUtil.CAUGHT_THROWABLE_NAME );
        codeBlock.addStatement( "throw new $T( $N )",
                                IllegalStateException.class,
                                GeneratorUtil.CAUGHT_THROWABLE_NAME );
      }
      codeBlock.nextControlFlow( "catch( final $T $N )", Error.class, GeneratorUtil.CAUGHT_THROWABLE_NAME );
      codeBlock.addStatement( "throw $N", GeneratorUtil.CAUGHT_THROWABLE_NAME );
      codeBlock.nextControlFlow( "catch( final $T $N )", Throwable.class, GeneratorUtil.CAUGHT_THROWABLE_NAME );
      codeBlock.addStatement( "throw new $T( $N )", IllegalStateException.class, GeneratorUtil.CAUGHT_THROWABLE_NAME );
    }
    codeBlock.endControlFlow();
    builder.addCode( codeBlock.build() );

    return builder.build();
  }
}
