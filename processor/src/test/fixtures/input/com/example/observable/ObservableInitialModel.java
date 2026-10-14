package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.ObservableInitial;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class ObservableInitialModel
{
  @Observable
  @NonNull
  abstract String getName();

  abstract void setName( @NonNull String name );

  @Observable
  @NonNull
  abstract String getFirstName();

  abstract void setFirstName( @NonNull String firstName );

  @Observable
  abstract int getAge();

  abstract void setAge( int age );

  @Observable
  @Nullable
  abstract String getNickname();

  abstract void setNickname( @Nullable String nickname );

  @ObservableInitial
  @NonNull
  static final String INITIAL_NAME = "Bob";

  @ObservableInitial
  @NonNull
  static final String INITIAL_FIRST_NAME = "Jane";

  @ObservableInitial
  static int getInitialAge()
  {
    return 23;
  }

  @ObservableInitial( name = "nickname" )
  static @Nullable String initialNickname()
  {
    return "Bobby";
  }
}
