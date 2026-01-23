package arez.doc.examples.at_observable_initial;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.ObservableInitial;
import javax.annotation.Nonnull;

@ArezComponent
abstract class MyModel
{
  @Observable
  @Nonnull
  abstract String getName();

  abstract void setName( @Nonnull String name );

  @Observable
  abstract int getAge();

  abstract void setAge( int age );

  @ObservableInitial
  @Nonnull
  static final String INITIAL_Name = "Anonymous";

  @ObservableInitial
  static int getInitialAge()
  {
    return 18;
  }
}
