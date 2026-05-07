package com.example.requires_transaction;

import arez.annotations.ArezComponent;
import arez.annotations.RequiresTransaction;

@ArezComponent
public abstract class FinalRequiresTransactionModel
{
  @RequiresTransaction
  final void perform()
  {
  }
}
