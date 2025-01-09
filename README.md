# Lingua Bibliotheca
[![Discord](https://img.shields.io/discord/790631506313478155?color=0a48c4&label=Discord)](https://discord.gg/8Cx26tfWNs)

Server side translation handler with player based language support. 
Configure the default server language in the config.

If a player joins a server with this mod the server will respect the players client language, otherwise 
the servers language will be used. 

### For Mod(Pack) Devs

To create a translation that can be used server side simply add a file under
`data/modid/lang` (the same structure as a resource pack but with `data` instead of `assets`).
Then just send components as a translatable one as if you would normally for client/server texts. 
This mod will automatically handle everything else.

Additionally this mod allows for multiline translation which require a bit more setup though.
In vanilla text components cannot use linebreaks in them and so a translation with multiple lines is not possible.
Multiline translations in this mod are defined by a number suffix after a translation key  
E.g. If the lang file contains the entries:
```
"some.translation.1": ...
"some.translation.2": ...
"some.translation.3": ...
```
Then "some.translation" has 3 lines
`LanguageAPI#getFormattedKeys` will fetch all multiline translations assigned to the given key
```
LanguageAPI#getFormattedKeys("some.translation")
```
will return the above 3 lines.
You can then send these keys the usual way.

To use this mod as a dependency add the following snippet to your build.gradle:  
```groovy
repositories {
    maven {
        name = "Flemmli97"
        url "https://gitlab.com/api/v4/projects/21830712/packages/maven"
    }
}

dependencies {    
    // Fabric/Loom==========    
    modImplementation include("io.github.flemmli97:lingua_bib:${minecraft_version}-${mod_version}-${mod_loader}")
    
    // Forge==========    
    // Ideally use jar in jar
    compile fg.deobf("io.github.flemmli97:lingua_bib:${minecraft_version}-${mod_version}-${mod_loader}")
}
```

