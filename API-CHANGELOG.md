# Changelog

All notable changes **to the API and library** of this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/), and this project adheres
to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased] - ${maven.build.timestamp}

### API

#### Added
- initial release of the new BetonQuest API
- `FunctionExpression` as an atomic evaluable element of a function
- `FunctionDefinition` and `FunctionAssignment` as a way to split functions into declaration and calculation parts
- `MathFunction` as a way to define functions combining declaration and calculation parts
- `FunctionProvider` as a retriever for functions
- `FunctionIdentifier` as `ReadableIdentifier` to identify functions in the user script
- `Functions` interface to access functions defined in the user script
- `BetonQuestApi::functions` to retrieve the `Functions` instance
- `QuestPredicate` as equivalent to `QuestBiPredicate` with only a single argument
#### Changed

#### Deprecated

#### Removed

### Library

#### Added

#### Changed

#### Deprecated

#### Removed
