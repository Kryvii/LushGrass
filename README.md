<p align="center">
  <img src="src/main/resources/icon.png" width="180" alt="Lush Grass logo">
</p>

# Lush Grass

Lush Grass is a lightweight, vanilla-style visual and world-generation mod for
Minecraft 1.21.1 on NeoForge.

![Lush Grass before and after comparison](docs/images/before-after.png)

## Features

- Improves the appearance of vanilla grass blocks.
- Adds configurable grass tufts to unobstructed grass blocks.
- Adds placeable low grass and includes it in vanilla grass generation.
- Supports customization through resource packs and data packs.

## Requirements

- Minecraft 1.21.1
- NeoForge 21.1.152 or newer for Minecraft 1.21.1
- Java 21

## Configuration

The client configuration is stored in `config/lush_grass-client.toml`.

| Option | Default | Description |
| --- | --- | --- |
| `visuals.full_grass_block_coverage` | `true` | Improves vanilla grass blocks with continuous grass coverage. |
| `visuals.render_grass_tufts` | `true` | Renders short grass on unobstructed vanilla grass blocks. |

Changing either option refreshes the affected chunks.

## Resource Packs and Data Packs

Resource packs can customize low grass without code:

- Override `assets/lush_grass/blockstates/low_grass.json` to select one or more
  weighted models.
- Override `assets/lush_grass/models/block/low_grass.json` and
  `assets/lush_grass/models/item/low_grass.json` for block and item visuals.
- Override `assets/lush_grass/models/block/grass_tuft.json` to change the model
  used by attached grass tufts.

A resource pack can create visual variants for the existing low-grass block.
Separate variant block IDs still require a mod.

Data packs can override the files in
`data/lush_grass/worldgen/configured_feature` to change the balance between
vanilla short grass and low grass:

| Short grass weight | Low grass weight | Low grass share |
| --- | --- | --- |
| `3` | `1` | 25% |
| `1` | `1` | 50% |
| `1` | `3` | 75% |
| keep | remove the low-grass entry | disabled |

The biome modifier in `data/lush_grass/neoforge/biome_modifier` controls which
vanilla features are replaced. Ferns and other entries can remain unchanged.

When `visuals.full_grass_block_coverage` is enabled, Lush Grass intentionally
uses its full-coverage grass model. Disable that option when a connected
texture pack needs direct control of the vanilla grass-block model.

## Development

```powershell
.\gradlew.bat runData
.\gradlew.bat build
.\gradlew.bat runClient
```

Generated data is committed from `src/generated/resources`; its `.cache`
directory is ignored.

## License

- Source code: [BSD 3-Clause](LICENSE)
- Original non-code assets: [CC BY-NC-SA 4.0](LICENSE-ASSETS)
- Third-party notices: [NOTICE](NOTICE)
