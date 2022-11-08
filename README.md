# Ghost Block
_Ghost Block is a Spigot plugin dedicated to create Ghost Blocks (Blocks only visible to one player) & in a future Ghost NPCs_
## TODO: 
- [x] Ghost Blocks (Ghost Block Command)
- [x] Ghost Block Clear (Ghost Block Clear Command) _(By using this feature, you restore the original terrain to the player)_
- [x] Ghost Block Fill (Ghost Block Fill SubCommand)
- [x] Ghost Block Clear with Fill (Ghost Block Clear Command) _(By using this feature, you restore the original terrain to the player)_
- [x] Blocks Persistent Through World Unload _(When we leave the chucks and return we should still have some block, halfway done because rn you have to update by clicking the block in order to recover the block)_
- [x] Ghost Blocks Impossible To Break
- [ ] Ghost NPCs (Ghost NPC Command)
- [ ] Ghost NPC Clear (Ghost NPC Clear Command)
- [ ] Clean Up Code (Remove comments and unused code)
## Features
- Ghost Blocks (Ghost Block Command)
- Ghost Block Clear (Ghost Block Clear Command) _(By using this feature, you restore the original terrain to the player)_
- Ghost Block Fill (Ghost Block Fill SubCommand)
- Ghost Block Clear with Fill (Ghost Block Clear Command) _(By using this feature, you restore the original terrain to the player)_
- Blocks Persistent Through World Unload _(When we leave the chucks and return we should still have some block, halfway done because rn you have to update by clicking the block in order to recover the block)_
- Ghost Blocks Impossible To Break
## Dependencies
- World Edit ^7.20
- Protocol Lib ^4.8.0
- hCore (Right now isn't actually being used, is going to be used in the future when we add NPCs, should be shadow into the JAR file) (Build time only) ^0.5.1
## Setup
**For Devs:**  
Simply clone this repo by doing
```console
git clone https://github.com/Ftsos/GhostBlock
```
Then import into Intellij.

To Build the repo just run the package maven lifecycle goal by using the Intellij IDE or by running
```console
mvn clean package
```

---

**For Users:**  
Just run the plugin jar, with the runtime dependencies (World Edit, Protocol Lib)

## Contributors
- Ftsos (Main Dev) (Discord: F_tsos#4081)
- DonKolia (Gave ideas, helped testing, and was used on his server) (Discord: DonKolia#5845) 
## Disclaimer
I am not planning to provide further support, or to continue working on this project. DO NOT EXPECT ME TO UPDATE THIS TO FUTHER MC VERSIONS OR TO PROVIDE SUPPORT. This doesn't represent my actual coding skills, I know the code is really bad but I did it asap and the plugin was not planned with all this functionality in mind, it was designed to be a small plugin that simply sends one block at a time, the code needs to be refactored and proper bug fixes applied in order to continue with this project.
