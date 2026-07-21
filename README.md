<p align="center">
  <img src="src/main/resources/icon.png" width="180" alt="Lush Grass logo">
</p>

# Lush Grass

Lush Grass is a lightweight, vanilla-style client visual mod for Minecraft
1.20.1 on Forge.

![Lush Grass before and after comparison](docs/images/before-after_1.png)

## Features

- Enhances vanilla grass blocks so grasslands look lusher while retaining the
  vanilla style.
- Provides a connected-texture appearance for grass blocks, creating more
  natural visual transitions between adjacent grass blocks.
- Provides complete snowy grass-block coverage for a cleaner appearance in
  snowy environments.
- Renders grass tufts on unobstructed grass blocks to add depth and variety to
  grasslands.
- Provides client-side configuration with independent controls for grass-block
  coverage and grass-tuft rendering.
- Integrates with Oculus and Embeddium so added grass tufts use vanilla grass
  shader materials, including shader-pack vegetation movement where supported.

## Requirements

- Forge 47.2.0 or newer for Minecraft 1.20.1.

Server installation is not required.

Rubidium and Embeddium are alternatives and should not be installed together.
When Oculus and Embeddium are both installed, Lush Grass marks its additional
grass geometry as vanilla short grass for shader material mapping.

## Configuration

Open **Mods > Lush Grass > Config**, then select **Visuals** to change the two
rendering options in game.

The client configuration is stored in `config/lush_grass-client.toml`.

| Option | Default | Description |
| --- | --- | --- |
| `visuals.full_grass_block_coverage` | `true` | Improves vanilla grass blocks with continuous grass coverage. |
| `visuals.render_grass_tufts` | `true` | Renders short grass on unobstructed vanilla grass blocks. |

Changing either option refreshes the affected chunks.

## License

- Lush Grass is licensed under the [MIT License](LICENSE).
- Third-party notices: [NOTICE](NOTICE)
