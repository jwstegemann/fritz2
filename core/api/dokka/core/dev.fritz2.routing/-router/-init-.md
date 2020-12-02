[core](../../index.md) / [dev.fritz2.routing](../index.md) / [Router](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

(js) `Router(route: `[`Route`](../-route/index.md)`<T>)`

Router register the event-listener for hashchange-event and
handles route-changes. Therefore it uses a [Route](../-route/index.md) object
which can [Route.marshal](../-route/marshal.md) and [Route.unmarshal](../-route/unmarshal.md) the given type.

### Parameters

`T` - type to marshal and unmarshal