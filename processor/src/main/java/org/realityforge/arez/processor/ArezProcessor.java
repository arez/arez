package org.realityforge.arez.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import org.realityforge.arez.annotations.Container;

/**
 * Annotation processor that analyzes Arez annotated source and generates models from the annotations.
 */
@SuppressWarnings( "Duplicates" )
@AutoService( Processor.class )
@SupportedAnnotationTypes( { "org.realityforge.arez.annotations.*", "javax.annotation.PostConstruct" } )
@SupportedSourceVersion( SourceVersion.RELEASE_8 )
public final class ArezProcessor
  extends AbstractJavaPoetProcessor
{
  private static final ClassName AREZ_CLASSNAME = ClassName.get( "org.realityforge.arez", "Arez" );
  private static final ClassName AREZ_CONTEXT_CLASSNAME = ClassName.get( "org.realityforge.arez", "ArezContext" );
  private static final ClassName OBSERVABLE_CLASSNAME = ClassName.get( "org.realityforge.arez", "Observable" );
  private static final ClassName COMPUTED_VALUE_CLASSNAME = ClassName.get( "org.realityforge.arez", "ComputedValue" );
  private static final ClassName DISPOSABLE_CLASSNAME = ClassName.get( "org.realityforge.arez", "Disposable" );
  private static final ClassName ACTION_STARTED_CLASSNAME =
    ClassName.get( "org.realityforge.arez.spy", "ActionStartedEvent" );
  private static final ClassName ACTION_COMPLETED_CLASSNAME =
    ClassName.get( "org.realityforge.arez.spy", "ActionCompletedEvent" );
  private static final String FIELD_PREFIX = "$$arez$$_";
  private static final String CONTEXT_FIELD_NAME = FIELD_PREFIX + "context";
  private static final String NEXT_ID_FIELD_NAME = FIELD_PREFIX + "nextId";
  private static final String ID_FIELD_NAME = FIELD_PREFIX + "id";
  private static final String DISPOSED_FIELD_NAME = FIELD_PREFIX + "disposed";
  private static final String STARTED_AT_VARIABLE_NAME = FIELD_PREFIX + "startedAt";
  private static final String DURATION_VARIABLE_NAME = FIELD_PREFIX + "duration";
  private static final String RESULT_VARIABLE_NAME = FIELD_PREFIX + "result";
  private static final String COMPLETED_VARIABLE_NAME = FIELD_PREFIX + "completed";
  private static final String THROWABLE_VARIABLE_NAME = FIELD_PREFIX + "throwable";

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean process( final Set<? extends TypeElement> annotations, final RoundEnvironment env )
  {
    final Set<? extends Element> elements = env.getElementsAnnotatedWith( Container.class );
    processElements( elements );
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void process( @Nonnull final Element element )
    throws IOException, ArezProcessorException
  {
    final ContainerDescriptor descriptor = ContainerDescriptorParser.parse( element, processingEnv.getElementUtils() );
    emitTypeSpec( descriptor.getPackageElement().getQualifiedName().toString(), builder( descriptor ) );
  }

  /**
   * Build the enhanced class for specified container.
   */
  @Nonnull
  private TypeSpec builder( @Nonnull final ContainerDescriptor descriptor )
    throws ArezProcessorException
  {
    final TypeElement element = descriptor.getElement();

    final AnnotationSpec generatedAnnotation =
      AnnotationSpec.builder( Generated.class ).addMember( "value", "$S", getClass().getName() ).build();

    final StringBuilder name = new StringBuilder( "Arez_" + element.getSimpleName() );

    TypeElement t = element;
    while ( NestingKind.TOP_LEVEL != t.getNestingKind() )
    {
      t = (TypeElement) t.getEnclosingElement();
      name.insert( 0, t.getSimpleName() + "$" );
    }

    final TypeSpec.Builder builder = TypeSpec.classBuilder( name.toString() ).
      superclass( TypeName.get( element.asType() ) ).
      addTypeVariables( ProcessorUtil.getTypeArgumentsAsNames( descriptor.asDeclaredType() ) ).
      addModifiers( Modifier.FINAL ).
      addAnnotation( generatedAnnotation );
    ProcessorUtil.copyAccessModifiers( element, builder );

    if ( descriptor.isDisposable() )
    {
      builder.addSuperinterface( DISPOSABLE_CLASSNAME );
    }

    buildFields( descriptor, builder );

    buildConstructors( descriptor, builder );

    if ( !descriptor.isSingleton() )
    {
      builder.addMethod( buildIdGetter( descriptor ) );
    }

    if ( descriptor.isDisposable() )
    {
      builder.addMethod( buildIsDisposed( descriptor ) );
      builder.addMethod( buildDispose( descriptor ) );
    }

    for ( final ObservableDescriptor observable : descriptor.getObservables() )
    {
      builder.addMethod( buildObservableGetter( observable ) );
      builder.addMethod( buildObservableSetter( observable ) );
    }

    for ( final ActionDescriptor action : descriptor.getActions() )
    {
      builder.addMethod( buildAction( descriptor, action ) );
    }

    for ( final ComputedDescriptor computed : descriptor.getComputeds() )
    {
      builder.addMethod( buildComputed( computed ) );
    }

    return builder.build();
  }

  /**
   * Generate the wrapper around Computed method.
   */
  @Nonnull
  private MethodSpec buildComputed( @Nonnull final ComputedDescriptor descriptor )
    throws ArezProcessorException
  {
    final ExecutableElement computed = descriptor.getComputed();
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( computed.getSimpleName().toString() );
    ProcessorUtil.copyAccessModifiers( computed, builder );
    ProcessorUtil.copyExceptions( computed, builder );
    ProcessorUtil.copyTypeParameters( computed, builder );
    ProcessorUtil.copyDocumentedAnnotations( computed, builder );
    builder.addAnnotation( Override.class );
    final TypeMirror returnType = computed.getReturnType();
    builder.returns( TypeName.get( returnType ) );

    if ( computed.getTypeParameters().isEmpty() )
    {
      builder.addStatement( "return this.$N.get()", FIELD_PREFIX + descriptor.getName() );
    }
    else
    {
      builder.addStatement( "return ($T) this.$N.get()",
                            TypeName.get( computed.getReturnType() ).box(),
                            FIELD_PREFIX + descriptor.getName() );
    }

    return builder.build();
  }

  /**
   * Generate the action wrapper.
   */
  @Nonnull
  private MethodSpec buildAction( @Nonnull final ContainerDescriptor containerDescriptor,
                                  @Nonnull final ActionDescriptor descriptor )
    throws ArezProcessorException
  {
    final ExecutableElement action = descriptor.getAction();
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( action.getSimpleName().toString() );
    ProcessorUtil.copyAccessModifiers( action, builder );
    ProcessorUtil.copyExceptions( action, builder );
    ProcessorUtil.copyTypeParameters( action, builder );
    ProcessorUtil.copyDocumentedAnnotations( action, builder );
    builder.addAnnotation( Override.class );
    final TypeMirror returnType = action.getReturnType();
    builder.returns( TypeName.get( returnType ) );

    final boolean isProcedure = returnType.getKind() == TypeKind.VOID;
    final boolean isSafe = action.getThrownTypes().isEmpty();

    final StringBuilder statement = new StringBuilder();
    final ArrayList<Object> parameterNames = new ArrayList<>();

    if ( !isProcedure )
    {
      statement.append( "final $T $N = " );
      parameterNames.add( TypeName.get( returnType ) );
      parameterNames.add( RESULT_VARIABLE_NAME );
    }
    statement.append( "this.$N." );
    parameterNames.add( CONTEXT_FIELD_NAME );

    if ( isProcedure && isSafe )
    {
      statement.append( "safeProcedure" );
    }
    else if ( isProcedure )
    {
      statement.append( "procedure" );
    }
    else if ( isSafe )
    {
      statement.append( "safeFunction" );
    }
    else
    {
      statement.append( "function" );
    }

    statement.append( "(" );

    if ( containerDescriptor.isSingleton() )
    {
      statement.append( "this.$N.areNamesEnabled() ? $S : null" );
      parameterNames.add( CONTEXT_FIELD_NAME );
      parameterNames.add( getPrefix( containerDescriptor ) + descriptor.getName() );
    }
    else
    {
      statement.append( "this.$N.areNamesEnabled() ? $N() + $S : null" );
      parameterNames.add( CONTEXT_FIELD_NAME );
      parameterNames.add( ID_FIELD_NAME );
      parameterNames.add( descriptor.getName() );
    }

    statement.append( ", " );
    statement.append( descriptor.isMutation() );
    statement.append( ", () -> super." );
    statement.append( action.getSimpleName() );
    statement.append( "(" );

    boolean firstParam = true;
    for ( final VariableElement element : action.getParameters() )
    {
      final ParameterSpec.Builder param =
        ParameterSpec.builder( TypeName.get( element.asType() ), element.getSimpleName().toString(), Modifier.FINAL );
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

    statement.append( ") )" );

    if ( containerDescriptor.isDisposable() )
    {
      builder.addStatement( "assert !$N", DISPOSED_FIELD_NAME );
    }

    builder.addStatement( "$T $N = null", Throwable.class, THROWABLE_VARIABLE_NAME );
    builder.addStatement( "$T $N = false", boolean.class, COMPLETED_VARIABLE_NAME );
    builder.addStatement( "$T $N = 0L", long.class, STARTED_AT_VARIABLE_NAME );

    final CodeBlock.Builder codeBlock = CodeBlock.builder();
    codeBlock.beginControlFlow( "try" );

    actionStartedSpyEvent( codeBlock, containerDescriptor, descriptor );
    codeBlock.addStatement( statement.toString(), parameterNames.toArray() );
    codeBlock.addStatement( "$N = true", COMPLETED_VARIABLE_NAME );
    actionCompletedSpyEvent( codeBlock, containerDescriptor, descriptor, isProcedure );
    if ( !isProcedure )
    {
      codeBlock.addStatement( "return $N", RESULT_VARIABLE_NAME );
    }

    for ( final TypeMirror exception : action.getThrownTypes() )
    {
      codeBlock.nextControlFlow( "catch( final $T e )", exception );
      codeBlock.addStatement( "throw e" );
    }

    if ( action.getThrownTypes().stream().noneMatch( t -> t.toString().equals( "java.lang.Throwable" ) ) )
    {
      if ( action.getThrownTypes().stream().noneMatch( t -> t.toString().equals( "java.lang.Exception" ) ) )
      {
        if ( action.getThrownTypes().stream().noneMatch( t -> t.toString().equals( "java.lang.RuntimeException" ) ) )
        {
          codeBlock.nextControlFlow( "catch( final $T e )", RuntimeException.class );
          codeBlock.addStatement( "$N = e", THROWABLE_VARIABLE_NAME );
          codeBlock.addStatement( "throw e" );
        }
        codeBlock.nextControlFlow( "catch( final $T e )", Exception.class );
        codeBlock.addStatement( "$N = e", THROWABLE_VARIABLE_NAME );
        codeBlock.addStatement( "throw new $T( e )", IllegalStateException.class );
      }
      codeBlock.nextControlFlow( "catch( final $T e )", Error.class );
      codeBlock.addStatement( "$N = e", THROWABLE_VARIABLE_NAME );
      codeBlock.addStatement( "throw e" );
      codeBlock.nextControlFlow( "catch( final $T e )", Throwable.class );
      codeBlock.addStatement( "$N = e", THROWABLE_VARIABLE_NAME );
      codeBlock.addStatement( "throw new $T( e )", IllegalStateException.class );
    }
    codeBlock.nextControlFlow( "finally" );

    // Send completed spy event if necessary
    codeBlock.beginControlFlow( "if ( !$N )", COMPLETED_VARIABLE_NAME );
    if ( !isProcedure )
    {
      codeBlock.addStatement( "final $T $N = null", TypeName.get( returnType ).box(), RESULT_VARIABLE_NAME );
    }
    actionCompletedSpyEvent( codeBlock, containerDescriptor, descriptor, isProcedure );
    codeBlock.endControlFlow();

    codeBlock.endControlFlow();
    builder.addCode( codeBlock.build() );

    return builder.build();
  }

  private void actionStartedSpyEvent( @Nonnull final CodeBlock.Builder codeBlock,
                                      @Nonnull final ContainerDescriptor containerDescriptor,
                                      @Nonnull final ActionDescriptor descriptor )
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
    if ( !containerDescriptor.isSingleton() )
    {
      sb.append( "$N() + $S" );
      reportParameters.add( ID_FIELD_NAME );
      reportParameters.add( descriptor.getName() );
    }
    else
    {
      sb.append( "$S" );
      reportParameters.add( getPrefix( containerDescriptor ) + descriptor.getName() );
    }
    sb.append( ", new Object[]{" );
    boolean firstParam = true;
    for ( final VariableElement element : descriptor.getAction().getParameters() )
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

  private void actionCompletedSpyEvent( @Nonnull final CodeBlock.Builder codeBlock,
                                        @Nonnull final ContainerDescriptor containerDescriptor,
                                        @Nonnull final ActionDescriptor descriptor,
                                        final boolean isProcedure )
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
    if ( !containerDescriptor.isSingleton() )
    {
      sb.append( "$N() + $S" );
      reportParameters.add( ID_FIELD_NAME );
      reportParameters.add( descriptor.getName() );
    }
    else
    {
      sb.append( "$S" );
      reportParameters.add( getPrefix( containerDescriptor ) + descriptor.getName() );
    }
    sb.append( ", new Object[]{" );
    boolean firstParam = true;
    for ( final VariableElement element : descriptor.getAction().getParameters() )
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

  /**
   * Generate the setter that reports that ensures that the access is reported as Observable.
   */
  @Nonnull
  private MethodSpec buildObservableSetter( @Nonnull final ObservableDescriptor observable )
    throws ArezProcessorException
  {
    final ExecutableElement getter = observable.getGetter();
    final ExecutableElement setter = observable.getSetter();
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( setter.getSimpleName().toString() );
    ProcessorUtil.copyAccessModifiers( setter, builder );
    ProcessorUtil.copyExceptions( setter, builder );
    ProcessorUtil.copyTypeParameters( setter, builder );
    ProcessorUtil.copyDocumentedAnnotations( setter, builder );

    builder.addAnnotation( Override.class );

    final VariableElement element = setter.getParameters().get( 0 );
    final String paramName = element.getSimpleName().toString();
    final TypeName type = TypeName.get( element.asType() );
    final ParameterSpec.Builder param =
      ParameterSpec.builder( type, paramName, Modifier.FINAL );
    ProcessorUtil.copyDocumentedAnnotations( element, param );
    builder.addParameter( param.build() );

    final CodeBlock.Builder codeBlock = CodeBlock.builder();
    final String accessor = "super." + getter.getSimpleName() + "()";
    final String mutator = "super." + setter.getSimpleName() + "($N)";
    if ( type.isPrimitive() )
    {
      codeBlock.beginControlFlow( "if ( $N != " + accessor + " )", paramName );
    }
    else
    {
      codeBlock.beginControlFlow( "if ( !$T.equals($N, " + accessor + ") )", Objects.class, paramName );
    }
    codeBlock.addStatement( mutator, paramName );
    codeBlock.addStatement( "this.$N.reportChanged()", fieldName( observable ) );
    codeBlock.endControlFlow();
    builder.addCode( codeBlock.build() );

    return builder.build();
  }

  /**
   * Generate the getter for id.
   */
  @Nonnull
  private MethodSpec buildIdGetter( @Nonnull final ContainerDescriptor descriptor )
    throws ArezProcessorException
  {
    assert !descriptor.isSingleton();

    final MethodSpec.Builder builder =
      MethodSpec.methodBuilder( ID_FIELD_NAME ).
        addModifiers( Modifier.PRIVATE );

    builder.returns( TypeName.get( String.class ) );
    final ExecutableElement containerId = descriptor.getContainerId();

    if ( null == containerId )
    {
      builder.addStatement( "return $S + $N + $S", getPrefix( descriptor ), ID_FIELD_NAME, "." );
    }
    else
    {
      builder.addStatement( "return $S + $N() + $S", getPrefix( descriptor ), containerId.getSimpleName(), "." );
    }
    return builder.build();
  }

  /**
   * Generate the dispose method.
   */
  @Nonnull
  private MethodSpec buildDispose( @Nonnull final ContainerDescriptor descriptor )
    throws ArezProcessorException
  {
    assert descriptor.isDisposable();

    final MethodSpec.Builder builder =
      MethodSpec.methodBuilder( "dispose" ).
        addModifiers( Modifier.PUBLIC ).
        addAnnotation( Override.class );

    final CodeBlock.Builder codeBlock = CodeBlock.builder();
    codeBlock.beginControlFlow( "if ( !isDisposed() )" );
    codeBlock.addStatement( "$N = true", DISPOSED_FIELD_NAME );
    final ExecutableElement preDispose = descriptor.getPreDispose();
    if ( null != preDispose )
    {
      codeBlock.addStatement( "super.$N()", preDispose.getSimpleName() );
    }
    for ( final ComputedDescriptor computed : descriptor.getComputeds() )
    {
      codeBlock.addStatement( "$N.dispose()", FIELD_PREFIX + computed.getName() );
    }
    for ( final ObservableDescriptor observable : descriptor.getObservables() )
    {
      codeBlock.addStatement( "$N.dispose()", fieldName( observable ) );
    }
    final ExecutableElement postDispose = descriptor.getPostDispose();
    if ( null != postDispose )
    {
      codeBlock.addStatement( "super.$N()", postDispose.getSimpleName() );
    }
    codeBlock.endControlFlow();

    builder.addCode( codeBlock.build() );

    return builder.build();
  }

  /**
   * Generate the isDisposed method.
   */
  @Nonnull
  private MethodSpec buildIsDisposed( @Nonnull final ContainerDescriptor descriptor )
    throws ArezProcessorException
  {
    assert descriptor.isDisposable();

    final MethodSpec.Builder builder =
      MethodSpec.methodBuilder( "isDisposed" ).
        addModifiers( Modifier.PUBLIC ).
        addAnnotation( Override.class ).
        returns( TypeName.BOOLEAN );

    builder.addStatement( "return $N", DISPOSED_FIELD_NAME );

    return builder.build();
  }

  /**
   * Generate the getter that ensures that the access is reported.
   */
  @Nonnull
  private MethodSpec buildObservableGetter( @Nonnull final ObservableDescriptor observable )
    throws ArezProcessorException
  {
    final ExecutableElement getter = observable.getGetter();
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( getter.getSimpleName().toString() );
    ProcessorUtil.copyAccessModifiers( getter, builder );
    ProcessorUtil.copyExceptions( getter, builder );
    ProcessorUtil.copyTypeParameters( getter, builder );
    ProcessorUtil.copyDocumentedAnnotations( getter, builder );

    builder.addAnnotation( Override.class );
    builder.returns( TypeName.get( getter.getReturnType() ) );
    builder.addStatement( "this.$N.reportObserved()", fieldName( observable ) );
    builder.addStatement( "return super." + getter.getSimpleName() + "()" );
    return builder.build();
  }

  /**
   * Build the fields required to make class Observable. This involves;
   * <ul>
   * <li>the context field if there is any @Action methods.</li>
   * <li>the observable object for every @Observable.</li>
   * <li>the ComputedValue object for every @Computed method.</li>
   * </ul>
   */
  private void buildFields( @Nonnull final ContainerDescriptor descriptor, @Nonnull final TypeSpec.Builder builder )
  {
    // If we don't have a method for object id but we need one then synthesize it
    if ( !descriptor.isSingleton() && null == descriptor.getContainerId() )
    {
      final FieldSpec.Builder nextIdField =
        FieldSpec.builder( TypeName.LONG, NEXT_ID_FIELD_NAME, Modifier.VOLATILE, Modifier.STATIC, Modifier.PRIVATE );
      builder.addField( nextIdField.build() );

      final FieldSpec.Builder idField =
        FieldSpec.builder( TypeName.LONG, ID_FIELD_NAME, Modifier.FINAL, Modifier.PRIVATE );
      builder.addField( idField.build() );
    }

    if ( descriptor.isDisposable() )
    {
      final FieldSpec.Builder disposableField =
        FieldSpec.builder( TypeName.BOOLEAN, DISPOSED_FIELD_NAME, Modifier.PRIVATE );
      builder.addField( disposableField.build() );
    }

    // Create the field that contains the context
    {
      final FieldSpec.Builder field =
        FieldSpec.builder( AREZ_CONTEXT_CLASSNAME, CONTEXT_FIELD_NAME, Modifier.FINAL, Modifier.PRIVATE ).
          addAnnotation( Nonnull.class );
      builder.addField( field.build() );
    }

    for ( final ObservableDescriptor observable : descriptor.getObservables() )
    {
      final FieldSpec.Builder field =
        FieldSpec.builder( OBSERVABLE_CLASSNAME, fieldName( observable ), Modifier.FINAL, Modifier.PRIVATE ).
          addAnnotation( Nonnull.class );
      builder.addField( field.build() );
    }
    for ( final ComputedDescriptor computed : descriptor.getComputeds() )
    {
      final TypeName parameterType =
        computed.getComputed().getTypeParameters().isEmpty() ?
        TypeName.get( computed.getComputed().getReturnType() ).box() :
        WildcardTypeName.subtypeOf( TypeName.OBJECT );
      final ParameterizedTypeName typeName =
        ParameterizedTypeName.get( COMPUTED_VALUE_CLASSNAME, parameterType );
      final FieldSpec.Builder field =
        FieldSpec.builder( typeName,
                           FIELD_PREFIX + computed.getName(),
                           Modifier.FINAL,
                           Modifier.PRIVATE ).
          addAnnotation( Nonnull.class );
      builder.addField( field.build() );
    }
  }

  /**
   * Return the name of the field for specified Observable.
   */
  @Nonnull
  private String fieldName( @Nonnull final ObservableDescriptor observable )
  {
    return FIELD_PREFIX + observable.getName();
  }

  /**
   * Build all constructors as they appear on the Container class.
   * Arez Observable fields are populated as required and parameters are passed up to superclass.
   */
  private void buildConstructors( @Nonnull final ContainerDescriptor descriptor,
                                  @Nonnull final TypeSpec.Builder builder )
  {
    for ( final ExecutableElement constructor : ProcessorUtil.getConstructors( descriptor.getElement() ) )
    {
      builder.addMethod( buildConstructor( descriptor, constructor ) );
    }
  }

  /**
   * Build a constructor based on the supplied constructor
   */
  @Nonnull
  private MethodSpec buildConstructor( @Nonnull final ContainerDescriptor descriptor,
                                       @Nonnull final ExecutableElement constructor )
  {
    final MethodSpec.Builder builder = MethodSpec.constructorBuilder();
    ProcessorUtil.copyAccessModifiers( constructor, builder );
    ProcessorUtil.copyExceptions( constructor, builder );
    ProcessorUtil.copyTypeParameters( constructor, builder );

    final StringBuilder superCall = new StringBuilder();
    superCall.append( "super(" );
    final ArrayList<String> parameterNames = new ArrayList<>();

    boolean firstParam = true;
    for ( final VariableElement element : constructor.getParameters() )
    {
      final ParameterSpec.Builder param =
        ParameterSpec.builder( TypeName.get( element.asType() ), element.getSimpleName().toString(), Modifier.FINAL );
      ProcessorUtil.copyDocumentedAnnotations( element, param );
      builder.addParameter( param.build() );
      parameterNames.add( element.getSimpleName().toString() );
      if ( !firstParam )
      {
        superCall.append( "," );
      }
      firstParam = false;
      superCall.append( "$N" );
    }

    superCall.append( ")" );
    builder.addStatement( superCall.toString(), parameterNames.toArray() );

    builder.addStatement( "this.$N = $T.context()", CONTEXT_FIELD_NAME, AREZ_CLASSNAME );

    final ExecutableElement containerId = descriptor.getContainerId();
    // Synthesize Id if required
    if ( !descriptor.isSingleton() && null == containerId )
    {
      builder.addStatement( "this.$N = $N++", ID_FIELD_NAME, NEXT_ID_FIELD_NAME );
    }

    for ( final ObservableDescriptor observable : descriptor.getObservables() )
    {
      if ( descriptor.isSingleton() )
      {
        builder.addStatement( "this.$N = this.$N.createObservable( this.$N.areNamesEnabled() ? $S : null )",
                              fieldName( observable ),
                              CONTEXT_FIELD_NAME,
                              CONTEXT_FIELD_NAME,
                              getPrefix( descriptor ) + observable.getName() );
      }
      else
      {
        builder.addStatement( "this.$N = this.$N.createObservable( this.$N.areNamesEnabled() ? $N() + $S : null )",
                              fieldName( observable ),
                              CONTEXT_FIELD_NAME,
                              CONTEXT_FIELD_NAME,
                              ID_FIELD_NAME,
                              observable.getName() );
      }
    }
    for ( final ComputedDescriptor computed : descriptor.getComputeds() )
    {
      final ArrayList<Object> parameters = new ArrayList<>();
      final StringBuilder sb = new StringBuilder();
      sb.append( "this.$N = this.$N.createComputedValue( this.$N.areNamesEnabled() ? " );
      parameters.add( FIELD_PREFIX + computed.getName() );
      parameters.add( CONTEXT_FIELD_NAME );
      parameters.add( CONTEXT_FIELD_NAME );
      if ( descriptor.isSingleton() )
      {
        sb.append( "$S" );
        parameters.add( getPrefix( descriptor ) + computed.getName() );
      }
      else
      {
        sb.append( "$N() + $S" );
        parameters.add( ID_FIELD_NAME );
        parameters.add( computed.getName() );
      }
      sb.append( " : null, super::$N, $T::equals, " );
      parameters.add( computed.getComputed().getSimpleName().toString() );
      parameters.add( Objects.class );

      if ( null != computed.getOnActivate() )
      {
        sb.append( "this::$N" );
        parameters.add( computed.getOnActivate().getSimpleName().toString() );
      }
      else
      {
        sb.append( "null" );
      }
      sb.append( ", " );

      if ( null != computed.getOnDeactivate() )
      {
        sb.append( "this::$N" );
        parameters.add( computed.getOnDeactivate().getSimpleName().toString() );
      }
      else
      {
        sb.append( "null" );
      }
      sb.append( ", " );

      if ( null != computed.getOnStale() )
      {
        sb.append( "this::$N" );
        parameters.add( computed.getOnStale().getSimpleName().toString() );
      }
      else
      {
        sb.append( "null" );
      }

      sb.append( " )" );
      builder.addStatement( sb.toString(), parameters.toArray() );
    }

    final ExecutableElement postConstruct = descriptor.getPostConstruct();
    if ( null != postConstruct )
    {
      builder.addStatement( "super.$N()", postConstruct.getSimpleName().toString() );
    }

    return builder.build();
  }

  /**
   * Get prefix specified by container if any.
   */
  @Nonnull
  private String getPrefix( @Nonnull final ContainerDescriptor descriptor )
  {
    return descriptor.getName().isEmpty() ? "" : descriptor.getName() + ".";
  }
}
