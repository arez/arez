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
  abstract static class Car
  {
    @Inverse
    abstract Collection<Wheel> getWheels();
  }

  @ArezComponent
  abstract static class Wheel
  {
    @Reference
    abstract Car getCar();

    @ReferenceId
    @Observable
    abstract int getCarId();

    abstract void setCarId( int carId );
  }
}
