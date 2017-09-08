package org.realityforge.arez.processor;

import java.util.List;
import javax.annotation.Nonnull;
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
import javax.lang.model.util.Types;
import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.ContainerId;
import org.realityforge.arez.annotations.Observable;

final class ContainerDescriptorParser
{
  private static final String SENTINEL_NAME = "<default>";

  private ContainerDescriptorParser()
  {
  }

  static ContainerDescriptor parse( @Nonnull final Element element,
                                    @Nonnull final Elements elementUtils,
                                    @Nonnull final Types typeUtils )
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
      new ContainerDescriptor( name, container.singleton(), packageElement, typeElement );

    final List<ExecutableElement> methods = ProcessorUtil.getMethods( typeElement );
    for ( final ExecutableElement method : methods )
    {
      processMethod( descriptor, method );
    }
    //TODO: Validate observers/populate here
    //TODO: Validate that there is no name collision between Action/Observable/Computed methods
    if ( descriptor.getObservables().isEmpty() && descriptor.getActions().isEmpty() )
    {
      throw new ArezProcessorException( "@Container target has no methods annotated with @Action or @Observable",
                                        typeElement );
    }

    return descriptor;
  }

  private static void processMethod( @Nonnull final ContainerDescriptor descriptor,
                                     @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    final Action action = method.getAnnotation( Action.class );
    final Observable observable = method.getAnnotation( Observable.class );
    final Computed computed = method.getAnnotation( Computed.class );
    final ContainerId containerId = method.getAnnotation( ContainerId.class );

    if ( null != action && null != observable )
    {
      throw new ArezProcessorException( "Method can not be annotated with both @Action and @Observable", method );
    }
    else if ( null != action && null != computed )
    {
      throw new ArezProcessorException( "Method can not be annotated with both @Action and @Computed", method );
    }
    else if ( null != action && null != containerId )
    {
      throw new ArezProcessorException( "Method can not be annotated with both @Action and @ContainerId", method );
    }
    else if ( null != observable && null != computed )
    {
      throw new ArezProcessorException( "Method can not be annotated with both @Observable and @Computed", method );
    }
    else if ( null != observable && null != containerId )
    {
      throw new ArezProcessorException( "Method can not be annotated with both @Observable and @ContainerId", method );
    }
    else if ( null != containerId && null != computed )
    {
      throw new ArezProcessorException( "Method can not be annotated with both @ContainerId and @Computed", method );
    }

    if ( null != observable )
    {
      processObservable( descriptor, observable, method );
    }
    else if ( null != action )
    {
      processAction( descriptor, action, method );
    }
    else if ( null != computed )
    {
      processComputed( descriptor, computed, method );
    }
    else if ( null != containerId )
    {
      processContainerId( descriptor, containerId, method );
    }
  }

  private static void processContainerId( @Nonnull final ContainerDescriptor descriptor,
                                          @Nonnull final ContainerId containerId,
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

  private static void processComputed( @Nonnull final ContainerDescriptor descriptor,
                                       @Nonnull final Computed annotation,
                                       @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    //TODO:
  }

  private static void processAction( @Nonnull final ContainerDescriptor descriptor,
                                     @Nonnull final Action annotation,
                                     @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    if ( method.getModifiers().contains( Modifier.FINAL ) )
    {
      throw new ArezProcessorException( "@Action target must not be final", method );
    }
    else if ( method.getModifiers().contains( Modifier.STATIC ) )
    {
      throw new ArezProcessorException( "@Action target must not be static", method );
    }

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

  private static void processObservable( @Nonnull final ContainerDescriptor descriptor,
                                         @Nonnull final Observable annotation,
                                         @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    if ( method.getModifiers().contains( Modifier.FINAL ) )
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
           methodName.length() > 4 &&
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
    final ObservableDescriptor observable = descriptor.getObservableByName( name );
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
