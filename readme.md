Extends the [Muse Test Framework](https://github.com/ChrisLMerrill/muse) with HTTP capabilities using the 
[OkHttp](https://github.com/square/okhttp) libraray.

# Installation

Build the library and drop the JAR into the *lib* folder of your project. After restarting 
[MuseIDE](http://ide4selenium.com), the new capabilities will be available.  

# Usage

## Steps

### Create client

The *Create client* step initializes a new HTTP client. The client is responsible for opening, re-using and closing 
connections to a web server.

*Factory* parameter is required - it must reference an *HTTP Client Configuration* in your project.

*Client name* parameter is optional - it is the name of the variable that will hold the HTTP client. If not supplied, the default 
name will be used ("_okhttp_client"). All HTTP commands that need a client will also use this name by default. Use of this is only recommended 
if maintaining multiple HTTP clients.

### Get a URL

Issue a GET request for a URL.

*URL* parameter is required - it must resolve to a string formatted as a valid URL.

*Result name* parameter is optional - it specifies the name of the variable that will hold the HTTP result object.
The default is "result".

*Client* parameter is optional - it must resolve to an HTTP client object (e.g. $"_okhttp_client").      

## Value Sources

### cookieValue

Returns the value of a cookie from the CookieStore in the default HTTP client.

*Name* parameter is required - the name of the cookie to retrieve the value from.

## Data objects

### HTTP client

Represents a collection of open connections to web servers. Opens new connections when needed, re-uses and closes 
connections based on its configuration and HTTP requirements. 

### HTTP result

Contains the result of an HTTP transaction. 

*success* is a boolean property indicating the status of the transaction.

*failure_message* a string property which may indicate the cause of failure when applicable (otherwise null). 

*response* is a property of type Response (from the OkHttp library). Useful sub-properties include:

1. code (integer)
2. message (string)
3. body - a ResponseBody (from the OkHttp library) that contains these useful sub-properties:
  * bytes - the raw bytes of the body content.
  * contentLength (integer) - the length of the body (bytes).
  * string - a string reprsentation of the body content.
  
# Thanks and Credits

I originally developed this as part of my load testing work for [Web Performance](http://webperformance.com/). Thanks 
goes there for allowing me to open-source the work. I have several posts on the 
[Web Performance Blog](https://www.webperformance.com/load-testing-tools/blog/) related to 
[MuseIDE](https://www.webperformance.com/load-testing-tools/blog/category/museide/) and 
[load testing](https://www.webperformance.com/load-testing-tools/blog/category/load-testing/).  