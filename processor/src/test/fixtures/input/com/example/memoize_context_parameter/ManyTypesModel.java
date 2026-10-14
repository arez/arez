package com.example.memoize_context_parameter;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.MemoizeContextParameter;
import java.util.Collections;
import java.util.List;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class ManyTypesModel
{
  @Memoize
  public long getTime()
  {
    return 0;
  }

  @NonNull
  @Memoize
  public String getText()
  {
    return "";
  }

  @NonNull
  public List<String> getTextCollection()
  {
    return Collections.singletonList( "" );
  }

  @Memoize
  public long count( final long time, float someOtherParameter )
  {
    return time;
  }

  @NonNull
  @MemoizeContextParameter
  String captureMyContextVar()
  {
    return "";
  }

  void pushMyContextVar( @Nullable String var )
  {
  }

  void popMyContextVar( @Nullable String var )
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
