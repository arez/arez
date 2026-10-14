package com.example.requires_transaction;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.RequiresTransaction;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class ObserveAndRequiresTransactionModel
{
  @Observe
  @RequiresTransaction
  void perform()
  {
  }
}
