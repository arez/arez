---
title: Scheduler
---

Arez contains a scheduler that is responsible for:

* executing the autorun observer's tracked functions when any dependencies of the tracked function are changed.
* executing the tracker observer's scheduling callback when any dependencies of the tracked function are changed.
* recalculating the computed value when any dependencies of the computed value is changed since it was last calculated.

In all cases the Arez scheduler reacts to changes in the Arez state and schedules a "reaction". Arez uses
[transactions](transactions.md) to batch changes to Arez state and will only attempt to schedule a reaction when
the outer most transaction is completed. This makes sure intermediate or incomplete values produced during a
transaction are not visible to the rest of the application until the transaction has completed.

The scheduler contains a list of pending reactions and will execute the reactions in the order in which they
were scheduled. A reaction is scheduled the first time any dependency of the reaction is changed (i.e. the
{@api_url: Observable.reportChanged()::Observable::reportChanged()} is invoked on the dependency). The reaction
will not be rescheduled while the reaction is in the pending reactions list.

Recalculation of computed values can be scheduled but before the transaction has completed the transaction may try
to read the computed value in which case the computed value will be re-calculated immediately. The computed value
will still appear in the pending reactions list but the reaction will be skipped by the scheduler.

The API allows users to create autorun observers that will not run immediately. This is sometimes necessary when
creating complex components that need to be completely constructed prior to enabling the autorun tracking function.
To support this the Arez api provides the {@api_url: ArezContext.triggerScheduler()::ArezContext::triggerScheduler()}
method that will start the scheduler if there are pending reactions and the scheduler is not currently running.

## Pausing and Resuming

It is also possible to pause and resume the scheduler when needed. This is rarely needed as it is the only mechanism
where it is possible to create an inconsistent state in Arez. (i.e. Observables have updated but not all the autorun
observers and computed values have reacted and are consistent with the new state.)

However it is sometimes needed by Arez-based frameworks with complex concurrency needs that can ensure that no code
interacts with Arez components while the scheduler is paused. A simple example of how pausing works is as follows:

{@file_content: file=org/realityforge/arez/doc/examples/scheduler/SchedulerExample.java "start_line=^  {" "end_line=^  }" include_start_line=false include_end_line=false strip_block=true}
