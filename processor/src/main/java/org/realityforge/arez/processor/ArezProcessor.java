package org.realityforge.arez.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import static javax.tools.Diagnostic.Kind.*;

/**
 * Annotation processor that analyzes Arez annotated source and generates models from the annotations.
 */
@SuppressWarnings( "Duplicates" )
@AutoService( Processor.class )
@SupportedAnnotationTypes( { "org.realityforge.arez.annotations.*", "javax.annotation.PostConstruct" } )
@SupportedSourceVersion( SourceVersion.RELEASE_8 )
public final class ArezProcessor
  extends AbstractProcessor
{
  /**
   * {@inheritDoc}
   */
  @Override
  public boolean process( final Set<? extends TypeElement> annotations, final RoundEnvironment env )
  {
    final TypeElement annotation =
      processingEnv.getElementUtils().getTypeElement( Constants.COMPONENT_ANNOTATION_CLASSNAME );
    final Set<? extends Element> elements = env.getElementsAnnotatedWith( annotation );
    processElements( elements );
    return false;
  }

  private void processElements( @Nonnull final Set<? extends Element> elements )
  {
    for ( final Element element : elements )
    {
      try
      {
        process( element );
      }
      catch ( final IOException ioe )
      {
        processingEnv.getMessager().printMessage( ERROR, ioe.getMessage(), element );
      }
      catch ( final ArezProcessorException e )
      {
        processingEnv.getMessager().printMessage( ERROR, e.getMessage(), e.getElement() );
      }
      catch ( final Throwable e )
      {
        final StringWriter sw = new StringWriter();
        e.printStackTrace( new PrintWriter( sw ) );
        sw.flush();

        final String message =
          "Unexpected error will running the " + getClass().getName() + " processor. This has " +
          "resulted in a failure to process the code and has left the compiler in an invalid " +
          "state. Please report the failure to the developers so that it can be fixed.\n" +
          " Report the error at: https://github.com/arez/arez/issues\n" +
          "\n\n" +
          sw.toString();
        processingEnv.getMessager().printMessage( ERROR, message, element );
      }
    }
  }

  private void process( @Nonnull final Element element )
    throws IOException, ArezProcessorException
  {
    final PackageElement packageElement = processingEnv.getElementUtils().getPackageOf( element );
    final TypeElement typeElement = (TypeElement) element;
    final ComponentDescriptor descriptor = parse( packageElement, typeElement );
    emitTypeSpec( descriptor.getPackageName(), descriptor.buildType( processingEnv.getTypeUtils() ) );
    if ( descriptor.hasRepository() )
    {
      emitTypeSpec( descriptor.getPackageName(), descriptor.buildRepository( processingEnv.getTypeUtils() ) );
      emitTypeSpec( descriptor.getPackageName(), descriptor.buildRepositoryExtension() );
      if ( descriptor.shouldGenerateDaggerModule() )
      {
        emitTypeSpec( descriptor.getPackageName(), descriptor.buildDaggerModule() );
      }
    }
  }

  @SuppressWarnings( "unchecked" )
  @Nonnull
  private ComponentDescriptor parse( final PackageElement packageElement, final TypeElement typeElement )
    throws ArezProcessorException
  {
    if ( ElementKind.CLASS != typeElement.getKind() )
    {
      throw new ArezProcessorException( "@ArezComponent target must be a class", typeElement );
    }
    else if ( typeElement.getModifiers().contains( Modifier.ABSTRACT ) )
    {
      throw new ArezProcessorException( "@ArezComponent target must not be abstract", typeElement );
    }
    else if ( typeElement.getModifiers().contains( Modifier.FINAL ) )
    {
      throw new ArezProcessorException( "@ArezComponent target must not be final", typeElement );
    }
    else if ( NestingKind.TOP_LEVEL != typeElement.getNestingKind() &&
              !typeElement.getModifiers().contains( Modifier.STATIC ) )
    {
      throw new ArezProcessorException( "@ArezComponent target must not be a non-static nested class", typeElement );
    }
    final AnnotationMirror arezComponent =
      ProcessorUtil.getAnnotationByType( typeElement, Constants.COMPONENT_ANNOTATION_CLASSNAME );
    final String declaredType = getAnnotationParameter( arezComponent, "type" );
    final boolean nameIncludesId = getAnnotationParameter( arezComponent, "nameIncludesId" );
    final boolean allowEmpty = getAnnotationParameter( arezComponent, "allowEmpty" );
    final boolean inject = isInjectionRequired( typeElement );
    final boolean deferSchedule = getAnnotationParameter( arezComponent, "deferSchedule" );

    final String type =
      ProcessorUtil.isSentinelName( declaredType ) ? typeElement.getSimpleName().toString() : declaredType;

    if ( !ProcessorUtil.isJavaIdentifier( type ) )
    {
      throw new ArezProcessorException( "@ArezComponent specified invalid type parameter", typeElement );
    }

    if ( inject && ProcessorUtil.getConstructors( typeElement ).size() > 1 )
    {
      throw new ArezProcessorException( "@ArezComponent specified inject parameter but has more than one constructor",
                                        typeElement );
    }

    final List<ExecutableElement> methods = ProcessorUtil.getMethods( typeElement, processingEnv.getTypeUtils() );
    final boolean generateToString = methods.stream()
      .noneMatch( m -> m.getSimpleName().toString().equals( "toString" ) &&
                       m.getParameters().size() == 0 &&
                       !( m.getEnclosingElement().getSimpleName().toString().equals( "Object" ) &&
                          "java.lang".equals( processingEnv.getElementUtils().
                            getPackageOf( m.getEnclosingElement() ).getQualifiedName().toString() ) ) );

    final ComponentDescriptor descriptor =
      new ComponentDescriptor( processingEnv.getElementUtils(),
                               type,
                               nameIncludesId,
                               allowEmpty,
                               inject,
                               deferSchedule,
                               generateToString,
                               packageElement,
                               typeElement );

    descriptor.analyzeCandidateMethods( methods, processingEnv.getTypeUtils() );
    descriptor.validate();

    for ( final ObservableDescriptor observable : descriptor.getObservables() )
    {
      if ( observable.expectSetter() )
      {
        final TypeMirror returnType = observable.getGetterType().getReturnType();
        final TypeMirror parameterType = observable.getSetterType().getParameterTypes().get( 0 );
        if ( !processingEnv.getTypeUtils().isSameType( parameterType, returnType ) )
        {
          throw new ArezProcessorException( "@Observable property defines a setter and getter with different types." +
                                            " Getter type: " + returnType + " Setter type: " + parameterType + ".",
                                            observable.getGetter() );
        }
      }
    }

    final AnnotationMirror repository =
      ProcessorUtil.findAnnotationByType( typeElement, Constants.REPOSITORY_ANNOTATION_CLASSNAME );
    if ( null != repository )
    {
      final List<TypeElement> extensions =
        ProcessorUtil.getTypeMirrorsAnnotationParameter( processingEnv.getElementUtils(),
                                                         typeElement,
                                                         Constants.REPOSITORY_ANNOTATION_CLASSNAME,
                                                         "extensions" ).stream().
          map( typeMirror -> (TypeElement) processingEnv.getTypeUtils().asElement( typeMirror ) ).
          collect( Collectors.toList() );
      final String name = getAnnotationParameter( repository, "name" );
      final boolean dagger = isRepositoryDaggerRequired( typeElement );
      final boolean repositoryInject = isRepositoryInjectionRequired( typeElement );
      descriptor.configureRepository( name, extensions, dagger, dagger || repositoryInject );
    }

    return descriptor;
  }

  private boolean isInjectionRequired( @Nonnull final TypeElement typeElement )
  {
    final VariableElement injectParameter = (VariableElement)
      ProcessorUtil.getAnnotationValue( processingEnv.getElementUtils(),
                                        typeElement,
                                        Constants.COMPONENT_ANNOTATION_CLASSNAME,
                                        "inject" ).getValue();
    switch ( injectParameter.getSimpleName().toString() )
    {
      case "TRUE":
        return true;
      case "FALSE":
        return false;
      default:
        return ProcessorUtil.getFieldElements( typeElement ).stream().anyMatch( this::hasInjectAnnotation ) ||
               ProcessorUtil.getMethods( typeElement, processingEnv.getTypeUtils() ).
                 stream().anyMatch( this::hasInjectAnnotation );
    }
  }

  private boolean isRepositoryInjectionRequired( @Nonnull final TypeElement typeElement )
  {
    final VariableElement injectParameter = (VariableElement)
      ProcessorUtil.getAnnotationValue( processingEnv.getElementUtils(),
                                        typeElement,
                                        Constants.REPOSITORY_ANNOTATION_CLASSNAME,
                                        "inject" ).getValue();
    switch ( injectParameter.getSimpleName().toString() )
    {
      case "TRUE":
        return true;
      case "FALSE":
        return false;
      default:
        return null != processingEnv.getElementUtils().getTypeElement( Constants.INJECT_ANNOTATION_CLASSNAME );
    }
  }

  private boolean isRepositoryDaggerRequired( @Nonnull final TypeElement typeElement )
  {
    final VariableElement daggerParameter = (VariableElement)
      ProcessorUtil.getAnnotationValue( processingEnv.getElementUtils(),
                                        typeElement,
                                        Constants.REPOSITORY_ANNOTATION_CLASSNAME,
                                        "dagger" ).getValue();
    switch ( daggerParameter.getSimpleName().toString() )
    {
      case "TRUE":
        return true;
      case "FALSE":
        return false;
      default:
        return null != processingEnv.getElementUtils().getTypeElement( Constants.DAGGER_MODULE_CLASSNAME );
    }
  }

  private boolean hasInjectAnnotation( final Element method )
  {
    return null != ProcessorUtil.findAnnotationByType( method, Constants.INJECT_ANNOTATION_CLASSNAME );
  }

  @SuppressWarnings( "unchecked" )
  private <T> T getAnnotationParameter( @Nonnull final AnnotationMirror annotation,
                                        @Nonnull final String parameterName )
  {
    return (T) ProcessorUtil.getAnnotationValue( processingEnv.getElementUtils(),
                                                 annotation,
                                                 parameterName ).getValue();
  }

  private void emitTypeSpec( @Nonnull final String packageName, @Nonnull final TypeSpec typeSpec )
    throws IOException
  {
    JavaFile.builder( packageName, typeSpec ).
      skipJavaLangImports( true ).
      build().
      writeTo( processingEnv.getFiler() );
  }
}
