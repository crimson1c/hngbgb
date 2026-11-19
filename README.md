# Pet Whistle

Requires Minecraft Java 1.21.1, Fabric Loader 0.20.1 or newer, Fabric API, and Java 21. Install the mod and Fabric API on both client and server.

Craft with two iron ingots, a bone and string: iron in the top middle and center, bone at middle right, string at bottom left. Right-click to recall loaded, nearby tamed wolves, cats and parrots in the same dimension. Sneak-right-click cycles pet types. Leashed and riding pets are skipped. A successful use costs one durability (128 uses) and applies a ten-second cooldown. The search is a box around the player; unloaded pets are not loaded by the whistle.

Edit `config/petwhistle.properties` and restart to change `range` (1–128 blocks, default 32) and `cooldownTicks` (20 ticks per second, default 200).

This initial implementation was AI-generated. Review Modrinth's current AI policy before publishing. The item currently uses Minecraft's goat horn texture as a placeholder.
