package org.realityforge.arez.processor;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
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
import org.realityforge.arez.annotations.OnStale;
import org.realityforge.arez.annotations.PostDispose;
import org.realityforge.arez.annotations.PreDispose;

final class ContainerDescriptorParser
{
  private static final String SENTINEL_NAME = "<default>";

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
      container.name().equals( SENTINEL_NAME ) ? typeElement.getSimpleName().toString() : container.name();

    final ContainerDescriptor descriptor =
      new ContainerDescriptor( name, container.singleton(), container.disposable(), packageElement, typeElement );

    processMethods( typeElement, descriptor );

    validateComputedHookMethods( descriptor );

    if ( descriptor.getObservables().isEmpty() &&
         descriptor.getActions().isEmpty() &&
         descriptor.getComputeds().isEmpty() &&
         descriptor.getAutoruns().isEmpty() )
    {
      throw new ArezProcessorException( "@Container target has no methods annotated with @Action, " +
                                        "@Computed, @Observable or @Autorun", typeElement );
    }

    return descriptor;
  }

  private static void validateComputedHookMethods( final ContainerDescriptor descriptor )
    throws ArezProcessorException
  {
    for ( final ComputedDescriptor computed : descriptor.getComputeds() )
    {
      if ( !computed.hasComputed() )
      {
        if ( null != computed.getOnActivate() )
        {
          throw new ArezProcessorException( "@OnActivate exists but there is no corresponding @Computed",
                                            computed.getOnActivate() );
        }
        else if ( null != computed.getOnDeactivate() )
        {
          throw new ArezProcessorException( "@OnDeactivate exists but there is no corresponding @Computed",
                                            computed.getOnDeactivate() );
        }
        else
        {
          final ExecutableElement onStale = computed.getOnStale();
          assert null != onStale;
          throw new ArezProcessorException( "@OnStale exists but there is no corresponding @Computed", onStale );
        }
      }
    }
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
      processContainerId( descriptor, method );
      return true;
    }
    else if ( null != postConstruct )
    {
      processPostConstruct( descriptor, method );
      return true;
    }
    else if ( null != preDispose )
    {
      processPreDispose( descriptor, method );
      return true;
    }
    else if ( null != postDispose )
    {
      processPostDispose( descriptor, method );
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

  private static void processContainerId( @Nonnull final ContainerDescriptor descriptor,
                                          @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    if ( descriptor.isSingleton() )
    {
      throw new ArezProcessorException( "@ContainerId must not exist if @Container is a singleton", method );
    }
    else if ( method.getModifiers().contains( Modifier.STATIC ) )
    {
      throw new ArezProcessorException( "@ContainerId target must not be static", method );
    }
    else if ( method.getModifiers().contains( Modifier.PRIVATE ) )
    {
      throw new ArezProcessorException( "@ContainerId target must not be private", method );
    }
    else if ( !method.getModifiers().contains( Modifier.FINAL ) )
    {
      throw new ArezProcessorException( "@ContainerId target must be final", method );
    }
    else if ( TypeKind.VOID == method.getReturnType().getKind() )
    {
      throw new ArezProcessorException( "@ContainerId target must return a value", method );
    }
    else if ( 0 != method.getParameters().size() )
    {
      throw new ArezProcessorException( "@ContainerId target must not have any parameters", method );
    }
    final ExecutableElement existing = descriptor.getContainerId();
    if ( null != existing )
    {
      throw new ArezProcessorException( "@ContainerId target duplicates existing method named " +
                                        existing.getSimpleName(), method );
    }
    else
    {
      descriptor.setContainerId( method );
    }
  }

  private static void processPostConstruct( @Nonnull final ContainerDescriptor descriptor,
                                            @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    if ( method.getModifiers().contains( Modifier.STATIC ) )
    {
      throw new ArezProcessorException( "@PostConstruct target must not be static", method );
    }
    else if ( method.getModifiers().contains( Modifier.PRIVATE ) )
    {
      throw new ArezProcessorException( "@PostConstruct target must not be private", method );
    }
    else if ( TypeKind.VOID != method.getReturnType().getKind() )
    {
      throw new ArezProcessorException( "@PostConstruct target must not return a value", method );
    }
    else if ( 0 != method.getParameters().size() )
    {
      throw new ArezProcessorException( "@PostConstruct target must not have any parameters", method );
    }
    final ExecutableElement existing = descriptor.getPostConstruct();
    if ( null != existing )
    {
      throw new ArezProcessorException( "@PostConstruct target duplicates existing method named " +
                                        existing.getSimpleName(), method );
    }
    else
    {
      descriptor.setPostConstruct( method );
    }
  }

  private static void processPreDispose( @Nonnull final ContainerDescriptor descriptor,
                                         @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    if ( !descriptor.isDisposable() )
    {
      throw new ArezProcessorException( "@PreDispose must not exist if @Container set disposable to false", method );
    }
    else if ( method.getModifiers().contains( Modifier.STATIC ) )
    {
      throw new ArezProcessorException( "@PreDispose target must not be static", method );
    }
    else if ( method.getModifiers().contains( Modifier.PRIVATE ) )
    {
      throw new ArezProcessorException( "@PreDispose target must not be private", method );
    }
    else if ( !method.getParameters().isEmpty() )
    {
      throw new ArezProcessorException( "@PreDispose target must not have any parameters", method );
    }
    else if ( TypeKind.VOID != method.getReturnType().getKind() )
    {
      throw new ArezProcessorException( "@PreDispose target must not return a value", method );
    }
    else if ( !method.getThrownTypes().isEmpty() )
    {
      throw new ArezProcessorException( "@PreDispose target must not throw any exceptions", method );
    }
    final ExecutableElement existing = descriptor.getPreDispose();
    if ( null != existing )
    {
      throw new ArezProcessorException( "@PreDispose target duplicates existing method named " +
                                        existing.getSimpleName(), method );
    }
    else
    {
      descriptor.setPreDispose( method );
    }
  }

  private static void processOnActivate( @Nonnull final ContainerDescriptor descriptor,
                                         @Nonnull final OnActivate annotation,
                                         @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    if ( method.getModifiers().contains( Modifier.STATIC ) )
    {
      throw new ArezProcessorException( "@OnActivate target must not be static", method );
    }
    else if ( method.getModifiers().contains( Modifier.PRIVATE ) )
    {
      throw new ArezProcessorException( "@OnActivate target must not be private", method );
    }
    else if ( !method.getParameters().isEmpty() )
    {
      throw new ArezProcessorException( "@OnActivate target must not have any parameters", method );
    }
    else if ( TypeKind.VOID != method.getReturnType().getKind() )
    {
      throw new ArezProcessorException( "@OnActivate target must not return a value", method );
    }
    else if ( !method.getThrownTypes().isEmpty() )
    {
      throw new ArezProcessorException( "@OnActivate target must not throw any exceptions", method );
    }
    final String name = deriveOnActivateName( method, annotation );
    final ComputedDescriptor computed = descriptor.getComputed( name );

    if ( null != computed )
    {
      final ExecutableElement existing = computed.getOnActivate();
      if ( null != existing )
      {
        throw new ArezProcessorException( "@OnActivate target duplicates existing method named " +
                                          existing.getSimpleName(),
                                          method );
      }
      else
      {
        computed.setOnActivate( method );
      }
    }
    else
    {
      final ComputedDescriptor computedDescriptor = new ComputedDescriptor( name );
      computedDescriptor.setOnActivate( method );
      descriptor.addComputed( computedDescriptor );
    }
  }

  @Nonnull
  private static String deriveOnActivateName( @Nonnull final ExecutableElement method,
                                              @Nonnull final OnActivate annotation )
    throws ArezProcessorException
  {
    final String name;
    if ( annotation.name().equals( SENTINEL_NAME ) )
    {
      final String methodName = method.getSimpleName().toString();
      final String suffix = "Activate";
      final int suffixLength = suffix.length();
      final int length = methodName.length();
      if ( methodName.startsWith( "on" ) &&
           methodName.endsWith( suffix ) &&
           length > ( 2 + suffixLength ) &&
           Character.isUpperCase( methodName.charAt( 2 ) ) )
      {
        name = Character.toLowerCase( methodName.charAt( 2 ) ) +
               methodName.substring( 3, length - suffixLength );
      }
      else
      {
        throw new ArezProcessorException( "Unable to derive name for @OnActivate as does not match " +
                                          "on[Name]" + suffix + " pattern. Please specify name.", method );
      }
    }
    else
    {
      name = annotation.name();
      if ( name.isEmpty() || !isJavaIdentifier( name ) )
      {
        throw new ArezProcessorException( "Method annotated with @OnActivate specified invalid name " + name, method );
      }
    }
    return name;
  }

  private static void processOnDeactivate( @Nonnull final ContainerDescriptor descriptor,
                                           @Nonnull final OnDeactivate annotation,
                                           @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    if ( method.getModifiers().contains( Modifier.STATIC ) )
    {
      throw new ArezProcessorException( "@OnDeactivate target must not be static", method );
    }
    else if ( method.getModifiers().contains( Modifier.PRIVATE ) )
    {
      throw new ArezProcessorException( "@OnDeactivate target must not be private", method );
    }
    else if ( !method.getParameters().isEmpty() )
    {
      throw new ArezProcessorException( "@OnDeactivate target must not have any parameters", method );
    }
    else if ( TypeKind.VOID != method.getReturnType().getKind() )
    {
      throw new ArezProcessorException( "@OnDeactivate target must not return a value", method );
    }
    else if ( !method.getThrownTypes().isEmpty() )
    {
      throw new ArezProcessorException( "@OnDeactivate target must not throw any exceptions", method );
    }
    final String name = deriveOnDeactivateName( method, annotation );
    final ComputedDescriptor computed = descriptor.getComputed( name );

    if ( null != computed )
    {
      final ExecutableElement existing = computed.getOnDeactivate();
      if ( null != existing )
      {
        throw new ArezProcessorException( "@OnDeactivate target duplicates existing method named " +
                                          existing.getSimpleName(),
                                          method );
      }
      else
      {
        computed.setOnDeactivate( method );
      }
    }
    else
    {
      final ComputedDescriptor computedDescriptor = new ComputedDescriptor( name );
      computedDescriptor.setOnDeactivate( method );
      descriptor.addComputed( computedDescriptor );
    }
  }

  @Nonnull
  private static String deriveOnDeactivateName( @Nonnull final ExecutableElement method,
                                                @Nonnull final OnDeactivate annotation )
    throws ArezProcessorException
  {
    final String name;
    if ( annotation.name().equals( SENTINEL_NAME ) )
    {
      final String methodName = method.getSimpleName().toString();
      final String suffix = "Deactivate";
      final int suffixLength = suffix.length();
      final int length = methodName.length();
      if ( methodName.startsWith( "on" ) &&
           methodName.endsWith( suffix ) &&
           length > ( 2 + suffixLength ) &&
           Character.isUpperCase( methodName.charAt( 2 ) ) )
      {
        name = Character.toLowerCase( methodName.charAt( 2 ) ) +
               methodName.substring( 3, length - suffixLength );
      }
      else
      {
        throw new ArezProcessorException( "Unable to derive name for @OnDeactivate as does not match " +
                                          "on[Name]" + suffix + " pattern. Please specify name.", method );
      }
    }
    else
    {
      name = annotation.name();
      if ( name.isEmpty() || !isJavaIdentifier( name ) )
      {
        throw new ArezProcessorException( "Method annotated with @OnDeactivate specified invalid name " + name,
                                          method );
      }
    }
    return name;
  }

  private static void processOnStale( @Nonnull final ContainerDescriptor descriptor,
                                      @Nonnull final OnStale annotation,
                                      @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    if ( method.getModifiers().contains( Modifier.STATIC ) )
    {
      throw new ArezProcessorException( "@OnStale target must not be static", method );
    }
    else if ( method.getModifiers().contains( Modifier.PRIVATE ) )
    {
      throw new ArezProcessorException( "@OnStale target must not be private", method );
    }
    else if ( !method.getParameters().isEmpty() )
    {
      throw new ArezProcessorException( "@OnStale target must not have any parameters", method );
    }
    else if ( TypeKind.VOID != method.getReturnType().getKind() )
    {
      throw new ArezProcessorException( "@OnStale target must not return a value", method );
    }
    else if ( !method.getThrownTypes().isEmpty() )
    {
      throw new ArezProcessorException( "@OnStale target must not throw any exceptions", method );
    }
    final String name = deriveOnStaleName( method, annotation );
    final ComputedDescriptor computed = descriptor.getComputed( name );

    if ( null != computed )
    {
      final ExecutableElement existing = computed.getOnStale();
      if ( null != existing )
      {
        throw new ArezProcessorException( "@OnStale target duplicates existing method named " +
                                          existing.getSimpleName(),
                                          method );
      }
      else
      {
        computed.setOnStale( method );
      }
    }
    else
    {
      final ComputedDescriptor computedDescriptor = new ComputedDescriptor( name );
      computedDescriptor.setOnStale( method );
      descriptor.addComputed( computedDescriptor );
    }
  }

  @Nonnull
  private static String deriveOnStaleName( @Nonnull final ExecutableElement method, final @Nonnull OnStale annotation )
    throws ArezProcessorException
  {
    final String name;
    if ( annotation.name().equals( SENTINEL_NAME ) )
    {
      final String methodName = method.getSimpleName().toString();
      final String suffix = "Stale";
      final int suffixLength = suffix.length();
      final int length = methodName.length();
      if ( methodName.startsWith( "on" ) &&
           methodName.endsWith( suffix ) &&
           length > ( 2 + suffixLength ) &&
           Character.isUpperCase( methodName.charAt( 2 ) ) )
      {
        name = Character.toLowerCase( methodName.charAt( 2 ) ) +
               methodName.substring( 3, length - suffixLength );
      }
      else
      {
        throw new ArezProcessorException( "Unable to derive name for @OnStale as does not match " +
                                          "on[Name]" + suffix + " pattern. Please specify name.", method );
      }
    }
    else
    {
      name = annotation.name();
      if ( name.isEmpty() || !isJavaIdentifier( name ) )
      {
        throw new ArezProcessorException( "Method annotated with @OnStale specified invalid name " + name, method );
      }
    }
    return name;
  }

  private static void processPostDispose( @Nonnull final ContainerDescriptor descriptor,
                                          @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    if ( !descriptor.isDisposable() )
    {
      throw new ArezProcessorException( "@PostDispose must not exist if @Container set disposable to false", method );
    }
    else if ( method.getModifiers().contains( Modifier.STATIC ) )
    {
      throw new ArezProcessorException( "@PostDispose target must not be static", method );
    }
    else if ( method.getModifiers().contains( Modifier.PRIVATE ) )
    {
      throw new ArezProcessorException( "@PostDispose target must not be private", method );
    }
    else if ( !method.getParameters().isEmpty() )
    {
      throw new ArezProcessorException( "@PostDispose target must not have any parameters", method );
    }
    else if ( TypeKind.VOID != method.getReturnType().getKind() )
    {
      throw new ArezProcessorException( "@PostDispose target must not return a value", method );
    }
    else if ( !method.getThrownTypes().isEmpty() )
    {
      throw new ArezProcessorException( "@PostDispose target must not throw any exceptions", method );
    }
    final ExecutableElement existing = descriptor.getPostDispose();
    if ( null != existing )
    {
      throw new ArezProcessorException( "@PostDispose target duplicates existing method named " +
                                        existing.getSimpleName(), method );
    }
    else
    {
      descriptor.setPostDispose( method );
    }
  }

  private static void processComputed( @Nonnull final ContainerDescriptor descriptor,
                                       @Nonnull final Computed annotation,
                                       @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    if ( method.getModifiers().contains( Modifier.PRIVATE ) )
    {
      throw new ArezProcessorException( "@Computed target must not be private", method );
    }
    else if ( method.getModifiers().contains( Modifier.FINAL ) )
    {
      throw new ArezProcessorException( "@Computed target must not be final", method );
    }
    else if ( method.getModifiers().contains( Modifier.STATIC ) )
    {
      throw new ArezProcessorException( "@Computed target must not be static", method );
    }
    else if ( TypeKind.VOID == method.getReturnType().getKind() )
    {
      throw new ArezProcessorException( "@Computed target must not have a void return type", method );
    }
    else if ( !method.getParameters().isEmpty() )
    {
      throw new ArezProcessorException( "@Computed target must not have parameters", method );
    }
    else if ( !method.getThrownTypes().isEmpty() )
    {
      throw new ArezProcessorException( "@Computed target must not throw exceptions", method );
    }

    final String name = deriveComputedName( method, annotation );

    checkNameUnique( descriptor, name, method, Computed.class );
    final ComputedDescriptor computed = descriptor.getComputed( name );
    if ( null != computed )
    {
      if ( computed.hasComputed() )
      {
        throw new ArezProcessorException( "Method annotated with @Computed specified name " + name +
                                          " that duplicates computed defined by method " +
                                          computed.getDefiner().getSimpleName(), method );
      }
      else
      {
        computed.setComputed( method );
      }
    }
    else
    {
      final ComputedDescriptor computedDescriptor = new ComputedDescriptor( name );
      computedDescriptor.setComputed( method );
      descriptor.addComputed( computedDescriptor );
    }
  }

  @Nonnull
  private static String deriveComputedName( @Nonnull final ExecutableElement method,
                                            @Nonnull final Computed annotation )
    throws ArezProcessorException
  {
    final String name;
    if ( annotation.name().equals( SENTINEL_NAME ) )
    {
      final String methodName = method.getSimpleName().toString();
      if ( methodName.startsWith( "get" ) &&
           methodName.length() > 4 &&
           Character.isUpperCase( methodName.charAt( 3 ) ) )
      {
        name = Character.toLowerCase( methodName.charAt( 3 ) ) + methodName.substring( 4 );
      }
      else if ( methodName.startsWith( "is" ) &&
                methodName.length() > 3 &&
                Character.isUpperCase( methodName.charAt( 2 ) ) )
      {
        name = Character.toLowerCase( methodName.charAt( 2 ) ) + methodName.substring( 3 );
      }
      else
      {
        name = methodName;
      }
    }
    else
    {
      name = annotation.name();
      if ( name.isEmpty() || !isJavaIdentifier( name ) )
      {
        throw new ArezProcessorException( "Method annotated with @Computed specified invalid name " + name, method );
      }
    }
    return name;
  }

  private static void processAction( @Nonnull final ContainerDescriptor descriptor,
                                     @Nonnull final Action annotation,
                                     @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    if ( method.getModifiers().contains( Modifier.PRIVATE ) )
    {
      throw new ArezProcessorException( "@Action target must not be private", method );
    }
    else if ( method.getModifiers().contains( Modifier.FINAL ) )
    {
      throw new ArezProcessorException( "@Action target must not be final", method );
    }
    else if ( method.getModifiers().contains( Modifier.STATIC ) )
    {
      throw new ArezProcessorException( "@Action target must not be static", method );
    }

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
    if ( annotation.name().equals( SENTINEL_NAME ) )
    {
      name = method.getSimpleName().toString();
    }
    else
    {
      name = annotation.name();
      if ( name.isEmpty() || !isJavaIdentifier( name ) )
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
    if ( method.getModifiers().contains( Modifier.PRIVATE ) )
    {
      throw new ArezProcessorException( "@Autorun target must not be private", method );
    }
    else if ( method.getModifiers().contains( Modifier.FINAL ) )
    {
      throw new ArezProcessorException( "@Autorun target must not be final", method );
    }
    else if ( method.getModifiers().contains( Modifier.STATIC ) )
    {
      throw new ArezProcessorException( "@Autorun target must not be static", method );
    }
    else if ( !method.getParameters().isEmpty() )
    {
      throw new ArezProcessorException( "@Autorun target must not have any parameters", method );
    }
    else if ( !method.getThrownTypes().isEmpty() )
    {
      throw new ArezProcessorException( "@Autorun target must not throw any exceptions", method );
    }
    else if ( TypeKind.VOID != method.getReturnType().getKind() )
    {
      throw new ArezProcessorException( "@Autorun target must not return a value", method );
    }

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
    if ( annotation.name().equals( SENTINEL_NAME ) )
    {
      name = method.getSimpleName().toString();
    }
    else
    {
      name = annotation.name();
      if ( name.isEmpty() || !isJavaIdentifier( name ) )
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
    if ( method.getModifiers().contains( Modifier.PRIVATE ) )
    {
      throw new ArezProcessorException( "@Observable target must not be private", method );
    }
    else if ( method.getModifiers().contains( Modifier.FINAL ) )
    {
      throw new ArezProcessorException( "@Observable target must not be final", method );
    }
    else if ( method.getModifiers().contains( Modifier.STATIC ) )
    {
      throw new ArezProcessorException( "@Observable target must not be static", method );
    }

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

      if ( methodName.startsWith( "set" ) &&
           methodName.length() > 3 &&
           Character.isUpperCase( methodName.charAt( 3 ) ) )
      {
        name = Character.toLowerCase( methodName.charAt( 3 ) ) + methodName.substring( 4 );
      }
      else
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
      if ( methodName.startsWith( "get" ) &&
           methodName.length() > 3 &&
           Character.isUpperCase( methodName.charAt( 3 ) ) )
      {
        name = Character.toLowerCase( methodName.charAt( 3 ) ) + methodName.substring( 4 );
      }
      else if ( methodName.startsWith( "is" ) &&
                methodName.length() > 3 &&
                Character.isUpperCase( methodName.charAt( 2 ) ) )
      {
        name = Character.toLowerCase( methodName.charAt( 2 ) ) + methodName.substring( 3 );
      }
      else
      {
        name = methodName;
      }
    }
    // Override name if supplied by user
    if ( !annotation.name().equals( SENTINEL_NAME ) )
    {
      name = annotation.name();
      if ( !name.isEmpty() )
      {
        if ( !isJavaIdentifier( name ) )
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

  private static boolean isJavaIdentifier( @Nonnull final String value )
  {
    if ( !Character.isJavaIdentifierStart( value.charAt( 0 ) ) )
    {
      return false;
    }
    else
    {
      final int length = value.length();
      for ( int i = 1; i < length; i++ )
      {
        if ( !Character.isJavaIdentifierPart( value.charAt( i ) ) )
        {
          return false;
        }
      }

      return true;
    }
  }
}
