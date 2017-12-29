---
title: Motivation
---

Arez is a simple, efficient and scalable state management library for client-side applications. It is based
on the same dataflow programming model. The reactive programming model found in spreadsheets is a well known
example of this style of development. A minimal set of data is used to describe the application state and then
the rest of the application is derived from this minimal set of data.

## Managing State is Hard

Managing state can be hard. When state changes it can be difficult to know which parts of the view needs to be
updated, and/or which processes need to be modified and/or cancelled.

Consider the following scenario. If a new item is added to a shopping cart it may involve adding a row to a
table with the item details, updating an item count elsewhere, updating the amount left on a voucher, updating
the subtotal if the voucher reaches $0. It may also several asynchronous processes such as requesting availability
for the item, requesting product details and an image for the product. If that item is immediately removed then
all those actions need to be reversed, and the processes canceled. This may be interleaved with other updates.

Even for this very simple domain, the complexity of the code required to achieve correct or even just
"good enough" state management can be significant.

## Managing State in Arez

The goal of Arez is to eliminate the complexity around managing state.

In an Arez web application, the view is considered a derivation of the application state. This includes all
state that is considered important enough to manage. In a typically application this includes any dynamic data
that is presented on a page, the current url, the state of UI controls (be they pagination controls, form
controls, spinners) etc. Non-view components of a web application should also be derived from the application
state. It would not be unusual to have the WebSocket connections to backend services derived from application
state.

In Arez, the way that the view is modified is to update the the application state and let the view react to
the changes. The view will be notified that the state has been updated and will update itself to reflect the
current state. When the view is purely a projection of the application state, the update process is relatively
easy to implement.

In some scenarios it is useful to derive one piece of data once and use it multiple locations. i.e. In the
shopping cart example, the total value of the goods in the shopping cart may be used to calculate the amount
left on the voucher and the subtotal. Arez has the ability to cache this value and only recalculate it if it
becomes stale.
