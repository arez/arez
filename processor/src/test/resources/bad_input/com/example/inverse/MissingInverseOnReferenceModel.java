package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Inverse;
import arez.annotations.Observable;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import java.util.Collection;

public class MissingInverseOnReferenceModel
{
  @ArezComponent
  static abstract class Wheel
  {
    @Reference
    abstract Car getCar();

    @ReferenceId
    @Observable
    abstract int getCarId();

    abstract void setCarId( int carId );
  }

  @ArezComponent
  static abstract class Car
  {
    @Inverse
    abstract Collection<Wheel> getWheels();
  }
}
