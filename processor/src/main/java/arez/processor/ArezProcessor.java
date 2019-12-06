package arez.processor;

import com.google.auto.common.SuperficialValidation;
import com.squareup.javapoet.TypeSpec;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import static javax.tools.Diagnostic.Kind.*;

/**
 * Annotation processor that analyzes Arez annotated source and generates models from the annotations.
 */
@SupportedAnnotationTypes( Constants.COMPONENT_ANNOTATION_CLASSNAME )
@SupportedSourceVersion( SourceVersion.RELEASE_8 )
@SupportedOptions( { "arez.defer.unresolved", "arez.defer.errors" } )
public final class ArezProcessor
  extends AbstractStandardProcessor
{
  @Nonnull
  @Override
  protected String getRootAnnotationClassname()
  {
    return Constants.COMPONENT_ANNOTATION_CLASSNAME;
  }

  @Override
  @Nonnull
  protected final String getIssueTrackerURL()
  {
    return "https://github.com/arez/arez/issues";
  }

  @Nonnull
  @Override
  protected String getOptionPrefix()
  {
    return "arez";
  }

  @Override
  protected void process( @Nonnull final TypeElement element )
    throws IOException, ProcessorException
  {
    final ComponentDescriptor descriptor = parse( element );
    final String packageName = descriptor.getPackageName();
    emitTypeSpec( packageName, descriptor.buildType( processingEnv ) );
    if ( descriptor.needsDaggerIntegration() )
    {
      if ( descriptor.needsDaggerComponentExtension() )
      {
        assert ComponentDescriptor.InjectMode.CONSUME == descriptor.getInjectMode();
        emitTypeSpec( packageName, Generator.buildConsumerDaggerComponentExtension( processingEnv, descriptor ) );
      }
      else if ( descriptor.needsDaggerModule() )
      {
        emitTypeSpec( packageName, descriptor.buildComponentDaggerModule( processingEnv ) );
      }
    }
    if ( descriptor.hasRepository() )
    {
      emitTypeSpec( packageName, descriptor.buildRepository( processingEnv ) );
    }
  }

  @Nonnull
  private ComponentDescriptor parse( @Nonnull final TypeElement typeElement )
    throws ProcessorException
  {
    if ( ElementKind.CLASS != typeElement.getKind() && ElementKind.INTERFACE != typeElement.getKind() )
    {
      throw new ProcessorException( "@ArezComponent target must be a class or an interface", typeElement );
    }
    else if ( typeElement.getModifiers().contains( Modifier.FINAL ) )
    {
      throw new ProcessorException( "@ArezComponent target must not be final", typeElement );
    }
    else if ( NestingKind.TOP_LEVEL != typeElement.getNestingKind() &&
              !typeElement.getModifiers().contains( Modifier.STATIC ) )
    {
      throw new ProcessorException( "@ArezComponent target must not be a non-static nested class", typeElement );
    }
    final AnnotationMirror arezComponent =
      AnnotationsUtil.getAnnotationByType( typeElement, Constants.COMPONENT_ANNOTATION_CLASSNAME );
    final String declaredType = getAnnotationParameter( arezComponent, "name" );
    final boolean disposeOnDeactivate = getAnnotationParameter( arezComponent, "disposeOnDeactivate" );
    final boolean observableFlag = isComponentObservableRequired( arezComponent, typeElement, disposeOnDeactivate );
    final boolean disposeNotifierFlag = ProcessorUtil.isDisposableTrackableRequired( typeElement );
    final boolean allowEmpty = getAnnotationParameter( arezComponent, "allowEmpty" );
    final List<AnnotationMirror> scopeAnnotations =
      typeElement.getAnnotationMirrors().stream().filter( this::isScopeAnnotation ).collect( Collectors.toList() );
    final AnnotationMirror scopeAnnotation = scopeAnnotations.isEmpty() ? null : scopeAnnotations.get( 0 );
    final List<VariableElement> fields = ProcessorUtil.getFieldElements( typeElement );
    ensureNoFieldInjections( fields );
    ensureNoMethodInjections( typeElement );
    final VariableElement daggerParameter = getAnnotationParameter( arezComponent, "dagger" );
    final String daggerMode = daggerParameter.getSimpleName().toString();

    final String injectMode = getInjectMode( arezComponent, typeElement, scopeAnnotation, daggerMode );
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

    if ( !typeElement.getModifiers().contains( Modifier.ABSTRACT ) )
    {
      throw new ProcessorException( "@ArezComponent target must be abstract", typeElement );
    }

    final String type =
      Constants.SENTINEL.equals( declaredType ) ? typeElement.getSimpleName().toString() : declaredType;

    if ( !SourceVersion.isIdentifier( type ) )
    {
      throw new ProcessorException( "@ArezComponent target specified an invalid type '" + type + "'. The " +
                                    "type must be a valid java identifier.", typeElement );
    }
    else if ( SourceVersion.isKeyword( type ) )
    {
      throw new ProcessorException( "@ArezComponent target specified an invalid type '" + type + "'. The " +
                                    "type must not be a java keyword.", typeElement );
    }

    if ( !scopeAnnotations.isEmpty() && ProcessorUtil.getConstructors( typeElement ).size() > 1 )
    {
      throw new ProcessorException( "@ArezComponent target has specified a scope annotation but has more than " +
                                    "one constructor and thus is not a candidate for injection",
                                    typeElement );
    }

    if ( !"NONE".equals( injectMode ) && ProcessorUtil.getConstructors( typeElement ).size() > 1 )
    {
      throw new ProcessorException( "@ArezComponent specified inject parameter but has more than one constructor",
                                    typeElement );
    }

    if ( scopeAnnotations.size() > 1 )
    {
      final List<String> scopes = scopeAnnotations.stream()
        .map( a -> processingEnv.getTypeUtils().asElement( a.getAnnotationType() ).asType().toString() )
        .collect( Collectors.toList() );
      throw new ProcessorException( "@ArezComponent target has specified multiple scope annotations: " + scopes,
                                    typeElement );
    }
    if ( !observableFlag && disposeOnDeactivate )
    {
      throw new ProcessorException( "@ArezComponent target has specified observable = DISABLE and " +
                                    "disposeOnDeactivate = true which is not a valid combination", typeElement );
    }

    boolean generatesFactoryToInject = false;
    if ( dagger )
    {
      final ExecutableElement ctor = ProcessorUtil.getConstructors( typeElement ).get( 0 );
      assert null != ctor;
      final List<? extends VariableElement> perInstanceParameters = ctor.getParameters()
        .stream()
        .filter( f -> AnnotationsUtil.hasAnnotationOfType( f, Constants.PER_INSTANCE_ANNOTATION_CLASSNAME ) )
        .collect( Collectors.toList() );
      if ( !perInstanceParameters.isEmpty() )
      {
        if ( "PROVIDE".equals( injectMode ) )
        {
          throw new ProcessorException( "@ArezComponent target has specified at least one @PerInstance " +
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

    final String priority = getDefaultPriority( arezComponent );
    final Priority defaultPriority =
      null == priority ? null : "DEFAULT".equals( priority ) ? Priority.NORMAL : Priority.valueOf( priority );

    final ComponentDescriptor descriptor =
      new ComponentDescriptor( processingEnv,
                               type,
                               allowEmpty,
                               defaultPriority,
                               observableFlag,
                               disposeNotifierFlag,
                               disposeOnDeactivate,
                               injectMode,
                               dagger,
                               generatesFactoryToInject,
                               requireEquals,
                               requireVerify,
                               scopeAnnotation,
                               deferSchedule,
                               generateToString,
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
          throw new ProcessorException( "@Observable property defines a setter and getter with different types." +
                                        " Getter type: " + returnType + " Setter type: " + parameterType + ".",
                                        observable.getGetter() );
        }
      }
    }

    final AnnotationMirror repository =
      AnnotationsUtil.findAnnotationByType( typeElement, Constants.REPOSITORY_ANNOTATION_CLASSNAME );
    if ( null != repository )
    {
      final List<TypeElement> extensions =
        AnnotationsUtil.getTypeMirrorsAnnotationParameter( typeElement,
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
      throw new ProcessorException( "@ArezComponent target has specified observable = DISABLE and " +
                                    "but is also annotated with the @Repository annotation which requires " +
                                    "that the observable != DISABLE.", typeElement );
    }
    if ( descriptor.hasRepository() &&
         AnnotationsUtil.hasAnnotationOfType( typeElement, Constants.SINGLETON_ANNOTATION_CLASSNAME ) )
    {
      throw new ProcessorException( "@ArezComponent target is annotated with both the " +
                                    "@arez.annotations.Repository annotation and the " +
                                    "javax.inject.Singleton annotation which is an invalid " +
                                    "combination.", typeElement );
    }
    if ( !descriptor.isDisposeNotifier() && descriptor.hasRepository() )
    {
      throw new ProcessorException( "@ArezComponent target has specified the disposeNotifier = DISABLE " +
                                    "annotation parameter but is also annotated with @Repository that " +
                                    "requires disposeNotifier = ENABLE.", typeElement );
    }

    final boolean idRequired = isIdRequired( descriptor, arezComponent );
    descriptor.setIdRequired( idRequired );
    if ( !idRequired )
    {
      if ( descriptor.hasRepository() )
      {
        throw new ProcessorException( "@ArezComponent target has specified the idRequired = DISABLE " +
                                      "annotation parameter but is also annotated with @Repository that " +
                                      "requires idRequired = ENABLE.", typeElement );
      }
      if ( descriptor.hasComponentIdMethod() )
      {
        throw new ProcessorException( "@ArezComponent target has specified the idRequired = DISABLE " +
                                      "annotation parameter but also has annotated a method with @ComponentId " +
                                      "that requires idRequired = ENABLE.", typeElement );
      }
      if ( !descriptor.getComponentIdRefs().isEmpty() )
      {
        throw new ProcessorException( "@ArezComponent target has specified the idRequired = DISABLE " +
                                      "annotation parameter but also has annotated a method with @ComponentIdRef " +
                                      "that requires idRequired = ENABLE.", typeElement );
      }
    }

    warnOnUnmanagedComponentReferences( descriptor, fields );

    return descriptor;
  }

  private void ensureNoFieldInjections( @Nonnull final List<VariableElement> fields )
  {
    for ( final VariableElement field : fields )
    {
      if ( hasInjectAnnotation( field ) )
      {
        throw new ProcessorException( "@Inject is not supported on fields in an Arez component. " +
                                      "Use constructor injection instead.", field );
      }
    }
  }

  private void ensureNoMethodInjections( @Nonnull final TypeElement typeElement )
  {
    final List<ExecutableElement> methods =
      ProcessorUtil.getMethods( typeElement, processingEnv.getElementUtils(), processingEnv.getTypeUtils() );
    for ( final ExecutableElement method : methods )
    {
      if ( hasInjectAnnotation( method ) )
      {
        throw new ProcessorException( "@Inject is not supported on methods in an Arez component. " +
                                      "Use constructor injection instead.", method );
      }
    }
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
               ( isDisposeNotifier || isTypeAnnotatedActAsComponent || verifyReferencesToComponent( field ) ) &&
               isUnmanagedComponentReferenceNotSuppressed( field ) )
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
              "@SuppressWarnings( \"" + Constants.WARNING_UNMANAGED_COMPONENT_REFERENCE + "\" ) or " +
              "@SuppressArezWarnings( \"" + Constants.WARNING_UNMANAGED_COMPONENT_REFERENCE + "\" )";
            processingEnv.getMessager().printMessage( WARNING, message, field );
          }
        }
      }
    }

    for ( final ObservableDescriptor observable : descriptor.getObservables() )
    {
      if ( observable.isAbstract() )
      {
        final ExecutableElement getter = observable.getGetter();
        if ( SuperficialValidation.validateElement( getter ) )
        {
          final TypeMirror returnType = getter.getReturnType();
          final Element returnElement = processingEnv.getTypeUtils().asElement( returnType );
          final boolean isDisposeNotifier =
            processingEnv.getTypeUtils().isAssignable( returnType, disposeNotifier.asType() );
          final boolean isTypeAnnotatedByComponentAnnotation =
            !isDisposeNotifier && isElementAnnotatedBy( returnElement, Constants.COMPONENT_ANNOTATION_CLASSNAME );
          final boolean isTypeAnnotatedActAsComponent =
            !isDisposeNotifier &&
            !isTypeAnnotatedByComponentAnnotation &&
            isElementAnnotatedBy( returnElement, Constants.ACT_AS_COMPONENT_ANNOTATION_CLASSNAME );
          if ( isDisposeNotifier || isTypeAnnotatedByComponentAnnotation || isTypeAnnotatedActAsComponent )
          {
            if ( !descriptor.isDependencyDefined( getter ) &&
                 !descriptor.isCascadeDisposeDefined( getter ) &&
                 ( isDisposeNotifier ||
                   isTypeAnnotatedActAsComponent ||
                   verifyReferencesToComponent( returnElement ) ) &&
                 isUnmanagedComponentReferenceNotSuppressed( getter ) &&
                 ( observable.hasSetter() && isUnmanagedComponentReferenceNotSuppressed( observable.getSetter() ) ) )
            {
              final String label =
                isDisposeNotifier ? "an implementation of DisposeNotifier" :
                isTypeAnnotatedByComponentAnnotation ? "an Arez component" :
                "annotated with @ActAsComponent";
              final String message =
                "Method named '" + getter.getSimpleName().toString() + "' has a return type that is " + label +
                " but is not annotated with @" + Constants.CASCADE_DISPOSE_ANNOTATION_CLASSNAME + " or " +
                "@" + Constants.COMPONENT_DEPENDENCY_ANNOTATION_CLASSNAME + ". This scenario can cause errors. " +
                "Please annotate the method as appropriate or suppress the warning by annotating the method with " +
                "@SuppressWarnings( \"" + Constants.WARNING_UNMANAGED_COMPONENT_REFERENCE + "\" ) or " +
                "@SuppressArezWarnings( \"" + Constants.WARNING_UNMANAGED_COMPONENT_REFERENCE + "\" )";
              processingEnv.getMessager().printMessage( WARNING, message, getter );
            }
          }
        }
      }
    }
  }

  private boolean verifyReferencesToComponent( @Nonnull final VariableElement field )
  {
    return verifyReferencesToComponent( processingEnv.getTypeUtils().asElement( field.asType() ) );
  }

  private boolean verifyReferencesToComponent( @Nonnull final Element element )
  {
    assert SuperficialValidation.validateElement( element );

    final VariableElement verifyReferencesToComponent = (VariableElement)
      AnnotationsUtil.getAnnotationValue( element,
                                          Constants.COMPONENT_ANNOTATION_CLASSNAME,
                                          "verifyReferencesToComponent" ).getValue();
    switch ( verifyReferencesToComponent.getSimpleName().toString() )
    {
      case "ENABLE":
        return true;
      case "DISABLE":
        return false;
      default:
        return ProcessorUtil.isDisposableTrackableRequired( element );
    }
  }

  private boolean isUnmanagedComponentReferenceNotSuppressed( @Nonnull final Element element )
  {
    return !ProcessorUtil.isWarningSuppressed( element,
                                               Constants.WARNING_UNMANAGED_COMPONENT_REFERENCE,
                                               Constants.SUPPRESS_AREZ_WARNINGS_ANNOTATION_CLASSNAME );
  }

  private boolean isTypeAnnotatedByActAsComponentAnnotation( @Nonnull final VariableElement field )
  {
    final Element element = processingEnv.getTypeUtils().asElement( field.asType() );
    return isElementAnnotatedBy( element, Constants.ACT_AS_COMPONENT_ANNOTATION_CLASSNAME );
  }

  private boolean isTypeAnnotatedByComponentAnnotation( @Nonnull final VariableElement field )
  {
    final Element element = processingEnv.getTypeUtils().asElement( field.asType() );
    return isElementAnnotatedBy( element, Constants.COMPONENT_ANNOTATION_CLASSNAME );
  }

  private boolean isElementAnnotatedBy( @Nullable final Element element, @Nonnull final String annotation )
  {
    return null != element &&
           SuperficialValidation.validateElement( element ) &&
           AnnotationsUtil.hasAnnotationOfType( element, annotation );
  }

  private boolean isScopeAnnotation( @Nonnull final AnnotationMirror a )
  {
    final Element element = processingEnv.getTypeUtils().asElement( a.getAnnotationType() );
    return AnnotationsUtil.hasAnnotationOfType( element, Constants.SCOPE_ANNOTATION_CLASSNAME );
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
               AnnotationsUtil.hasAnnotationOfType( typeElement, Constants.REPOSITORY_ANNOTATION_CLASSNAME );
    }
  }

  @Nonnull
  private String getInjectMode( @Nonnull final AnnotationMirror arezComponent,
                                @Nonnull final TypeElement typeElement,
                                @Nullable final AnnotationMirror scopeAnnotation,
                                final String daggerMode )
  {
    final VariableElement injectParameter = getAnnotationParameter( arezComponent, "inject" );
    final String mode = injectParameter.getSimpleName().toString();
    if ( "AUTODETECT".equals( mode ) )
    {
      final boolean shouldInject = daggerMode.equals( "ENABLE" ) || null != scopeAnnotation;
      return shouldInject ? "PROVIDE" : "NONE";
    }
    else if ( "NONE".equals( mode ) )
    {
      if ( daggerMode.equals( "ENABLE" ) )
      {
        throw new ProcessorException( "@ArezComponent target has a dagger parameter that resolved to ENABLE " +
                                      "but the inject parameter is set to NONE and this is not a valid " +
                                      "combination of parameters.", typeElement );
      }
      if ( null != scopeAnnotation )
      {
        throw new ProcessorException( "@ArezComponent target is annotated with scope annotation " +
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
    return AnnotationsUtil.hasAnnotationOfType( method, Constants.REFERENCE_ANNOTATION_CLASSNAME ) ||
           AnnotationsUtil.hasAnnotationOfType( method, Constants.REFERENCE_ID_ANNOTATION_CLASSNAME ) ||
           AnnotationsUtil.hasAnnotationOfType( method, Constants.INVERSE_ANNOTATION_CLASSNAME );
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
        return AnnotationsUtil.hasAnnotationOfType( typeElement, Constants.REPOSITORY_ANNOTATION_CLASSNAME );
    }
  }

  @Nullable
  private String getDefaultPriority( @Nonnull final AnnotationMirror arezComponent )
  {
    final AnnotationValue value =
      AnnotationsUtil.findAnnotationValueNoDefaults( arezComponent, "defaultPriority" );
    return null == value ? null : ( (VariableElement) value.getValue() ).getSimpleName().toString();
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
               !descriptor.getComponentIdRefs().isEmpty() ||
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

  private boolean hasInjectAnnotation( @Nonnull final Element method )
  {
    return AnnotationsUtil.hasAnnotationOfType( method, Constants.INJECT_ANNOTATION_CLASSNAME );
  }

  @Nonnull
  private <T> T getAnnotationParameter( @Nonnull final AnnotationMirror annotation,
                                        @Nonnull final String parameterName )
  {
    return AnnotationsUtil.getAnnotationValue( annotation, parameterName );
  }

  private void emitTypeSpec( @Nonnull final String packageName, @Nonnull final TypeSpec typeSpec )
    throws IOException
  {
    GeneratorUtil.emitJavaType( packageName, typeSpec, processingEnv.getFiler() );
  }
}
