# Multi-verse of Decay Manager (MoD Manager)
##### by Phacops 
###### (A.K.A Eocryphops when the Username is taken ;))

https://github.com/Eocryphops/Multi-verseOfDecayManager

A Mod Manager for State of Decay, able to manage and combine multiple mods that change the state of Mission_Mission0.xml or Facilities.xml.

##### Why do we need a mod manager?
If we don't have a dynamic manager for State of Decay mods that modify some of the same XML files, then they simply cannot be used together without some manual tweaking.

Also, because coding is fun. So, even though we'll eventually have a new game, at least we can maximize our fun with the old one until I potentially need to build a new one for the next game :D

----


##How to Install & Operate


####If you only have this because you installed a mod that came packaged with it:
	
* Do the following:
 * Unpack into your SoD game directory, probably at -
 * C:\Program Files (x86)\Steam\steamapps\common\State of Decay YOSE\Game\

* After unpacking:
 * Simply double-click to run the .jar, like you would any executable and it'll apply the mod.
 * If you're worried about the contents, then no worries - they're open source :)
 * No creepy crawlies in there, mate. See the source here - https://github.com/Eocryphops/SoDModManager


#### If you have multiple mods that came packed with this, then you can exclude ones you don't want to run:

 * You'll do that in: /sodmodmanager/XmlMods/XmlFilesToExcludeFromBuilding.txt
 * This will be in an interface for you in the future
 * If you do change which ones are included/excluded, see the instructions in the next section for changes


#### If you made changes to the XML files or included/excluded a mod:

If: 
* You're a modder/user and you made changes to a mod.xml
* You exclude/include a mod from:
 * /sodmodmanager/XmlMods/XmlFilesToExcludeFromBuilding.txt

Then:
* Any time you make a change, or have made a change:
 * Rerun the .jar by double-clicking on it, like you would any executable
 * That will apply your changes to the relevant files
 * Should take a few seconds to run as it will clean up any old objects then apply

----


###How to Get Started Using This for Mods/Modding

#### Easy method with revision history
* Click "Fork" in the top right next to Watch/Unwatch and Star.
 * This gives you all my files immediately, in a single package
 * Allows you to make your own changes to the files for your mod in a contained way
 * When I update the Mod Manager, you get my files from upstream, because you forked, which allows you to package the latest with your Mod, so users get the fullest from it along with yourself
 * If you like, you can even upload your mod to Github like I have here, so that users can fork yours as well, thus allowing them to make their own changes and also garner updates as well, with an effective history for them to rollback to, as needed

If you use this method, then I'd recommend either being familiar with Git practices and commands, or at least use SourceTree from Atlassian, which is a visual presentation of the same, that should be easier for some to use.

Github has an interface of it's own as well, but the above seems to be better for users not as familiar.

Get that here:
https://www.atlassian.com/software/sourcetree

####Templates

Basic Mod Template: 
* /sodmodmanager/XmlMods/CopyMe-ModXMLTemplate.xml
 * Just follow the basic structure there. If you need further instructions, see the following 
file with the same structure, 


Mod Template with Instructions: 
* /sodmodmanager/XmlMods/Instructions - ModXML.xml


Actualized Example Mod:
* /sodmodmanager/XmlMods/FortitudeMod.xml
 * The file for my Fortitude Mod which uses this system, which holds exact usages of the elements which work correctly in the mod when run.


Exclusion File:
* /sodmodmanager/XmlMods/XmlFilesToExcludeFromBuilding.txt
 * For users, or modders, wanting to exclude a .xml file from being parsed. 


####Further Instructions for Modders

* Copy /sodmodmanager/XmlMods/CopyMe-ModXMLTemplate.xml
 * That's the basic template to be filled in
* Paste it in /XmlMods/ folder
 * This is what will be parsed by the system to apply each mod's changes
* Fill in your own mod data, with all the objects you need in each file
 * If you need additional instructions and examples, @see:
 * /sodmodmanager/XmlMods/Instructions - ModXML.xml
 * /sodmodmanager/XmlMods/FortitudeMod.xml
* Once you're filled up, and match the excpected structure, then you just run the .jar whenever you make changes that will delete all of your old objects and add the new versions from the .xml in XmlMods.
	
This concept essentially leaves you to work in the direct mod file, and run the builder, 
rather than have to track down your existing entries each time, see if you correctly updated one or the other,
and have to continue making them directly in that enormous file that bogs down the good fully-featured editors like Notepad++. Oh my.

Simply put, you get strong code folding and handling abilities, without the choking ;)


#### What else does it do?
* Surrounds your mod changes with a wrapper so you can easily find it
 * E.g. ModWrapper mod_author="phacops" mod_name="Fortitude_Mod" mod_segment="Savini_Entities" placement="start_tag"
* Organizes your XML so that the Attributes of each element are in alphabetical order for easy finding
 * E.g. Entity EntityClass="SurveyPoint" EntityGuid="403D4E79B3B28330" EntityId="4370" Layer="RTS" Name="SurveyPoint1" Pos="1501.3844,800.42261,101.17547" mod_name="Fortitude_Mod"
 * Notice that it also applies your Mod Name as an attribute to each tag so you know exactly which ones are yours, and so it knows which ones to delete when you tell it you don't want them anymore, or if a user wants to remove your mod - easy!
* If a Mission_Mission0.xml file doesn't exist, it will create one before adding the requested mods
 * And, because it comes pre-packed, it can give you a 100% clean version anytime - no ruining your game because you killed the Mission file [I've certainly never done that while modding...ahem ;)]
* If a Facilities.xml file doesn't exist, it will create one before adding the requested mods
 * Same as above for Mission :)
* It also has a system for ignoring mod XMLs should a user want to disable them
* It will also later be able to change the load order for overwriting common elements, as needed
