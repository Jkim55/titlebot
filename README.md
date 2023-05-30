# Title Bot

A project composed of two applications, `titlebot-server` & `titlebot-client`.

As expected given the naming, `titlebot-server` is the backend app written in Scala with Akka-HTTP and `titlebot-client` is the frontend app written in React with JavaScript.

`titlebot-client` serves up a webapp with a form that allow users can look up a website's title & favIcon given a domain (sans the "www" prefix).

`titlebot-server` handles the request from `titlebot-client` given a valid domain input.

Example of domains to search:
```
cnn.com
nytimes.com
cooking.nytimes.com/recipes
cooking.nytimes.com/recipes/1023629-butternut-squash-congee-with-chile-oil
```

## Running the Apps
Run the applications in two steps. 

### Step 1 
At the project root, open two terminal session & run:
```
$ ./start_backend.sh
```
Application runs on port 9000.

### Step 2
Open another terminal session & run:
```
$ ./start_frontend.sh
```
Application runs on port 3000.

To view the running application, navigate to: `http://localhost:3000`