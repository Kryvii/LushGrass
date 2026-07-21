<p align="center">
  <img src="src/main/resources/icon.png" width="180" alt="Lush Grass logo">
</p>

# Lush Grass

Lush Grass is a lightweight, vanilla-style client visual mod for Minecraft
1.20.1 on Forge.

![Lush Grass before and after comparison](docs/images/before-after.png)

## Features

- Improves the appearance of vanilla grass blocks.
- Adds configurable grass tufts to unobstructed grass blocks.
- Supports customization through resource packs.

## Requirements

- Minecraft 1.20.1
- Forge 47.2.0 or newer for Minecraft 1.20.1
- Java 17

Server installation is not required.

Oculus and Embeddium are optional. When both are installed, Lush Grass keeps the
additional grass geometry shader-aware where the available Oculus/Sodium hooks
support per-quad material overrides.

## Configuration

The client configuration is stored in `config/lush_grass-client.toml`.

| Option | Default | Description |
| --- | --- | --- |
| `visuals.full_grass_block_coverage` | `true` | Improves vanilla grass blocks with continuous grass coverage. |
| `visuals.render_grass_tufts` | `true` | Renders short grass on unobstructed vanilla grass blocks. |

Changing either option refreshes the affected chunks.

## License

- Lush Grass is licensed under the [MIT License](LICENSE).
- Third-party notices: [NOTICE](NOTICE)
