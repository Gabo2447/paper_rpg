# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased] - ${maven.build.timestamp}

### Added
- new `kernel system` featuring topological component loading and dependency injection for core modules
- new custom functional interfaces
- new custom `logging system` with factory support, topic logging, and exception handling
- new `configuration` access system
### Changed
### Fixed
- error handling and dependency resolution in the topological component loader to support polymorphic types
### Deprecated
### Removed
- `SimpleCoreComponentLoader` loader
### Security

## [1.2.0] - 2026-07-16

### Added
- new mana system for abilities
- new visual controller for user interface updates

### Changed
### Fixed
### Deprecated
### Removed
### Security

## [1.1.0] - 2026-07-16

### Added
- unified `/use` command that automatically detects and executes the active equipped ability, replacing `/jump`
- local SQLite database support for storing class data and cooldowns

### Changed
- background threading for database loading and saving to prevent server lag
- hikariCP connection pool integration for optimized database communication

### Fixed
### Deprecated
### Removed
### Security

## [1.0.1] - 2026-07-15

### Added
### Changed
- refactored storage system to use a clean `DataManager` interface and YAML implementation

### Fixed
- critical memory leak where player data remained in RAM after disconnecting

### Deprecated
### Removed
### Security

## [1.0.0] - 2026-07-15

### Added
- new class and skill system featuring `Wind Leap` (active) and `Lava Leather` (passive)
- automatic saving of player profiles in YAML format
- interactive custom inventory menu (`SkillsMenuHolder`) for skill selection

### Changed
### Fixed
### Deprecated
### Removed
### Security