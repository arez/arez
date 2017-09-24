package com.example.type_params;

import java.io.IOException;
import java.io.Writer;

public abstract class MiddleModel<W extends Writer>
  extends AbstractModel<IOException,W>
{
  public MiddleModel( final W writer )
  {
    super( writer );
  }

  public MiddleModel( final IOException error )
  {
    super( error );
  }

  public MiddleModel( final IOException error, final W writer, final int i )
  {
    super( error, writer, i );
  }
}
