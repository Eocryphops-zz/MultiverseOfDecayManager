
Multiverse of Decay Manager (MoD Manager)
by Phacops
(A.K.A Eocryphops when the Username is taken ;))

https://github.com/Eocryphops/MultiverseOfDecayManager

====
Want a better ReadMe?, or just the Modder's Instructions on how to use?
====
See the readme on GitHub for better details on this: https://github.com/Eocryphops/MultiverseOfDecayManager/blob/master/README.md



====
How to Install & Operate
====

----
If you only have this because you installed a mod that came packaged with it:
----

* Do the following:
 * Unpack into your SoD game directory, probably at -
 * C:\Program Files (x86)\Steam\steamapps\common\State of Decay YOSE\Game\

* After unpacking:
 * Simply double-click to run the .jar, like you would any executable and it'll apply the mod.
 * If you're worried about the contents, then no worries - they're open source :)
 * No creepy crawlies in there, mate. See the source here - https://github.com/Eocryphops/SoDModManager

----
If you have multiple mods that came packed with this, then you can exclude ones you don't want to run:
----

 * You'll do that in: /XmlMods/ModManagement/XmlFilesToExcludeFromBuilding.txt
 * This will be in an interface for you in the future
 * If you do change which ones are included/excluded, see the instructions in the next section for changes

----
If you made changes to the XML files or included/excluded a mod:
----

If: 
* You're a modder/user and you made changes to a mod.xml
* You exclude/include a mod from:
 * /XmlMods/ModManagement/XmlFilesToExcludeFromBuilding.txt

Then:
* Any time you make a change, or have made a change:
 * Rerun the .jar by double-clicking on it, like you would any executable
 * That will apply your changes to the relevant files
 * Should take a few seconds to run as it will clean up any old objects then apply