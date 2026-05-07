package com.example.requires_transaction;

import arez.annotations.ArezComponent;
import arez.annotations.RequiresTransaction;

@ArezComponent
public abstract class AbstractRequiresTransactionModel
{
  @RequiresTransaction
  abstract void perform();
}
