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
import com.squareup.javapoet.TypeVariableName;
import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
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
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import org.realityforge.arez.annotations.Container;

/**
 * Annotation processor that analyzes Arez annotated source and generates Observable models.
 */
@SuppressWarnings( "Duplicates" )
@AutoService( Processor.class )
@SupportedAnnotationTypes( { "org.realityforge.arez.annotations.Action",
                             "org.realityforge.arez.annotations.Computed",
                             "org.realityforge.arez.annotations.Container",
                             "org.realityforge.arez.annotations.ContainerId",
                             "org.realityforge.arez.annotations.Observable" } )
@SupportedSourceVersion( SourceVersion.RELEASE_8 )
public final class ArezProcessor
  extends AbstractJavaPoetProcessor
{
  private static final ClassName AREZ_CONTEXT_CLASSNAME = ClassName.get( "org.realityforge.arez", "ArezContext" );
  private static final ClassName OBSERVABLE_CLASSNAME = ClassName.get( "org.realityforge.arez", "Observable" );
  private static final ClassName COMPUTED_VALUE_CLASSNAME = ClassName.get( "org.realityforge.arez", "ComputedValue" );
  private static final String FIELD_PREFIX = "$$arez$$_";
  private static final String CONTEXT_FIELD_NAME = FIELD_PREFIX + "context";
  private static final String NEXT_ID_FIELD_NAME = FIELD_PREFIX + "nextId";
  private static final String ID_FIELD_NAME = FIELD_PREFIX + "id";

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

    final TypeSpec.Builder builder = TypeSpec.classBuilder( "Arez_" + element.getSimpleName() ).
      superclass( TypeName.get( element.asType() ) ).
      addTypeVariables( ProcessorUtil.getTypeArgumentsAsNames( descriptor.asDeclaredType() ) ).
      addModifiers( Modifier.FINAL ).
      addAnnotation( generatedAnnotation );
    ProcessorUtil.copyAccessModifiers( element, builder );

    buildFields( descriptor, builder );

    buildConstructors( descriptor, builder );

    if ( !descriptor.isSingleton() )
    {
      builder.addMethod( buildIdGetter( descriptor ) );
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
    builder.addAnnotation( Override.class );
    final TypeMirror returnType = computed.getReturnType();
    builder.returns( TypeName.get( returnType ) );

    builder.addStatement( "return this.$N.get()", FIELD_PREFIX + descriptor.getName() );

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
    builder.addAnnotation( Override.class );
    final TypeMirror returnType = action.getReturnType();
    builder.returns( TypeName.get( returnType ) );

    final boolean isProcedure = returnType.getKind() == TypeKind.VOID;
    final boolean isSafe = action.getThrownTypes().isEmpty();

    final StringBuilder statement = new StringBuilder();
    final ArrayList<String> parameterNames = new ArrayList<>();

    if ( !isProcedure )
    {
      statement.append( "return " );
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

    if ( isSafe || action.getThrownTypes().stream().anyMatch( t -> t.toString().equals( "java.lang.Exception" ) ) )
    {
      builder.addStatement( statement.toString(), parameterNames.toArray() );
    }
    else
    {
      final CodeBlock.Builder codeBlock = CodeBlock.builder();
      codeBlock.beginControlFlow( "try" );
      codeBlock.addStatement( statement.toString(), parameterNames.toArray() );

      for ( final TypeMirror exception : action.getThrownTypes() )
      {
        codeBlock.nextControlFlow( "catch( final $T e )", exception );
        codeBlock.addStatement( "throw e" );
      }

      codeBlock.nextControlFlow( "catch( final $T e )", RuntimeException.class );
      codeBlock.addStatement( "throw e" );
      codeBlock.nextControlFlow( "catch( final $T e )", Exception.class );
      codeBlock.addStatement( "throw new $T( e )", UndeclaredThrowableException.class );
      codeBlock.endControlFlow();
      builder.addCode( codeBlock.build() );
    }

    return builder.build();
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
    ProcessorUtil.copyExceptions( getter, builder );

    builder.addAnnotation( Override.class );

    final VariableElement element = setter.getParameters().get( 0 );
    final String paramName = element.getSimpleName().toString();
    final TypeName type = TypeName.get( element.asType() );
    final ParameterSpec.Builder param =
      ParameterSpec.builder( type, paramName, Modifier.FINAL );
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
    codeBlock.addStatement( "this.$N.reportObserved()", fieldName( observable ) );
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
      final TypeVariableName fieldType =
        TypeVariableName.get( "org.realityforge.arez.ComputedValue",
                              TypeName.get( computed.getComputed().getReturnType() ).box() );

      final ParameterizedTypeName typeName =
        ParameterizedTypeName.get( COMPUTED_VALUE_CLASSNAME,
                                   TypeName.get( computed.getComputed().getReturnType() ).box() );
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

    final StringBuilder superCall = new StringBuilder();
    superCall.append( "super(" );
    final ArrayList<String> parameterNames = new ArrayList<>();

    // Add the first context class parameter
    {
      final ParameterSpec.Builder param =
        ParameterSpec.builder( AREZ_CONTEXT_CLASSNAME, CONTEXT_FIELD_NAME, Modifier.FINAL ).
          addAnnotation( Nonnull.class );
      builder.addParameter( param.build() );
    }

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

    final ExecutableElement containerId = descriptor.getContainerId();
    // Synthesize Id if required
    if ( !descriptor.isSingleton() && null == containerId )
    {
      builder.addStatement( "this.$N = $N++", ID_FIELD_NAME, NEXT_ID_FIELD_NAME );
    }

    builder.addStatement( "this.$N = $N", CONTEXT_FIELD_NAME, CONTEXT_FIELD_NAME );

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
      if ( descriptor.isSingleton() )
      {
        //context.createComputedValue( "Person.fullName", super::getFullName, Objects::equals )
        builder.addStatement( "this.$N = this.$N.createComputedValue( this.$N.areNamesEnabled() ? $S : null, " +
                              "super::$N, $T::equals )",
                              FIELD_PREFIX + computed.getName(),
                              CONTEXT_FIELD_NAME,
                              CONTEXT_FIELD_NAME,
                              getPrefix( descriptor ) + computed.getName(),
                              computed.getComputed().getSimpleName().toString(),
                              Objects.class );
      }
      else
      {
        builder.addStatement( "this.$N = this.$N.createComputedValue( this.$N.areNamesEnabled() ? $N() + $S : null, " +
                              "super::$N, $T::equals )",
                              FIELD_PREFIX + computed.getName(),
                              CONTEXT_FIELD_NAME,
                              CONTEXT_FIELD_NAME,
                              ID_FIELD_NAME,
                              computed.getName(),
                              computed.getComputed().getSimpleName().toString(),
                              Objects.class );
      }
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
