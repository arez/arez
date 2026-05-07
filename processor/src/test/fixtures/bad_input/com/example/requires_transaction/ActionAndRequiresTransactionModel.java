package com.example.requires_transaction;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.RequiresTransaction;

@ArezComponent
abstract class ActionAndRequiresTransactionModel
{
  @Action
  @RequiresTransaction
  void perform()
  {
  }
}
