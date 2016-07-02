# SoDModManager
For managing mods that require XML changes, thus allowing multiple mods to be installed by adding elements to existing

----


##How to Install & Operate


####If you only have this because you installed a mod that came packaged with it:
	
#####Do the following:
Unpack into your SoD game directory, probably at -
C:\Program Files (x86)\Steam\steamapps\common\State of Decay YOSE\Game\

#####After unpacking:
Simply double-click to run the .jar, like you would any executable and it'll apply the mod.

If you're worried about the contents, then no worries - they're open source :)

No creepy crawlies in there, mate. See the source here - https://github.com/Eocryphops/SoDModManager


#### If you have multiple mods that came packed with this, then you can exclude ones you don't want to run:

You'll do that in: /sodmodmanager/XmlMods/XmlFilesToExcludeFromBuilding.txt

This will be in an interface for you in the future.

If you do change which ones are included/excluded, see the instructions in the next section for changes.


#### If you made changes to the XML files or included/excluded a mod:

If you 're a modder, you make changes to a mod.xml, or exclude/include one from /sodmodmanager/XmlMods/XmlFilesToExcludeFromBuilding.txt, this section is for you. 

Any time you make changes, you'll need to rerun the .jar by double-clicking on it, like you would any executable.
That will apply your changes to the relevant files. Should take a few seconds to run
as it will clean up any old objects then apply.

----


###How to Get Started Using This for Mods/Modding

####Templates

#####Basic Mod Template: 
/sodmodmanager/XmlMods/CopyMe-ModXMLTemplate.xml

Just follow the basic structure there. If you need further instructions, see the following 
file with the same structure, 


#####Mod Template with Instructions: 
/sodmodmanager/XmlMods/Instructions - ModXML.xml


#####Actualized Example Mod: /sodmodmanager/XmlMods/FortitudeMod.xml
The file for my Fortitude Mod which uses this system, which holds exact usages
of the elements which work correctly in the mod when run.


#####Exclusion File:
/sodmodmanager/XmlMods/XmlFilesToExcludeFromBuilding.txt

For users, or modders, wanting to exclude a .xml file from being parsed. 


####Further Instructions for Modders

1) You'll add a new .xml file that you can copy from the basic of instructional templates listed above,
	depending on whether you need additional instructions and examples or not.
2) Fill in your own mod data, with all the objects you need in each file.
3) Once you're filled up, and match the excpected structure, then you just run the .jar whenever you make changes
	that will delete all of your old objects and add the new versions from the .xml in XmlMods.
	
This concept essentially leaves you to work in the direct mod file, and run the builder, 
rather than have to track down your existing entries each time, see if you correctly updated one or the other,
and have to continue making them directly in that enormous file that bogs down the good fully-featured editors like Notepad++. Oh my.

Simply put, you get strong code folding and handling abilities, without the choking ;)