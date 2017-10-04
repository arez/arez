package org.realityforge.arez.processor;

import com.squareup.javapoet.ClassName;

@SuppressWarnings( "Duplicates" )
final class GeneratorUtil
{
  static final ClassName GUARDS_CLASSNAME = ClassName.get( "org.realityforge.braincheck", "Guards" );
  static final ClassName AREZ_CLASSNAME = ClassName.get( "org.realityforge.arez", "Arez" );
  static final ClassName AREZ_CONTEXT_CLASSNAME = ClassName.get( "org.realityforge.arez", "ArezContext" );
  static final ClassName OBSERVABLE_CLASSNAME = ClassName.get( "org.realityforge.arez", "Observable" );
  static final ClassName OBSERVER_CLASSNAME = ClassName.get( "org.realityforge.arez", "Observer" );
  static final ClassName COMPUTED_VALUE_CLASSNAME = ClassName.get( "org.realityforge.arez", "ComputedValue" );
  static final ClassName DISPOSABLE_CLASSNAME = ClassName.get( "org.realityforge.arez", "Disposable" );
  static final String FIELD_PREFIX = "$$arez$$_";
  static final String CAUGHT_THROWABLE_NAME = FIELD_PREFIX + "e";
  static final String RESULT_VARIABLE_NAME = FIELD_PREFIX + "result";
  static final String DISPOSED_FIELD_NAME = FIELD_PREFIX + "disposed";
  static final String ID_FIELD_NAME = FIELD_PREFIX + "id";
  static final String NAME_METHOD_NAME = FIELD_PREFIX + "name";
  static final String NEXT_ID_FIELD_NAME = FIELD_PREFIX + "nextId";
  static final String CONTEXT_FIELD_NAME = FIELD_PREFIX + "context";

  private GeneratorUtil()
  {
  }
}
