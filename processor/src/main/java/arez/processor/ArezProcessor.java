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
@AutoService( Processor.class )
@SupportedAnnotationTypes( { "arez.annotations.*" } )
@SupportedSourceVersion( SourceVersion.RELEASE_8 )
@SupportedOptions( "arez.defer.unresolved" )
public final class ArezProcessor
  extends AbstractProcessor
{
  @Nonnull
  private HashSet<TypeElement> _deferred = new HashSet<>();
  private int _invalidTypeCount;
  private RoundEnvironment _env;

  @Override
  public boolean process( final Set<? extends TypeElement> annotations, final RoundEnvironment env )
  {
    final TypeElement annotation =
      processingEnv.getElementUtils().getTypeElement( Constants.COMPONENT_ANNOTATION_CLASSNAME );
    final Set<? extends Element> elements = env.getElementsAnnotatedWith( annotation );

    final Map<String, String> options = processingEnv.getOptions();
    final String deferUnresolvedValue = options.get( "arez.defer.unresolved" );
    final boolean deferUnresolved = null == deferUnresolvedValue || "true".equals( deferUnresolvedValue );

    _env = env;

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
    if ( _env.processingOver() )
    {
      if ( 0 != _invalidTypeCount )
      {
        processingEnv
          .getMessager()
          .printMessage( ERROR, "ArezProcessor failed to process " + _invalidTypeCount +
                                " types. See earlier warnings for further details." );
      }
      _invalidTypeCount = 0;
    }
    _env = null;
    return true;
  }

  private void processingErrorMessage( @Nonnull final TypeElement target )
  {
    reportError( "ArezProcessor unable to process " + target.getQualifiedName() +
                 " because not all of its dependencies could be resolved. Check for " +
                 "compilation errors or a circular dependency with generated code.",
                 target );
  }

  private void reportError( @Nonnull final String message, @Nullable final Element element )
  {
    _invalidTypeCount++;
    if ( _env.errorRaised() || _env.processingOver() )
    {
      processingEnv.getMessager().printMessage( ERROR, message, element );
    }
    else
    {
      processingEnv.getMessager().printMessage( MANDATORY_WARNING, message, element );
    }
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
        reportError( ioe.getMessage(), element );
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
          else if ( errorLocation instanceof VariableElement )
          {
            final VariableElement variableElement = (VariableElement) errorLocation;
            final TypeElement typeElement = (TypeElement) variableElement.getEnclosingElement();
            location = typeElement.getQualifiedName() + "." + variableElement.getSimpleName();
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

          reportError( e.getMessage(), element );
          reportError( message, null );
        }
        reportError( e.getMessage(), e.getElement() );
      }
      catch ( final Throwable e )
      {
        final StringWriter sw = new StringWriter();
        e.printStackTrace( new PrintWriter( sw ) );
        sw.flush();

        final String message =
          "Unexpected error running the " + getClass().getName() + " processor. This has " +
          "resulted in a failure to process the code and has left the compiler in an invalid " +
          "state. Please report the failure to the developers so that it can be fixed.\n" +
          " Report the error at: https://github.com/arez/arez/issues\n" +
          "\n\n" +
          sw.toString();
        reportError( message, element );
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
    if ( descriptor.needsDaggerIntegration() )
    {
      if ( descriptor.needsDaggerComponentExtension() )
      {
        if ( ComponentDescriptor.InjectMode.PROVIDE == descriptor.getInjectMode() )
        {
          emitTypeSpec( descriptor.getPackageName(), Generator.buildProviderDaggerComponentExtension( descriptor ) );
        }
        else
        {
          emitTypeSpec( descriptor.getPackageName(), Generator.buildConsumerDaggerComponentExtension( descriptor ) );
        }
      }
      else if ( descriptor.needsDaggerModule() )
      {
        emitTypeSpec( descriptor.getPackageName(), descriptor.buildComponentDaggerModule() );
      }
    }
    if ( descriptor.hasRepository() )
    {
      emitTypeSpec( descriptor.getPackageName(), descriptor.buildRepository( processingEnv.getTypeUtils() ) );
    }
  }

  @Nonnull
  private ComponentDescriptor parse( @Nonnull final PackageElement packageElement,
                                     @Nonnull final TypeElement typeElement )
    throws ArezProcessorException
  {
    if ( ElementKind.CLASS != typeElement.getKind() && ElementKind.INTERFACE != typeElement.getKind() )
    {
      throw new ArezProcessorException( "@ArezComponent target must be a class or an interface", typeElement );
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
    // Is the component marked as generated
    final boolean generated =
      null != ProcessorUtil.findAnnotationByType( typeElement, Constants.GENERATED_ANNOTATION_CLASSNAME ) ||
      null != ProcessorUtil.findAnnotationByType( typeElement, Constants.JAVA9_GENERATED_ANNOTATION_CLASSNAME );
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
    final boolean observableFlag = isComponentObservableRequired( arezComponent, typeElement, disposeOnDeactivate );
    final boolean disposeNotifierFlag =
      ProcessorUtil.isDisposableTrackableRequired( processingEnv.getElementUtils(), typeElement );
    final boolean allowConcrete = getAnnotationParameter( arezComponent, "allowConcrete" );
    final boolean allowEmpty = getAnnotationParameter( arezComponent, "allowEmpty" );
    final List<AnnotationMirror> scopeAnnotations =
      typeElement.getAnnotationMirrors().stream().filter( this::isScopeAnnotation ).collect( Collectors.toList() );
    final AnnotationMirror scopeAnnotation = scopeAnnotations.isEmpty() ? null : scopeAnnotations.get( 0 );
    final List<VariableElement> fields = ProcessorUtil.getFieldElements( typeElement );
    final boolean fieldInjections = fields.stream().anyMatch( this::hasInjectAnnotation );
    final boolean methodInjections =
      ProcessorUtil.getMethods( typeElement, processingEnv.getElementUtils(), processingEnv.getTypeUtils() )
        .stream()
        .anyMatch( this::hasInjectAnnotation );
    final boolean nonConstructorInjections = fieldInjections || methodInjections;
    final VariableElement daggerParameter = getAnnotationParameter( arezComponent, "dagger" );
    final String daggerMode = daggerParameter.getSimpleName().toString();

    final String injectMode =
      getInjectMode( arezComponent,
                     typeElement,
                     scopeAnnotation,
                     daggerMode,
                     fieldInjections,
                     methodInjections );
    final boolean dagger =
      "ENABLE".equals( daggerMode ) ||
      (
        "AUTODETECT".equals( daggerMode ) &&
        !"NONE".equals( injectMode ) &&
        null != processingEnv.getElementUtils().getTypeElement( Constants.DAGGER_MODULE_CLASSNAME )
      );

    final boolean requireEquals = isEqualsRequired( arezComponent, typeElement );
    final boolean requireVerify = isVerifyRequired( arezComponent, typeElement );
    final boolean deferSchedule = getAnnotationParameter( arezComponent, "deferSchedule" );

    final boolean isClassAbstract = typeElement.getModifiers().contains( Modifier.ABSTRACT );
    if ( !isClassAbstract && !allowConcrete )
    {
      throw new ArezProcessorException( "@ArezComponent target must be abstract unless the allowConcrete " +
                                        "parameter is set to true", typeElement );
    }
    else if ( isClassAbstract && allowConcrete )
    {
      throw new ArezProcessorException( "@ArezComponent target must be concrete if the allowConcrete " +
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

    if ( !"NONE".equals( injectMode ) && ProcessorUtil.getConstructors( typeElement ).size() > 1 )
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

    boolean generatesFactoryToInject = false;
    if ( dagger )
    {
      final ExecutableElement ctor = ProcessorUtil.getConstructors( typeElement ).get( 0 );
      assert null != ctor;
      final List<? extends VariableElement> perInstanceParameters = ctor.getParameters()
        .stream()
        .filter( f -> null != ProcessorUtil.findAnnotationByType( f, Constants.PER_INSTANCE_ANNOTATION_CLASSNAME ) )
        .collect( Collectors.toList() );
      if ( !perInstanceParameters.isEmpty() )
      {
        if ( "PROVIDE".equals( injectMode ) )
        {
          throw new ArezProcessorException( "@ArezComponent target has specified at least one @PerInstance " +
                                            "parameter on the constructor but has set inject parameter to PROVIDE. " +
                                            "The component cannot be provided to other components if the invoker " +
                                            "must supply per-instance parameters so either change the inject " +
                                            "parameter to CONSUME or remove the @PerInstance parameter.",
                                            ctor );
        }
        generatesFactoryToInject = true;
      }
    }

    final List<ExecutableElement> methods =
      ProcessorUtil.getMethods( typeElement, processingEnv.getElementUtils(), processingEnv.getTypeUtils() );
    final boolean generateToString = methods.stream().
      noneMatch( m -> m.getSimpleName().toString().equals( "toString" ) &&
                      m.getParameters().size() == 0 &&
                      !( m.getEnclosingElement().getSimpleName().toString().equals( "Object" ) &&
                         "java.lang".equals( processingEnv.getElementUtils().
                           getPackageOf( m.getEnclosingElement() ).getQualifiedName().toString() ) ) );

    final ComponentDescriptor descriptor =
      new ComponentDescriptor( processingEnv.getSourceVersion(),
                               processingEnv.getElementUtils(),
                               processingEnv.getTypeUtils(),
                               type,
                               nameIncludesId,
                               allowEmpty,
                               generated,
                               observableFlag,
                               disposeNotifierFlag,
                               disposeOnDeactivate,
                               injectMode,
                               dagger,
                               generatesFactoryToInject,
                               nonConstructorInjections,
                               requireEquals,
                               requireVerify,
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
      final String repositoryInjectConfig = getRepositoryInjectMode( repository );
      final String repositoryDaggerConfig = getRepositoryDaggerConfig( repository );
      descriptor.configureRepository( name, extensions, repositoryInjectConfig, repositoryDaggerConfig );
    }
    if ( !observableFlag && descriptor.hasRepository() )
    {
      throw new ArezProcessorException( "@ArezComponent target has specified observable = DISABLE and " +
                                        "but is also annotated with the @Repository annotation which requires " +
                                        "that the observable != DISABLE.", typeElement );
    }
    if ( descriptor.hasRepository() &&
         null != ProcessorUtil.findAnnotationByType( typeElement, Constants.SINGLETON_ANNOTATION_CLASSNAME ) )
    {
      throw new ArezProcessorException( "@ArezComponent target is annotated with both the " +
                                        "@arez.annotations.Repository annotation and the " +
                                        "javax.inject.Singleton annotation which is an invalid " +
                                        "combination.", typeElement );
    }
    if ( !descriptor.isDisposeNotifier() && descriptor.hasRepository() )
    {
      throw new ArezProcessorException( "@ArezComponent target has specified the disposeNotifier = DISABLE " +
                                        "annotation parameter but is also annotated with @Repository that " +
                                        "requires disposeNotifier = ENABLE.", typeElement );
    }

    final boolean idRequired = isIdRequired( descriptor, arezComponent );
    descriptor.setIdRequired( idRequired );
    if ( !idRequired )
    {
      if ( descriptor.hasRepository() )
      {
        throw new ArezProcessorException( "@ArezComponent target has specified the idRequired = DISABLE " +
                                          "annotation parameter but is also annotated with @Repository that " +
                                          "requires idRequired = ENABLE.", typeElement );
      }
      if ( descriptor.hasComponentIdMethod() )
      {
        throw new ArezProcessorException( "@ArezComponent target has specified the idRequired = DISABLE " +
                                          "annotation parameter but also has annotated a method with @ComponentId " +
                                          "that requires idRequired = ENABLE.", typeElement );
      }
      if ( descriptor.hasComponentIdRefMethod() )
      {
        throw new ArezProcessorException( "@ArezComponent target has specified the idRequired = DISABLE " +
                                          "annotation parameter but also has annotated a method with @ComponentIdRef " +
                                          "that requires idRequired = ENABLE.", typeElement );
      }
    }

    warnOnUnmanagedComponentReferences( descriptor, fields );

    return descriptor;
  }

  private void warnOnUnmanagedComponentReferences( @Nonnull final ComponentDescriptor descriptor,
                                                   @Nonnull final List<VariableElement> fields )
  {
    final TypeElement disposeNotifier =
      processingEnv.getElementUtils().getTypeElement( Constants.DISPOSE_NOTIFIER_CLASSNAME );
    assert null != disposeNotifier;

    for ( final VariableElement field : fields )
    {
      if ( !field.getModifiers().contains( Modifier.STATIC ) && SuperficialValidation.validateElement( field ) )
      {
        final boolean isDisposeNotifier =
          processingEnv.getTypeUtils().isAssignable( field.asType(), disposeNotifier.asType() );
        final boolean isTypeAnnotatedByComponentAnnotation =
          !isDisposeNotifier && isTypeAnnotatedByComponentAnnotation( field );
        final boolean isTypeAnnotatedActAsComponent =
          !isDisposeNotifier &&
          !isTypeAnnotatedByComponentAnnotation &&
          isTypeAnnotatedByActAsComponentAnnotation( field );
        if ( isDisposeNotifier || isTypeAnnotatedByComponentAnnotation || isTypeAnnotatedActAsComponent )
        {
          if ( !descriptor.isDependencyDefined( field ) &&
               !descriptor.isCascadeDisposeDefined( field ) &&
               !isUnmanagedComponentReferenceSuppressed( field ) &&
               ( isDisposeNotifier || isTypeAnnotatedActAsComponent || verifyReferencesToComponent( field ) ) )
          {
            final String label =
              isDisposeNotifier ? "an implementation of DisposeNotifier" :
              isTypeAnnotatedByComponentAnnotation ? "an Arez component" :
              "annotated with @ActAsComponent";
            final String message =
              "Field named '" + field.getSimpleName().toString() + "' has a type that is " + label +
              " but is not annotated with @" + Constants.CASCADE_DISPOSE_ANNOTATION_CLASSNAME + " or " +
              "@" + Constants.COMPONENT_DEPENDENCY_ANNOTATION_CLASSNAME + ". This scenario can cause Please " +
              "annotate the field as appropriate or suppress the warning by annotating the field with " +
              "@SuppressWarnings( \"" + Constants.UNMANAGED_COMPONENT_REFERENCE_SUPPRESSION + "\" )";
            processingEnv.getMessager().printMessage( WARNING, message, field );
          }
        }
      }
    }
  }

  private boolean verifyReferencesToComponent( @Nonnull final VariableElement field )
  {
    final Element element = processingEnv.getTypeUtils().asElement( field.asType() );
    assert null != element && SuperficialValidation.validateElement( element );

    final VariableElement verifyReferencesToComponent = (VariableElement)
      ProcessorUtil.getAnnotationValue( processingEnv.getElementUtils(),
                                        element,
                                        Constants.COMPONENT_ANNOTATION_CLASSNAME,
                                        "verifyReferencesToComponent" ).getValue();
    switch ( verifyReferencesToComponent.getSimpleName().toString() )
    {
      case "ENABLE":
        return true;
      case "DISABLE":
        return false;
      default:
        return ProcessorUtil.isDisposableTrackableRequired( processingEnv.getElementUtils(), element );
    }
  }

  private boolean isUnmanagedComponentReferenceSuppressed( @Nonnull final Element element )
  {
    return isWarningSuppressed( element, Constants.UNMANAGED_COMPONENT_REFERENCE_SUPPRESSION );
  }

  private boolean isWarningSuppressed( @Nonnull final Element element, @Nonnull final String warning )
  {
    final SuppressWarnings annotation = element.getAnnotation( SuppressWarnings.class );
    if ( null != annotation )
    {
      for ( final String suppression : annotation.value() )
      {
        if ( warning.equals( suppression ) )
        {
          return true;
        }
      }
    }
    final Element enclosingElement = element.getEnclosingElement();
    return null != enclosingElement && isWarningSuppressed( enclosingElement, warning );
  }

  private boolean isTypeAnnotatedByActAsComponentAnnotation( @Nonnull final VariableElement field )
  {
    final Element element = processingEnv.getTypeUtils().asElement( field.asType() );
    return null != element &&
           SuperficialValidation.validateElement( element ) &&
           null != ProcessorUtil.findAnnotationByType( element, Constants.ACT_AS_COMPONENT_ANNOTATION_CLASSNAME );
  }

  private boolean isTypeAnnotatedByComponentAnnotation( @Nonnull final VariableElement field )
  {
    final Element element = processingEnv.getTypeUtils().asElement( field.asType() );
    return null != element &&
           SuperficialValidation.validateElement( element ) &&
           null != ProcessorUtil.findAnnotationByType( element, Constants.COMPONENT_ANNOTATION_CLASSNAME );
  }

  private boolean isScopeAnnotation( @Nonnull final AnnotationMirror a )
  {
    final Element element = processingEnv.getTypeUtils().asElement( a.getAnnotationType() );
    return null != ProcessorUtil.findAnnotationByType( element, Constants.SCOPE_ANNOTATION_CLASSNAME );
  }

  private boolean isComponentObservableRequired( @Nonnull final AnnotationMirror arezComponent,
                                                 @Nonnull final TypeElement typeElement,
                                                 final boolean disposeOnDeactivate )
  {
    final VariableElement variableElement = getAnnotationParameter( arezComponent, "observable" );
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

  @Nonnull
  private String getInjectMode( @Nonnull final AnnotationMirror arezComponent,
                                @Nonnull final TypeElement typeElement,
                                @Nullable final AnnotationMirror scopeAnnotation,
                                final String daggerMode,
                                final boolean fieldInjections,
                                final boolean methodInjections )
  {
    final VariableElement injectParameter = getAnnotationParameter( arezComponent, "inject" );
    final String mode = injectParameter.getSimpleName().toString();
    if ( "AUTODETECT".equals( mode ) )
    {
      final boolean shouldInject =
        daggerMode.equals( "ENABLE" ) || null != scopeAnnotation || fieldInjections || methodInjections;
      return shouldInject ? "PROVIDE" : "NONE";
    }
    else if ( "NONE".equals( mode ) )
    {
      if ( daggerMode.equals( "ENABLE" ) )
      {
        throw new ArezProcessorException( "@ArezComponent target has a dagger parameter that resolved to ENABLE " +
                                          "but the inject parameter is set to NONE and this is not a valid " +
                                          "combination of parameters.", typeElement );
      }
      if ( fieldInjections )
      {
        throw new ArezProcessorException( "@ArezComponent target has fields annotated with the javax.inject.Inject " +
                                          "annotation but the inject parameter is set to NONE and this is not a " +
                                          "valid scenario. Remove the @Inject annotation(s) or change the inject " +
                                          "parameter to a value other than NONE.", typeElement );
      }
      if ( methodInjections )
      {
        throw new ArezProcessorException( "@ArezComponent target has methods annotated with the javax.inject.Inject " +
                                          "annotation but the inject parameter is set to NONE and this is not a " +
                                          "valid scenario. Remove the @Inject annotation(s) or change the inject " +
                                          "parameter to a value other than NONE.", typeElement );
      }
      if ( null != scopeAnnotation )
      {
        throw new ArezProcessorException( "@ArezComponent target is annotated with scope annotation " +
                                          scopeAnnotation + " but the inject parameter is set to NONE and this " +
                                          "is not a valid scenario. Remove the scope annotation or change the " +
                                          "inject parameter to a value other than NONE.", typeElement );
      }
      return mode;
    }
    else
    {
      return mode;
    }
  }

  private boolean isVerifyRequired( @Nonnull final AnnotationMirror arezComponent,
                                    @Nonnull final TypeElement typeElement )
  {
    final VariableElement daggerParameter = getAnnotationParameter( arezComponent, "verify" );
    switch ( daggerParameter.getSimpleName().toString() )
    {
      case "ENABLE":
        return true;
      case "DISABLE":
        return false;
      default:
        return ProcessorUtil.getMethods( typeElement, processingEnv.getElementUtils(), processingEnv.getTypeUtils() ).
          stream().anyMatch( this::hasReferenceAnnotations );
    }
  }

  private boolean hasReferenceAnnotations( @Nonnull final Element method )
  {
    return null != ProcessorUtil.findAnnotationByType( method, Constants.REFERENCE_ANNOTATION_CLASSNAME ) ||
           null != ProcessorUtil.findAnnotationByType( method, Constants.REFERENCE_ID_ANNOTATION_CLASSNAME ) ||
           null != ProcessorUtil.findAnnotationByType( method, Constants.INVERSE_ANNOTATION_CLASSNAME );
  }

  private boolean isEqualsRequired( @Nonnull final AnnotationMirror arezComponent,
                                    @Nonnull final TypeElement typeElement )
  {
    final VariableElement injectParameter = getAnnotationParameter( arezComponent, "requireEquals" );
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

  private boolean isIdRequired( @Nonnull final ComponentDescriptor descriptor,
                                @Nonnull final AnnotationMirror arezComponent )
  {
    final VariableElement injectParameter = getAnnotationParameter( arezComponent, "requireId" );
    switch ( injectParameter.getSimpleName().toString() )
    {
      case "ENABLE":
        return true;
      case "DISABLE":
        return false;
      default:
        return descriptor.hasRepository() ||
               descriptor.hasComponentIdMethod() ||
               descriptor.hasComponentIdRefMethod() ||
               descriptor.hasInverses();
    }
  }

  @Nonnull
  private String getRepositoryInjectMode( @Nonnull final AnnotationMirror repository )
  {
    final VariableElement injectParameter = getAnnotationParameter( repository, "inject" );
    return injectParameter.getSimpleName().toString();
  }

  @Nonnull
  private String getRepositoryDaggerConfig( @Nonnull final AnnotationMirror repository )
  {
    final VariableElement daggerParameter = getAnnotationParameter( repository, "dagger" );
    return daggerParameter.getSimpleName().toString();
  }

  private boolean hasInjectAnnotation( final Element method )
  {
    return null != ProcessorUtil.findAnnotationByType( method, Constants.INJECT_ANNOTATION_CLASSNAME );
  }

  @Nonnull
  private <T> T getAnnotationParameter( @Nonnull final AnnotationMirror annotation,
                                        @Nonnull final String parameterName )
  {
    return ProcessorUtil.getAnnotationValue( processingEnv.getElementUtils(), annotation, parameterName );
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
