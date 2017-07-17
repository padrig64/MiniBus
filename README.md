Yet another in-memory pub/sub event bus for Java 8.

Note that it is **not** ready for production use.

# Principles

* **Publishers** (producers) send events for particular **topics** on different **channels** of the **event bus**.
* **Subscribers** (consumers) receive the events that they are interested in and that they did not **filter** out.

# Features

* **Type safe:** The API relies on compilation rather than convention.
* **Single, static event bus:** For the simplest use cases where only one event bus is needed.
* **Multiple, non static event buses:** For the use cases where several event buses are needed at the same time.
* **Topic hierarchy:** Topics can have a parent topic. A parent topic (e.g. "Geography") is said to cover its child
topics (e.g. "France", "The Netherlands"). Entities subscribing to parent topics will receive events for all child
topics. But entities subscribing to a child topic will not receive events for its parent or peer topics.
* **Typed topics:** The topics defined the type of the content being dispatched.
* **Filtering:** Event filters can be used and based on topics, event classes, content classes, channels or anything you
like.
* **Channels:** Channels may optionally be used for dispatching and can serve as a filter.
* **Channel flows:** Channels can have sub-channels. A parent channel can flow into sub-channels but not the other way
around. For example, events sent on the broadcast channel will be received by all event handlers listening on any
channel. A parent channel may know its children, but a sub-channel does not know its parent.
* **Undelivered event handlers:** Handlers can be defined to process undelivered events, for example, for logging and
debugging purposes.
* **Unchecked exception handlers:** Handlers can be defined to process unchecked exceptions that may be thrown the
events are being dispatched or processed by event handlers.
* **Ready to use:** No need to create classes for each channel, topic, event, filter or handler. The provided simple
implementations can be used for most use cases.
* **Flexible and extensible:** The event bus is unopinionated and can easily be extended for more use cases.