## Reproducer for https://github.com/eclipse-ee4j/jersey/issues/4097

### How to run
Build with gradle and run the `main` method of the `Main` class.

### Explanation
The `main` method starts a server with a resource that reuses a `jakarta.ws.rs.core.Response` object. This is obviously wrong.
Then three requests to this resource are performed. The third request reproducibly hangs on server with this stacktrace:

```
"qtp1299641336-26@1431" prio=5 tid=0x1a nid=NA waiting
  java.lang.Thread.State: WAITING
	  at jdk.internal.misc.Unsafe.park(Unsafe.java:-1)
	  at java.util.concurrent.locks.LockSupport.park(LockSupport.java:211)
	  at java.util.concurrent.CompletableFuture$Signaller.block(CompletableFuture.java:1864)
	  at java.util.concurrent.ForkJoinPool.unmanagedBlock(ForkJoinPool.java:3463)
	  at java.util.concurrent.ForkJoinPool.managedBlock(ForkJoinPool.java:3434)
	  at java.util.concurrent.CompletableFuture.waitingGet(CompletableFuture.java:1898)
	  at java.util.concurrent.CompletableFuture.get(CompletableFuture.java:2072)
	  at org.glassfish.jersey.servlet.internal.ResponseWriter.getResponseContext(ResponseWriter.java:275)
	  at org.glassfish.jersey.servlet.internal.ResponseWriter.callSendError(ResponseWriter.java:191)
	  at org.glassfish.jersey.servlet.internal.ResponseWriter.commit(ResponseWriter.java:170)
	  at org.glassfish.jersey.server.ContainerResponse.close(ContainerResponse.java:390)
	  at org.glassfish.jersey.server.ServerRuntime$Responder.writeResponse(ServerRuntime.java:714)
	  at org.glassfish.jersey.server.ServerRuntime$Responder.processResponse(ServerRuntime.java:373)
	  at org.glassfish.jersey.server.ServerRuntime$Responder.process(ServerRuntime.java:363)
	  at org.glassfish.jersey.server.ServerRuntime$1.run(ServerRuntime.java:258)
	  at org.glassfish.jersey.internal.Errors$1.call(Errors.java:248)
	  at org.glassfish.jersey.internal.Errors$1.call(Errors.java:244)
	  at org.glassfish.jersey.internal.Errors.process(Errors.java:292)
	  at org.glassfish.jersey.internal.Errors.process(Errors.java:274)
	  at org.glassfish.jersey.internal.Errors.process(Errors.java:244)
	  at org.glassfish.jersey.process.internal.RequestScope.runInScope(RequestScope.java:265)
	  at org.glassfish.jersey.server.ServerRuntime.process(ServerRuntime.java:234)
	  at org.glassfish.jersey.server.ApplicationHandler.handle(ApplicationHandler.java:684)
	  at org.glassfish.jersey.servlet.WebComponent.serviceImpl(WebComponent.java:394)
	  at org.glassfish.jersey.servlet.WebComponent.service(WebComponent.java:346)
	  at org.glassfish.jersey.servlet.ServletContainer.service(ServletContainer.java:358)
	  at org.glassfish.jersey.servlet.ServletContainer.service(ServletContainer.java:311)
	  at org.glassfish.jersey.servlet.ServletContainer.service(ServletContainer.java:205)
	  at org.eclipse.jetty.servlet.ServletHolder.handle(ServletHolder.java:764)
	  at org.eclipse.jetty.servlet.ServletHandler.doHandle(ServletHandler.java:508)
	  at org.eclipse.jetty.server.handler.ScopedHandler.nextHandle(ScopedHandler.java:221)
	  at org.eclipse.jetty.server.handler.ContextHandler.doHandle(ContextHandler.java:1375)
	  at org.eclipse.jetty.server.handler.ScopedHandler.nextScope(ScopedHandler.java:176)
	  at org.eclipse.jetty.servlet.ServletHandler.doScope(ServletHandler.java:463)
	  at org.eclipse.jetty.server.handler.ScopedHandler.nextScope(ScopedHandler.java:174)
	  at org.eclipse.jetty.server.handler.ContextHandler.doScope(ContextHandler.java:1297)
	  at org.eclipse.jetty.server.handler.ScopedHandler.handle(ScopedHandler.java:129)
	  at org.eclipse.jetty.server.handler.HandlerWrapper.handle(HandlerWrapper.java:122)
	  at org.eclipse.jetty.server.Server.handle(Server.java:562)
	  at org.eclipse.jetty.server.HttpChannel.lambda$handle$0(HttpChannel.java:505)
	  at org.eclipse.jetty.server.HttpChannel$$Lambda$303/0x0000000800dbc918.dispatch(Unknown Source:-1)
	  at org.eclipse.jetty.server.HttpChannel.dispatch(HttpChannel.java:762)
	  at org.eclipse.jetty.server.HttpChannel.handle(HttpChannel.java:497)
	  at org.eclipse.jetty.server.HttpConnection.onFillable(HttpConnection.java:282)
	  at org.eclipse.jetty.io.AbstractConnection$ReadCallback.succeeded(AbstractConnection.java:319)
	  at org.eclipse.jetty.io.FillInterest.fillable(FillInterest.java:100)
	  at org.eclipse.jetty.io.SelectableChannelEndPoint$1.run(SelectableChannelEndPoint.java:53)
	  at org.eclipse.jetty.util.thread.strategy.AdaptiveExecutionStrategy.runTask(AdaptiveExecutionStrategy.java:412)
	  at org.eclipse.jetty.util.thread.strategy.AdaptiveExecutionStrategy.consumeTask(AdaptiveExecutionStrategy.java:381)
	  at org.eclipse.jetty.util.thread.strategy.AdaptiveExecutionStrategy.tryProduce(AdaptiveExecutionStrategy.java:268)
	  at org.eclipse.jetty.util.thread.strategy.AdaptiveExecutionStrategy.lambda$new$0(AdaptiveExecutionStrategy.java:138)
	  at org.eclipse.jetty.util.thread.strategy.AdaptiveExecutionStrategy$$Lambda$237/0x0000000800d3f100.run(Unknown Source:-1)
	  at org.eclipse.jetty.util.thread.ReservedThreadExecutor$ReservedThread.run(ReservedThreadExecutor.java:407)
	  at org.eclipse.jetty.util.thread.QueuedThreadPool.runJob(QueuedThreadPool.java:894)
	  at org.eclipse.jetty.util.thread.QueuedThreadPool$Runner.run(QueuedThreadPool.java:1038)
	  at java.lang.Thread.run(Thread.java:833)
```