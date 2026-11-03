# BIAST: Minecraft Edition — 0.4.0

Fan-made Minecraft Forge mod project for **Minecraft 1.20.1 / Forge 47.2.0 / Java 17**.

## Included in this source release

- Four hostile NPC entity types: Slacker, Observer, CubedHexa22 and Ashle.
- Individual entity model geometry and per-entity PNG texture files in `assets/biast/textures/entity/`.
- Contractee/Entity player roles and NPC targeting.
- Contractee dash command (`/biast dash`).
- Supply items: Medkit, Stun Charm, Dash Charge, Void Relic and Golden Teapot.
- BIAST Field Guide item (`biast:command_book`), which prints the command and gameplay reference in chat when used.
- LMS soundtrack: the user-provided **ALL BETS ARE OFF (ft. @wintryLyricist) — BIAST OST**, converted from MP3 to OGG for Minecraft.
- LMS is triggered when the online, living Contractee count falls from at least two to exactly one. The track is 1:38.406 (98.406 seconds); the server-side LMS timer is 1,968 ticks (98.4 seconds at 20 TPS).

## Commands

- `/biast help` — command overview.
- `/biast list` — entity and role list.
- `/biast spawn slacker`
- `/biast spawn observer`
- `/biast spawn cubedhexa22`
- `/biast spawn ashle`
- `/biast entity <player> <slacker|observer|cubedhexa22|ashle>` — assign a player as an Entity.
- `/biast human <player>` — return a player to Contractee.
- `/biast dash` — use the Contractee dash.
- `/give @s biast:command_book` — give yourself the in-game guide book.

## Build

1. Install a 64-bit Java 17 JDK.
2. Install Gradle 8.x (or generate/use a Gradle wrapper for your environment).
3. From this project directory run `gradle build`.
4. The jar should be generated under `build/libs/`. Copy it into `.minecraft/mods/`.

This environment did not have Gradle or ForgeGradle dependency access, so this source package has **not** been compiled or launched in Minecraft. Treat it as a development build, not a verified release JAR. Please test on a backup world first.

## Important notes

- The supplied soundtrack is included as `src/main/resources/assets/biast/sounds/music/lms_theme.ogg`; Minecraft sound assets use OGG Vorbis.
- The song is played once near the final Contractee; Minecraft handles the audio playback duration. The 1,968-tick LMS timer is a server-side round timer.
- This project is an unofficial fan-made work and is not affiliated with the BIAST Roblox developers. Textures/models are fan-made approximations rather than extracted official Roblox assets.
