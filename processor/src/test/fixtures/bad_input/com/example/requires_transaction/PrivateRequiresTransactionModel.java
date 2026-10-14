package com.example.requires_transaction;

import arez.annotations.ArezComponent;
import arez.annotations.RequiresTransaction;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
public abstract class PrivateRequiresTransactionModel
{
  @RequiresTransaction
  private void perform()
  {
  }
}
