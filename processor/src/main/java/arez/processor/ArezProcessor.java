package arez.processor;

import com.google.auto.common.SuperficialValidation;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.ProcessingEnvironment;
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
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import static arez.processor.ProcessorUtil.*;
import static javax.tools.Diagnostic.Kind.*;

/**
 * Annotation processor that analyzes Arez annotated source and generates models from the annotations.
 */
@SupportedAnnotationTypes( Constants.COMPONENT_ANNOTATION_CLASSNAME )
@SupportedSourceVersion( SourceVersion.RELEASE_8 )
@SupportedOptions( { "arez.defer.unresolved", "arez.defer.errors" } )
public final class ArezProcessor
  extends AbstractArezProcessor
{
  private static final Pattern ON_ACTIVATE_PATTERN = Pattern.compile( "^on([A-Z].*)Activate$" );
  private static final Pattern ON_DEACTIVATE_PATTERN = Pattern.compile( "^on([A-Z].*)Deactivate$" );
  private static final Pattern SETTER_PATTERN = Pattern.compile( "^set([A-Z].*)$" );
  static final Pattern GETTER_PATTERN = Pattern.compile( "^get([A-Z].*)$" );
  private static final Pattern ISSER_PATTERN = Pattern.compile( "^is([A-Z].*)$" );
  private static final List<String> OBJECT_METHODS =
    Arrays.asList( "hashCode", "equals", "clone", "toString", "finalize", "getClass", "wait", "notifyAll", "notify" );
  private static final List<String> AREZ_SPECIAL_METHODS =
    Arrays.asList( "observe", "dispose", "isDisposed", "getArezId" );
  private static final Pattern ID_GETTER_PATTERN = Pattern.compile( "^get([A-Z].*)Id$" );
  private static final Pattern RAW_ID_GETTER_PATTERN = Pattern.compile( "^(.*)Id$" );
  private static final Pattern OBSERVABLE_REF_PATTERN = Pattern.compile( "^get([A-Z].*)ObservableValue$" );
  private static final Pattern COMPUTABLE_VALUE_REF_PATTERN = Pattern.compile( "^get([A-Z].*)ComputableValue$" );
  private static final Pattern OBSERVER_REF_PATTERN = Pattern.compile( "^get([A-Z].*)Observer$" );
  private static final Pattern ON_DEPS_CHANGE_PATTERN = Pattern.compile( "^on([A-Z].*)DepsChange" );

  @Nonnull
  private ObservableDescriptor addObservable( @Nonnull final ComponentDescriptor component,
                                              @Nonnull final AnnotationMirror annotation,
                                              @Nonnull final ExecutableElement method,
                                              @Nonnull final ExecutableType methodType )
    throws ProcessorException
  {
    MemberChecks.mustBeOverridable( component.getElement(),
                                    Constants.COMPONENT_ANNOTATION_CLASSNAME,
                                    Constants.OBSERVABLE_ANNOTATION_CLASSNAME,
                                    method );

    final String declaredName = AnnotationsUtil.getAnnotationValue( annotation, "name" );
    final boolean expectSetter = AnnotationsUtil.getAnnotationValue( annotation, "expectSetter" );
    final VariableElement readOutsideTransaction =
      AnnotationsUtil.getAnnotationValue( annotation, "readOutsideTransaction" );
    final VariableElement writeOutsideTransaction =
      AnnotationsUtil.getAnnotationValue( annotation, "writeOutsideTransaction" );
    final boolean setterAlwaysMutates = AnnotationsUtil.getAnnotationValue( annotation, "setterAlwaysMutates" );
    final Boolean requireInitializer = isInitializerRequired( method );

    final TypeMirror returnType = method.getReturnType();
    final String methodName = method.getSimpleName().toString();
    String name;
    final boolean setter;
    if ( TypeKind.VOID == returnType.getKind() )
    {
      setter = true;
      //Should be a setter
      if ( 1 != method.getParameters().size() )
      {
        throw new ProcessorException( "@Observable target should be a setter or getter", method );
      }

      name = ProcessorUtil.deriveName( method, SETTER_PATTERN, declaredName );
      if ( null == name )
      {
        name = methodName;
      }
    }
    else
    {
      setter = false;
      //Must be a getter
      if ( 0 != method.getParameters().size() )
      {
        throw new ProcessorException( "@Observable target should be a setter or getter", method );
      }
      name = getPropertyAccessorName( method, declaredName );
    }
    // Override name if supplied by user
    if ( !Constants.SENTINEL.equals( declaredName ) )
    {
      name = declaredName;
      if ( !SourceVersion.isIdentifier( name ) )
      {
        throw new ProcessorException( "@Observable target specified an invalid name '" + name + "'. The " +
                                      "name must be a valid java identifier.", method );
      }
      else if ( SourceVersion.isKeyword( name ) )
      {
        throw new ProcessorException( "@Observable target specified an invalid name '" + name + "'. The " +
                                      "name must not be a java keyword.", method );
      }
    }
    checkNameUnique( component, name, method, Constants.OBSERVABLE_ANNOTATION_CLASSNAME );

    if ( setter && !expectSetter )
    {
      throw new ProcessorException( "Method annotated with @Observable is a setter but defines " +
                                    "expectSetter = false for observable named " + name, method );
    }

    final ObservableDescriptor observable = component.findOrCreateObservable( name );
    observable.setReadOutsideTransaction( readOutsideTransaction.getSimpleName().toString() );
    observable.setWriteOutsideTransaction( writeOutsideTransaction.getSimpleName().toString() );
    if ( !setterAlwaysMutates )
    {
      observable.setSetterAlwaysMutates( false );
    }
    if ( !expectSetter )
    {
      observable.setExpectSetter( false );
    }
    if ( !observable.expectSetter() )
    {
      if ( observable.hasSetter() )
      {
        throw new ProcessorException( "Method annotated with @Observable defines expectSetter = false but a " +
                                      "setter exists named " + observable.getSetter().getSimpleName() +
                                      "for observable named " + name, method );
      }
    }
    if ( setter )
    {
      if ( observable.hasSetter() )
      {
        throw new ProcessorException( "Method annotated with @Observable defines duplicate setter for " +
                                      "observable named " + name, method );
      }
      if ( !observable.expectSetter() )
      {
        throw new ProcessorException( "Method annotated with @Observable defines expectSetter = false but a " +
                                      "setter exists for observable named " + name, method );
      }
      observable.setSetter( method, methodType );
    }
    else
    {
      if ( observable.hasGetter() )
      {
        throw new ProcessorException( "Method annotated with @Observable defines duplicate getter for " +
                                      "observable named " + name, method );
      }
      observable.setGetter( method, methodType );
    }
    if ( null != requireInitializer )
    {
      if ( !method.getModifiers().contains( Modifier.ABSTRACT ) )
      {
        throw new ProcessorException( "@Observable target set initializer parameter to ENABLED but " +
                                      "method is not abstract.", method );
      }
      final Boolean existing = observable.getInitializer();
      if ( null == existing )
      {
        observable.setInitializer( requireInitializer );
      }
      else if ( existing != requireInitializer )
      {
        throw new ProcessorException( "@Observable target set initializer parameter to value that differs from " +
                                      "the paired observable method.", method );
      }
    }
    return observable;
  }

  private void addObservableValueRef( @Nonnull final ComponentDescriptor component,
                                      @Nonnull final AnnotationMirror annotation,
                                      @Nonnull final ExecutableElement method,
                                      @Nonnull final ExecutableType methodType )
    throws ProcessorException
  {
    ArezUtils.mustBeStandardRefMethod( processingEnv,
                                       component,
                                       method,
                                       Constants.OBSERVABLE_VALUE_REF_ANNOTATION_CLASSNAME );

    final TypeMirror returnType = methodType.getReturnType();
    if ( TypeKind.DECLARED != returnType.getKind() ||
         !toRawType( returnType ).toString().equals( "arez.ObservableValue" ) )
    {
      throw new ProcessorException( "Method annotated with @ObservableValueRef must return an instance of " +
                                    "arez.ObservableValue", method );
    }

    final String declaredName = AnnotationsUtil.getAnnotationValue( annotation, "name" );
    final String name;
    if ( Constants.SENTINEL.equals( declaredName ) )
    {
      name = ProcessorUtil.deriveName( method, OBSERVABLE_REF_PATTERN, declaredName );
      if ( null == name )
      {
        throw new ProcessorException( "Method annotated with @ObservableValueRef should specify name or be " +
                                      "named according to the convention get[Name]ObservableValue", method );
      }
    }
    else
    {
      name = declaredName;
      if ( !SourceVersion.isIdentifier( name ) )
      {
        throw new ProcessorException( "@ObservableValueRef target specified an invalid name '" + name + "'. The " +
                                      "name must be a valid java identifier.", method );
      }
      else if ( SourceVersion.isKeyword( name ) )
      {
        throw new ProcessorException( "@ObservableValueRef target specified an invalid name '" + name + "'. The " +
                                      "name must not be a java keyword.", method );
      }
    }

    component.findOrCreateObservable( name ).addRefMethod( method, methodType );
  }

  private void addComputableValueRef( @Nonnull final ComponentDescriptor component,
                                      @Nonnull final AnnotationMirror annotation,
                                      @Nonnull final ExecutableElement method,
                                      @Nonnull final ExecutableType methodType )
    throws ProcessorException
  {
    ArezUtils.mustBeRefMethod( component, method, Constants.COMPUTABLE_VALUE_REF_ANNOTATION_CLASSNAME );
    ArezUtils.shouldBeInternalRefMethod( processingEnv,
                                         component,
                                         method,
                                         Constants.COMPUTABLE_VALUE_REF_ANNOTATION_CLASSNAME );

    final TypeMirror returnType = methodType.getReturnType();
    if ( TypeKind.DECLARED != returnType.getKind() ||
         !toRawType( returnType ).toString().equals( "arez.ComputableValue" ) )
    {
      throw new ProcessorException( "Method annotated with @ComputableValueRef must return an instance of " +
                                    "arez.ComputableValue", method );
    }

    final String declaredName = AnnotationsUtil.getAnnotationValue( annotation, "name" );
    final String name;
    if ( Constants.SENTINEL.equals( declaredName ) )
    {
      name = ProcessorUtil.deriveName( method, COMPUTABLE_VALUE_REF_PATTERN, declaredName );
      if ( null == name )
      {
        throw new ProcessorException( "Method annotated with @ComputableValueRef should specify name or be " +
                                      "named according to the convention get[Name]ComputableValue", method );
      }
    }
    else
    {
      name = declaredName;
      if ( !SourceVersion.isIdentifier( name ) )
      {
        throw new ProcessorException( "@ComputableValueRef target specified an invalid name '" + name + "'. The " +
                                      "name must be a valid java identifier.", method );
      }
      else if ( SourceVersion.isKeyword( name ) )
      {
        throw new ProcessorException( "@ComputableValueRef target specified an invalid name '" + name + "'. The " +
                                      "name must not be a java keyword.", method );
      }
    }

    MemberChecks.mustBeSubclassCallable( component.getElement(),
                                         Constants.COMPONENT_ANNOTATION_CLASSNAME,
                                         Constants.COMPUTABLE_VALUE_REF_ANNOTATION_CLASSNAME,
                                         method );
    MemberChecks.mustNotThrowAnyExceptions( Constants.COMPUTABLE_VALUE_REF_ANNOTATION_CLASSNAME, method );
    component.findOrCreateMemoize( name ).addRefMethod( method, methodType );
  }

  @Nonnull
  private String deriveMemoizeName( @Nonnull final ExecutableElement method,
                                    @Nonnull final AnnotationMirror annotation )
    throws ProcessorException
  {
    final String name = AnnotationsUtil.getAnnotationValue( annotation, "name" );
    if ( Constants.SENTINEL.equals( name ) )
    {
      return getPropertyAccessorName( method, name );
    }
    else
    {
      if ( !SourceVersion.isIdentifier( name ) )
      {
        throw new ProcessorException( "@Memoize target specified an invalid name '" + name + "'. The " +
                                      "name must be a valid java identifier.", method );
      }
      else if ( SourceVersion.isKeyword( name ) )
      {
        throw new ProcessorException( "@Memoize target specified an invalid name '" + name + "'. The " +
                                      "name must not be a java keyword.", method );
      }
      return name;
    }
  }

  private void addOnActivate( @Nonnull final ComponentDescriptor component,
                              @Nonnull final AnnotationMirror annotation,
                              @Nonnull final ExecutableElement method )
    throws ProcessorException
  {
    final String name = deriveHookName( component, method,
                                        ON_ACTIVATE_PATTERN,
                                        "Activate",
                                        AnnotationsUtil.getAnnotationValue( annotation, "name" ) );
    MemberChecks.mustNotBeAbstract( Constants.ON_ACTIVATE_ANNOTATION_CLASSNAME, method );
    MemberChecks.mustBeSubclassCallable( component.getElement(),
                                         Constants.COMPONENT_ANNOTATION_CLASSNAME,
                                         Constants.ON_ACTIVATE_ANNOTATION_CLASSNAME,
                                         method );
    final List<? extends VariableElement> parameters = method.getParameters();

    if (
      !(
        parameters.isEmpty() ||
        ( 1 == parameters.size() &&
          Constants.COMPUTABLE_VALUE_CLASSNAME.equals( toRawType( parameters.get( 0 ).asType() ).toString() ) )
      )
    )
    {
      throw new ProcessorException( "@OnActivate target must not have any parameters or must have a single " +
                                    "parameter of type arez.ComputableValue", method );
    }

    MemberChecks.mustNotReturnAnyValue( Constants.ON_ACTIVATE_ANNOTATION_CLASSNAME, method );
    MemberChecks.mustNotThrowAnyExceptions( Constants.ON_ACTIVATE_ANNOTATION_CLASSNAME, method );
    ArezUtils.mustBeInternalHookMethod( processingEnv,
                                        component,
                                        method,
                                        Constants.ON_ACTIVATE_ANNOTATION_CLASSNAME );

    component.findOrCreateMemoize( name ).setOnActivate( method );
  }

  private void addOnDeactivate( @Nonnull final ComponentDescriptor component,
                                @Nonnull final AnnotationMirror annotation,
                                @Nonnull final ExecutableElement method )
    throws ProcessorException
  {
    final String name =
      deriveHookName( component, method,
                      ON_DEACTIVATE_PATTERN,
                      "Deactivate",
                      AnnotationsUtil.getAnnotationValue( annotation, "name" ) );
    MemberChecks.mustBeLifecycleHook( component.getElement(),
                                      Constants.COMPONENT_ANNOTATION_CLASSNAME,
                                      Constants.ON_DEACTIVATE_ANNOTATION_CLASSNAME,
                                      method );
    ArezUtils.mustBeInternalHookMethod( processingEnv,
                                        component,
                                        method,
                                        Constants.ON_DEACTIVATE_ANNOTATION_CLASSNAME );
    component.findOrCreateMemoize( name ).setOnDeactivate( method );
  }

  @Nonnull
  private String deriveHookName( @Nonnull final ComponentDescriptor component,
                                 @Nonnull final ExecutableElement method,
                                 @Nonnull final Pattern pattern,
                                 @Nonnull final String type,
                                 @Nonnull final String name )
    throws ProcessorException
  {
    final String value = ProcessorUtil.deriveName( method, pattern, name );
    if ( null == value )
    {
      throw new ProcessorException( "Unable to derive name for @On" + type + " as does not match " +
                                    "on[Name]" + type + " pattern. Please specify name.", method );
    }
    else if ( !SourceVersion.isIdentifier( value ) )
    {
      throw new ProcessorException( "@On" + type + " target specified an invalid name '" + value + "'. The " +
                                    "name must be a valid java identifier.", component.getElement() );
    }
    else if ( SourceVersion.isKeyword( value ) )
    {
      throw new ProcessorException( "@On" + type + " target specified an invalid name '" + value + "'. The " +
                                    "name must not be a java keyword.", component.getElement() );
    }
    else
    {
      return value;
    }
  }

  private void addComponentStateRef( @Nonnull final ComponentDescriptor component,
                                     @Nonnull final AnnotationMirror annotation,
                                     @Nonnull final ExecutableElement method )
    throws ProcessorException
  {
    ArezUtils.mustBeStandardRefMethod( processingEnv,
                                       component,
                                       method,
                                       Constants.COMPONENT_STATE_REF_ANNOTATION_CLASSNAME );

    final TypeMirror returnType = method.getReturnType();
    if ( TypeKind.BOOLEAN != returnType.getKind() )
    {
      throw new ProcessorException( "Method annotated with @ComponentStateRef must return a boolean", method );
    }
    final VariableElement variableElement = AnnotationsUtil.getAnnotationValue( annotation, "value" );
    final ComponentStateRefDescriptor.State state =
      ComponentStateRefDescriptor.State.valueOf( variableElement.getSimpleName().toString() );

    component.getComponentStateRefs().add( new ComponentStateRefDescriptor( method, state ) );
  }

  private void addContextRef( @Nonnull final ComponentDescriptor component, @Nonnull final ExecutableElement method )
    throws ProcessorException
  {
    ArezUtils.mustBeStandardRefMethod( processingEnv,
                                       component,
                                       method,
                                       Constants.CONTEXT_REF_ANNOTATION_CLASSNAME );
    MemberChecks.mustReturnAnInstanceOf( processingEnv,
                                         method,
                                         Constants.OBSERVER_REF_ANNOTATION_CLASSNAME,
                                         "arez.ArezContext" );
    component.getContextRefs().add( method );
  }

  private void addComponentIdRef( @Nonnull final ComponentDescriptor component,
                                  @Nonnull final ExecutableElement method )
  {
    ArezUtils.mustBeRefMethod( component, method, Constants.COMPONENT_ID_REF_ANNOTATION_CLASSNAME );
    MemberChecks.mustNotHaveAnyParameters( Constants.COMPONENT_ID_REF_ANNOTATION_CLASSNAME, method );
    component.getComponentIdRefs().add( method );
  }

  private void addComponentRef( @Nonnull final ComponentDescriptor component, @Nonnull final ExecutableElement method )
    throws ProcessorException
  {
    ArezUtils.mustBeStandardRefMethod( processingEnv,
                                       component,
                                       method,
                                       Constants.COMPONENT_REF_ANNOTATION_CLASSNAME );
    MemberChecks.mustReturnAnInstanceOf( processingEnv,
                                         method,
                                         Constants.COMPONENT_REF_ANNOTATION_CLASSNAME,
                                         "arez.Component" );
    component.getComponentRefs().add( method );
  }

  private void setComponentId( @Nonnull final ComponentDescriptor component,
                               @Nonnull final ExecutableElement componentId,
                               @Nonnull final ExecutableType componentIdMethodType )
    throws ProcessorException
  {
    MemberChecks.mustNotBeAbstract( Constants.COMPONENT_ID_ANNOTATION_CLASSNAME, componentId );
    MemberChecks.mustBeSubclassCallable( component.getElement(),
                                         Constants.COMPONENT_ANNOTATION_CLASSNAME,
                                         Constants.COMPONENT_ID_ANNOTATION_CLASSNAME,
                                         componentId );
    MemberChecks.mustBeFinal( Constants.COMPONENT_ID_ANNOTATION_CLASSNAME, componentId );
    MemberChecks.mustNotHaveAnyParameters( Constants.COMPONENT_ID_ANNOTATION_CLASSNAME, componentId );
    MemberChecks.mustReturnAValue( Constants.COMPONENT_ID_ANNOTATION_CLASSNAME, componentId );
    MemberChecks.mustNotThrowAnyExceptions( Constants.COMPONENT_ID_ANNOTATION_CLASSNAME, componentId );

    if ( null != component.getComponentId() )
    {
      throw new ProcessorException( "@ComponentId target duplicates existing method named " +
                                    component.getComponentId().getSimpleName(), componentId );
    }
    else
    {
      component.setComponentId( Objects.requireNonNull( componentId ) );
      component.setComponentIdMethodType( componentIdMethodType );
    }
  }

  private void setComponentTypeNameRef( @Nonnull final ComponentDescriptor component,
                                        @Nonnull final ExecutableElement method )
    throws ProcessorException
  {
    ArezUtils.mustBeStandardRefMethod( processingEnv,
                                       component,
                                       method,
                                       Constants.COMPONENT_TYPE_NAME_REF_ANNOTATION_CLASSNAME );
    MemberChecks.mustReturnAnInstanceOf( processingEnv,
                                         method,
                                         Constants.COMPONENT_TYPE_NAME_REF_ANNOTATION_CLASSNAME,
                                         String.class.getName() );
    component.getComponentTypeNameRefs().add( method );
  }

  private void addComponentNameRef( @Nonnull final ComponentDescriptor component,
                                    @Nonnull final ExecutableElement method )
    throws ProcessorException
  {
    ArezUtils.mustBeStandardRefMethod( processingEnv,
                                       component,
                                       method,
                                       Constants.COMPONENT_NAME_REF_ANNOTATION_CLASSNAME );
    MemberChecks.mustReturnAnInstanceOf( processingEnv,
                                         method,
                                         Constants.COMPONENT_NAME_REF_ANNOTATION_CLASSNAME,
                                         String.class.getName() );
    component.getComponentNameRefs().add( method );
  }

  private void addPostConstruct( @Nonnull final ComponentDescriptor component, @Nonnull final ExecutableElement method )
    throws ProcessorException
  {
    MemberChecks.mustBeLifecycleHook( component.getElement(),
                                      Constants.COMPONENT_ANNOTATION_CLASSNAME,
                                      Constants.POST_CONSTRUCT_ANNOTATION_CLASSNAME,
                                      method );
    ArezUtils.shouldBeInternalLifecycleMethod( processingEnv,
                                               component,
                                               method,
                                               Constants.POST_CONSTRUCT_ANNOTATION_CLASSNAME );
    component.getPostConstructs().add( method );
  }

  private void addPreDispose( @Nonnull final ComponentDescriptor component, @Nonnull final ExecutableElement method )
    throws ProcessorException
  {
    MemberChecks.mustBeLifecycleHook( component.getElement(),
                                      Constants.COMPONENT_ANNOTATION_CLASSNAME,
                                      Constants.PRE_DISPOSE_ANNOTATION_CLASSNAME,
                                      method );
    ArezUtils.shouldBeInternalLifecycleMethod( processingEnv,
                                               component,
                                               method,
                                               Constants.PRE_DISPOSE_ANNOTATION_CLASSNAME );
    component.getPreDisposes().add( method );
  }

  private void addPostDispose( @Nonnull final ComponentDescriptor component, @Nonnull final ExecutableElement method )
    throws ProcessorException
  {
    MemberChecks.mustBeLifecycleHook( component.getElement(),
                                      Constants.COMPONENT_ANNOTATION_CLASSNAME,
                                      Constants.POST_DISPOSE_ANNOTATION_CLASSNAME,
                                      method );
    ArezUtils.shouldBeInternalLifecycleMethod( processingEnv,
                                               component,
                                               method,
                                               Constants.POST_DISPOSE_ANNOTATION_CLASSNAME );
    component.getPostDisposes().add( method );
  }

  private void linkUnAnnotatedObservables( @Nonnull final ComponentDescriptor component,
                                           @Nonnull final Map<String, CandidateMethod> getters,
                                           @Nonnull final Map<String, CandidateMethod> setters )
    throws ProcessorException
  {
    for ( final ObservableDescriptor observable : component.getObservables().values() )
    {
      if ( !observable.hasSetter() && !observable.hasGetter() )
      {
        throw new ProcessorException( "@ObservableValueRef target unable to be associated with an " +
                                      "Observable property", observable.getRefMethods().get( 0 ).getMethod() );
      }
      else if ( !observable.hasSetter() && observable.expectSetter() )
      {
        final CandidateMethod candidate = setters.remove( observable.getName() );
        if ( null != candidate )
        {
          MemberChecks.mustBeOverridable( component.getElement(),
                                          Constants.COMPONENT_ANNOTATION_CLASSNAME,
                                          Constants.OBSERVABLE_ANNOTATION_CLASSNAME,
                                          candidate.getMethod() );
          observable.setSetter( candidate.getMethod(), candidate.getMethodType() );
        }
        else if ( observable.hasGetter() )
        {
          throw new ProcessorException( "@Observable target defined getter but no setter was defined and no " +
                                        "setter could be automatically determined", observable.getGetter() );
        }
      }
      else if ( !observable.hasGetter() )
      {
        final CandidateMethod candidate = getters.remove( observable.getName() );
        if ( null != candidate )
        {
          MemberChecks.mustBeOverridable( component.getElement(),
                                          Constants.COMPONENT_ANNOTATION_CLASSNAME,
                                          Constants.OBSERVABLE_ANNOTATION_CLASSNAME,
                                          candidate.getMethod() );
          observable.setGetter( candidate.getMethod(), candidate.getMethodType() );
        }
        else
        {
          throw new ProcessorException( "@Observable target defined setter but no getter was defined and no " +
                                        "getter could be automatically determined", observable.getSetter() );
        }
      }
    }

    // Find pairs of un-annotated abstract setter/getter pairs and treat them as if they
    // are annotated with @Observable
    for ( final Map.Entry<String, CandidateMethod> entry : new ArrayList<>( getters.entrySet() ) )
    {
      final CandidateMethod getter = entry.getValue();
      if ( getter.getMethod().getModifiers().contains( Modifier.ABSTRACT ) )
      {
        final String name = entry.getKey();
        final CandidateMethod setter = setters.remove( name );
        if ( null != setter && setter.getMethod().getModifiers().contains( Modifier.ABSTRACT ) )
        {
          final ObservableDescriptor observable = component.findOrCreateObservable( name );
          observable.setGetter( getter.getMethod(), getter.getMethodType() );
          observable.setSetter( setter.getMethod(), setter.getMethodType() );
          getters.remove( name );
        }
      }
    }
  }

  private void linkUnAnnotatedObserves( @Nonnull final ComponentDescriptor component,
                                        @Nonnull final Map<String, CandidateMethod> observes,
                                        @Nonnull final Map<String, CandidateMethod> onDepsChanges )
    throws ProcessorException
  {
    for ( final ObserveDescriptor observe : component.getObserves().values() )
    {
      if ( !observe.hasObserve() )
      {
        final CandidateMethod candidate = observes.remove( observe.getName() );
        if ( null != candidate )
        {
          observe.setObserveMethod( false,
                                    Priority.NORMAL,
                                    true,
                                    true,
                                    true,
                                    "AREZ",
                                    false,
                                    false,
                                    candidate.getMethod(),
                                    candidate.getMethodType() );
        }
        else
        {
          throw new ProcessorException( "@OnDepsChange target has no corresponding @Observe that could " +
                                        "be automatically determined", observe.getOnDepsChange() );
        }
      }
      else if ( !observe.hasOnDepsChange() )
      {
        final CandidateMethod candidate = onDepsChanges.remove( observe.getName() );
        if ( null != candidate )
        {
          setOnDepsChange( component, observe, candidate.getMethod() );
        }
      }
    }
  }

  private void setOnDepsChange( @Nonnull final ComponentDescriptor component,
                                @Nonnull final ObserveDescriptor observe,
                                @Nonnull final ExecutableElement method )
  {
    MemberChecks.mustNotBeAbstract( Constants.ON_DEPS_CHANGE_ANNOTATION_CLASSNAME, method );
    MemberChecks.mustBeSubclassCallable( component.getElement(),
                                         Constants.COMPONENT_ANNOTATION_CLASSNAME,
                                         Constants.ON_DEPS_CHANGE_ANNOTATION_CLASSNAME,
                                         method );
    final List<? extends VariableElement> parameters = method.getParameters();
    if (
      !(
        parameters.isEmpty() ||
        ( 1 == parameters.size() && Constants.OBSERVER_CLASSNAME.equals( parameters.get( 0 ).asType().toString() ) )
      )
    )
    {
      throw new ProcessorException( "@OnDepsChange target must not have any parameters or must have a single " +
                                    "parameter of type arez.Observer", method );
    }

    MemberChecks.mustNotReturnAnyValue( Constants.ON_DEPS_CHANGE_ANNOTATION_CLASSNAME, method );
    MemberChecks.mustNotThrowAnyExceptions( Constants.ON_DEPS_CHANGE_ANNOTATION_CLASSNAME, method );
    ArezUtils.mustBeInternalHookMethod( processingEnv,
                                        component,
                                        method,
                                        Constants.ON_DEPS_CHANGE_ANNOTATION_CLASSNAME );
    observe.setOnDepsChange( method );
  }

  private void verifyNoDuplicateAnnotations( @Nonnull final ExecutableElement method )
    throws ProcessorException
  {
    final List<String> annotations =
      Arrays.asList( Constants.ACTION_ANNOTATION_CLASSNAME,
                     Constants.OBSERVE_ANNOTATION_CLASSNAME,
                     Constants.ON_DEPS_CHANGE_ANNOTATION_CLASSNAME,
                     Constants.OBSERVER_REF_ANNOTATION_CLASSNAME,
                     Constants.OBSERVABLE_ANNOTATION_CLASSNAME,
                     Constants.OBSERVABLE_VALUE_REF_ANNOTATION_CLASSNAME,
                     Constants.MEMOIZE_ANNOTATION_CLASSNAME,
                     Constants.COMPUTABLE_VALUE_REF_ANNOTATION_CLASSNAME,
                     Constants.COMPONENT_REF_ANNOTATION_CLASSNAME,
                     Constants.COMPONENT_ID_ANNOTATION_CLASSNAME,
                     Constants.COMPONENT_NAME_REF_ANNOTATION_CLASSNAME,
                     Constants.COMPONENT_TYPE_NAME_REF_ANNOTATION_CLASSNAME,
                     Constants.CASCADE_DISPOSE_ANNOTATION_CLASSNAME,
                     Constants.CONTEXT_REF_ANNOTATION_CLASSNAME,
                     Constants.POST_CONSTRUCT_ANNOTATION_CLASSNAME,
                     Constants.PRE_DISPOSE_ANNOTATION_CLASSNAME,
                     Constants.POST_DISPOSE_ANNOTATION_CLASSNAME,
                     Constants.REFERENCE_ANNOTATION_CLASSNAME,
                     Constants.REFERENCE_ID_ANNOTATION_CLASSNAME,
                     Constants.ON_ACTIVATE_ANNOTATION_CLASSNAME,
                     Constants.ON_DEACTIVATE_ANNOTATION_CLASSNAME,
                     Constants.COMPONENT_DEPENDENCY_ANNOTATION_CLASSNAME );
    final Map<String, Collection<String>> exceptions = new HashMap<>();
    exceptions.put( Constants.OBSERVABLE_ANNOTATION_CLASSNAME,
                    Arrays.asList( Constants.COMPONENT_DEPENDENCY_ANNOTATION_CLASSNAME,
                                   Constants.CASCADE_DISPOSE_ANNOTATION_CLASSNAME,
                                   Constants.REFERENCE_ID_ANNOTATION_CLASSNAME ) );
    exceptions.put( Constants.REFERENCE_ANNOTATION_CLASSNAME,
                    Collections.singletonList( Constants.CASCADE_DISPOSE_ANNOTATION_CLASSNAME ) );

    MemberChecks.verifyNoOverlappingAnnotations( method, annotations, exceptions );
  }

  private void verifyNoDuplicateAnnotations( @Nonnull final VariableElement field )
    throws ProcessorException
  {
    MemberChecks.verifyNoOverlappingAnnotations( field,
                                                 Arrays.asList( Constants.COMPONENT_DEPENDENCY_ANNOTATION_CLASSNAME,
                                                                Constants.CASCADE_DISPOSE_ANNOTATION_CLASSNAME ),
                                                 Collections.emptyMap() );
  }

  @Nonnull
  private String getPropertyAccessorName( @Nonnull final ExecutableElement method, @Nonnull final String specifiedName )
    throws ProcessorException
  {
    String name = ProcessorUtil.deriveName( method, GETTER_PATTERN, specifiedName );
    if ( null != name )
    {
      return name;
    }
    if ( method.getReturnType().getKind() == TypeKind.BOOLEAN )
    {
      name = ProcessorUtil.deriveName( method, ISSER_PATTERN, specifiedName );
      if ( null != name )
      {
        return name;
      }
    }
    return method.getSimpleName().toString();
  }

  private void validate( final boolean allowEmpty, @Nonnull final ComponentDescriptor component )
    throws ProcessorException
  {
    component.getCascadeDisposes().values().forEach( CascadeDisposableDescriptor::validate );
    component.getObservables().values().forEach( ObservableDescriptor::validate );
    component.getMemoizes().values().forEach( e -> e.validate( processingEnv ) );
    component.getObserves().values().forEach( ObserveDescriptor::validate );
    component.getDependencies().values().forEach( DependencyDescriptor::validate );
    component.getReferences().values().forEach( ReferenceDescriptor::validate );
    component.getInverses().values().forEach( InverseDescriptor::validate );

    final boolean hasReactiveElements =
      component.getObservables().isEmpty() &&
      component.getActions().isEmpty() &&
      component.getMemoizes().isEmpty() &&
      component.getDependencies().isEmpty() &&
      component.getCascadeDisposes().isEmpty() &&
      component.getReferences().isEmpty() &&
      component.getInverses().isEmpty() &&
      component.getObserves().isEmpty();

    if ( null != component.getDefaultPriority() &&
         component.getMemoizes().isEmpty() &&
         component.getObserves().isEmpty() &&
         isWarningNotSuppressed( component.getElement(), Constants.WARNING_UNNECESSARY_DEFAULT_PRIORITY ) )
    {
      final String message =
        MemberChecks.toSimpleName( Constants.COMPONENT_ANNOTATION_CLASSNAME ) + " target should not specify " +
        "the defaultPriority parameter unless it contains methods annotated with either the " +
        MemberChecks.toSimpleName( Constants.MEMOIZE_ANNOTATION_CLASSNAME ) + " annotation or the " +
        MemberChecks.toSimpleName( Constants.OBSERVE_ANNOTATION_CLASSNAME ) + " annotation. " +
        suppressedBy( Constants.WARNING_UNNECESSARY_DEFAULT_PRIORITY );
      processingEnv.getMessager().printMessage( WARNING, message, component.getElement() );
    }
    if ( !allowEmpty && hasReactiveElements )
    {
      throw new ProcessorException( "@ArezComponent target has no methods annotated with @Action, " +
                                    "@CascadeDispose, @Memoize, @Observable, @Inverse, " +
                                    "@Reference, @ComponentDependency or @Observe", component.getElement() );
    }
    else if ( allowEmpty &&
              !hasReactiveElements &&
              isWarningNotSuppressed( component.getElement(), Constants.WARNING_UNNECESSARY_ALLOW_EMPTY ) )
    {
      final String message =
        "@ArezComponent target has specified allowEmpty = true but has methods " +
        "annotated with @Action, @CascadeDispose, @Memoize, @Observable, @Inverse, " +
        "@Reference, @ComponentDependency or @Observe. " +
        suppressedBy( Constants.WARNING_UNNECESSARY_ALLOW_EMPTY );
      processingEnv.getMessager().printMessage( WARNING, message, component.getElement() );
    }

    if ( component.isDeferSchedule() && !component.requiresSchedule() )
    {
      throw new ProcessorException( "@ArezComponent target has specified the deferSchedule = true " +
                                    "annotation parameter but has no methods annotated with @Observe, " +
                                    "@ComponentDependency or @Memoize(keepAlive=true)", component.getElement() );
    }
    for ( final ExecutableElement componentIdRef : component.getComponentIdRefs() )
    {
      if ( null != component.getComponentId() &&
           !processingEnv.getTypeUtils()
             .isSameType( component.getComponentId().getReturnType(), componentIdRef.getReturnType() ) )
      {
        throw new ProcessorException( "@ComponentIdRef target has a return type " + componentIdRef.getReturnType() +
                                      " and a @ComponentId annotated method with a return type " +
                                      componentIdRef.getReturnType() + ". The types must match.",
                                      component.getElement() );
      }
      else if ( null == component.getComponentId() &&
                !processingEnv.getTypeUtils()
                  .isSameType( processingEnv.getTypeUtils().getPrimitiveType( TypeKind.INT ),
                               componentIdRef.getReturnType() ) )
      {
        throw new ProcessorException( "@ComponentIdRef target has a return type " + componentIdRef.getReturnType() +
                                      " but no @ComponentId annotated method. The type is expected to be of " +
                                      "type int.", component.getElement() );
      }
    }
    if ( InjectMode.NONE != component.getInjectMode() )
    {
      for ( final ExecutableElement constructor : ProcessorUtil.getConstructors( component.getElement() ) )
      {
        // The annotation processor engine can not distinguish between a "default constructor"
        // synthesized by the compiler and one written by a user that has the same signature.
        // So our check just skips scenarios where the constructor could be synthetic.
        if ( constructor.getModifiers().contains( Modifier.PUBLIC ) &&
             !( constructor.getParameters().isEmpty() && constructor.getThrownTypes().isEmpty() ) )
        {
          throw new ProcessorException( "@ArezComponent target has a public constructor but the inject parameter " +
                                        "does not resolve to NONE. Public constructors are not necessary when " +
                                        "the instantiation of the component is managed by the injection framework.",
                                        constructor );
        }
      }
      if ( InjectMode.PROVIDE == component.getInjectMode() &&
           component.isDagger() &&
           !component.getElement().getModifiers().contains( Modifier.PUBLIC ) )
      {
        throw new ProcessorException( "@ArezComponent target is not public but is configured as inject = PROVIDE " +
                                      "using the dagger injection framework. Due to constraints within the " +
                                      "dagger framework the type needs to made public.",
                                      component.getElement() );
      }
    }
  }

  private void processCascadeDisposeFields( @Nonnull final ComponentDescriptor component )
  {
    ProcessorUtil.getFieldElements( component.getElement() )
      .stream()
      .filter( f -> AnnotationsUtil.hasAnnotationOfType( f, Constants.CASCADE_DISPOSE_ANNOTATION_CLASSNAME ) )
      .forEach( field -> processCascadeDisposeField( component, field ) );
  }

  private void processCascadeDisposeField( @Nonnull final ComponentDescriptor component,
                                           @Nonnull final VariableElement field )
  {
    verifyNoDuplicateAnnotations( field );
    MemberChecks.mustBeSubclassCallable( component.getElement(),
                                         Constants.COMPONENT_ANNOTATION_CLASSNAME,
                                         Constants.CASCADE_DISPOSE_ANNOTATION_CLASSNAME,
                                         field );
    mustBeCascadeDisposeTypeCompatible( field );
    component.addCascadeDispose( new CascadeDisposableDescriptor( field ) );
  }

  private boolean shouldRepositoryDefineCreate( @Nonnull final ComponentDescriptor component )
  {
    final String value = AnnotationsUtil.getEnumAnnotationParameter( component.getElement(),
                                                                     Constants.REPOSITORY_ANNOTATION_CLASSNAME,
                                                                     "attach" );
    switch ( value )
    {
      case "CREATE_ONLY":
      case "CREATE_OR_ATTACH":
        return true;
      default:
        return false;
    }
  }

  private boolean shouldRepositoryDefineDestroy( @Nonnull final ComponentDescriptor component )
  {
    final String value = AnnotationsUtil.getEnumAnnotationParameter( component.getElement(),
                                                                     Constants.REPOSITORY_ANNOTATION_CLASSNAME,
                                                                     "detach" );
    switch ( value )
    {
      case "DESTROY_ONLY":
      case "DESTROY_OR_DETACH":
        return true;
      default:
        return false;
    }
  }

  private boolean shouldRepositoryDefineDetach( @Nonnull final ComponentDescriptor component )
  {
    final String value =
      AnnotationsUtil.getEnumAnnotationParameter( component.getElement(),
                                                  Constants.REPOSITORY_ANNOTATION_CLASSNAME,
                                                  "detach" );
    switch ( value )
    {
      case "DETACH_ONLY":
      case "DESTROY_OR_DETACH":
        return true;
      default:
        return false;
    }
  }

  @Nonnull
  private String suppressedBy( @Nonnull final String warning )
  {
    return MemberChecks.suppressedBy( warning, Constants.SUPPRESS_AREZ_WARNINGS_ANNOTATION_CLASSNAME );
  }

  private boolean isWarningNotSuppressed( @Nonnull final Element element, @Nonnull final String warning )
  {
    return !ProcessorUtil.isWarningSuppressed( element,
                                               warning,
                                               Constants.SUPPRESS_AREZ_WARNINGS_ANNOTATION_CLASSNAME );
  }

  @SuppressWarnings( "SameParameterValue" )
  @Nonnull
  private String extractName( @Nonnull final ExecutableElement method,
                              @Nonnull final Function<ExecutableElement, String> function,
                              @Nonnull final String annotationClassname )
  {
    return AnnotationsUtil.extractName( method, function, annotationClassname, "name", Constants.SENTINEL );
  }

  private void mustBeCascadeDisposeTypeCompatible( @Nonnull final VariableElement field )
  {
    final TypeElement disposable =
      processingEnv.getElementUtils().getTypeElement( Constants.DISPOSABLE_CLASSNAME );
    assert null != disposable;
    final TypeMirror typeMirror = field.asType();
    if ( !processingEnv.getTypeUtils().isAssignable( typeMirror, disposable.asType() ) )
    {
      final TypeElement typeElement = (TypeElement) processingEnv.getTypeUtils().asElement( typeMirror );
      final AnnotationMirror value =
        null != typeElement ?
        AnnotationsUtil.findAnnotationByType( typeElement, Constants.COMPONENT_ANNOTATION_CLASSNAME ) :
        null;
      if ( null == value || !ProcessorUtil.isDisposableTrackableRequired( typeElement ) )
      {
        //The type of the field must implement {@link arez.Disposable} or must be annotated by {@link ArezComponent}
        throw new ProcessorException( "@CascadeDispose target must be assignable to " +
                                      Constants.DISPOSABLE_CLASSNAME + " or a type annotated with @ArezComponent",
                                      field );
      }
    }
  }

  private void addCascadeDisposeMethod( @Nonnull final ComponentDescriptor component,
                                        @Nonnull final ExecutableElement method,
                                        @Nullable final ObservableDescriptor observable )
  {
    MemberChecks.mustNotHaveAnyParameters( Constants.CASCADE_DISPOSE_ANNOTATION_CLASSNAME, method );
    MemberChecks.mustNotThrowAnyExceptions( Constants.CASCADE_DISPOSE_ANNOTATION_CLASSNAME, method );
    MemberChecks.mustBeSubclassCallable( component.getElement(),
                                         Constants.COMPONENT_ANNOTATION_CLASSNAME,
                                         Constants.CASCADE_DISPOSE_ANNOTATION_CLASSNAME,
                                         method );
    mustBeCascadeDisposeTypeCompatible( method );
    component.addCascadeDispose( new CascadeDisposableDescriptor( method, observable ) );
  }

  private void mustBeCascadeDisposeTypeCompatible( @Nonnull final ExecutableElement method )
  {
    final TypeElement disposable =
      processingEnv.getElementUtils().getTypeElement( Constants.DISPOSABLE_CLASSNAME );
    assert null != disposable;
    final TypeMirror typeMirror = method.getReturnType();
    if ( !processingEnv.getTypeUtils().isAssignable( typeMirror, disposable.asType() ) )
    {
      final TypeElement typeElement = (TypeElement) processingEnv.getTypeUtils().asElement( typeMirror );
      final AnnotationMirror value =
        null != typeElement ?
        AnnotationsUtil.findAnnotationByType( typeElement, Constants.COMPONENT_ANNOTATION_CLASSNAME ) :
        null;
      if ( null == value || !ProcessorUtil.isDisposableTrackableRequired( typeElement ) )
      {
        //The type of the field must implement {@link arez.Disposable} or must be annotated by {@link ArezComponent}
        throw new ProcessorException( "@CascadeDispose target must return a type assignable to " +
                                      Constants.DISPOSABLE_CLASSNAME + " or a type annotated with @ArezComponent",
                                      method );
      }
    }
  }

  private void addOrUpdateDependency( @Nonnull final ComponentDescriptor component,
                                      @Nonnull final ExecutableElement method,
                                      @Nonnull final ObservableDescriptor observable )
  {
    final DependencyDescriptor dependencyDescriptor =
      component.getDependencies().computeIfAbsent( method, m -> createMethodDependencyDescriptor( component, method ) );
    dependencyDescriptor.setObservable( observable );
  }

  private boolean shouldRepositoryDefineAttach( @Nonnull final ComponentDescriptor component )
  {
    final String value = AnnotationsUtil.getEnumAnnotationParameter( component.getElement(),
                                                                     Constants.REPOSITORY_ANNOTATION_CLASSNAME,
                                                                     "attach" );
    switch ( value )
    {
      case "ATTACH_ONLY":
      case "CREATE_OR_ATTACH":
        return true;
      default:
        return false;
    }
  }

  private void addAction( @Nonnull final ComponentDescriptor component,
                          @Nonnull final AnnotationMirror annotation,
                          @Nonnull final ExecutableElement method,
                          @Nonnull final ExecutableType methodType )
    throws ProcessorException
  {
    MemberChecks.mustBeWrappable( component.getElement(),
                                  Constants.COMPONENT_ANNOTATION_CLASSNAME,
                                  Constants.ACTION_ANNOTATION_CLASSNAME,
                                  method );

    final String name =
      extractName( method, m -> m.getSimpleName().toString(), Constants.ACTION_ANNOTATION_CLASSNAME );
    checkNameUnique( component, name, method, Constants.ACTION_ANNOTATION_CLASSNAME );
    final boolean mutation = AnnotationsUtil.getAnnotationValue( annotation, "mutation" );
    final boolean requireNewTransaction = AnnotationsUtil.getAnnotationValue( annotation, "requireNewTransaction" );
    final boolean reportParameters = AnnotationsUtil.getAnnotationValue( annotation, "reportParameters" );
    final boolean reportResult = AnnotationsUtil.getAnnotationValue( annotation, "reportResult" );
    final boolean verifyRequired = AnnotationsUtil.getAnnotationValue( annotation, "verifyRequired" );
    final ActionDescriptor action =
      new ActionDescriptor( component,
                            name,
                            requireNewTransaction,
                            mutation,
                            verifyRequired,
                            reportParameters,
                            reportResult,
                            method,
                            methodType );
    component.getActions().put( action.getName(), action );
  }

  private void addObserve( @Nonnull final ComponentDescriptor component,
                           @Nonnull final AnnotationMirror annotation,
                           @Nonnull final ExecutableElement method,
                           @Nonnull final ExecutableType methodType )
    throws ProcessorException
  {
    final String name = deriveObserveName( method, annotation );
    checkNameUnique( component, name, method, Constants.OBSERVE_ANNOTATION_CLASSNAME );
    final boolean mutation = AnnotationsUtil.getAnnotationValue( annotation, "mutation" );
    final boolean observeLowerPriorityDependencies =
      AnnotationsUtil.getAnnotationValue( annotation, "observeLowerPriorityDependencies" );
    final boolean nestedActionsAllowed = AnnotationsUtil.getAnnotationValue( annotation, "nestedActionsAllowed" );
    final VariableElement priority = AnnotationsUtil.getAnnotationValue( annotation, "priority" );
    final boolean reportParameters = AnnotationsUtil.getAnnotationValue( annotation, "reportParameters" );
    final boolean reportResult = AnnotationsUtil.getAnnotationValue( annotation, "reportResult" );
    final VariableElement executor = AnnotationsUtil.getAnnotationValue( annotation, "executor" );
    final VariableElement depType = AnnotationsUtil.getAnnotationValue( annotation, "depType" );

    component
      .findOrCreateObserve( name )
      .setObserveMethod( mutation,
                         toPriority( component.getDefaultPriority(), priority ),
                         executor.getSimpleName().toString().equals( "INTERNAL" ),
                         reportParameters,
                         reportResult,
                         depType.getSimpleName().toString(),
                         observeLowerPriorityDependencies,
                         nestedActionsAllowed,
                         method,
                         methodType );
  }

  @Nonnull
  private String deriveObserveName( @Nonnull final ExecutableElement method,
                                    @Nonnull final AnnotationMirror annotation )
    throws ProcessorException
  {
    final String name = AnnotationsUtil.getAnnotationValue( annotation, "name" );
    if ( Constants.SENTINEL.equals( name ) )
    {
      return method.getSimpleName().toString();
    }
    else
    {
      if ( !SourceVersion.isIdentifier( name ) )
      {
        throw new ProcessorException( "@Observe target specified an invalid name '" + name + "'. The " +
                                      "name must be a valid java identifier.", method );
      }
      else if ( SourceVersion.isKeyword( name ) )
      {
        throw new ProcessorException( "@Observe target specified an invalid name '" + name + "'. The " +
                                      "name must not be a java keyword.", method );
      }
      return name;
    }
  }

  private void addOnDepsChange( @Nonnull final ComponentDescriptor component,
                                @Nonnull final AnnotationMirror annotation,
                                @Nonnull final ExecutableElement method )
    throws ProcessorException
  {
    final String name =
      deriveHookName( component, method,
                      ON_DEPS_CHANGE_PATTERN,
                      "DepsChange",
                      AnnotationsUtil.getAnnotationValue( annotation, "name" ) );
    setOnDepsChange( component, component.findOrCreateObserve( name ), method );
  }

  private void addObserverRef( @Nonnull final ComponentDescriptor component,
                               @Nonnull final AnnotationMirror annotation,
                               @Nonnull final ExecutableElement method,
                               @Nonnull final ExecutableType methodType )
    throws ProcessorException
  {
    ArezUtils.mustBeStandardRefMethod( processingEnv,
                                       component,
                                       method,
                                       Constants.OBSERVER_REF_ANNOTATION_CLASSNAME );
    MemberChecks.mustReturnAnInstanceOf( processingEnv,
                                         method,
                                         Constants.OBSERVER_REF_ANNOTATION_CLASSNAME,
                                         Constants.OBSERVER_CLASSNAME );

    final String declaredName = AnnotationsUtil.getAnnotationValue( annotation, "name" );
    final String name;
    if ( Constants.SENTINEL.equals( declaredName ) )
    {
      name = ProcessorUtil.deriveName( method, OBSERVER_REF_PATTERN, declaredName );
      if ( null == name )
      {
        throw new ProcessorException( "Method annotated with @ObserverRef should specify name or be " +
                                      "named according to the convention get[Name]Observer", method );
      }
    }
    else
    {
      name = declaredName;
      if ( !SourceVersion.isIdentifier( name ) )
      {
        throw new ProcessorException( "@ObserverRef target specified an invalid name '" + name + "'. The " +
                                      "name must be a valid java identifier.", method );
      }
      else if ( SourceVersion.isKeyword( name ) )
      {
        throw new ProcessorException( "@ObserverRef target specified an invalid name '" + name + "'. The " +
                                      "name must not be a java keyword.", method );
      }
    }
    component.getObserverRefs().computeIfAbsent( name, s -> new ArrayList<>() )
      .add( new CandidateMethod( method, methodType ) );
  }

  private void addMemoize( @Nonnull final ComponentDescriptor component,
                           @Nonnull final AnnotationMirror annotation,
                           @Nonnull final ExecutableElement method,
                           @Nonnull final ExecutableType methodType )
    throws ProcessorException
  {
    final String name = deriveMemoizeName( method, annotation );
    checkNameUnique( component, name, method, Constants.MEMOIZE_ANNOTATION_CLASSNAME );
    final boolean keepAlive = AnnotationsUtil.getAnnotationValue( annotation, "keepAlive" );
    final boolean reportResult = AnnotationsUtil.getAnnotationValue( annotation, "reportResult" );
    final boolean observeLowerPriorityDependencies =
      AnnotationsUtil.getAnnotationValue( annotation, "observeLowerPriorityDependencies" );
    final VariableElement readOutsideTransaction =
      AnnotationsUtil.getAnnotationValue( annotation, "readOutsideTransaction" );
    final VariableElement priority = AnnotationsUtil.getAnnotationValue( annotation, "priority" );
    final VariableElement depType = AnnotationsUtil.getAnnotationValue( annotation, "depType" );
    final String depTypeAsString = depType.getSimpleName().toString();
    component.findOrCreateMemoize( name ).setMemoize( method,
                                                      methodType,
                                                      keepAlive,
                                                      toPriority( component.getDefaultPriority(),
                                                                  priority ),
                                                      reportResult,
                                                      observeLowerPriorityDependencies,
                                                      readOutsideTransaction.getSimpleName().toString(),
                                                      depTypeAsString );
  }

  @Nonnull
  private Priority toPriority( @Nullable final Priority defaultPriority,
                               @Nonnull final VariableElement priorityElement )
  {
    final String priorityName = priorityElement.getSimpleName().toString();
    return "DEFAULT".equals( priorityName ) ?
           null != defaultPriority ? defaultPriority : Priority.NORMAL :
           Priority.valueOf( priorityName );
  }

  private void autodetectObservableInitializers( @Nonnull final ComponentDescriptor component )
  {
    for ( final ObservableDescriptor observable : component.getObservables().values() )
    {
      if ( null == observable.getInitializer() && observable.hasGetter() )
      {
        if ( observable.hasSetter() )
        {
          final boolean initializer =
            autodetectInitializer( observable.getGetter() ) && autodetectInitializer( observable.getSetter() );
          observable.setInitializer( initializer );
        }
        else
        {
          observable.setInitializer( autodetectInitializer( observable.getGetter() ) );
        }
      }
    }
  }

  private boolean hasDependencyAnnotation( @Nonnull final ExecutableElement method )
  {
    return AnnotationsUtil.hasAnnotationOfType( method, Constants.COMPONENT_DEPENDENCY_ANNOTATION_CLASSNAME );
  }

  private void ensureTargetTypeAligns( @Nonnull final ComponentDescriptor component,
                                       @Nonnull final InverseDescriptor descriptor,
                                       @Nonnull final TypeMirror target )
  {
    if ( !processingEnv.getTypeUtils().isSameType( target, component.getElement().asType() ) )
    {
      throw new ProcessorException( "@Inverse target expected to find an associated @Reference annotation with " +
                                    "a target type equal to " + descriptor.getTargetType() + " but the actual " +
                                    "target type is " + target, descriptor.getObservable().getGetter() );
    }
  }

  @Nullable
  private TypeElement getInverseManyTypeTarget( @Nonnull final ProcessingEnvironment processingEnv,
                                                @Nonnull final ExecutableElement method )
  {
    final TypeName typeName = TypeName.get( method.getReturnType() );
    if ( typeName instanceof ParameterizedTypeName )
    {
      final ParameterizedTypeName type = (ParameterizedTypeName) typeName;
      if ( isSupportedInverseCollectionType( type.rawType.toString() ) && !type.typeArguments.isEmpty() )
      {
        final TypeElement typeElement =
          processingEnv.getElementUtils().getTypeElement( type.typeArguments.get( 0 ).toString() );
        if ( AnnotationsUtil.hasAnnotationOfType( typeElement, Constants.COMPONENT_ANNOTATION_CLASSNAME ) )
        {
          return typeElement;
        }
        else
        {
          throw new ProcessorException( "@Inverse target expected to return a type annotated with " +
                                        Constants.COMPONENT_ANNOTATION_CLASSNAME, method );
        }
      }
    }
    return null;
  }

  private boolean isSupportedInverseCollectionType( @Nonnull final String typeClassname )
  {
    return Collection.class.getName().equals( typeClassname ) ||
           Set.class.getName().equals( typeClassname ) ||
           List.class.getName().equals( typeClassname );
  }

  @Nonnull
  private String getInverseReferenceNameParameter( @Nonnull final ComponentDescriptor component,
                                                   @Nonnull final ExecutableElement method )
  {
    final String declaredName =
      (String) AnnotationsUtil.getAnnotationValue( method,
                                                   Constants.INVERSE_ANNOTATION_CLASSNAME,
                                                   "referenceName" ).getValue();
    final String name;
    if ( Constants.SENTINEL.equals( declaredName ) )
    {
      name = ProcessorUtil.firstCharacterToLowerCase( component.getElement().getSimpleName().toString() );
    }
    else
    {
      name = declaredName;
      if ( !SourceVersion.isIdentifier( name ) )
      {
        throw new ProcessorException( "@Inverse target specified an invalid referenceName '" + name + "'. The " +
                                      "name must be a valid java identifier.", method );
      }
      else if ( SourceVersion.isKeyword( name ) )
      {
        throw new ProcessorException( "@Inverse target specified an invalid referenceName '" + name + "'. The " +
                                      "name must not be a java keyword.", method );
      }
    }
    return name;
  }

  private void linkDependencies( @Nonnull final ComponentDescriptor component,
                                 @Nonnull final Collection<CandidateMethod> candidates )
  {
    component.getObservables()
      .values()
      .stream()
      .filter( ObservableDescriptor::hasGetter )
      .filter( o -> hasDependencyAnnotation( o.getGetter() ) )
      .forEach( o -> addOrUpdateDependency( component, o.getGetter(), o ) );

    component.getMemoizes()
      .values()
      .stream()
      .filter( MemoizeDescriptor::hasMemoize )
      .map( MemoizeDescriptor::getMethod )
      .filter( this::hasDependencyAnnotation )
      .forEach( method1 -> component.addDependency( createMethodDependencyDescriptor( component, method1 ) ) );

    candidates
      .stream()
      .map( CandidateMethod::getMethod )
      .filter( this::hasDependencyAnnotation )
      .forEach( method -> component.addDependency( createMethodDependencyDescriptor( component, method ) ) );
  }

  private void linkCascadeDisposeObservables( @Nonnull final ComponentDescriptor component )
  {
    for ( final ObservableDescriptor observable : component.getObservables().values() )
    {
      final CascadeDisposableDescriptor cascadeDisposableDescriptor = observable.getCascadeDisposableDescriptor();
      if ( null == cascadeDisposableDescriptor )
      {
        //@CascadeDisposable can only occur on getter so if we don't have it then we look in
        // cascadeDisposableDescriptor list to see if we can match getter
        final CascadeDisposableDescriptor descriptor = component.getCascadeDisposes().get( observable.getGetter() );
        if ( null != descriptor )
        {
          descriptor.setObservable( observable );
        }
      }
    }
  }

  private void linkCascadeDisposeReferences( @Nonnull final ComponentDescriptor component )
  {
    for ( final ReferenceDescriptor reference : component.getReferences().values() )
    {
      final CascadeDisposableDescriptor cascadeDisposableDescriptor = reference.getCascadeDisposableDescriptor();
      if ( null == cascadeDisposableDescriptor && reference.hasMethod() )
      {
        final CascadeDisposableDescriptor descriptor = component.getCascadeDisposes().get( reference.getMethod() );
        if ( null != descriptor )
        {
          descriptor.setReference( reference );
        }
      }
    }
  }

  private void linkObserverRefs( @Nonnull final ComponentDescriptor component )
  {
    for ( final Map.Entry<String, List<CandidateMethod>> entry : component.getObserverRefs().entrySet() )
    {
      final String key = entry.getKey();
      final List<CandidateMethod> methods = entry.getValue();
      final ObserveDescriptor observed = component.getObserves().get( key );
      if ( null != observed )
      {
        methods.stream().map( CandidateMethod::getMethod ).forEach( observed::addRefMethod );
      }
      else
      {
        throw new ProcessorException( "@ObserverRef target defined observer named '" + key + "' but no " +
                                      "@Observe method with that name exists", methods.get( 0 ).getMethod() );
      }
    }
  }

  @Nullable
  private Boolean isInitializerRequired( @Nonnull final ExecutableElement element )
  {
    final AnnotationMirror annotation =
      AnnotationsUtil.findAnnotationByType( element, Constants.OBSERVABLE_ANNOTATION_CLASSNAME );
    final AnnotationValue v =
      null == annotation ? null : AnnotationsUtil.findAnnotationValueNoDefaults( annotation, "initializer" );
    final String value = null == v ? "AUTODETECT" : ( (VariableElement) v.getValue() ).getSimpleName().toString();
    switch ( value )
    {
      case "ENABLE":
        return Boolean.TRUE;
      case "DISABLE":
        return Boolean.FALSE;
      default:
        return null;
    }
  }

  private boolean autodetectInitializer( @Nonnull final ExecutableElement element )
  {
    return element.getModifiers().contains( Modifier.ABSTRACT ) &&
           (
             (
               // Getter
               element.getReturnType().getKind() != TypeKind.VOID &&
               ProcessorUtil.hasNonnullAnnotation( element ) &&
               !AnnotationsUtil.hasAnnotationOfType( element, Constants.INVERSE_ANNOTATION_CLASSNAME )
             ) ||
             (
               // Setter
               1 == element.getParameters().size() &&
               AnnotationsUtil.hasAnnotationOfType( element.getParameters().get( 0 ),
                                                    Constants.NONNULL_ANNOTATION_CLASSNAME )
             )
           );
  }

  private void checkNameUnique( @Nonnull final ComponentDescriptor component, @Nonnull final String name,
                                @Nonnull final ExecutableElement sourceMethod,
                                @Nonnull final String sourceAnnotationName )
    throws ProcessorException
  {
    final ActionDescriptor action = component.getActions().get( name );
    if ( null != action )
    {
      throw toException( name,
                         sourceAnnotationName,
                         sourceMethod,
                         Constants.ACTION_ANNOTATION_CLASSNAME,
                         action.getAction() );
    }
    final MemoizeDescriptor memoize = component.getMemoizes().get( name );
    if ( null != memoize && memoize.hasMemoize() )
    {
      throw toException( name,
                         sourceAnnotationName,
                         sourceMethod,
                         Constants.MEMOIZE_ANNOTATION_CLASSNAME,
                         memoize.getMethod() );
    }
    // Observe have pairs so let the caller determine whether a duplicate occurs in that scenario
    if ( !sourceAnnotationName.equals( Constants.OBSERVE_ANNOTATION_CLASSNAME ) )
    {
      final ObserveDescriptor observed = component.getObserves().get( name );
      if ( null != observed )
      {
        throw toException( name,
                           sourceAnnotationName,
                           sourceMethod,
                           Constants.OBSERVE_ANNOTATION_CLASSNAME,
                           observed.getMethod() );
      }
    }
    // Observables have pairs so let the caller determine whether a duplicate occurs in that scenario
    if ( !sourceAnnotationName.equals( Constants.OBSERVABLE_ANNOTATION_CLASSNAME ) )
    {
      final ObservableDescriptor observable = component.getObservables().get( name );
      if ( null != observable )
      {
        throw toException( name,
                           sourceAnnotationName,
                           sourceMethod,
                           Constants.OBSERVABLE_ANNOTATION_CLASSNAME,
                           observable.getDefiner() );
      }
    }
  }

  @Nonnull
  private ProcessorException toException( @Nonnull final String name,
                                          @Nonnull final String sourceAnnotationName,
                                          @Nonnull final ExecutableElement sourceMethod,
                                          @Nonnull final String targetAnnotationName,
                                          @Nonnull final ExecutableElement targetElement )
  {
    return new ProcessorException( "Method annotated with " + MemberChecks.toSimpleName( sourceAnnotationName ) +
                                   " specified name " + name + " that duplicates " +
                                   MemberChecks.toSimpleName( targetAnnotationName ) + " defined by method " +
                                   targetElement.getSimpleName(), sourceMethod );
  }

  private void processComponentDependencyFields( @Nonnull final ComponentDescriptor component )
  {
    ProcessorUtil.getFieldElements( component.getElement() )
      .stream()
      .filter( f -> AnnotationsUtil.hasAnnotationOfType( f, Constants.COMPONENT_DEPENDENCY_ANNOTATION_CLASSNAME ) )
      .forEach( field -> processComponentDependencyField( component, field ) );
  }

  private void processComponentDependencyField( @Nonnull final ComponentDescriptor component,
                                                @Nonnull final VariableElement field )
  {
    verifyNoDuplicateAnnotations( field );
    MemberChecks.mustBeSubclassCallable( component.getElement(),
                                         Constants.COMPONENT_ANNOTATION_CLASSNAME,
                                         Constants.COMPONENT_DEPENDENCY_ANNOTATION_CLASSNAME,
                                         field );
    component.addDependency( createFieldDependencyDescriptor( component, field ) );
  }

  private void addReference( @Nonnull final ComponentDescriptor component,
                             @Nonnull final AnnotationMirror annotation,
                             @Nonnull final ExecutableElement method,
                             @Nonnull final ExecutableType methodType )
  {
    MemberChecks.mustNotHaveAnyParameters( Constants.REFERENCE_ANNOTATION_CLASSNAME, method );
    MemberChecks.mustBeSubclassCallable( component.getElement(),
                                         Constants.COMPONENT_ANNOTATION_CLASSNAME,
                                         Constants.REFERENCE_ANNOTATION_CLASSNAME,
                                         method );
    MemberChecks.mustNotThrowAnyExceptions( Constants.REFERENCE_ANNOTATION_CLASSNAME, method );
    MemberChecks.mustReturnAValue( Constants.REFERENCE_ANNOTATION_CLASSNAME, method );
    MemberChecks.mustBeAbstract( Constants.REFERENCE_ANNOTATION_CLASSNAME, method );

    final String name = getReferenceName( annotation, method );
    final String linkType = getLinkType( method );
    final String inverseName;
    final Multiplicity inverseMultiplicity;
    if ( hasInverse( annotation ) )
    {
      inverseMultiplicity = getReferenceInverseMultiplicity( annotation );
      inverseName = getReferenceInverseName( component, annotation, method, inverseMultiplicity );
      final TypeMirror returnType = method.getReturnType();
      if ( !( returnType instanceof DeclaredType ) ||
           !AnnotationsUtil.hasAnnotationOfType( ( (DeclaredType) returnType ).asElement(),
                                                 Constants.COMPONENT_ANNOTATION_CLASSNAME ) )
      {
        throw new ProcessorException( "@Reference target expected to return a type annotated with " +
                                      Constants.COMPONENT_ANNOTATION_CLASSNAME + " if there is an " +
                                      "inverse reference.", method );
      }
    }
    else
    {
      inverseName = null;
      inverseMultiplicity = null;
    }
    final ReferenceDescriptor descriptor = component.findOrCreateReference( name );
    descriptor.setMethod( method, methodType, linkType, inverseName, inverseMultiplicity );
    verifyMultiplicityOfAssociatedInverseMethod( component, descriptor );
  }

  private boolean hasInverse( @Nonnull final AnnotationMirror annotation )
  {
    final VariableElement variableElement = AnnotationsUtil.getAnnotationValue( annotation, "inverse" );
    switch ( variableElement.getSimpleName().toString() )
    {
      case "ENABLE":
        return true;
      case "DISABLE":
        return false;
      default:
        return null != AnnotationsUtil.findAnnotationValueNoDefaults( annotation, "inverseName" ) ||
               null != AnnotationsUtil.findAnnotationValueNoDefaults( annotation, "inverseMultiplicity" );
    }
  }

  private void verifyMultiplicityOfAssociatedReferenceMethod( @Nonnull final ComponentDescriptor component,
                                                              @Nonnull final InverseDescriptor descriptor )
  {
    final Multiplicity multiplicity =
      ProcessorUtil
        .getMethods( descriptor.getTargetType(),
                     processingEnv.getElementUtils(),
                     processingEnv.getTypeUtils() )
        .stream()
        .map( m -> {
          final AnnotationMirror a =
            AnnotationsUtil.findAnnotationByType( m, Constants.REFERENCE_ANNOTATION_CLASSNAME );
          if ( null != a && getReferenceName( a, m ).equals( descriptor.getReferenceName() ) )
          {
            if ( null == AnnotationsUtil.findAnnotationValueNoDefaults( a, "inverse" ) &&
                 null == AnnotationsUtil.findAnnotationValueNoDefaults( a, "inverseName" ) &&
                 null == AnnotationsUtil.findAnnotationValueNoDefaults( a, "inverseMultiplicity" ) )
            {
              throw new ProcessorException( "@Inverse target found an associated @Reference on the method '" +
                                            m.getSimpleName() + "' on type '" +
                                            descriptor.getTargetType().getQualifiedName() + "' but the " +
                                            "annotation has not configured an inverse.",
                                            descriptor.getObservable().getGetter() );
            }
            ensureTargetTypeAligns( component, descriptor, m.getReturnType() );
            return getReferenceInverseMultiplicity( a );
          }
          else
          {
            return null;
          }
        } )
        .filter( Objects::nonNull )
        .findAny()
        .orElse( null );
    if ( null == multiplicity )
    {
      throw new ProcessorException( "@Inverse target expected to find an associated @Reference annotation with " +
                                    "a name parameter equal to '" + descriptor.getReferenceName() + "' on class " +
                                    descriptor.getTargetType().getQualifiedName() + " but is unable to " +
                                    "locate a matching method.", descriptor.getObservable().getGetter() );
    }

    if ( descriptor.getMultiplicity() != multiplicity )
    {
      throw new ProcessorException( "@Inverse target has a multiplicity of " + descriptor.getMultiplicity() +
                                    " but that associated @Reference has a multiplicity of " + multiplicity +
                                    ". The multiplicity must align.", descriptor.getObservable().getGetter() );
    }
  }

  @Nonnull
  private String getLinkType( @Nonnull final ExecutableElement method )
  {
    final VariableElement injectParameter = (VariableElement)
      AnnotationsUtil.getAnnotationValue( method,
                                          Constants.REFERENCE_ANNOTATION_CLASSNAME,
                                          "load" ).getValue();
    return injectParameter.getSimpleName().toString();
  }

  @Nonnull
  private String getReferenceName( @Nonnull final AnnotationMirror annotation,
                                   @Nonnull final ExecutableElement method )
  {
    final String declaredName = AnnotationsUtil.getAnnotationValue( annotation, "name" );
    final String name;
    if ( Constants.SENTINEL.equals( declaredName ) )
    {
      final String candidate = ProcessorUtil.deriveName( method, GETTER_PATTERN, declaredName );
      if ( null == candidate )
      {
        name = method.getSimpleName().toString();
      }
      else
      {
        name = candidate;
      }
    }
    else
    {
      name = declaredName;
      if ( !SourceVersion.isIdentifier( name ) )
      {
        throw new ProcessorException( "@Reference target specified an invalid name '" + name + "'. The " +
                                      "name must be a valid java identifier.", method );
      }
      else if ( SourceVersion.isKeyword( name ) )
      {
        throw new ProcessorException( "@Reference target specified an invalid name '" + name + "'. The " +
                                      "name must not be a java keyword.", method );
      }
    }
    return name;
  }

  @Nonnull
  private Multiplicity getReferenceInverseMultiplicity( @Nonnull final AnnotationMirror annotation )
  {
    final VariableElement variableElement = AnnotationsUtil.getAnnotationValue( annotation, "inverseMultiplicity" );
    switch ( variableElement.getSimpleName().toString() )
    {
      case "MANY":
        return Multiplicity.MANY;
      case "ONE":
        return Multiplicity.ONE;
      default:
        return Multiplicity.ZERO_OR_ONE;
    }
  }

  @Nonnull
  private String getReferenceInverseName( @Nonnull final ComponentDescriptor component,
                                          @Nonnull final AnnotationMirror annotation,
                                          @Nonnull final ExecutableElement method,
                                          @Nonnull final Multiplicity multiplicity )
  {
    final String declaredName = AnnotationsUtil.getAnnotationValue( annotation, "inverseName" );
    final String name;
    if ( Constants.SENTINEL.equals( declaredName ) )
    {
      final String baseName = component.getElement().getSimpleName().toString();
      return ProcessorUtil.firstCharacterToLowerCase( baseName ) + ( Multiplicity.MANY == multiplicity ? "s" : "" );
    }
    else
    {
      name = declaredName;
      if ( !SourceVersion.isIdentifier( name ) )
      {
        throw new ProcessorException( "@Reference target specified an invalid inverseName '" + name + "'. The " +
                                      "inverseName must be a valid java identifier.", method );
      }
      else if ( SourceVersion.isKeyword( name ) )
      {
        throw new ProcessorException( "@Reference target specified an invalid inverseName '" + name + "'. The " +
                                      "inverseName must not be a java keyword.", method );
      }
    }
    return name;
  }

  private void ensureTargetTypeAligns( @Nonnull final ComponentDescriptor component,
                                       @Nonnull final ReferenceDescriptor descriptor,
                                       @Nonnull final TypeMirror target )
  {
    if ( !processingEnv.getTypeUtils().isSameType( target, component.getElement().asType() ) )
    {
      throw new ProcessorException( "@Reference target expected to find an associated @Inverse annotation with " +
                                    "a target type equal to " + component.getElement().getQualifiedName() + " but " +
                                    "the actual target type is " + target, descriptor.getMethod() );
    }
  }

  private void verifyMultiplicityOfAssociatedInverseMethod( @Nonnull final ComponentDescriptor component,
                                                            @Nonnull final ReferenceDescriptor descriptor )
  {
    final TypeElement element =
      (TypeElement) processingEnv.getTypeUtils().asElement( descriptor.getMethod().getReturnType() );
    final String defaultInverseName =
      descriptor.hasInverse() ?
      null :
      ProcessorUtil.firstCharacterToLowerCase( component.getElement().getSimpleName().toString() ) + "s";
    final Multiplicity multiplicity =
      ProcessorUtil
        .getMethods( element, processingEnv.getElementUtils(), processingEnv.getTypeUtils() )
        .stream()
        .map( m -> {
          final AnnotationMirror a = AnnotationsUtil.findAnnotationByType( m, Constants.INVERSE_ANNOTATION_CLASSNAME );
          if ( null == a )
          {
            return null;
          }
          final String inverseName = getInverseName( a, m );
          if ( !descriptor.hasInverse() && inverseName.equals( defaultInverseName ) )
          {
            throw new ProcessorException( "@Reference target has not configured an inverse but there is an " +
                                          "associated @Inverse annotated method named '" + m.getSimpleName() +
                                          "' on type '" + element.getQualifiedName() + "'.",
                                          descriptor.getMethod() );
          }
          if ( descriptor.hasInverse() && inverseName.equals( descriptor.getInverseName() ) )
          {
            final TypeElement target = getInverseManyTypeTarget( processingEnv, m );
            if ( null != target )
            {
              ensureTargetTypeAligns( component, descriptor, target.asType() );
              return Multiplicity.MANY;
            }
            else
            {
              ensureTargetTypeAligns( component, descriptor, m.getReturnType() );
              return ProcessorUtil.hasNonnullAnnotation( m ) ? Multiplicity.ONE : Multiplicity.ZERO_OR_ONE;
            }
          }
          else
          {
            return null;
          }
        } )
        .filter( Objects::nonNull )
        .findAny()
        .orElse( null );

    if ( descriptor.hasInverse() )
    {
      if ( null == multiplicity )
      {
        throw new ProcessorException( "@Reference target expected to find an associated @Inverse annotation " +
                                      "with a name parameter equal to '" + descriptor.getInverseName() + "' on " +
                                      "class " + descriptor.getMethod().getReturnType() + " but is unable to " +
                                      "locate a matching method.", descriptor.getMethod() );
      }

      final Multiplicity inverseMultiplicity = descriptor.getInverseMultiplicity();
      if ( inverseMultiplicity != multiplicity )
      {
        throw new ProcessorException( "@Reference target has an inverseMultiplicity of " + inverseMultiplicity +
                                      " but that associated @Inverse has a multiplicity of " + multiplicity +
                                      ". The multiplicity must align.", descriptor.getMethod() );
      }
    }
  }

  @Nonnull
  private DependencyDescriptor createMethodDependencyDescriptor( @Nonnull final ComponentDescriptor descriptor,
                                                                 @Nonnull final ExecutableElement method )
  {
    MemberChecks.mustNotHaveAnyParameters( Constants.COMPONENT_DEPENDENCY_ANNOTATION_CLASSNAME, method );
    MemberChecks.mustBeSubclassCallable( descriptor.getElement(),
                                         Constants.COMPONENT_ANNOTATION_CLASSNAME,
                                         Constants.COMPONENT_DEPENDENCY_ANNOTATION_CLASSNAME,
                                         method );
    MemberChecks.mustNotThrowAnyExceptions( Constants.COMPONENT_DEPENDENCY_ANNOTATION_CLASSNAME, method );
    MemberChecks.mustReturnAValue( Constants.COMPONENT_DEPENDENCY_ANNOTATION_CLASSNAME, method );

    final boolean validateTypeAtRuntime =
      (Boolean) AnnotationsUtil.getAnnotationValue( method,
                                                    Constants.COMPONENT_DEPENDENCY_ANNOTATION_CLASSNAME,
                                                    "validateTypeAtRuntime" ).getValue();

    final TypeMirror type = method.getReturnType();
    if ( TypeKind.DECLARED != type.getKind() )
    {
      throw new ProcessorException( "@ComponentDependency target must return a non-primitive value", method );
    }
    if ( !validateTypeAtRuntime )
    {
      final TypeElement disposeNotifier =
        processingEnv.getElementUtils().getTypeElement( Constants.DISPOSE_NOTIFIER_CLASSNAME );
      assert null != disposeNotifier;
      if ( !processingEnv.getTypeUtils().isAssignable( type, disposeNotifier.asType() ) )
      {
        final TypeElement typeElement = (TypeElement) processingEnv.getTypeUtils().asElement( type );
        if ( !isActAsComponentAnnotated( typeElement ) && !isDisposeTrackableComponent( typeElement ) )
        {
          throw new ProcessorException( "@ComponentDependency target must return an instance compatible with " +
                                        Constants.DISPOSE_NOTIFIER_CLASSNAME + " or a type annotated " +
                                        "with @ArezComponent(disposeNotifier=ENABLE) or @ActAsComponent", method );
        }
      }
    }

    final boolean cascade = isActionCascade( method );
    return new DependencyDescriptor( descriptor, method, cascade );
  }

  @Nonnull
  private DependencyDescriptor createFieldDependencyDescriptor( @Nonnull final ComponentDescriptor descriptor,
                                                                @Nonnull final VariableElement field )
  {
    MemberChecks.mustBeSubclassCallable( descriptor.getElement(),
                                         Constants.COMPONENT_ANNOTATION_CLASSNAME,
                                         Constants.COMPONENT_DEPENDENCY_ANNOTATION_CLASSNAME,
                                         field );
    MemberChecks.mustBeFinal( Constants.COMPONENT_DEPENDENCY_ANNOTATION_CLASSNAME, field );

    final boolean validateTypeAtRuntime =
      (Boolean) AnnotationsUtil.getAnnotationValue( field,
                                                    Constants.COMPONENT_DEPENDENCY_ANNOTATION_CLASSNAME,
                                                    "validateTypeAtRuntime" ).getValue();

    final TypeMirror type = field.asType();
    if ( TypeKind.DECLARED != type.getKind() )
    {
      throw new ProcessorException( "@ComponentDependency target must be a non-primitive value", field );
    }
    if ( !validateTypeAtRuntime )
    {
      final TypeElement disposeNotifier =
        processingEnv.getElementUtils().getTypeElement( Constants.DISPOSE_NOTIFIER_CLASSNAME );
      assert null != disposeNotifier;
      if ( !processingEnv.getTypeUtils().isAssignable( type, disposeNotifier.asType() ) )
      {
        final TypeElement typeElement = (TypeElement) processingEnv.getTypeUtils().asElement( type );
        if ( !isActAsComponentAnnotated( typeElement ) && !isDisposeTrackableComponent( typeElement ) )
        {
          throw new ProcessorException( "@ComponentDependency target must be an instance compatible with " +
                                        Constants.DISPOSE_NOTIFIER_CLASSNAME + " or a type annotated " +
                                        "with @ArezComponent(disposeNotifier=ENABLE) or @ActAsComponent", field );
        }
      }
    }

    if ( !isActionCascade( field ) )
    {
      throw new ProcessorException( "@ComponentDependency target defined an action of 'SET_NULL' but the " +
                                    "dependency is on a final field and can not be set to null.", field );

    }

    return new DependencyDescriptor( descriptor, field );
  }

  private boolean isActionCascade( @Nonnull final Element method )
  {
    final VariableElement parameter = (VariableElement)
      AnnotationsUtil.getAnnotationValue( method,
                                          Constants.COMPONENT_DEPENDENCY_ANNOTATION_CLASSNAME,
                                          "action" ).getValue();
    switch ( parameter.getSimpleName().toString() )
    {
      case "CASCADE":
        return true;
      case "SET_NULL":
      default:
        return false;
    }
  }

  @SuppressWarnings( "BooleanMethodIsAlwaysInverted" )
  private boolean isActAsComponentAnnotated( @Nonnull final TypeElement typeElement )
  {
    return AnnotationsUtil.hasAnnotationOfType( typeElement, Constants.ACT_AS_COMPONENT_ANNOTATION_CLASSNAME );
  }

  @SuppressWarnings( "BooleanMethodIsAlwaysInverted" )
  private boolean isDisposeTrackableComponent( @Nonnull final TypeElement typeElement )
  {
    return AnnotationsUtil.hasAnnotationOfType( typeElement, Constants.COMPONENT_ANNOTATION_CLASSNAME ) &&
           ProcessorUtil.isDisposableTrackableRequired( typeElement );
  }

  @Nonnull
  @Override
  protected String getRootAnnotationClassname()
  {
    return Constants.COMPONENT_ANNOTATION_CLASSNAME;
  }

  @Override
  protected void process( @Nonnull final TypeElement element )
    throws IOException, ProcessorException
  {
    final ComponentDescriptor descriptor = parse( element );
    final String packageName = descriptor.getPackageName();
    emitTypeSpec( packageName, ComponentGenerator.buildType( processingEnv, descriptor ) );
    if ( descriptor.needsDaggerIntegration() )
    {
      if ( descriptor.needsDaggerComponentExtension() )
      {
        assert InjectMode.CONSUME == descriptor.getInjectMode();
        emitTypeSpec( packageName, DaggerComponentExtensionGenerator.buildType( processingEnv, descriptor ) );
      }
      else if ( descriptor.needsDaggerModule() )
      {
        emitTypeSpec( packageName, DaggerModuleGenerator.buildType( processingEnv, descriptor ) );
      }
    }
    if ( descriptor.hasRepository() )
    {
      emitTypeSpec( packageName, RepositoryGenerator.buildType( processingEnv, descriptor.getRepository() ) );
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
    final VariableElement defaultReadOutsideTransaction =
      getAnnotationParameter( arezComponent, "defaultReadOutsideTransaction" );
    final VariableElement defaultWriteOutsideTransaction =
      getAnnotationParameter( arezComponent, "defaultWriteOutsideTransaction" );

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
      new ComponentDescriptor( type,
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
                               typeElement,
                               defaultReadOutsideTransaction.getSimpleName().toString().equals( "ENABLE" ),
                               defaultWriteOutsideTransaction.getSimpleName().toString().equals( "ENABLE" ) );

    analyzeCandidateMethods( descriptor, methods, processingEnv.getTypeUtils() );
    validate( allowEmpty, descriptor );

    for ( final ObservableDescriptor observable : descriptor.getObservables().values() )
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
        AnnotationsUtil.getTypeElementsAnnotationParameter( processingEnv,
                                                            typeElement,
                                                            Constants.REPOSITORY_ANNOTATION_CLASSNAME,
                                                            "extensions" );
      final String repositoryInjectConfig = getRepositoryInjectMode( repository );
      final String repositoryDaggerConfig = getRepositoryDaggerConfig( repository );
      final boolean shouldRepositoryDefineCreate = shouldRepositoryDefineCreate( descriptor );
      final boolean shouldRepositoryDefineAttach = shouldRepositoryDefineAttach( descriptor );
      final boolean shouldRepositoryDefineDestroy = shouldRepositoryDefineDestroy( descriptor );
      final boolean shouldRepositoryDefineDetach = shouldRepositoryDefineDetach( descriptor );
      for ( final TypeElement extension : extensions )
      {
        if ( ElementKind.INTERFACE != extension.getKind() )
        {
          throw new ProcessorException( "Class annotated with @Repository defined an extension that is " +
                                        "not an interface. Extension: " + extension.getQualifiedName(),
                                        descriptor.getElement() );
        }

        for ( final Element enclosedElement : extension.getEnclosedElements() )
        {
          if ( ElementKind.METHOD == enclosedElement.getKind() )
          {
            final ExecutableElement method = (ExecutableElement) enclosedElement;
            if ( !method.isDefault() &&
                 !( method.getSimpleName().toString().equals( "self" ) && 0 == method.getParameters().size() ) )
            {
              throw new ProcessorException( "Class annotated with @Repository defined an extension that has " +
                                            "a non default method. Extension: " + extension.getQualifiedName() +
                                            " Method: " + method, descriptor.getElement() );
            }
          }
        }
      }
      descriptor.configureRepository( extensions,
                                      repositoryInjectConfig,
                                      repositoryDaggerConfig,
                                      shouldRepositoryDefineCreate,
                                      shouldRepositoryDefineAttach,
                                      shouldRepositoryDefineDestroy,
                                      shouldRepositoryDefineDetach );
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

  private void analyzeCandidateMethods( @Nonnull final ComponentDescriptor componentDescriptor,
                                        @Nonnull final List<ExecutableElement> methods,
                                        @Nonnull final Types typeUtils )
    throws ProcessorException
  {
    for ( final ExecutableElement method : methods )
    {
      final String methodName = method.getSimpleName().toString();
      if ( AREZ_SPECIAL_METHODS.contains( methodName ) && method.getParameters().isEmpty() )
      {
        throw new ProcessorException( "Method defined on a class annotated by @ArezComponent uses a name " +
                                      "reserved by Arez", method );
      }
      else if ( methodName.startsWith( ComponentGenerator.FIELD_PREFIX ) ||
                methodName.startsWith( ComponentGenerator.OBSERVABLE_DATA_FIELD_PREFIX ) ||
                methodName.startsWith( ComponentGenerator.REFERENCE_FIELD_PREFIX ) ||
                methodName.startsWith( ComponentGenerator.FRAMEWORK_PREFIX ) )
      {
        throw new ProcessorException( "Method defined on a class annotated by @ArezComponent uses a name " +
                                      "with a prefix reserved by Arez", method );
      }
    }
    final Map<String, CandidateMethod> getters = new HashMap<>();
    final Map<String, CandidateMethod> setters = new HashMap<>();
    final Map<String, CandidateMethod> observes = new HashMap<>();
    final Map<String, CandidateMethod> onDepsChanges = new HashMap<>();
    for ( final ExecutableElement method : methods )
    {
      final ExecutableType methodType =
        (ExecutableType) typeUtils.asMemberOf( (DeclaredType) componentDescriptor.getElement().asType(), method );
      if ( !analyzeMethod( componentDescriptor, method, methodType ) )
      {
        /*
         * If we get here the method was not annotated so we can try to detect if it is a
         * candidate arez method in case some arez annotations are implied via naming conventions.
         */
        if ( method.getModifiers().contains( Modifier.STATIC ) )
        {
          continue;
        }

        final CandidateMethod candidateMethod = new CandidateMethod( method, methodType );
        final boolean voidReturn = method.getReturnType().getKind() == TypeKind.VOID;
        final int parameterCount = method.getParameters().size();
        String name;

        if ( !method.getModifiers().contains( Modifier.FINAL ) )
        {
          name = ProcessorUtil.deriveName( method, SETTER_PATTERN, Constants.SENTINEL );
          if ( voidReturn && 1 == parameterCount && null != name )
          {
            setters.put( name, candidateMethod );
            continue;
          }
          name = ProcessorUtil.deriveName( method, ISSER_PATTERN, Constants.SENTINEL );
          if ( !voidReturn && 0 == parameterCount && null != name )
          {
            getters.put( name, candidateMethod );
            continue;
          }
          name = ProcessorUtil.deriveName( method, GETTER_PATTERN, Constants.SENTINEL );
          if ( !voidReturn && 0 == parameterCount && null != name )
          {
            getters.put( name, candidateMethod );
            continue;
          }
        }
        name = ProcessorUtil.deriveName( method, ON_DEPS_CHANGE_PATTERN, Constants.SENTINEL );
        if ( voidReturn && null != name )
        {
          if ( 0 == parameterCount ||
               (
                 1 == parameterCount &&
                 Constants.OBSERVER_CLASSNAME.equals( method.getParameters().get( 0 ).asType().toString() )
               )
          )
          {
            onDepsChanges.put( name, candidateMethod );
            continue;
          }
        }

        final String methodName = method.getSimpleName().toString();
        if ( !OBJECT_METHODS.contains( methodName ) )
        {
          observes.put( methodName, candidateMethod );
        }
      }
    }

    linkUnAnnotatedObservables( componentDescriptor, getters, setters );
    linkUnAnnotatedObserves( componentDescriptor, observes, onDepsChanges );
    linkObserverRefs( componentDescriptor );
    linkCascadeDisposeObservables( componentDescriptor );
    linkCascadeDisposeReferences( componentDescriptor );

    // CascadeDispose returned false but it was actually processed so lets remove them from getters set

    componentDescriptor.getCascadeDisposes().keySet().forEach( method -> {
      for ( final Map.Entry<String, CandidateMethod> entry : new HashMap<>( getters ).entrySet() )
      {
        if ( method.equals( entry.getValue().getMethod() ) )
        {
          getters.remove( entry.getKey() );
        }
      }
    } );

    linkDependencies( componentDescriptor, getters.values() );

    autodetectObservableInitializers( componentDescriptor );

    /*
     * All of the maps will have called remove() for all matching candidates.
     * Thus any left are the non-arez methods.
     */

    ensureNoAbstractMethods( componentDescriptor, getters.values() );
    ensureNoAbstractMethods( componentDescriptor, setters.values() );
    ensureNoAbstractMethods( componentDescriptor, observes.values() );
    ensureNoAbstractMethods( componentDescriptor, onDepsChanges.values() );

    processCascadeDisposeFields( componentDescriptor );
    processComponentDependencyFields( componentDescriptor );
  }

  private void ensureNoAbstractMethods( @Nonnull final ComponentDescriptor componentDescriptor,
                                        @Nonnull final Collection<CandidateMethod> candidateMethods )
  {
    candidateMethods
      .stream()
      .map( CandidateMethod::getMethod )
      .filter( m -> m.getModifiers().contains( Modifier.ABSTRACT ) )
      .forEach( m -> {
        throw new ProcessorException( "@ArezComponent target has an abstract method not implemented by " +
                                      "framework. The method is named " + m.getSimpleName(),
                                      componentDescriptor.getElement() );
      } );
  }

  private boolean analyzeMethod( @Nonnull final ComponentDescriptor descriptor,
                                 @Nonnull final ExecutableElement method,
                                 @Nonnull final ExecutableType methodType )
    throws ProcessorException
  {
    verifyNoDuplicateAnnotations( method );

    final AnnotationMirror action =
      AnnotationsUtil.findAnnotationByType( method, Constants.ACTION_ANNOTATION_CLASSNAME );
    final AnnotationMirror observed =
      AnnotationsUtil.findAnnotationByType( method, Constants.OBSERVE_ANNOTATION_CLASSNAME );
    final AnnotationMirror observable =
      AnnotationsUtil.findAnnotationByType( method, Constants.OBSERVABLE_ANNOTATION_CLASSNAME );
    final AnnotationMirror observableValueRef =
      AnnotationsUtil.findAnnotationByType( method, Constants.OBSERVABLE_VALUE_REF_ANNOTATION_CLASSNAME );
    final AnnotationMirror memoize =
      AnnotationsUtil.findAnnotationByType( method, Constants.MEMOIZE_ANNOTATION_CLASSNAME );
    final AnnotationMirror computableValueRef =
      AnnotationsUtil.findAnnotationByType( method, Constants.COMPUTABLE_VALUE_REF_ANNOTATION_CLASSNAME );
    final AnnotationMirror contextRef =
      AnnotationsUtil.findAnnotationByType( method, Constants.CONTEXT_REF_ANNOTATION_CLASSNAME );
    final AnnotationMirror stateRef =
      AnnotationsUtil.findAnnotationByType( method, Constants.COMPONENT_STATE_REF_ANNOTATION_CLASSNAME );
    final AnnotationMirror componentRef =
      AnnotationsUtil.findAnnotationByType( method, Constants.COMPONENT_REF_ANNOTATION_CLASSNAME );
    final AnnotationMirror componentId =
      AnnotationsUtil.findAnnotationByType( method, Constants.COMPONENT_ID_ANNOTATION_CLASSNAME );
    final AnnotationMirror componentIdRef =
      AnnotationsUtil.findAnnotationByType( method, Constants.COMPONENT_ID_REF_ANNOTATION_CLASSNAME );
    final AnnotationMirror componentTypeName =
      AnnotationsUtil.findAnnotationByType( method, Constants.COMPONENT_TYPE_NAME_REF_ANNOTATION_CLASSNAME );
    final AnnotationMirror componentNameRef =
      AnnotationsUtil.findAnnotationByType( method, Constants.COMPONENT_NAME_REF_ANNOTATION_CLASSNAME );
    final AnnotationMirror postConstruct =
      AnnotationsUtil.findAnnotationByType( method, Constants.POST_CONSTRUCT_ANNOTATION_CLASSNAME );
    final AnnotationMirror ejbPostConstruct =
      AnnotationsUtil.findAnnotationByType( method, Constants.EJB_POST_CONSTRUCT_ANNOTATION_CLASSNAME );
    final AnnotationMirror preDispose =
      AnnotationsUtil.findAnnotationByType( method, Constants.PRE_DISPOSE_ANNOTATION_CLASSNAME );
    final AnnotationMirror postDispose =
      AnnotationsUtil.findAnnotationByType( method, Constants.POST_DISPOSE_ANNOTATION_CLASSNAME );
    final AnnotationMirror onActivate =
      AnnotationsUtil.findAnnotationByType( method, Constants.ON_ACTIVATE_ANNOTATION_CLASSNAME );
    final AnnotationMirror onDeactivate =
      AnnotationsUtil.findAnnotationByType( method, Constants.ON_DEACTIVATE_ANNOTATION_CLASSNAME );
    final AnnotationMirror onDepsChange =
      AnnotationsUtil.findAnnotationByType( method, Constants.ON_DEPS_CHANGE_ANNOTATION_CLASSNAME );
    final AnnotationMirror observerRef =
      AnnotationsUtil.findAnnotationByType( method, Constants.OBSERVER_REF_ANNOTATION_CLASSNAME );
    final AnnotationMirror dependency =
      AnnotationsUtil.findAnnotationByType( method, Constants.COMPONENT_DEPENDENCY_ANNOTATION_CLASSNAME );
    final AnnotationMirror reference =
      AnnotationsUtil.findAnnotationByType( method, Constants.REFERENCE_ANNOTATION_CLASSNAME );
    final AnnotationMirror referenceId =
      AnnotationsUtil.findAnnotationByType( method, Constants.REFERENCE_ID_ANNOTATION_CLASSNAME );
    final AnnotationMirror inverse =
      AnnotationsUtil.findAnnotationByType( method, Constants.INVERSE_ANNOTATION_CLASSNAME );
    final AnnotationMirror cascadeDispose =
      AnnotationsUtil.findAnnotationByType( method, Constants.CASCADE_DISPOSE_ANNOTATION_CLASSNAME );

    if ( null != observable )
    {
      final ObservableDescriptor observableDescriptor = addObservable( descriptor,
                                                                       observable, method, methodType );
      if ( null != referenceId )
      {
        addReferenceId( descriptor, referenceId, observableDescriptor, method );
      }
      if ( null != inverse )
      {
        addInverse( descriptor, inverse, observableDescriptor, method );
      }
      if ( null != cascadeDispose )
      {
        addCascadeDisposeMethod( descriptor, method, observableDescriptor );
      }
      return true;
    }
    else if ( null != observableValueRef )
    {
      addObservableValueRef( descriptor, observableValueRef, method, methodType );
      return true;
    }
    else if ( null != action )
    {
      addAction( descriptor, action, method, methodType );
      return true;
    }
    else if ( null != observed )
    {
      addObserve( descriptor, observed, method, methodType );
      return true;
    }
    else if ( null != onDepsChange )
    {
      addOnDepsChange( descriptor, onDepsChange, method );
      return true;
    }
    else if ( null != observerRef )
    {
      addObserverRef( descriptor, observerRef, method, methodType );
      return true;
    }
    else if ( null != contextRef )
    {
      addContextRef( descriptor, method );
      return true;
    }
    else if ( null != stateRef )
    {
      addComponentStateRef( descriptor, stateRef, method );
      return true;
    }
    else if ( null != memoize )
    {
      addMemoize( descriptor, memoize, method, methodType );
      return true;
    }
    else if ( null != computableValueRef )
    {
      addComputableValueRef( descriptor, computableValueRef, method, methodType );
      return true;
    }
    else if ( null != reference )
    {
      if ( null != cascadeDispose )
      {
        addCascadeDisposeMethod( descriptor, method, null );
      }
      addReference( descriptor, reference, method, methodType );
      return true;
    }
    else if ( null != cascadeDispose )
    {
      addCascadeDisposeMethod( descriptor, method, null );
      // Return false so that it can be picked as the getter of an @Observable or linked to a @Reference
      return false;
    }
    else if ( null != componentIdRef )
    {
      addComponentIdRef( descriptor, method );
      return true;
    }
    else if ( null != componentRef )
    {
      addComponentRef( descriptor, method );
      return true;
    }
    else if ( null != componentId )
    {
      setComponentId( descriptor, method, methodType );
      return true;
    }
    else if ( null != componentNameRef )
    {
      addComponentNameRef( descriptor, method );
      return true;
    }
    else if ( null != componentTypeName )
    {
      setComponentTypeNameRef( descriptor, method );
      return true;
    }
    else if ( null != ejbPostConstruct )
    {
      throw new ProcessorException( "@" + Constants.EJB_POST_CONSTRUCT_ANNOTATION_CLASSNAME + " annotation " +
                                    "not supported in components annotated with @ArezComponent, use the @" +
                                    Constants.POST_CONSTRUCT_ANNOTATION_CLASSNAME + " annotation instead.",
                                    method );
    }
    else if ( null != postConstruct )
    {
      addPostConstruct( descriptor, method );
      return true;
    }
    else if ( null != preDispose )
    {
      addPreDispose( descriptor, method );
      return true;
    }
    else if ( null != postDispose )
    {
      addPostDispose( descriptor, method );
      return true;
    }
    else if ( null != onActivate )
    {
      addOnActivate( descriptor, onActivate, method );
      return true;
    }
    else if ( null != onDeactivate )
    {
      addOnDeactivate( descriptor, onDeactivate, method );
      return true;
    }
    else if ( null != dependency )
    {
      descriptor.addDependency( createMethodDependencyDescriptor( descriptor, method ) );
      return false;
    }
    else if ( null != referenceId )
    {
      addReferenceId( descriptor, referenceId, method );
      return true;
    }
    else if ( null != inverse )
    {
      addInverse( descriptor, inverse, method, methodType );
      return true;
    }
    else
    {
      return false;
    }
  }

  private void addReferenceId( @Nonnull final ComponentDescriptor descriptor,
                               @Nonnull final AnnotationMirror annotation,
                               @Nonnull final ObservableDescriptor observable,
                               @Nonnull final ExecutableElement method )
  {
    MemberChecks.mustNotHaveAnyParameters( Constants.REFERENCE_ID_ANNOTATION_CLASSNAME, method );
    MemberChecks.mustBeSubclassCallable( descriptor.getElement(),
                                         Constants.COMPONENT_ANNOTATION_CLASSNAME,
                                         Constants.REFERENCE_ID_ANNOTATION_CLASSNAME,
                                         method );
    MemberChecks.mustNotThrowAnyExceptions( Constants.REFERENCE_ID_ANNOTATION_CLASSNAME, method );
    MemberChecks.mustReturnAValue( Constants.REFERENCE_ID_ANNOTATION_CLASSNAME, method );

    final String name = getReferenceIdName( annotation, method );
    descriptor.findOrCreateReference( name ).setObservable( observable );
  }

  private void addReferenceId( @Nonnull final ComponentDescriptor descriptor,
                               @Nonnull final AnnotationMirror annotation,
                               @Nonnull final ExecutableElement method )
  {
    MemberChecks.mustNotHaveAnyParameters( Constants.REFERENCE_ID_ANNOTATION_CLASSNAME, method );
    MemberChecks.mustBeSubclassCallable( descriptor.getElement(),
                                         Constants.COMPONENT_ANNOTATION_CLASSNAME,
                                         Constants.REFERENCE_ID_ANNOTATION_CLASSNAME,
                                         method );
    MemberChecks.mustNotThrowAnyExceptions( Constants.REFERENCE_ID_ANNOTATION_CLASSNAME, method );
    MemberChecks.mustReturnAValue( Constants.REFERENCE_ID_ANNOTATION_CLASSNAME, method );

    final String name = getReferenceIdName( annotation, method );
    descriptor.findOrCreateReference( name ).setIdMethod( method );
  }

  @Nonnull
  private String getReferenceIdName( @Nonnull final AnnotationMirror annotation,
                                     @Nonnull final ExecutableElement method )
  {
    final String declaredName = AnnotationsUtil.getAnnotationValue( annotation, "name" );
    final String name;
    if ( Constants.SENTINEL.equals( declaredName ) )
    {
      final String candidate = ProcessorUtil.deriveName( method, ID_GETTER_PATTERN, declaredName );
      if ( null == candidate )
      {
        final String candidate2 = ProcessorUtil.deriveName( method, RAW_ID_GETTER_PATTERN, declaredName );
        if ( null == candidate2 )
        {
          throw new ProcessorException( "@ReferenceId target has not specified a name and does not follow " +
                                        "the convention \"get[Name]Id\" or \"[name]Id\"", method );
        }
        else
        {
          name = candidate2;
        }
      }
      else
      {
        name = candidate;
      }
    }
    else
    {
      name = declaredName;
      if ( !SourceVersion.isIdentifier( name ) )
      {
        throw new ProcessorException( "@ReferenceId target specified an invalid name '" + name + "'. The " +
                                      "name must be a valid java identifier.", method );
      }
      else if ( SourceVersion.isKeyword( name ) )
      {
        throw new ProcessorException( "@ReferenceId target specified an invalid name '" + name + "'. The " +
                                      "name must not be a java keyword.", method );
      }
    }
    return name;
  }

  private void addInverse( @Nonnull final ComponentDescriptor descriptor,
                           @Nonnull final AnnotationMirror annotation,
                           @Nonnull final ExecutableElement method,
                           @Nonnull final ExecutableType methodType )
  {
    MemberChecks.mustNotHaveAnyParameters( Constants.INVERSE_ANNOTATION_CLASSNAME, method );
    MemberChecks.mustBeSubclassCallable( descriptor.getElement(),
                                         Constants.COMPONENT_ANNOTATION_CLASSNAME,
                                         Constants.INVERSE_ANNOTATION_CLASSNAME,
                                         method );
    MemberChecks.mustNotThrowAnyExceptions( Constants.INVERSE_ANNOTATION_CLASSNAME, method );
    MemberChecks.mustReturnAValue( Constants.INVERSE_ANNOTATION_CLASSNAME, method );
    MemberChecks.mustBeAbstract( Constants.INVERSE_ANNOTATION_CLASSNAME, method );

    final String name = getInverseName( annotation, method );
    final ObservableDescriptor observable = descriptor.findOrCreateObservable( name );
    observable.setGetter( method, methodType );

    addInverse( descriptor, annotation, observable, method );
  }

  private void addInverse( @Nonnull final ComponentDescriptor descriptor,
                           @Nonnull final AnnotationMirror annotation,
                           @Nonnull final ObservableDescriptor observable,
                           @Nonnull final ExecutableElement method )
  {
    MemberChecks.mustNotHaveAnyParameters( Constants.INVERSE_ANNOTATION_CLASSNAME, method );
    MemberChecks.mustBeSubclassCallable( descriptor.getElement(),
                                         Constants.COMPONENT_ANNOTATION_CLASSNAME,
                                         Constants.INVERSE_ANNOTATION_CLASSNAME,
                                         method );
    MemberChecks.mustNotThrowAnyExceptions( Constants.INVERSE_ANNOTATION_CLASSNAME, method );
    MemberChecks.mustReturnAValue( Constants.INVERSE_ANNOTATION_CLASSNAME, method );
    MemberChecks.mustBeAbstract( Constants.INVERSE_ANNOTATION_CLASSNAME, method );

    final String name = getInverseName( annotation, method );
    final InverseDescriptor existing = descriptor.getInverses().get( name );
    if ( null != existing )
    {
      throw new ProcessorException( "@Inverse target defines duplicate inverse for name '" + name +
                                    "'. The other inverse is " + existing.getObservable().getGetter(),
                                    method );
    }
    else
    {
      final TypeMirror type = method.getReturnType();

      final Multiplicity multiplicity;
      TypeElement targetType = getInverseManyTypeTarget( processingEnv, method );
      if ( null != targetType )
      {
        multiplicity = Multiplicity.MANY;
      }
      else
      {
        if ( !( type instanceof DeclaredType ) ||
             !AnnotationsUtil.hasAnnotationOfType( ( (DeclaredType) type ).asElement(),
                                                   Constants.COMPONENT_ANNOTATION_CLASSNAME ) )
        {
          throw new ProcessorException( "@Inverse target expected to return a type annotated with " +
                                        Constants.COMPONENT_ANNOTATION_CLASSNAME, method );
        }
        targetType = (TypeElement) ( (DeclaredType) type ).asElement();
        if ( ProcessorUtil.hasNonnullAnnotation( method ) )
        {
          multiplicity = Multiplicity.ONE;
        }
        else if ( AnnotationsUtil.hasAnnotationOfType( method, Constants.NULLABLE_ANNOTATION_CLASSNAME ) )
        {
          multiplicity = Multiplicity.ZERO_OR_ONE;
        }
        else
        {
          throw new ProcessorException( "@Inverse target expected to be annotated with either " +
                                        Constants.NULLABLE_ANNOTATION_CLASSNAME + " or " +
                                        Constants.NONNULL_ANNOTATION_CLASSNAME, method );
        }
      }
      final String referenceName = getInverseReferenceNameParameter( descriptor, method );
      final InverseDescriptor inverse =
        new InverseDescriptor( descriptor, observable, referenceName, multiplicity, targetType );
      descriptor.getInverses().put( name, inverse );
      verifyMultiplicityOfAssociatedReferenceMethod( descriptor, inverse );
    }
  }

  @Nonnull
  private String getInverseName( @Nonnull final AnnotationMirror annotation,
                                 @Nonnull final ExecutableElement method )
  {
    final String declaredName = AnnotationsUtil.getAnnotationValue( annotation, "name" );
    final String name;
    if ( Constants.SENTINEL.equals( declaredName ) )
    {
      final String candidate = ProcessorUtil.deriveName( method, GETTER_PATTERN, declaredName );
      name = null == candidate ? method.getSimpleName().toString() : candidate;
    }
    else
    {
      name = declaredName;
      if ( !SourceVersion.isIdentifier( name ) )
      {
        throw new ProcessorException( "@Inverse target specified an invalid name '" + name + "'. The " +
                                      "name must be a valid java identifier.", method );
      }
      else if ( SourceVersion.isKeyword( name ) )
      {
        throw new ProcessorException( "@Inverse target specified an invalid name '" + name + "'. The " +
                                      "name must not be a java keyword.", method );
      }
    }
    return name;
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

    for ( final ObservableDescriptor observable : descriptor.getObservables().values() )
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
