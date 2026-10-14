package com.example.requires_transaction;

import arez.annotations.ArezComponent;
import arez.annotations.RequiresTransaction;
import arez.annotations.TrackingMode;
import arez.annotations.TransactionMode;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( allowEmpty = true )
abstract class ConstrainedRequiresTransactionModel
{
  @RequiresTransaction( mode = TransactionMode.READ_ONLY, tracking = TrackingMode.NON_TRACKING )
  int perform( final int value )
  {
    return value + 1;
  }
}
