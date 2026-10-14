package com.example.type_params;

import java.io.IOException;
import java.io.Writer;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
abstract class MiddleModel<W extends Writer>
  extends AbstractModel<IOException, W>
{
  public MiddleModel( final @Nullable W writer )
  {
    super( writer );
  }

  public MiddleModel( final @Nullable IOException error )
  {
    super( error );
  }

  public MiddleModel( final @Nullable IOException error, final @Nullable W writer, final int i )
  {
    super( error, writer, i );
  }
}
