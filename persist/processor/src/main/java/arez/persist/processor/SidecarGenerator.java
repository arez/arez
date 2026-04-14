package arez.persist.processor;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import org.realityforge.proton.GeneratorUtil;
import org.realityforge.proton.SuppressWarningsUtil;

final class SidecarGenerator
{
  @Nonnull
  private static final ClassName AREZ_CLASSNAME = ClassName.get( "arez", "Arez" );
  @Nonnull
  private static final ClassName DISPOSABLE_CLASSNAME = ClassName.get( "arez", "Disposable" );
  @Nonnull
  private static final ClassName IDENTIFIABLE_CLASSNAME = ClassName.get( "arez.component", "Identifiable" );
  @Nonnull
  private static final ClassName COMPONENT_DEPENDENCY_CLASSNAME =
    ClassName.get( "arez.annotations", "ComponentDependency" );
  @Nonnull
  private static final ClassName AREZ_COMPONENT_CLASSNAME = ClassName.get( "arez.annotations", "ArezComponent" );
  @Nonnull
  private static final ClassName FEATURE_CLASSNAME = ClassName.get( "arez.annotations", "Feature" );
  @Nonnull
  private static final ClassName ACTION_CLASSNAME = ClassName.get( "arez.annotations", "Action" );
  @Nonnull
  private static final ClassName OBSERVE_CLASSNAME = ClassName.get( "arez.annotations", "Observe" );
  @Nonnull
  private static final ClassName POST_CONSTRUCT_CLASSNAME = ClassName.get( "arez.annotations", "PostConstruct" );
  @Nonnull
  private static final ClassName PRE_DISPOSE_CLASSNAME = ClassName.get( "arez.annotations", "PreDispose" );
  @Nonnull
  private static final ClassName PRIORITY_CLASSNAME = ClassName.get( "arez.annotations", "Priority" );
  @Nonnull
  private static final ClassName DEP_TYPE_CLASSNAME = ClassName.get( "arez.annotations", "DepType" );
  @Nonnull
  private static final ClassName AREZ_PERSIST_CLASSNAME = ClassName.get( "arez.persist.runtime", "ArezPersist" );
  @Nonnull
  private static final ClassName CONVERTER_CLASSNAME = ClassName.get( "arez.persist.runtime", "Converter" );
  @Nonnull
  private static final ClassName SCOPE_CLASSNAME = ClassName.get( "arez.persist.runtime", "Scope" );
  @Nonnull
  private static final ClassName STORE_CLASSNAME = ClassName.get( "arez.persist.runtime", "Store" );
  @Nonnull
  private static final ClassName TYPE_CONVERTER_CLASSNAME = ClassName.get( "arez.persist.runtime", "TypeConverter" );

  private SidecarGenerator()
  {
  }

  @Nonnull
  static TypeSpec buildType( @Nonnull final ProcessingEnvironment processingEnv,
                             @Nonnull final TypeDescriptor descriptor )
  {
    final TypeElement element = descriptor.getElement();
    final TypeSpec.Builder builder =
      TypeSpec
        .classBuilder( getSidecarName( element ) )
        .addModifiers( Modifier.ABSTRACT );
    if ( element.getModifiers().contains( Modifier.PUBLIC ) )
    {
      builder.addModifiers( Modifier.ABSTRACT );
    }

    builder.addAnnotation( AnnotationSpec.builder( AREZ_COMPONENT_CLASSNAME )
                             .addMember( "disposeNotifier", "$T.DISABLE", FEATURE_CLASSNAME )
                             .addMember( "requireId", "$T.DISABLE", FEATURE_CLASSNAME )
                             .build() );

    GeneratorUtil.addOriginatingTypes( element, builder );
    GeneratorUtil.copyWhitelistedAnnotations( element, builder );

    GeneratorUtil.addGeneratedAnnotation( processingEnv, builder, ArezPersistProcessor.class.getName() );
    SuppressWarningsUtil.addSuppressWarningsIfRequired( processingEnv,
                                                        builder,
                                                        Collections.emptyList(),
                                                        Collections.singletonList( element.asType() ) );

    builder.addField( FieldSpec.builder( TypeName.INT, "c_nextTaskId", Modifier.PRIVATE, Modifier.STATIC ).build() );

    // Create a nested keys type to eliminate any possibility GWT will
    // attempt to create a <clinit> for sidecar type and the deopt that brings  .
    builder.addType( buildKeysType( descriptor ) );

    // Create a separate class to hold converters to eliminate <clinit>
    builder.addType( buildConvertersType( descriptor ) );

    buildFieldAndConstructor( descriptor, builder );

    builder.addMethod( buildAttachMethod( descriptor ) );
    builder.addMethod( buildMaybeAttachMethod( descriptor ) );
    builder.addMethod( buildScheduleAttachMethod( descriptor ) );

    // build method to get component id as string from peer
    builder.addMethod( buildGetComponentIdMethod( descriptor ) );

    // Add observer that actually persists state on change
    builder.addMethod( MethodSpec.methodBuilder( "savePersistentProperties" )
                         .addAnnotation( AnnotationSpec.builder( OBSERVE_CLASSNAME )
                                           .addMember( "priority", "$T.LOWEST", PRIORITY_CLASSNAME )
                                           .addMember( "nestedActionsAllowed", "true" )
                                           .addMember( "depType", "$T.AREZ_OR_NONE", DEP_TYPE_CLASSNAME )
                                           .build() )
                         .addStatement( "persistState()" )
                         .build() );

    if ( descriptor.isPersistOnDispose() )
    // Add hook so that sidecar will save state unless the peer was disposed first
    {
      builder.addMethod( MethodSpec.methodBuilder( "preDispose" )
                           .addAnnotation( PRE_DISPOSE_CLASSNAME )
                           .addCode( CodeBlock.builder()
                                       .beginControlFlow( "if ( $T.isNotDisposed( _peer ) )", DISPOSABLE_CLASSNAME )
                                       .addStatement( "persistState()" )
                                       .endControlFlow()
                                       .build() )
                           .build() );
    }

    // Restore state when the component is created
    builder.addMethod( MethodSpec.methodBuilder( "postConstruct" )
                         .addAnnotation( POST_CONSTRUCT_CLASSNAME )
                         .addStatement( "restoreState()" )
                         .build() );

    builder.addMethod( buildRestoreStateMethod( processingEnv, descriptor ) );
    builder.addMethod( buildPersistStateMethod( processingEnv, descriptor ) );

    return builder.build();
  }

  @Nonnull
  private static MethodSpec buildGetComponentIdMethod( @Nonnull final TypeDescriptor descriptor )
  {
    final MethodSpec.Builder method = MethodSpec.methodBuilder( "getComponentId" )
      .returns( String.class )
      .addModifiers( Modifier.PRIVATE )
      .addAnnotation( GeneratorUtil.NONNULL_CLASSNAME );

    final ExecutableElement idMethod = descriptor.getIdMethod();
    if ( null == idMethod )
    {
      method.addStatement( "return $T.valueOf( $T.<$T>requireNonNull( $T.getArezId( _peer ) ) )",
                           String.class,
                           Objects.class,
                           Object.class,
                           IDENTIFIABLE_CLASSNAME );
    }
    else
    {
      method.addStatement( "return $T.valueOf( $T.<$T>requireNonNull( _peer.$N() ) )",
                           String.class,
                           Objects.class,
                           Object.class,
                           idMethod.getSimpleName().toString() );
    }
    return method.build();
  }

  @Nonnull
  private static MethodSpec buildAttachMethod( @Nonnull final TypeDescriptor descriptor )
  {
    final TypeElement element = descriptor.getElement();

    final MethodSpec.Builder method =
      MethodSpec
        .methodBuilder( "attach" )
        .addModifiers( Modifier.STATIC )
        .returns( getSidecarName( element ) )
        .addAnnotation( GeneratorUtil.NONNULL_CLASSNAME )
        .addParameter( ParameterSpec.builder( SCOPE_CLASSNAME, "scope", Modifier.FINAL )
                         .addAnnotation( GeneratorUtil.NONNULL_CLASSNAME )
                         .build() )
        .addParameter( ParameterSpec.builder( ClassName.get( element ), "peer", Modifier.FINAL )
                         .addAnnotation( GeneratorUtil.NONNULL_CLASSNAME )
                         .build() );

    method.addStatement( "assert $T.isNotDisposed( scope )", DISPOSABLE_CLASSNAME );
    method.addStatement( "assert $T.isNotDisposed( peer )", DISPOSABLE_CLASSNAME );

    final StringBuilder storeParams = new StringBuilder();

    for ( final String storeName : descriptor.getStoreNames() )
    {
      final String storeVar = storeVar( storeName );
      storeParams.append( ", " );
      storeParams.append( storeVar );
      method.addStatement( "final $T $N = $T.getStore( $S )",
                           STORE_CLASSNAME,
                           storeVar,
                           AREZ_PERSIST_CLASSNAME,
                           storeName );
    }

    method.addStatement( "return new $T( scope, peer" + storeParams + " )", getArezSidecarName( element ) );

    return method.build();
  }

  @Nonnull
  private static MethodSpec buildMaybeAttachMethod( @Nonnull final TypeDescriptor descriptor )
  {
    final TypeElement element = descriptor.getElement();

    // This works around possible bug whee a scope or peer could be disposed before the arez task runs
    // which can happen when navigating away from a page in response to a change
    final MethodSpec.Builder method =
      MethodSpec
        .methodBuilder( "maybeAttach" )
        .addModifiers( Modifier.PRIVATE, Modifier.STATIC )
        .addParameter( ParameterSpec.builder( SCOPE_CLASSNAME, "scope", Modifier.FINAL )
                         .addAnnotation( GeneratorUtil.NONNULL_CLASSNAME )
                         .build() )
        .addParameter( ParameterSpec.builder( ClassName.get( element ), "peer", Modifier.FINAL )
                         .addAnnotation( GeneratorUtil.NONNULL_CLASSNAME )
                         .build() );

    final CodeBlock.Builder block = CodeBlock.builder();
    block.beginControlFlow( "if ( $T.isNotDisposed( scope ) && $T.isNotDisposed( peer ) ) ",
                            DISPOSABLE_CLASSNAME,
                            DISPOSABLE_CLASSNAME );
    block.addStatement( "attach( scope, peer )" );
    block.endControlFlow();
    method.addCode( block.build() );

    return method.build();
  }

  @Nonnull
  private static MethodSpec buildScheduleAttachMethod( @Nonnull final TypeDescriptor descriptor )
  {
    final TypeElement element = descriptor.getElement();

    final MethodSpec.Builder method =
      MethodSpec
        .methodBuilder( "scheduleAttach" )
        .addModifiers( Modifier.STATIC )
        .addAnnotation( GeneratorUtil.NONNULL_CLASSNAME )
        .addParameter( ParameterSpec.builder( SCOPE_CLASSNAME, "scope", Modifier.FINAL )
                         .addAnnotation( GeneratorUtil.NONNULL_CLASSNAME )
                         .build() )
        .addParameter( ParameterSpec.builder( ClassName.get( element ), "peer", Modifier.FINAL )
                         .addAnnotation( GeneratorUtil.NONNULL_CLASSNAME )
                         .build() );

    method.addStatement( "assert $T.isNotDisposed( scope )", DISPOSABLE_CLASSNAME );
    method.addStatement( "assert $T.isNotDisposed( peer )", DISPOSABLE_CLASSNAME );
    method.addStatement( "$T.context().task( " +
                         "$T.areNamesEnabled() ? $S + ( ++c_nextTaskId ) : null, " +
                         "() -> maybeAttach( scope, peer ) " +
                         ")",
                         AREZ_CLASSNAME,
                         AREZ_CLASSNAME,
                         descriptor.getName() + "_PersistSidecar.attach." );

    return method.build();
  }

  @Nonnull
  private static ClassName getArezSidecarName( @Nonnull final TypeElement element )
  {
    return GeneratorUtil.getGeneratedClassName( element, "Arez_", "_PersistSidecar" );
  }

  @Nonnull
  private static ClassName getSidecarName( @Nonnull final TypeElement element )
  {
    return GeneratorUtil.getGeneratedClassName( element, "", "_PersistSidecar" );
  }

  @Nonnull
  private static MethodSpec buildRestoreStateMethod( @Nonnull final ProcessingEnvironment processingEnv,
                                                     @Nonnull final TypeDescriptor descriptor )
  {
    final MethodSpec.Builder method =
      MethodSpec
        .methodBuilder( "restoreState" )
        .addAnnotation( AnnotationSpec.builder( ACTION_CLASSNAME )
                          .addMember( "verifyRequired", "false" )
                          .build() );

    SuppressWarningsUtil.addSuppressWarningsIfRequired( processingEnv,
                                                        method,
                                                        descriptor.getProperties()
                                                          .stream()
                                                          .map( p -> p.getGetter().getReturnType() )
                                                          .collect( Collectors.toList() ) );
    final String idVar = "$ap$_id";
    method.addStatement( "final $T $N = getComponentId()", String.class, idVar );
    for ( final String storeName : descriptor.getStoreNames() )
    {
      final String fieldName = "_" + storeVar( storeName );
      final CodeBlock.Builder block = CodeBlock.builder();
      block.beginControlFlow( "if ( !$N.isDisposed() && $T.isNotDisposed( _scope ) )",
                              fieldName,
                              DISPOSABLE_CLASSNAME );
      block.addStatement( "final $T state = $N.get( _scope, $T.TYPE, $N, $T.TYPE_CONVERTER )",
                          ParameterizedTypeName.get( Map.class, String.class, Object.class ),
                          fieldName,
                          ClassName.bestGuess( "Keys" ),
                          idVar,
                          ClassName.bestGuess( "Converters" ) );
      final CodeBlock.Builder stateBlock = CodeBlock.builder();
      stateBlock.beginControlFlow( "if ( null != state )" );

      for ( final PropertyDescriptor property : descriptor.getPropertiesByStore( storeName ) )
      {
        final TypeName typeName = TypeName.get( property.getGetter().getReturnType() ).box();
        final String propName = "$prop$_" + property.getName();
        if ( TypeName.OBJECT.equals( typeName ) )
        {
          stateBlock.addStatement( "final $T $N = ( $T.$N )",
                                   typeName,
                                   propName,
                                   ClassName.bestGuess( "Keys" ),
                                   property.getConstantName() );
        }
        else
        {
          stateBlock.addStatement( "final $T $N = ($T) state.get( $T.$N )",
                                   typeName,
                                   propName,
                                   typeName,
                                   ClassName.bestGuess( "Keys" ),
                                   property.getConstantName() );
        }
        final CodeBlock.Builder setterBlock = CodeBlock.builder();
        setterBlock.beginControlFlow( "if ( null != $N )", propName );
        setterBlock.addStatement( "_peer.$N( $N )", property.getSetter().getSimpleName(), propName );
        setterBlock.endControlFlow();
        stateBlock.add( setterBlock.build() );
      }

      stateBlock.endControlFlow();
      block.add( stateBlock.build() );
      block.endControlFlow();
      method.addCode( block.build() );
    }

    return method.build();
  }

  @Nonnull
  private static MethodSpec buildPersistStateMethod( @Nonnull final ProcessingEnvironment processingEnv,
                                                     @Nonnull final TypeDescriptor descriptor )
  {
    final MethodSpec.Builder method =
      MethodSpec
        .methodBuilder( "persistState" )
        .addAnnotation( AnnotationSpec.builder( ACTION_CLASSNAME )
                          .addMember( "mutation", "false" )
                          .addMember( "verifyRequired", "false" )
                          .build() );

    SuppressWarningsUtil.addSuppressWarningsIfRequired( processingEnv,
                                                        method,
                                                        descriptor.getProperties()
                                                          .stream()
                                                          .map( p -> p.getGetter().getReturnType() )
                                                          .collect( Collectors.toList() ) );

    for ( final String storeName : descriptor.getStoreNames() )
    {
      final String fieldName = "_" + storeVar( storeName );
      final CodeBlock.Builder block = CodeBlock.builder();
      block.beginControlFlow( "if ( !$N.isDisposed() && $T.isNotDisposed( _scope ) )",
                              fieldName,
                              DISPOSABLE_CLASSNAME );
      block.addStatement( "final $T state = new $T<>()",
                          ParameterizedTypeName.get( Map.class, String.class, Object.class ),
                          HashMap.class );
      for ( final PropertyDescriptor property : descriptor.getPropertiesByStore( storeName ) )
      {
        final ExecutableElement getter = property.getGetter();
        final String propName = "$prop$_" + property.getName();
        final TypeMirror returnType = getter.getReturnType();
        block.addStatement( "final $T $N = _peer.$N()", returnType, propName, getter.getSimpleName() );
        final CodeBlock.Builder subBlock = CodeBlock.builder();
        final TypeKind kind = returnType.getKind();
        if ( TypeKind.DECLARED == kind )
        {
          subBlock.beginControlFlow( "if ( null != $N )", propName );
        }
        else if ( TypeKind.BOOLEAN == kind )
        {
          subBlock.beginControlFlow( "if ( $N )", propName );
        }
        else if ( TypeKind.LONG == kind )
        {
          subBlock.beginControlFlow( "if ( 0L != $N )", propName );
        }
        else if ( TypeKind.INT == kind || TypeKind.SHORT == kind || TypeKind.BYTE == kind || TypeKind.CHAR == kind )
        {
          subBlock.beginControlFlow( "if ( 0 != $N )", propName );
        }
        else if ( TypeKind.FLOAT == kind )
        {
          subBlock.beginControlFlow( "if ( 0.0F != $N )", propName );
        }
        else if ( TypeKind.DOUBLE == kind )
        {
          subBlock.beginControlFlow( "if ( 0.0 != $N )", propName );
        }
        subBlock.addStatement( "state.put( $T.$N, $N )",
                               ClassName.bestGuess( "Keys" ),
                               property.getConstantName(),
                               propName );
        subBlock.endControlFlow();
        block.add( subBlock.build() );
      }

      block.addStatement( "$N.save( _scope, $T.TYPE, getComponentId(), state, $T.TYPE_CONVERTER )",
                          fieldName,
                          ClassName.bestGuess( "Keys" ),
                          ClassName.bestGuess( "Converters" ) );
      block.endControlFlow();
      method.addCode( block.build() );
    }

    return method.build();
  }

  private static void buildFieldAndConstructor( @Nonnull final TypeDescriptor descriptor,
                                                @Nonnull final TypeSpec.Builder builder )
  {
    final TypeElement element = descriptor.getElement();
    final MethodSpec.Builder ctor = MethodSpec.constructorBuilder();
    builder.addField( FieldSpec
                        .builder( SCOPE_CLASSNAME, "_scope", Modifier.FINAL )
                        .addAnnotation( COMPONENT_DEPENDENCY_CLASSNAME )
                        .addAnnotation( GeneratorUtil.NONNULL_CLASSNAME )
                        .build() );
    ctor.addParameter( ParameterSpec
                         .builder( SCOPE_CLASSNAME, "scope", Modifier.FINAL )
                         .addAnnotation( GeneratorUtil.NONNULL_CLASSNAME )
                         .build() );
    ctor.addStatement( "$N = $T.requireNonNull( $N )", "_scope", Objects.class, "scope" );

    final ClassName peerType = ClassName.get( element );
    builder.addField( FieldSpec.builder( peerType, "_peer", Modifier.FINAL )
                        .addAnnotation( COMPONENT_DEPENDENCY_CLASSNAME )
                        .addAnnotation( GeneratorUtil.NONNULL_CLASSNAME )
                        .build() );
    ctor.addParameter( ParameterSpec
                         .builder( peerType, "peer", Modifier.FINAL )
                         .addAnnotation( GeneratorUtil.NONNULL_CLASSNAME )
                         .build() );
    ctor.addStatement( "$N = $T.requireNonNull( $N )", "_peer", Objects.class, "peer" );

    for ( final String storeName : descriptor.getStoreNames() )
    {
      final String varName = storeVar( storeName );
      final String fieldName = "_" + varName;
      builder.addField( FieldSpec.builder( STORE_CLASSNAME, fieldName, Modifier.PRIVATE, Modifier.FINAL )
                          .addAnnotation( GeneratorUtil.NONNULL_CLASSNAME )
                          .build() );
      ctor.addParameter( ParameterSpec
                           .builder( STORE_CLASSNAME, varName, Modifier.FINAL )
                           .addAnnotation( GeneratorUtil.NONNULL_CLASSNAME )
                           .build() );
      ctor.addStatement( "$N = $T.requireNonNull( $N )", fieldName, Objects.class, varName );
    }

    builder.addMethod( ctor.build() );
  }

  @Nonnull
  private static String storeVar( @Nonnull final String storeName )
  {
    return storeName + "Store";
  }

  @Nonnull
  private static TypeSpec buildKeysType( @Nonnull final TypeDescriptor descriptor )
  {
    final TypeSpec.Builder keys = TypeSpec.classBuilder( "Keys" );
    keys.addModifiers( Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL );
    keys.addField( FieldSpec
                     .builder( String.class, "TYPE", Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL )
                     .addAnnotation( GeneratorUtil.NONNULL_CLASSNAME )
                     .initializer( "$T.areNamesEnabled() ? $S : $T.class.getName()",
                                   AREZ_CLASSNAME,
                                   descriptor.getName(),
                                   descriptor.getElement() )
                     .build() );

    int propertyIndex = 0;
    for ( final PropertyDescriptor property : descriptor.getProperties() )
    {
      keys.addField( FieldSpec
                       .builder( String.class,
                                 property.getConstantName(),
                                 Modifier.PRIVATE,
                                 Modifier.STATIC,
                                 Modifier.FINAL )
                       .addAnnotation( GeneratorUtil.NONNULL_CLASSNAME )
                       .initializer( "$T.areNamesEnabled() ? $S : $S",
                                     AREZ_CLASSNAME,
                                     property.getName(),
                                     String.valueOf( (char) ( 'a' + propertyIndex ) ) )
                       .build() );
      propertyIndex++;
    }
    return keys.build();
  }

  @Nonnull
  private static TypeSpec buildConvertersType( @Nonnull final TypeDescriptor descriptor )
  {
    final TypeSpec.Builder builder = TypeSpec.classBuilder( "Converters" );
    builder.addModifiers( Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL );
    builder.addAnnotation( AnnotationSpec.builder( SuppressWarnings.class )
                             .addMember( "value", "$S", "unchecked" )
                             .addMember( "value", "$S", "rawtypes" )
                             .build() );

    final List<TypeMirror> typesToConvert =
      descriptor.getProperties()
        .stream()
        .map( p -> p.getGetter().getReturnType() )
        .sorted( Comparator.comparing( TypeMirror::toString ) )
        .distinct()
        .collect( Collectors.toList() );
    for ( final TypeMirror typeMirror : typesToConvert )
    {
      builder.addField( FieldSpec
                          .builder( CONVERTER_CLASSNAME,
                                    converterName( typeMirror ),
                                    Modifier.PRIVATE,
                                    Modifier.STATIC,
                                    Modifier.FINAL )
                          .addAnnotation( GeneratorUtil.NONNULL_CLASSNAME )
                          .initializer( "$T.getConverter( $T.class )", AREZ_PERSIST_CLASSNAME, typeMirror )
                          .build() );
    }

    builder.addField( FieldSpec
                        .builder( TYPE_CONVERTER_CLASSNAME,
                                  "TYPE_CONVERTER",
                                  Modifier.PRIVATE,
                                  Modifier.STATIC,
                                  Modifier.FINAL )
                        .addAnnotation( GeneratorUtil.NONNULL_CLASSNAME )
                        .initializer( "createTypeConverter()" )
                        .build() );

    final MethodSpec.Builder method = MethodSpec.methodBuilder( "createTypeConverter" )
      .addModifiers( Modifier.PRIVATE, Modifier.STATIC )
      .addAnnotation( GeneratorUtil.NONNULL_CLASSNAME )
      .returns( TYPE_CONVERTER_CLASSNAME );
    method.addStatement( "final $T converters = new $T<>()",
                         ParameterizedTypeName.get( ClassName.get( Map.class ),
                                                    ClassName.get( String.class ),
                                                    CONVERTER_CLASSNAME ),
                         HashMap.class );
    for ( final PropertyDescriptor property : descriptor.getProperties() )
    {
      method.addStatement( "converters.put( $S, $N )",
                           property.getName(),
                           converterName( property.getGetter().getReturnType() ) );
    }
    method.addStatement( "return new $T( converters )", TYPE_CONVERTER_CLASSNAME );
    builder.addMethod( method.build() );

    return builder.build();
  }

  @Nonnull
  private static String converterName( @Nonnull final TypeMirror typeMirror )
  {
    return "CONVERTER_" +
           typeMirror.toString()
             .replaceAll( "\\[", "_" )
             .replaceAll( "]", "_" )
             .replaceAll( "\\.", "__" );
  }
}
