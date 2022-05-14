# WebSocketScratch
Implementing a web socket server from *scratch* in Java using only Java SE

## Description
This web-sockets server is implemented using Java Server Sockets and Java Sockets which are located in java.net package.

## How to Run
* Clone the project
* Navigate to WebSocket class and run the main method.
* Can test out the WebSocket Handshake process by the dummy-curl script which I have included.

### Using WSCat
* Install wscat using

```
npm install -g wscat
```
* execute the following inside a terminal

```
wscat -c localhost:8080
```

#### Useful Resources for A Deep Dive
* https://ably.com/topic/websockets
* https://en.wikipedia.org/wiki/WebSocket
* https://developer.mozilla.org/en-US/docs/Web/API/WebSockets_API
* https://docs.oracle.com/javase/tutorial/networking/sockets/index.html
