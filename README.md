<p align="center">
  <img src="src/main/resources/icon.png" width="180" alt="Lush Grass logo">
</p>

# Lush Grass

Lush Grass is a lightweight, vanilla-style visual and world-generation mod for
Minecraft 1.21.1 on NeoForge.

## Features

- Gives vanilla grass blocks a continuous biome-tinted grass surface while
  keeping the dirt underside.
- Covers the exposed grass faces with snow when the vanilla `snowy` state is
  active.
- Renders short, biome-tinted grass tufts on unobstructed grass blocks as part
  of the cached chunk model.
- Adds `lush_grass:low_grass`, a placeable grass plant at three quarters of
  the vanilla short-grass height.
- Reuses vanilla short-grass survival, replacement, bonemeal, shears, seed
  drops, sounds, offset, and composting behavior.
- Replaces common vanilla grass-patch features so roughly half of generated
  short grass becomes low grass where applicable.
- Integrates with the standard NeoForge configuration screen and identifies
  low grass as vanilla short grass to supported Iris/Sodium shader pipelines.

The visual additions use chunk geometry. Lush Grass adds no entities, block
entities, ticks, per-frame block scans, or per-block draw calls.

## Requirements

- Minecraft 1.21.1
- NeoForge 21.1.152 or newer for Minecraft 1.21.1
- Java 21

Iris and Sodium are optional.

## Configuration

The client configuration is stored in `config/lush_grass-client.toml`.

| Option | Default | Description |
| --- | --- | --- |
| `visuals.full_grass_block_coverage` | `true` | Improves vanilla grass blocks with continuous grass coverage. |
| `visuals.render_grass_tufts` | `true` | Renders short grass on unobstructed vanilla grass blocks. |

Changing either option triggers one chunk rebuild. Disabling an option makes
the model delegate to the original baked grass-block model.

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

Data packs can override the configured features in
`data/lush_grass/worldgen/configured_feature` and the biome modifier in
`data/lush_grass/neoforge/biome_modifier`. This changes placement and
frequency, not the registered block or its client model.

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
