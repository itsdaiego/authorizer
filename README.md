# Authorizer

# Overview

This challenge uses 

# Installation & Usage

To run this project you will need both [docker](https://www.docker.com/) installed.

To run the application follow these steps

- `make build`

- `make run-app < operations-samples/create-valid-transaction-flow`

There are different types of operations stored in `operations-samples`, each dealing with different type of valid operation and invalid ones.


# Architecture


## Ports

That's the entrypoint to our application, a port might be anything from the "outside world" that connects to a given system, it could be a http server handling a request, a consumer from a messasing system (i.e Kafka) or in this case, an input from the stdin.


## Controllers

The controller will be responsible for handling the "flow" of the operation, it will first parse the input given by the port layer, parse using the adapter, call functins to deal with business logic and finally, parse the data back and send it back to the port.

## Adapters

Simple layer to translate a input from the outside world to a different format expected by the business logic layer (in this case a json to hash map)

## Logic

Layer responsible to deal with the business logic, it does not have knowledge about the persistence layer and expects the data to already be formatted by an adapter.

## Db

Layer responsible to abstract the implementation of creating or reading data from storage.
