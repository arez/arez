package arez.processor;

import com.google.auto.common.SuperficialValidation;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
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
@SupportedAnnotationTypes( { "arez.annotations.*" } )
@SupportedSourceVersion( SourceVersion.RELEASE_8 )
@SupportedOptions( "arez.defer.unresolved" )
public final class ArezProcessor
  extends AbstractProcessor
{
  @Nonnull
  private HashSet<TypeElement> _deferred = new HashSet<>();

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean process( final Set<? extends TypeElement> annotations, final RoundEnvironment env )
  {
    final TypeElement annotation =
      processingEnv.getElementUtils().getTypeElement( Constants.COMPONENT_ANNOTATION_CLASSNAME );
    final Set<? extends Element> elements = env.getElementsAnnotatedWith( annotation );

    final Map<String, String> options = processingEnv.getOptions();
    final String deferUnresolvedValue = options.get( "arez.defer.unresolved" );
    final boolean deferUnresolved = null == deferUnresolvedValue || "true".equals( deferUnresolvedValue );

    if ( deferUnresolved )
    {
      final Collection<Element> elementsToProcess = getElementsToProcess( elements );
      processElements( elementsToProcess, env );
      if ( env.getRootElements().isEmpty() && !_deferred.isEmpty() )
      {
        _deferred.forEach( this::processingErrorMessage );
        _deferred.clear();
      }
    }
    else
    {
      processElements( new ArrayList<>( elements ), env );
    }
    return true;
  }

  private void processingErrorMessage( @Nonnull final TypeElement target )
  {
    processingEnv
      .getMessager()
      .printMessage( ERROR,
                     "ArezProcessor unable to process " + target.getQualifiedName() +
                     " because not all of its dependencies could be resolved. Check for " +
                     "compilation errors or a circular dependency with generated code.",
                     target );
  }

  private void processElements( @Nonnull final Collection<Element> elements,
                                @Nonnull final RoundEnvironment env )
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
        final Element errorLocation = e.getElement();
        final Element outerElement = getOuterElement( errorLocation );
        if ( !env.getRootElements().contains( outerElement ) )
        {
          final String location;
          if ( errorLocation instanceof ExecutableElement )
          {
            final ExecutableElement executableElement = (ExecutableElement) errorLocation;
            final TypeElement typeElement = (TypeElement) executableElement.getEnclosingElement();
            location = typeElement.getQualifiedName() + "." + executableElement.getSimpleName();
          }
          else
          {
            assert errorLocation instanceof TypeElement;
            final TypeElement typeElement = (TypeElement) errorLocation;
            location = typeElement.getQualifiedName().toString();
          }

          final StringWriter sw = new StringWriter();
          processingEnv.getElementUtils().printElements( sw, errorLocation );
          sw.flush();

          final String message =
            "An error was generated processing the element " + element.getSimpleName() +
            " but the error was triggered by code not currently being compiled but inherited or " +
            "implemented by the element and may not be highlighted by your tooling or IDE. The " +
            "error occurred at " + location + " and may look like:\n" + sw.toString();

          processingEnv.getMessager().printMessage( ERROR, e.getMessage(), element );
          processingEnv.getMessager().printMessage( ERROR, message );
        }
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

  @Nonnull
  private Collection<Element> getElementsToProcess( @Nonnull final Set<? extends Element> elements )
  {
    final List<TypeElement> deferred = _deferred
      .stream()
      .map( e -> processingEnv.getElementUtils().getTypeElement( e.getQualifiedName() ) )
      .collect( Collectors.toList() );
    _deferred = new HashSet<>();

    final ArrayList<Element> elementsToProcess = new ArrayList<>();
    collectElementsToProcess( elements, elementsToProcess );
    collectElementsToProcess( deferred, elementsToProcess );
    return elementsToProcess;
  }

  private void collectElementsToProcess( @Nonnull final Collection<? extends Element> elements,
                                         @Nonnull final ArrayList<Element> elementsToProcess )
  {
    for ( final Element element : elements )
    {
      if ( SuperficialValidation.validateElement( element ) )
      {
        elementsToProcess.add( element );
      }
      else
      {
        _deferred.add( (TypeElement) element );
      }
    }
  }

  /**
   * Return the outer enclosing element.
   * This is either the top-level class, interface, enum, etc within a package.
   * This helps identify the top level compilation units.
   */
  @Nonnull
  private Element getOuterElement( @Nonnull final Element element )
  {
    Element result = element;
    while ( !( result.getEnclosingElement() instanceof PackageElement ) )
    {
      result = result.getEnclosingElement();
    }
    return result;
  }

  private void process( @Nonnull final Element element )
    throws IOException, ArezProcessorException
  {
    final PackageElement packageElement = processingEnv.getElementUtils().getPackageOf( element );
    final TypeElement typeElement = (TypeElement) element;
    final ComponentDescriptor descriptor = parse( packageElement, typeElement );
    emitTypeSpec( descriptor.getPackageName(), descriptor.buildType( processingEnv.getTypeUtils() ) );
    if ( descriptor.shouldGenerateComponentDaggerModule() )
    {
      emitTypeSpec( descriptor.getPackageName(), descriptor.buildComponentDaggerModule() );
    }
    if ( descriptor.hasRepository() )
    {
      emitTypeSpec( descriptor.getPackageName(), descriptor.buildRepository( processingEnv.getTypeUtils() ) );
    }
  }

  @SuppressWarnings( "unchecked" )
  @Nonnull
  private ComponentDescriptor parse( @Nonnull final PackageElement packageElement,
                                     @Nonnull final TypeElement typeElement )
    throws ArezProcessorException
  {
    if ( ElementKind.CLASS != typeElement.getKind() )
    {
      throw new ArezProcessorException( "@ArezComponent target must be a class", typeElement );
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
    final String declaredType = getAnnotationParameter( arezComponent, "name" );
    final AnnotationValue nameIncludesIdValue =
      ProcessorUtil.findAnnotationValueNoDefaults( arezComponent, "nameIncludesId" );
    final AnnotationMirror singletonAnnotation =
      ProcessorUtil.findAnnotationByType( typeElement, Constants.SINGLETON_ANNOTATION_CLASSNAME );
    final boolean nameIncludesIdDefault = null == singletonAnnotation;
    final boolean nameIncludesId =
      null == nameIncludesIdValue ? nameIncludesIdDefault : (boolean) nameIncludesIdValue.getValue();
    final boolean disposeOnDeactivate = getAnnotationParameter( arezComponent, "disposeOnDeactivate" );
    final boolean observableFlag = isComponentObservableRequired( typeElement, disposeOnDeactivate );
    final boolean disposeTrackableFlag = getAnnotationParameter( arezComponent, "disposeTrackable" );
    final boolean allowConcrete = getAnnotationParameter( arezComponent, "allowConcrete" );
    final boolean allowEmpty = getAnnotationParameter( arezComponent, "allowEmpty" );
    final List<AnnotationMirror> scopeAnnotations =
      typeElement.getAnnotationMirrors().stream().filter( this::isScopeAnnotation ).collect( Collectors.toList() );
    final AnnotationMirror scopeAnnotation = scopeAnnotations.isEmpty() ? null : scopeAnnotations.get( 0 );
    final boolean inject = isInjectionRequired( typeElement, scopeAnnotation );
    final boolean dagger = isDaggerRequired( typeElement, scopeAnnotation );
    final boolean requireEquals = isEqualsRequired( typeElement );
    final boolean deferSchedule = getAnnotationParameter( arezComponent, "deferSchedule" );

    if ( !typeElement.getModifiers().contains( Modifier.ABSTRACT ) && !allowConcrete )
    {
      throw new ArezProcessorException( "@ArezComponent target must be abstract unless the allowConcrete " +
                                        "parameter is set to true", typeElement );
    }

    final String type =
      ProcessorUtil.isSentinelName( declaredType ) ? typeElement.getSimpleName().toString() : declaredType;

    if ( !SourceVersion.isIdentifier( type ) )
    {
      throw new ArezProcessorException( "@ArezComponent target specified an invalid type '" + type + "'. The " +
                                        "type must be a valid java identifier.", typeElement );
    }
    else if ( SourceVersion.isKeyword( type ) )
    {
      throw new ArezProcessorException( "@ArezComponent target specified an invalid type '" + type + "'. The " +
                                        "type must not be a java keyword.", typeElement );
    }

    if ( !scopeAnnotations.isEmpty() && ProcessorUtil.getConstructors( typeElement ).size() > 1 )
    {
      throw new ArezProcessorException( "@ArezComponent target has specified a scope annotation but has more than " +
                                        "one constructor and thus is not a candidate for injection",
                                        typeElement );
    }

    if ( inject && ProcessorUtil.getConstructors( typeElement ).size() > 1 )
    {
      throw new ArezProcessorException( "@ArezComponent specified inject parameter but has more than one constructor",
                                        typeElement );
    }

    if ( scopeAnnotations.size() > 1 )
    {
      final List<String> scopes = scopeAnnotations.stream()
        .map( a -> processingEnv.getTypeUtils().asElement( a.getAnnotationType() ).asType().toString() )
        .collect( Collectors.toList() );
      throw new ArezProcessorException( "@ArezComponent target has specified multiple scope annotations: " + scopes,
                                        typeElement );
    }
    if ( !observableFlag && disposeOnDeactivate )
    {
      throw new ArezProcessorException( "@ArezComponent target has specified observable = DISABLE and " +
                                        "disposeOnDeactivate = true which is not a valid combination", typeElement );
    }

    final List<ExecutableElement> methods =
      ProcessorUtil.getMethods( typeElement, processingEnv.getElementUtils(), processingEnv.getTypeUtils() );
    final boolean generateToString = methods.stream().
      noneMatch( m -> m.getSimpleName().toString().equals( "toString" ) &&
                      m.getParameters().size() == 0 &&
                      !( m.getEnclosingElement().getSimpleName().toString().equals( "Object" ) &&
                         "java.lang".equals( processingEnv.getElementUtils().
                           getPackageOf( m.getEnclosingElement() ).getQualifiedName().toString() ) ) );

    final boolean injectClassesPresent =
      null != processingEnv.getElementUtils().getTypeElement( Constants.INJECT_ANNOTATION_CLASSNAME );

    final ComponentDescriptor descriptor =
      new ComponentDescriptor( processingEnv.getSourceVersion(),
                               processingEnv.getElementUtils(),
                               processingEnv.getTypeUtils(),
                               type,
                               nameIncludesId,
                               allowEmpty,
                               observableFlag,
                               disposeTrackableFlag,
                               disposeOnDeactivate,
                               injectClassesPresent,
                               dagger || inject,
                               dagger,
                               requireEquals,
                               scopeAnnotation,
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
        if ( !processingEnv.getTypeUtils().isSameType( parameterType, returnType ) &&
             !parameterType.toString().equals( returnType.toString() ) )
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
      final String repositoryInjectConfig = getRepositoryInjectConfig( typeElement );
      final String repositoryDaggerConfig = getRepositoryDaggerConfig( typeElement );
      descriptor.configureRepository( name, extensions, repositoryInjectConfig, repositoryDaggerConfig );
    }
    if ( !observableFlag && descriptor.hasRepository() )
    {
      throw new ArezProcessorException( "@ArezComponent target has specified observable = DISABLE and " +
                                        "but is also annotated with the @Repository annotation which requires " +
                                        "that the observable != DISABLE.", typeElement );
    }
    if ( !descriptor.isDisposeTrackable() && descriptor.hasRepository() )
    {
      throw new ArezProcessorException( "@ArezComponent target has specified the disposeTrackable = false " +
                                        "annotation parameter but is also annotated with @Repository that " +
                                        "requires disposeTrackable = true.", typeElement );
    }

    return descriptor;
  }

  private boolean isScopeAnnotation( @Nonnull final AnnotationMirror a )
  {
    final Element element = processingEnv.getTypeUtils().asElement( a.getAnnotationType() );
    return null != ProcessorUtil.findAnnotationByType( element, Constants.SCOPE_ANNOTATION_CLASSNAME );
  }

  private boolean isComponentObservableRequired( @Nonnull final TypeElement typeElement,
                                                 final boolean disposeOnDeactivate )
  {
    final VariableElement variableElement = (VariableElement)
      ProcessorUtil.getAnnotationValue( processingEnv.getElementUtils(),
                                        typeElement,
                                        Constants.COMPONENT_ANNOTATION_CLASSNAME,
                                        "observable" ).getValue();
    switch ( variableElement.getSimpleName().toString() )
    {
      case "ENABLE":
        return true;
      case "DISABLE":
        return false;
      default:
        return disposeOnDeactivate ||
               null != ProcessorUtil.findAnnotationByType( typeElement, Constants.REPOSITORY_ANNOTATION_CLASSNAME );
    }
  }

  private boolean isInjectionRequired( @Nonnull final TypeElement typeElement,
                                       @Nullable final AnnotationMirror scopeAnnotation )
  {
    final VariableElement injectParameter = (VariableElement)
      ProcessorUtil.getAnnotationValue( processingEnv.getElementUtils(),
                                        typeElement,
                                        Constants.COMPONENT_ANNOTATION_CLASSNAME,
                                        "inject" ).getValue();
    switch ( injectParameter.getSimpleName().toString() )
    {
      case "ENABLE":
        return true;
      case "DISABLE":
        return false;
      default:
        return null != scopeAnnotation ||
               ProcessorUtil.getFieldElements( typeElement ).stream().anyMatch( this::hasInjectAnnotation ) ||
               ProcessorUtil.getMethods( typeElement, processingEnv.getElementUtils(), processingEnv.getTypeUtils() ).
                 stream().anyMatch( this::hasInjectAnnotation );
    }
  }

  private boolean isDaggerRequired( @Nonnull final TypeElement typeElement,
                                    @Nullable final AnnotationMirror scopeAnnotation )
  {
    final VariableElement daggerParameter = (VariableElement)
      ProcessorUtil.getAnnotationValue( processingEnv.getElementUtils(),
                                        typeElement,
                                        Constants.COMPONENT_ANNOTATION_CLASSNAME,
                                        "dagger" ).getValue();
    switch ( daggerParameter.getSimpleName().toString() )
    {
      case "ENABLE":
        return true;
      case "DISABLE":
        return false;
      default:
        return null != scopeAnnotation &&
               null != processingEnv.getElementUtils().getTypeElement( Constants.DAGGER_MODULE_CLASSNAME );
    }
  }

  private boolean isEqualsRequired( @Nonnull final TypeElement typeElement )
  {
    final VariableElement injectParameter = (VariableElement)
      ProcessorUtil.getAnnotationValue( processingEnv.getElementUtils(),
                                        typeElement,
                                        Constants.COMPONENT_ANNOTATION_CLASSNAME,
                                        "requireEquals" ).getValue();
    switch ( injectParameter.getSimpleName().toString() )
    {
      case "ENABLE":
        return true;
      case "DISABLE":
        return false;
      default:
        return null != ProcessorUtil.findAnnotationByType( typeElement, Constants.REPOSITORY_ANNOTATION_CLASSNAME );
    }
  }

  @Nonnull
  private String getRepositoryInjectConfig( @Nonnull final TypeElement typeElement )
  {
    final VariableElement injectParameter = (VariableElement)
      ProcessorUtil.getAnnotationValue( processingEnv.getElementUtils(),
                                        typeElement,
                                        Constants.REPOSITORY_ANNOTATION_CLASSNAME,
                                        "inject" ).getValue();
    return injectParameter.getSimpleName().toString();
  }

  @Nonnull
  private String getRepositoryDaggerConfig( @Nonnull final TypeElement typeElement )
  {
    final VariableElement daggerParameter = (VariableElement)
      ProcessorUtil.getAnnotationValue( processingEnv.getElementUtils(),
                                        typeElement,
                                        Constants.REPOSITORY_ANNOTATION_CLASSNAME,
                                        "dagger" ).getValue();
    return daggerParameter.getSimpleName().toString();
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
