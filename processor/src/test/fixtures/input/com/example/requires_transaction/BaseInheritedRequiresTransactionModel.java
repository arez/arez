package com.example.requires_transaction;

import arez.annotations.RequiresTransaction;

abstract class BaseInheritedRequiresTransactionModel
{
  @RequiresTransaction
  void performBase()
  {
  }
}
