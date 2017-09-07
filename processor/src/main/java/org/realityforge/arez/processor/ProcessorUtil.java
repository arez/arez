package org.realityforge.arez.processor;

import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeVariableName;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;

final class ProcessorUtil
{
  private ProcessorUtil()
  {
  }

  @Nonnull
  static List<TypeVariableName> getTypeArgumentsAsNames( @Nonnull final DeclaredType declaredType )
  {
    final List<TypeVariableName> variables = new ArrayList<>();
    for ( final TypeMirror argument : declaredType.getTypeArguments() )
    {
      variables.add( TypeVariableName.get( (TypeVariable) argument ) );
    }
    return variables;
  }

  @Nonnull
  static TypeName[] getTypeArgumentsAsTypeNames( @Nonnull final DeclaredType declaredType )
  {
    final List<TypeVariableName> variables = getTypeArgumentsAsNames( declaredType );
    return variables.toArray( new TypeName[ variables.size() ] );
  }
}
