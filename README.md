# capital-gain

Nubank's Capital Gain Code Challenge.

## Table of Contents

- [Installation](#installation)
- [Usage](#usage)
- [Tests](#tests)
- [Lint](#lint)
- [Architecture](#architecture)
	- [core](#core)
	- [ports](#ports)
	- [controller](#controller)
	- [adapters](#adapters)
	- [business-logic](#business-logic)
	- [storage](#storage)
	- [in-memory-storage](#in-memory-storage)
	- [database](#database)

## Installation

To install the project you will need either `Docker` or `leiningen`. For `Docker`, there's a `Makefile` to ease not having to remember lenghty commands. For `leiningen`, the commands are pretty standard (`deps`, `run` and `test`).

To build the image just run:

```bash
$ make build-app
# or
$ lein deps
```

## Usage

To run you just have to pass the lists of operations via stdin in the JSON format. The cases provided in the spec PDF are in the [`resources`](resources) folder.

```bash
$ make run < resources/case_0.json
# or
$ lein run < resources/case_0.json
```

The output should be:

```json
[{"tax":0},{"tax":10000.0}]
[{"tax":0},{"tax":0}]
```

## Tests

To run the tests you should build their image first:

```bash
$ make build-test
```

Then to run them:

```bash
$ make test
# or
$ lein test # doesn't need the image
```

## Lint

The project uses [`clj-kondo`](https://github.com/borkdude/clj-kondo) for linting. You can either run it via `Docker` or directly in your machine.

```bash
$ make lint
# or
$ clj-kondo --lint src test
```

## Architecture

The code is inspired by the Hexagonal Architecture. The main layers of this project are:

- ports
- controller
- adapters
- business-logic

The `ports` layer will interact with the extern world. In our case, it will handle the user input from stdin.

Then the `controller` will process each list of operations using the `adapters` layer to both: pass data to the `business-logic`, and to return back the result to the `port`.

Here is a visual representation simplified:

<p align="center">
  <img src="https://user-images.githubusercontent.com/15306309/87804794-c2c39280-c82a-11ea-9390-0d7a5a1806db.png" alt="hexagonal-architecture" />
</p>

The reasoning behind this is that the `business-logic` is isolated so it has no knowledge of the outside world. The advantage of this is that we can create different `ports` and `adapters` to use the same logic with different circumstances, like to process a HTTP request, a Kafka message, CLI stdin, etc.

Below there is a simple explanation of each layer/namespace:

### core

> Entrypoint for the application.

It has the `-main` function, and since we are doing a CLI program, it only calls the `cli-stdin` port.

### ports

> Handles the outside world.

Right now it only has an implementation for processing CLI stdin. This function processes the stdin by accumulating lines until it finds the end of a JSON list (`]`), once that buffer is complete, it will call the controller with it.

### controller

> Handles a list of operations.

It has the `controller` function which adapts both at entry and exit of it to use the pure `business-logic` in the middle.

Also since the application is stateful (until a list of operations is finished processing), it uses the `storage` layer to save the data.

### adapters

> Glues the ports entrance and exit for internal components.

In our case it converts JSON to EDN and vice versa.

### business-logic

> Pure business logic.

It doesn't have any side effects, it just purely converts some data structures to other ones by applying the business rules.

### storage

> Contains the Storage protocol for storing and retrieving data.

### in-memory-storage

> Implements the Storage protocol for in memory operations.

It uses an `atom` to avoid concurrency problems.

### database

> Provides helper functions over the Storage protocol to deal with application entities with ease.
