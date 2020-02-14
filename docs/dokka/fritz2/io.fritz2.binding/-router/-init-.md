[fritz2](../../index.md) / [io.fritz2.binding](../index.md) / [Router](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`Router(route: `[`Route`](../-route/index.md)`<T>)`

Router register the event-listener for hashchange-event and
handles route-changes. Therefore it uses a [Route](../-route/index.md) object
which can [Route.marshal](../-route/marshal.md) and [Route.unmarshal](../-route/unmarshal.md) the given type.

### Parameters

`T` - type to marshal and unmarshal