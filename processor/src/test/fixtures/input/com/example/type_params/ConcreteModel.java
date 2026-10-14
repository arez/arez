package com.example.type_params;

import arez.annotations.ArezComponent;
import java.io.IOException;
import java.io.Writer;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class ConcreteModel<W extends Writer>
  extends MiddleModel<W>
{
  ConcreteModel( final @Nullable W writer )
  {
    super( writer );
  }

  ConcreteModel( final @Nullable IOException error )
  {
    super( error );
  }

  ConcreteModel( final @Nullable IOException error, final @Nullable W writer, final int i )
  {
    super( error, writer, i );
  }
}
