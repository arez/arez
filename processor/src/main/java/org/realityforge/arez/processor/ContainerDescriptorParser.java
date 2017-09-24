package org.realityforge.arez.processor;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.Autorun;
import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.ContainerId;
import org.realityforge.arez.annotations.Observable;
import org.realityforge.arez.annotations.OnActivate;
import org.realityforge.arez.annotations.OnDeactivate;
import org.realityforge.arez.annotations.OnDispose;
import org.realityforge.arez.annotations.OnStale;
import org.realityforge.arez.annotations.PostDispose;
import org.realityforge.arez.annotations.PreDispose;

final class ContainerDescriptorParser
{
  private static final Pattern ON_ACTIVATE_PATTERN = Pattern.compile( "^on([A-Z].*)Activate$" );
  private static final Pattern ON_DEACTIVATE_PATTERN = Pattern.compile( "^on([A-Z].*)Deactivate$" );
  private static final Pattern ON_STALE_PATTERN = Pattern.compile( "^on([A-Z].*)Stale$" );
  private static final Pattern ON_DISPOSE_PATTERN = Pattern.compile( "^on([A-Z].*)Dispose$" );
  private static final Pattern GETTER_PATTERN = Pattern.compile( "^get([A-Z].*)$" );
  private static final Pattern ISSER_PATTERN = Pattern.compile( "^is([A-Z].*)$" );
  private static final Pattern SETTER_PATTERN = Pattern.compile( "^set([A-Z].*)$" );

  private ContainerDescriptorParser()
  {
  }

  static ContainerDescriptor parse( @Nonnull final Element element, @Nonnull final Elements elementUtils )
    throws ArezProcessorException
  {
    final PackageElement packageElement = elementUtils.getPackageOf( element );
    final TypeElement typeElement = (TypeElement) element;
    if ( ElementKind.CLASS != element.getKind() )
    {
      throw new ArezProcessorException( "@Container target must be a class", element );
    }
    else if ( element.getModifiers().contains( Modifier.ABSTRACT ) )
    {
      throw new ArezProcessorException( "@Container target must not be abstract", element );
    }
    else if ( element.getModifiers().contains( Modifier.FINAL ) )
    {
      throw new ArezProcessorException( "@Container target must not be final", element );
    }
    else if ( NestingKind.TOP_LEVEL != typeElement.getNestingKind() &&
              !element.getModifiers().contains( Modifier.STATIC ) )
    {
      throw new ArezProcessorException( "@Container target must not be a non-static nested class", element );
    }
    final Container container = typeElement.getAnnotation( Container.class );
    final String name =
      ProcessorUtil.isSentinelName( container.name() ) ? typeElement.getSimpleName().toString() : container.name();

    final ContainerDescriptor descriptor =
      new ContainerDescriptor( name, container.singleton(), container.disposable(), packageElement, typeElement );

    processMethods( typeElement, descriptor );

    descriptor.validate();

    return descriptor;
  }

  private static void processMethods( @Nonnull final TypeElement typeElement,
                                      @Nonnull final ContainerDescriptor descriptor )
    throws ArezProcessorException
  {
    final Map<String, ExecutableElement> getters = new HashMap<>();
    final Map<String, ExecutableElement> setters = new HashMap<>();
    for ( final ExecutableElement method : ProcessorUtil.getMethods( typeElement ) )
    {
      if ( !processMethod( descriptor, method ) )
      {
        /*
         * If we get here the method was not annotated so we can try to detect if it is a
         * candidate @Observable in case some @Observables are not fully specified.
         */
        if ( method.getModifiers().contains( Modifier.FINAL ) )
        {
          continue;
        }
        else if ( method.getModifiers().contains( Modifier.STATIC ) )
        {
          continue;
        }

        final boolean voidReturn = method.getReturnType().getKind() == TypeKind.VOID;
        final int parameterCount = method.getParameters().size();
        final String methodName = method.getSimpleName().toString();
        if ( voidReturn &&
             1 == parameterCount &&
             methodName.startsWith( "set" ) &&
             ( methodName.length() > 3 && Character.isUpperCase( methodName.charAt( 3 ) ) ) )
        {
          final String observableName = Character.toLowerCase( methodName.charAt( 3 ) ) + methodName.substring( 4 );
          setters.put( observableName, method );
        }
        else if ( !voidReturn &&
                  0 == parameterCount &&
                  methodName.startsWith( "get" ) &&
                  ( methodName.length() > 3 && Character.isUpperCase( methodName.charAt( 3 ) ) ) )
        {
          final String observableName = Character.toLowerCase( methodName.charAt( 3 ) ) + methodName.substring( 4 );
          getters.put( observableName, method );
        }
        else if ( !voidReturn &&
                  0 == parameterCount &&
                  methodName.startsWith( "is" ) &&
                  ( methodName.length() > 2 && Character.isUpperCase( methodName.charAt( 2 ) ) ) )
        {
          final String observableName = Character.toLowerCase( methodName.charAt( 2 ) ) + methodName.substring( 3 );
          getters.put( observableName, method );
        }
      }
    }

    linkUnAnnotatedObservables( descriptor, getters, setters );
  }

  private static void linkUnAnnotatedObservables( @Nonnull final ContainerDescriptor descriptor,
                                                  @Nonnull final Map<String, ExecutableElement> getters,
                                                  @Nonnull final Map<String, ExecutableElement> setters )
    throws ArezProcessorException
  {
    for ( final ObservableDescriptor observable : descriptor.getObservables() )
    {
      if ( !observable.hasSetter() )
      {
        final ExecutableElement element = setters.get( observable.getName() );
        if ( null != element )
        {
          observable.setSetter( element );
        }
        else
        {
          throw new ArezProcessorException( "@Observable target defined getter but no setter was defined and no " +
                                            "setter could be automatically determined", observable.getGetter() );
        }
      }
      else if ( !observable.hasGetter() )
      {
        final ExecutableElement element = getters.get( observable.getName() );
        if ( null != element )
        {
          observable.setGetter( element );
        }
        else
        {
          throw new ArezProcessorException( "@Observable target defined setter but no getter was defined and no " +
                                            "getter could be automatically determined", observable.getSetter() );
        }
      }
    }
  }

  private static boolean processMethod( @Nonnull final ContainerDescriptor descriptor,
                                        @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    verifyNoDuplicateAnnotations( method );

    final Action action = method.getAnnotation( Action.class );
    final Autorun autorun = method.getAnnotation( Autorun.class );
    final Observable observable = method.getAnnotation( Observable.class );
    final Computed computed = method.getAnnotation( Computed.class );
    final ContainerId containerId = method.getAnnotation( ContainerId.class );
    final PostConstruct postConstruct = method.getAnnotation( PostConstruct.class );
    final PreDispose preDispose = method.getAnnotation( PreDispose.class );
    final PostDispose postDispose = method.getAnnotation( PostDispose.class );
    final OnActivate onActivate = method.getAnnotation( OnActivate.class );
    final OnDeactivate onDeactivate = method.getAnnotation( OnDeactivate.class );
    final OnStale onStale = method.getAnnotation( OnStale.class );
    final OnDispose onDispose = method.getAnnotation( OnDispose.class );

    if ( null != observable )
    {
      processObservable( descriptor, observable, method );
      return true;
    }
    else if ( null != action )
    {
      processAction( descriptor, action, method );
      return true;
    }
    else if ( null != autorun )
    {
      processAutorun( descriptor, autorun, method );
      return true;
    }
    else if ( null != computed )
    {
      processComputed( descriptor, computed, method );
      return true;
    }
    else if ( null != containerId )
    {
      descriptor.setContainerId( method );
      return true;
    }
    else if ( null != postConstruct )
    {
      descriptor.setPostConstruct( method );
      return true;
    }
    else if ( null != preDispose )
    {
      descriptor.setPreDispose( method );
      return true;
    }
    else if ( null != postDispose )
    {
      descriptor.setPostDispose( method );
      return true;
    }
    else if ( null != onActivate )
    {
      processOnActivate( descriptor, onActivate, method );
      return true;
    }
    else if ( null != onDeactivate )
    {
      processOnDeactivate( descriptor, onDeactivate, method );
      return true;
    }
    else if ( null != onStale )
    {
      processOnStale( descriptor, onStale, method );
      return true;
    }
    else if ( null != onDispose )
    {
      processOnDispose( descriptor, onDispose, method );
      return true;
    }
    else
    {
      return false;
    }
  }

  private static void verifyNoDuplicateAnnotations( @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    @SuppressWarnings( "unchecked" )
    final Class<? extends Annotation>[] annotationTypes =
      new Class[]{ Action.class,
                   Autorun.class,
                   Observable.class,
                   Computed.class,
                   ContainerId.class,
                   PostConstruct.class,
                   PreDispose.class,
                   PostDispose.class,
                   OnActivate.class,
                   OnDeactivate.class,
                   OnStale.class };
    for ( int i = 0; i < annotationTypes.length; i++ )
    {
      final Class<? extends Annotation> type1 = annotationTypes[ i ];
      final Object annotation1 = method.getAnnotation( type1 );
      if ( null != annotation1 )
      {
        for ( int j = i + 1; j < annotationTypes.length; j++ )
        {
          final Class<? extends Annotation> type2 = annotationTypes[ j ];
          final Object annotation2 = method.getAnnotation( type2 );
          if ( null != annotation2 )
          {
            final String message =
              "Method can not be annotated with both @" + type1.getSimpleName() + " and @" + type2.getSimpleName();
            throw new ArezProcessorException( message, method );
          }
        }
      }
    }
  }

  private static void processOnActivate( @Nonnull final ContainerDescriptor descriptor,
                                         @Nonnull final OnActivate annotation,
                                         @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    final String name = deriveHookName( method, ON_ACTIVATE_PATTERN, "Activate", annotation.name() );
    descriptor.findOrCreateComputed( name ).setOnActivate( method );
  }

  private static void processOnDeactivate( @Nonnull final ContainerDescriptor descriptor,
                                           @Nonnull final OnDeactivate annotation,
                                           @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    final String name = deriveHookName( method, ON_DEACTIVATE_PATTERN, "Deactivate", annotation.name() );
    descriptor.findOrCreateComputed( name ).setOnDeactivate( method );
  }

  private static void processOnStale( @Nonnull final ContainerDescriptor descriptor,
                                      @Nonnull final OnStale annotation,
                                      @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    final String name = deriveHookName( method, ON_STALE_PATTERN, "Stale", annotation.name() );
    descriptor.findOrCreateComputed( name ).setOnStale( method );
  }

  private static void processOnDispose( @Nonnull final ContainerDescriptor descriptor,
                                        @Nonnull final OnDispose annotation,
                                        @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    final String name = deriveHookName( method, ON_DISPOSE_PATTERN, "Dispose", annotation.name() );
    descriptor.findOrCreateComputed( name ).setOnDispose( method );
  }

  @Nonnull
  private static String deriveHookName( @Nonnull final ExecutableElement method,
                                        @Nonnull final Pattern pattern,
                                        @Nonnull final String type,
                                        @Nonnull final String name )
    throws ArezProcessorException
  {
    final String value = ProcessorUtil.deriveName( method, pattern, name );
    if ( null == value )
    {
      throw new ArezProcessorException( "Unable to derive name for @On" + type + " as does not match " +
                                        "on[Name]" + type + " pattern. Please specify name.", method );
    }
    else if ( value.isEmpty() || !ProcessorUtil.isJavaIdentifier( value ) )
    {
      throw new ArezProcessorException( "Method annotated with @On" + type + " specified invalid name " + value,
                                        method );
    }
    else
    {
      return value;
    }
  }

  private static void processComputed( @Nonnull final ContainerDescriptor descriptor,
                                       @Nonnull final Computed annotation,
                                       @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    final String name = deriveComputedName( method, annotation );
    checkNameUnique( descriptor, name, method, Computed.class );
    descriptor.findOrCreateComputed( name ).setComputed( method );
  }

  @Nonnull
  private static String deriveComputedName( @Nonnull final ExecutableElement method,
                                            @Nonnull final Computed annotation )
    throws ArezProcessorException
  {
    if ( ProcessorUtil.isSentinelName( annotation.name() ) )
    {
      return getPropertyAccessorName( method, annotation.name() );
    }
    else
    {
      final String name = annotation.name();
      if ( name.isEmpty() || !ProcessorUtil.isJavaIdentifier( name ) )
      {
        throw new ArezProcessorException( "Method annotated with @Computed specified invalid name " + name, method );
      }
      return name;
    }
  }

  private static void processAction( @Nonnull final ContainerDescriptor descriptor,
                                     @Nonnull final Action annotation,
                                     @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    MethodChecks.mustBeOverridable( Action.class, method );

    final String name = deriveActionName( method, annotation );
    checkNameUnique( descriptor, name, method, Action.class );
    final ActionDescriptor action = descriptor.getAction( name );
    if ( null != action )
    {
      throw new ArezProcessorException( "Method annotated with @Action specified name " + name +
                                        " that duplicates action defined by method " +
                                        action.getAction().getSimpleName(), method );
    }
    else
    {
      descriptor.addAction( new ActionDescriptor( name, annotation.mutation(), method ) );
    }
  }

  @Nonnull
  private static String deriveActionName( @Nonnull final ExecutableElement method, @Nonnull final Action annotation )
    throws ArezProcessorException
  {
    final String name;
    if ( ProcessorUtil.isSentinelName( annotation.name() ) )
    {
      name = method.getSimpleName().toString();
    }
    else
    {
      name = annotation.name();
      if ( name.isEmpty() || !ProcessorUtil.isJavaIdentifier( name ) )
      {
        throw new ArezProcessorException( "Method annotated with @Action specified invalid name " + name, method );
      }
    }
    return name;
  }

  private static void processAutorun( @Nonnull final ContainerDescriptor descriptor,
                                      @Nonnull final Autorun annotation,
                                      @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    MethodChecks.mustBeOverridable( Autorun.class, method );
    MethodChecks.mustNotHaveAnyParameters( Autorun.class, method );
    MethodChecks.mustNotThrowAnyExceptions( Autorun.class, method );
    MethodChecks.mustNotReturnAnyValue( Autorun.class, method );

    final String name = deriveAutorunName( method, annotation );
    checkNameUnique( descriptor, name, method, Autorun.class );
    final AutorunDescriptor autorun = descriptor.getAutorun( name );
    if ( null != autorun )
    {
      throw new ArezProcessorException( "Method annotated with @Autorun specified name " + name +
                                        " that duplicates autorun defined by method " +
                                        autorun.getAutorun().getSimpleName(), method );
    }
    else
    {
      descriptor.addAutorun( new AutorunDescriptor( name, annotation.mutation(), method ) );
    }
  }

  @Nonnull
  private static String deriveAutorunName( @Nonnull final ExecutableElement method, @Nonnull final Autorun annotation )
    throws ArezProcessorException
  {
    final String name;
    if ( ProcessorUtil.isSentinelName( annotation.name() ) )
    {
      name = method.getSimpleName().toString();
    }
    else
    {
      name = annotation.name();
      if ( name.isEmpty() || !ProcessorUtil.isJavaIdentifier( name ) )
      {
        throw new ArezProcessorException( "Method annotated with @Autorun specified invalid name " + name, method );
      }
    }
    return name;
  }

  private static void processObservable( @Nonnull final ContainerDescriptor descriptor,
                                         @Nonnull final Observable annotation,
                                         @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    MethodChecks.mustBeOverridable( Observable.class, method );

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
        throw new ArezProcessorException( "Method annotated with @Observable should be a setter or getter", method );
      }

      name = ProcessorUtil.deriveName( method, SETTER_PATTERN, annotation.name() );
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
        throw new ArezProcessorException( "Method annotated with @Observable should be a setter or getter", method );
      }
      name = getPropertyAccessorName( method, annotation.name() );
    }
    // Override name if supplied by user
    if ( !ProcessorUtil.isSentinelName( annotation.name() ) )
    {
      name = annotation.name();
      if ( !name.isEmpty() )
      {
        if ( !ProcessorUtil.isJavaIdentifier( name ) )
        {
          throw new ArezProcessorException( "Method annotated with @Observable specified invalid name " + name,
                                            method );
        }
      }
    }
    checkNameUnique( descriptor, name, method, Observable.class );
    final ObservableDescriptor observable = descriptor.findOrCreateObservable( name );
    if ( setter )
    {
      if ( observable.hasSetter() )
      {
        throw new ArezProcessorException( "Method annotated with @Observable defines duplicate setter for " +
                                          "observable named " + name, method );
      }
      observable.setSetter( method );
    }
    else
    {
      if ( observable.hasGetter() )
      {
        throw new ArezProcessorException( "Method annotated with @Observable defines duplicate getter for " +
                                          "observable named " + name, method );
      }
      observable.setGetter( method );
    }
  }

  private static void checkNameUnique( @Nonnull final ContainerDescriptor descriptor,
                                       @Nonnull final String name,
                                       @Nonnull final ExecutableElement sourceMethod,
                                       @Nonnull final Class<? extends Annotation> sourceType )
    throws ArezProcessorException
  {
    if ( Action.class != sourceType )
    {
      final ActionDescriptor element = descriptor.getAction( name );
      if ( null != element )
      {
        throw toException( name, sourceType, sourceMethod, Action.class, element.getAction() );
      }
    }
    if ( Computed.class != sourceType )
    {
      final ComputedDescriptor element = descriptor.getComputed( name );
      if ( null != element )
      {
        throw toException( name, sourceType, sourceMethod, Computed.class, element.getComputed() );
      }
    }
    if ( Observable.class != sourceType )
    {
      final ObservableDescriptor element = descriptor.getObservable( name );
      if ( null != element )
      {
        throw toException( name, sourceType, sourceMethod, Observable.class, element.getDefiner() );
      }
    }
    if ( Autorun.class != sourceType )
    {
      final AutorunDescriptor element = descriptor.getAutorun( name );
      if ( null != element )
      {
        throw toException( name, sourceType, sourceMethod, Autorun.class, element.getAutorun() );
      }
    }
  }

  @Nonnull
  private static ArezProcessorException toException( @Nonnull final String name,
                                                     @Nonnull final Class<? extends Annotation> source,
                                                     @Nonnull final ExecutableElement sourceMethod,
                                                     @Nonnull final Class<? extends Annotation> target,
                                                     @Nonnull final ExecutableElement targetElement )
  {
    return new ArezProcessorException( "Method annotated with @" + source.getSimpleName() + " specified name " +
                                       name + " that duplicates @" + target.getSimpleName() + " defined by " +
                                       "method " + targetElement.getSimpleName(), sourceMethod );
  }

  @Nonnull
  private static String getPropertyAccessorName( @Nonnull final ExecutableElement method,
                                                 @Nonnull final String specifiedName )
    throws ArezProcessorException
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
}
