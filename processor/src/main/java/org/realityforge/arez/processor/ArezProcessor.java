package org.realityforge.arez.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.io.IOException;
import java.util.ArrayList;
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
import org.realityforge.arez.annotations.Container;

/**
 * Annotation processor that analyzes Arez annotated source and generates Observable models.
 */
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
  private static final String FIELD_PREFIX = "$$arez$$_";
  private static final String CONTEXT_FIELD_NAME = FIELD_PREFIX + "context";

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
    final ContainerDescriptor descriptor =
      ContainerDescriptorParser.parse( element, processingEnv.getElementUtils(), processingEnv.getTypeUtils() );
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

    for ( final ObservableDescriptor observable : descriptor.getObservables() )
    {
      builder.addMethod( buildObservableGetter( descriptor, observable ) );
    }

    return builder.build();
  }

  /**
   * Generate the getter that reports that ensures that the access is reported as Observable.
   */
  @Nonnull
  private MethodSpec buildObservableGetter( @Nonnull final ContainerDescriptor descriptor,
                                            @Nonnull final ObservableDescriptor observable )
    throws ArezProcessorException
  {
    final ExecutableElement getter = observable.getGetter();
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( getter.getSimpleName().toString() );
    ProcessorUtil.copyAccessModifiers( getter, builder );

    builder.addAnnotation( Override.class );
    builder.returns( TypeName.get( getter.getReturnType() ) );

    final StringBuilder superCall = new StringBuilder();
    superCall.append( "return super." );
    superCall.append( getter.getSimpleName() );
    superCall.append( "(" );

    final ArrayList<String> parameterNames = new ArrayList<>();

    boolean firstParam = true;
    for ( final VariableElement element : getter.getParameters() )
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
    builder.addStatement( "this.$N.reportObserved()", fieldName( observable ) );
    builder.addStatement( superCall.toString(), parameterNames.toArray() );

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
    // Create the field that contains the context variable if it is needed
    if ( descriptor.shouldStoreContext() )
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
    if ( descriptor.shouldStoreContext() )
    {
      builder.addStatement( "this.$N = $N", CONTEXT_FIELD_NAME, CONTEXT_FIELD_NAME );
    }

    final String prefix = descriptor.getName().isEmpty() ? "" : descriptor.getName() + ".";
    for ( final ObservableDescriptor observable : descriptor.getObservables() )
    {
      builder.addStatement( "this.$N = $N.createObservable( $S )",
                            fieldName( observable ),
                            CONTEXT_FIELD_NAME,
                            prefix + observable.getName() );
    }

    return builder.build();
  }
}
