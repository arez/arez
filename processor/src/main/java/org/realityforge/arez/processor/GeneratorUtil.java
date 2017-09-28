package org.realityforge.arez.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import java.util.ArrayList;
import javax.annotation.Nonnull;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

@SuppressWarnings( "Duplicates" )
final class GeneratorUtil
{
  static final ClassName GUARDS_CLASSNAME = ClassName.get( "org.realityforge.braincheck", "Guards" );
  static final ClassName AREZ_CLASSNAME = ClassName.get( "org.realityforge.arez", "Arez" );
  static final ClassName AREZ_CONTEXT_CLASSNAME = ClassName.get( "org.realityforge.arez", "ArezContext" );
  static final ClassName OBSERVABLE_CLASSNAME = ClassName.get( "org.realityforge.arez", "Observable" );
  static final ClassName OBSERVER_CLASSNAME = ClassName.get( "org.realityforge.arez", "Observer" );
  static final ClassName COMPUTED_VALUE_CLASSNAME = ClassName.get( "org.realityforge.arez", "ComputedValue" );
  static final ClassName DISPOSABLE_CLASSNAME = ClassName.get( "org.realityforge.arez", "Disposable" );
  private static final ClassName ACTION_STARTED_CLASSNAME =
    ClassName.get( "org.realityforge.arez.spy", "ActionStartedEvent" );
  private static final ClassName ACTION_COMPLETED_CLASSNAME =
    ClassName.get( "org.realityforge.arez.spy", "ActionCompletedEvent" );
  static final String FIELD_PREFIX = "$$arez$$_";
  private static final String DURATION_VARIABLE_NAME = FIELD_PREFIX + "duration";
  static final String THROWABLE_VARIABLE_NAME = FIELD_PREFIX + "throwable";
  static final String COMPLETED_VARIABLE_NAME = FIELD_PREFIX + "completed";
  static final String RESULT_VARIABLE_NAME = FIELD_PREFIX + "result";
  static final String STARTED_AT_VARIABLE_NAME = FIELD_PREFIX + "startedAt";
  static final String DISPOSED_FIELD_NAME = FIELD_PREFIX + "disposed";
  static final String ID_FIELD_NAME = FIELD_PREFIX + "id";
  static final String NAME_METHOD_NAME = FIELD_PREFIX + "name";
  static final String NEXT_ID_FIELD_NAME = FIELD_PREFIX + "nextId";
  static final String CONTEXT_FIELD_NAME = FIELD_PREFIX + "context";

  private GeneratorUtil()
  {
  }

  static void actionStartedSpyEvent( @Nonnull final ComponentDescriptor componentDescriptor,
                                     @Nonnull final String name,
                                     final boolean tracked,
                                     @Nonnull final ExecutableElement method,
                                     @Nonnull final CodeBlock.Builder codeBlock )
  {
    final CodeBlock.Builder spyCodeBlock = CodeBlock.builder();
    spyCodeBlock.beginControlFlow( "if ( this.$N.areSpiesEnabled() && this.$N.getSpy().willPropagateSpyEvents() )",
                                   CONTEXT_FIELD_NAME,
                                   CONTEXT_FIELD_NAME );
    spyCodeBlock.addStatement( "$N = $T.currentTimeMillis()", STARTED_AT_VARIABLE_NAME, System.class );

    final StringBuilder sb = new StringBuilder();
    final ArrayList<Object> reportParameters = new ArrayList<>();
    sb.append( "this.$N.getSpy().reportSpyEvent( new $T( " );
    reportParameters.add( CONTEXT_FIELD_NAME );
    reportParameters.add( ACTION_STARTED_CLASSNAME );
    if ( !componentDescriptor.isSingleton() )
    {
      sb.append( "$N() + $S" );
      reportParameters.add( componentDescriptor.getComponentNameMethodName() );
      reportParameters.add( "." + name );
    }
    else
    {
      sb.append( "$S" );
      reportParameters.add( componentDescriptor.getNamePrefix() + name );
    }
    sb.append( ", " );
    sb.append( tracked );
    sb.append( ", new Object[]{" );
    boolean firstParam = true;
    for ( final VariableElement element : method.getParameters() )
    {
      if ( !firstParam )
      {
        sb.append( "," );
      }
      firstParam = false;
      sb.append( element.getSimpleName().toString() );
    }
    sb.append( "} ) )" );

    spyCodeBlock.addStatement( sb.toString(), reportParameters.toArray() );
    spyCodeBlock.endControlFlow();
    codeBlock.add( spyCodeBlock.build() );
  }

  static void actionCompletedSpyEvent( @Nonnull final ComponentDescriptor componentDescriptor,
                                       @Nonnull final String name,
                                       final boolean tracked,
                                       @Nonnull final ExecutableElement method,
                                       final boolean isProcedure,
                                       @Nonnull final CodeBlock.Builder codeBlock )
  {
    final CodeBlock.Builder spyCodeBlock = CodeBlock.builder();
    spyCodeBlock.beginControlFlow( "if ( this.$N.areSpiesEnabled() && this.$N.getSpy().willPropagateSpyEvents() )",
                                   CONTEXT_FIELD_NAME,
                                   CONTEXT_FIELD_NAME );
    spyCodeBlock.addStatement( "final long $N = $T.currentTimeMillis() - $N",
                               DURATION_VARIABLE_NAME,
                               System.class,
                               STARTED_AT_VARIABLE_NAME );

    final StringBuilder sb = new StringBuilder();
    final ArrayList<Object> reportParameters = new ArrayList<>();
    sb.append( "this.$N.getSpy().reportSpyEvent( new $T( " );
    reportParameters.add( CONTEXT_FIELD_NAME );
    reportParameters.add( ACTION_COMPLETED_CLASSNAME );
    if ( !componentDescriptor.isSingleton() )
    {
      sb.append( "$N() + $S" );
      reportParameters.add( componentDescriptor.getComponentNameMethodName() );
      reportParameters.add( "." + name );
    }
    else
    {
      sb.append( "$S" );
      reportParameters.add( componentDescriptor.getNamePrefix() + name );
    }
    sb.append( ", " );
    sb.append( tracked );
    sb.append( ", new Object[]{" );
    boolean firstParam = true;
    for ( final VariableElement element : method.getParameters() )
    {
      if ( !firstParam )
      {
        sb.append( "," );
      }
      firstParam = false;
      sb.append( element.getSimpleName().toString() );
    }
    sb.append( "}, " );
    if ( isProcedure )
    {
      sb.append( "false, null" );
    }
    else
    {
      sb.append( "true, $N" );
      reportParameters.add( RESULT_VARIABLE_NAME );
    }

    sb.append( ", $N, $N ) )" );
    reportParameters.add( THROWABLE_VARIABLE_NAME );
    reportParameters.add( DURATION_VARIABLE_NAME );

    spyCodeBlock.addStatement( sb.toString(), reportParameters.toArray() );
    spyCodeBlock.endControlFlow();
    codeBlock.add( spyCodeBlock.build() );
  }
}
