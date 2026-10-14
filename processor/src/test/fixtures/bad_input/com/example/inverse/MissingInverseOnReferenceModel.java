package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Inverse;
import arez.annotations.Observable;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import java.util.Collection;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class MissingInverseOnReferenceModel
{
  @NullMarked
  @ArezComponent
  abstract static class Car
  {
    @Inverse
    abstract @Nullable Collection<Wheel> getWheels();
  }

  @NullMarked
  @ArezComponent
  abstract static class Wheel
  {
    @Reference
    abstract @Nullable Car getCar();

    @ReferenceId
    @Observable
    abstract int getCarId();

    abstract void setCarId( int carId );
  }
}
