package com.example.type_params;

import arez.annotations.ArezComponent;
import java.io.IOException;
import java.io.Writer;

@ArezComponent
abstract class ConcreteModel<W extends Writer>
  extends MiddleModel<W>
{
  ConcreteModel( final W writer )
  {
    super( writer );
  }

  ConcreteModel( final IOException error )
  {
    super( error );
  }

  ConcreteModel( final IOException error, final W writer, final int i )
  {
    super( error, writer, i );
  }
}
