package com.example.type_params;

import java.io.IOException;
import java.io.Writer;
import org.realityforge.arez.annotations.ArezComponent;

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
