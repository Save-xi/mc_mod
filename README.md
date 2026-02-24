# McMod

A Minecraft Fabric mod built with Fabric Loader for Minecraft 1.20.1.

## Requirements

- Java 17 or higher
- Gradle 8.3+

## Building

```bash
./gradlew build
```

The compiled `.jar` will be in `build/libs/`.

## Development Setup

1. Clone the repository.
2. Run `./gradlew genSources` to generate Minecraft sources.
3. Import the project into your IDE (IntelliJ IDEA recommended).

## Running in Development

```bash
./gradlew runClient
```

## Project Structure

```
src/main/java/com/savexi/mcmod/McMod.java   - Main mod initializer
src/main/resources/fabric.mod.json          - Mod metadata
```

## License

MIT