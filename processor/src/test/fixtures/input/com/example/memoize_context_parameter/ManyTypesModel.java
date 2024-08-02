package com.example.memoize_context_parameter;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.MemoizeContextParameter;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;

@ArezComponent
abstract class ManyTypesModel
{
  @Memoize
  public long getTime()
  {
    return 0;
  }

  @Nonnull
  @Memoize
  public String getText()
  {
    return "";
  }

  @Nonnull
  public List<String> getTextCollection()
  {
    return Collections.singletonList( "" );
  }

  @Memoize
  public long count( final long time, float someOtherParameter )
  {
    return time;
  }

  @Nonnull
  @MemoizeContextParameter
  String captureMyContextVar()
  {
    return "";
  }

  void pushMyContextVar( String var )
  {
  }

  void popMyContextVar( String var )
  {
  }

  @MemoizeContextParameter
  int captureMyContextVar2()
  {
    return 0;
  }

  void pushMyContextVar2( int var )
  {
  }

  void popMyContextVar2( int var )
  {
  }
}
