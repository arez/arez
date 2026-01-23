package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.ObservableInitial;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@ArezComponent
abstract class ObservableInitialModel
{
  @Observable
  @Nonnull
  abstract String getName();

  abstract void setName( @Nonnull String name );

  @Observable
  @Nonnull
  abstract String getFirstName();

  abstract void setFirstName( @Nonnull String firstName );

  @Observable
  abstract int getAge();

  abstract void setAge( int age );

  @Observable
  @Nullable
  abstract String getNickname();

  abstract void setNickname( @Nullable String nickname );

  @ObservableInitial
  @Nonnull
  static final String INITIAL_NAME = "Bob";

  @ObservableInitial
  @Nonnull
  static final String INITIAL_FIRST_NAME = "Jane";

  @ObservableInitial
  static int getInitialAge()
  {
    return 23;
  }

  @ObservableInitial( name = "nickname" )
  static String initialNickname()
  {
    return "Bobby";
  }
}
