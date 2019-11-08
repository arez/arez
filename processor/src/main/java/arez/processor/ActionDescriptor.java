package arez.processor;

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
  private final boolean _requireNewTransaction;
  private final boolean _mutation;
  private final boolean _verifyRequired;
  private final boolean _reportParameters;
  private final boolean _reportResult;
  @Nonnull
  private final ExecutableElement _action;
  @Nonnull
  private final ExecutableType _actionType;

  ActionDescriptor( @Nonnull final ComponentDescriptor componentDescriptor,
                    @Nonnull final String name,
                    final boolean requireNewTransaction,
                    final boolean mutation,
                    final boolean verifyRequired,
                    final boolean reportParameters,
                    final boolean reportResult,
                    @Nonnull final ExecutableElement action,
                    @Nonnull final ExecutableType actionType )
  {
    _componentDescriptor = Objects.requireNonNull( componentDescriptor );
    _name = Objects.requireNonNull( name );
    _requireNewTransaction = requireNewTransaction;
    _mutation = mutation;
    _verifyRequired = verifyRequired;
    _reportParameters = reportParameters;
    _reportResult = reportResult;
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
    throws ProcessorException
  {
    builder.addMethod( buildAction() );
  }

  /**
   * Generate the action wrapper.
   */
  @Nonnull
  private MethodSpec buildAction()
    throws ProcessorException
  {
    final String methodName = _action.getSimpleName().toString();
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( methodName );
    ProcessorUtil.copyAccessModifiers( _action, builder );
    ProcessorUtil.copyExceptions( _actionType, builder );
    ProcessorUtil.copyTypeParameters( _actionType, builder );
    ProcessorUtil.copyWhitelistedAnnotations( _action, builder );
    builder.addAnnotation( Override.class );
    final TypeMirror returnType = _actionType.getReturnType();
    builder.returns( TypeName.get( returnType ) );

    final boolean isProcedure = returnType.getKind() == TypeKind.VOID;
    final List<? extends TypeMirror> thrownTypes = _action.getThrownTypes();
    final boolean isSafe = thrownTypes.isEmpty();

    final StringBuilder statement = new StringBuilder();
    final ArrayList<Object> params = new ArrayList<>();

    if ( !isProcedure )
    {
      statement.append( "return " );
    }

    statement.append( "this.$N.getContext()." );
    params.add( Generator.KERNEL_FIELD_NAME );

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

    statement.append( "$T.areNamesEnabled() ? this.$N.getName() + $S : null" );
    params.add( Generator.AREZ_CLASSNAME );
    params.add( Generator.KERNEL_FIELD_NAME );
    params.add( "." + getName() );

    statement.append( ", () -> " );
    if ( _componentDescriptor.isInterfaceType() )
    {
      statement.append( "$T." );
      params.add( _componentDescriptor.getClassName() );
    }
    statement.append( "super.$N(" );
    params.add( _action.getSimpleName().toString() );

    final List<? extends VariableElement> parameters = _action.getParameters();
    final int paramCount = parameters.size();

    boolean firstParam = true;
    if ( 0 != paramCount )
    {
      statement.append( " " );
    }
    for ( int i = 0; i < paramCount; i++ )
    {
      final VariableElement element = parameters.get( i );
      final TypeName parameterType = TypeName.get( _actionType.getParameterTypes().get( i ) );
      final ParameterSpec.Builder param =
        ParameterSpec.builder( parameterType, element.getSimpleName().toString(), Modifier.FINAL );
      ProcessorUtil.copyWhitelistedAnnotations( element, param );
      builder.addParameter( param.build() );
      params.add( element.getSimpleName().toString() );
      if ( !firstParam )
      {
        statement.append( ", " );
      }
      firstParam = false;
      statement.append( "$N" );
    }
    if ( 0 != paramCount )
    {
      statement.append( " " );
    }

    statement.append( "), " );

    appendFlags( statement, params );

    statement.append( ", " );

    if ( _reportParameters && !parameters.isEmpty() )
    {
      statement.append( "$T.areSpiesEnabled() ? new $T[] { " );
      params.add( Generator.AREZ_CLASSNAME );
      params.add( Object.class );
      firstParam = true;
      for ( final VariableElement parameter : parameters )
      {
        if ( !firstParam )
        {
          statement.append( ", " );
        }
        firstParam = false;
        params.add( parameter.getSimpleName().toString() );
        statement.append( "$N" );
      }
      statement.append( " } : null" );
    }
    else
    {
      statement.append( "null" );
    }
    statement.append( " )" );

    Generator.generateNotDisposedInvariant( builder, methodName );
    if ( isSafe )
    {
      builder.addStatement( statement.toString(), params.toArray() );
    }
    else
    {
      Generator.generateTryBlock( builder,
                                  thrownTypes,
                                  b -> b.addStatement( statement.toString(), params.toArray() ) );
    }

    return builder.build();
  }

  private void appendFlags( @Nonnull final StringBuilder expression, @Nonnull final ArrayList<Object> parameters )
  {
    final ArrayList<String> flags = new ArrayList<>();

    if ( _requireNewTransaction )
    {
      flags.add( "REQUIRE_NEW_TRANSACTION" );
    }
    if ( _mutation )
    {
      flags.add( "READ_WRITE" );
    }
    else
    {
      flags.add( "READ_ONLY" );
    }
    if ( _verifyRequired )
    {
      flags.add( "VERIFY_ACTION_REQUIRED" );
    }
    else
    {
      flags.add( "NO_VERIFY_ACTION_REQUIRED" );
    }
    if ( !_reportResult )
    {
      flags.add( "NO_REPORT_RESULT" );
    }

    expression.append( flags.stream().map( flag -> "$T." + flag ).collect( Collectors.joining( " | " ) ) );
    for ( int i = 0; i < flags.size(); i++ )
    {
      parameters.add( Generator.ACTION_FLAGS_CLASSNAME );
    }
  }
}
