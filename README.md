# ClearLag Plugin for 1.21.4

[![Minecraft Version](https://img.shields.io/badge/Minecraft-1.21.4-green)](https://www.minecraft.net)
[![PaperMC Compatible](https://img.shields.io/badge/PaperMC-Compatible-blue)](https://papermc.io)

## Features

- ⚡ **Configurable automatic clearing** of entities (items, mobs, XP, projectiles, vehicles)
- 🛡️ **Filtering** with whitelist/blacklist for items
- 🧊 **Chunk entity limits** to prevent overcrowding
- ⏳ **Age-based clearing** to protect newly dropped items
- 💬 **Customizable messages** with color codes and placeholders
- 🔐 **Permission system** with bypass option
- 🐛 **Debug logging** for troubleshooting

## Installation

1. Download the latest version from [Releases](#)
2. Place the `ClearLag.jar` in your server's `plugins` folder
3. Restart your server
4. Configure the plugin by editing `plugins/ClearLag/config.yml`
5. Reload config with `/clearlag reload` (or restart server)

## Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/clearlag` | Show help menu | `clearlag.command` |
| `/clearlag now` | Clear entities immediately | `clearlag.command` |
| `/clearlag reload` | Reload the configuration | `clearlag.command` |

## Permissions

| Permission | Description | Default |
|------------|-------------|---------|
| `clearlag.command` | Allows use of all commands | op |
