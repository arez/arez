package com.example.type_params;

import arez.annotations.ArezComponent;
import java.io.IOException;
import java.io.Writer;

@ArezComponent
public class ConcreteModel<W extends Writer>
  extends MiddleModel<W>
{
  public ConcreteModel( final W writer )
  {
    super( writer );
  }

  public ConcreteModel( final IOException error )
  {
    super( error );
  }

  public ConcreteModel( final IOException error, final W writer, final int i )
  {
    super( error, writer, i );
  }
}
