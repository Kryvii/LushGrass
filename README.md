<p align="center">
  <img src="src/main/resources/icon.png" width="180" alt="Lush Grass logo">
</p>

# Lush Grass

Lush Grass is a lightweight, vanilla-style client visual mod for Minecraft
1.21.1 on NeoForge.

![Lush Grass before and after comparison](docs/images/before-after.png)

## Features

- Improves the appearance of vanilla grass blocks.
- Adds configurable grass tufts to unobstructed grass blocks.
- Supports customization through resource packs.

## Requirements

- Minecraft 1.21.1
- NeoForge 21.1.152 or newer for Minecraft 1.21.1
- Java 21

Server installation is not required.

## Configuration

The client configuration is stored in `config/lush_grass-client.toml`.

| Option | Default | Description |
| --- | --- | --- |
| `visuals.full_grass_block_coverage` | `true` | Improves vanilla grass blocks with continuous grass coverage. |
| `visuals.render_grass_tufts` | `true` | Renders short grass on unobstructed vanilla grass blocks. |

Changing either option refreshes the affected chunks.

## License

- Source code: [BSD 3-Clause](LICENSE)
- Original non-code assets: [CC BY-NC-SA 4.0](LICENSE-ASSETS)
- Third-party notices: [NOTICE](NOTICE)
