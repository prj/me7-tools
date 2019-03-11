Part 1: Basic definitions

Definitions are stored in a XML format in the me7xmls directory.
There is also a file called "mapdef.xsd" which defines the schema according to which the XML files are created.
When creating definitions or editing existing ones, I strongly recommend you use an editor which supports schema and auto completing, as it will make your life a lot easier.

Let's take a look at locating the 16 bit KRKTE.
The first thing, is to find the KRKTE access in IDA Pro. This is simple - pick a binary you have a DAMOS for, and then go from there.
I will use the 551K RS4 binary for this example.

In this binary KRKTE is at 0x1C9DC. There are three places which access this value in the binary.
So we must choose one of them and concentrate on it. It is best to look for a spot that has unique code around it.
Like this one:


Time to create the initial XML:

<?xml version="1.0" encoding="UTF-8"?>
<map xmlns="http://prj-tuning.com/mapdef" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
   xsi:schemaLocation="http://prj-tuning.com/mapdef mapdef.xsd">
</map>

The first thing to add is the "ID". The "ID" is a unique identifier, which identifies the map.
There can be multiple XML files/definitions for the same ID. It is a good idea to name the file the same as the ID, so in this case KRKTE.xml.

Let's add the ID:
<?xml version="1.0" encoding="UTF-8"?>
<map xmlns="http://prj-tuning.com/mapdef" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
   xsi:schemaLocation="http://prj-tuning.com/mapdef mapdef.xsd">
   <id>KRKTE</id>
</map>

Now comes the most powerful feature of the map locator tool - the pattern.
The pattern is a way to locate a matching area in a binary. So we will have to create a pattern.

The pattern consists of the following building blocks:
HEX - bytes that should be matched exactly
XX - a byte that should be skipped.
XX<number> - means that zero to <number> bytes should be skipped.
Every time the pattern is matched, the offset that is returned is the address where the first character in the pattern matched.
If you would like to move the offset that is returned to an arbitrary place in the pattern, you can prefix any of the members of the pattern with MM.
The prefixed member will become the new reported offset.

For example F2 XX MMF2 XX.

Let's build a pattern for our location. It is a simple pattern, and comes out as follows:
F2 F4 XX XX 7C 44 E0 05 70 55.

We mask out the actual address from the pattern, because it will be different between binaries.
Let's add this to the xml:

<?xml version="1.0" encoding="UTF-8"?>
<map xmlns="http://prj-tuning.com/mapdef" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
   xsi:schemaLocation="http://prj-tuning.com/mapdef mapdef.xsd">
   <id>KRKTE</id>
   <pattern>F2 F4 XX XX 7C 44 E0 05 70 55</pattern>
</map>

Now the main building blocks have been added. It will find the pattern at an offset, however this is not terribly useful yet.
The way the C167 addresses memory is via a DPP and offset. So we have to specify where from the pattern location the DPP is located and where the offset is located.
In this case our DPP - 0x207h is located two bytes before the pattern start. If we omit this, the default DPP of 0x204h would be used.
Our offset - 0x09DC is located two bytes after the pattern start.

The total address is calculated as: dpp * 0x4000 - 0x800000 + offset. The subtraction is because of where the EEPROM is loaded.
Let's add this information to the XML:
<?xml version="1.0" encoding="UTF-8"?>
<map xmlns="http://prj-tuning.com/mapdef" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
   xsi:schemaLocation="http://prj-tuning.com/mapdef mapdef.xsd">
   <id>KRKTE</id>
   <pattern>F2 F4 XX XX 7C 44 E0 05 70 55</pattern>
   <address>
      <offset>2</offset>
      <dppOffset>-2</dppOffset>
   </address>
</map>

Alternatively a marker could be specified at F2 F4 MMXX XX... and the offset omitted, and DPP offset set to -4.

Now we just need to add the data to convert the located value to a legible form. This is done by the conversion element.

<?xml version="1.0" encoding="UTF-8"?>
<map xmlns="http://prj-tuning.com/mapdef" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
   xsi:schemaLocation="http://prj-tuning.com/mapdef mapdef.xsd">
   <id>KRKTE</id>
   <pattern>F2 F4 XX XX 7C 44 E0 05 70 55</pattern>
   <address>
      <offset>2</offset>
      <dppOffset>-2</dppOffset>
   </address>
   <conversion>
      <factor>0.000167</factor>
      <width>2</width>
      <endianness>LoHi</endianness>
   </conversion>
</map>

The factor is specified - the default factor is 1.0.
The offset is not needed - the default offset is 0.0, which suits us in this case.
The default width is 1 byte and the default endianness is HiLo. In this case the value is 2 bytes wide and bigendian, so we specify the endianness as LoHi.

And that's it - this XML is enough to detect KRKTE in almost any ME7 file, where it is a 16 bit value.
For 8 bit KRKTE's a slight modification is required, an example can be found in KRKTE_8.xml.

Part 2: Advanced definitions

Part 1 dealt with the very basics and simple definitions.
Let's do some more advanced things.

The map I am going to pick for this example is KFLBTS.
There are a couple challenges to overcome as well.
1. Both axes of the map, and their lengths have to be located.
2. KFLBTS is different between binaries. In some binaries the load axis is 16 bit, in others it is 8 bit.

Let's first find the map access in the binary.
I will find this in two binaries. One will be ME7.1.1 on the RS6, and the other a 512k narrowband ME7.5 Audi TT.

RS6:


TT:


There are a couple of issues with this.
1. The code is a bit different.
2. There is only one axis directly accessed, the other one is pre-loaded.

So let's try to write a pattern:
88 XX E6 FC MMXX XX E6 FD XX XX E6 FE XX XX E6 FF XX XX DA XX XX XX 08 04 XX2 5C 54 D7

Note the MM - this is the marker. A lot of things are masked out.
What uniquely identifies this area are the following instructions:
add r0, #4
shl r4, #5

This is all good and fine on the TT ECU, but the RS6 ECU has some data in the middle.
Luckily there is the "XX<number>" keyword, which allows to ignore 0 to <number> bytes. So this is used to skip the bytes on a RS6 ecu, and force a match.

Let's define the main map location:
<map xmlns="http://prj-tuning.com/mapdef" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
   xsi:schemaLocation="http://prj-tuning.com/mapdef mapdef.xsd ">
   <id>KFLBTS</id>
   <pattern>88 XX E6 FC MMXX XX E6 FD XX XX E6 FE XX XX E6 FF XX XX DA XX XX XX 08 04 XX2 5C 54 D7</pattern>
   <address>
      <dppOffset>4</dppOffset>
   </address>
   <conversion>
      <factor>0.007813</factor>
   </conversion>
</map>

The address offset is not needed, because our MM marker is right at where the address is.
The only things that have to be specified are the DPP offset, which is 4, and the conversion factor.
Everything else matches defaults (1 byte wide, no offset, and so on).

Of course in this case this is a 3D map, so two more maps must be defined - the horizontal and the vertical axis.
The main map XML merely contains references to them:
<map xmlns="http://prj-tuning.com/mapdef" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
   xsi:schemaLocation="http://prj-tuning.com/mapdef mapdef.xsd ">
   <id>KFLBTS</id>
   <pattern>88 XX E6 FC MMXX XX E6 FD XX XX E6 FE XX XX E6 FF XX XX DA XX XX XX 08 04 XX2 5C 54 D7</pattern>
   <address>
      <dppOffset>4</dppOffset>
   </address>
   <conversion>
      <factor>0.007813</factor>
   </conversion>
   <rowAxis>
      <id>SRL12GKUW</id>
      <id>SRL12GKUB</id>
   </rowAxis>
   <colAxis>
      <id>SNM16GKUB</id>
   </colAxis>
</map>

Note two references for the row axis. This is because on some ECU's the row axis consists of words, on others it consists of bytes (2 and 1 wide).
Should both be found, then the first one is preferred. So this also acts as an order.

The next thing is to define the axis maps.
The RPM axis is easy. We can use the same pattern, and just specify a different offset.
Every pattern that is looked up on a binary is cached. So from a performance standpoint, it is better to re-use exactly the same pattern without any changes. Moving the marker would mean that the binary has to be searched through again for example.

We get the following definition:
<?xml version="1.0" encoding="UTF-8"?>
<map xmlns="http://prj-tuning.com/mapdef" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
   xsi:schemaLocation="http://prj-tuning.com/mapdef mapdef.xsd ">
   <id>SNM16GKUB</id>
   <pattern>88 XX E6 FC MMXX XX E6 FD XX XX E6 FE XX XX E6 FF XX XX DA XX XX XX 08 04 XX2 5C 54 D7</pattern>
   <address>
      <offset>8</offset>
      <dppOffset>12</dppOffset>
   </address>
   <conversion>
      <factor>40</factor>
   </conversion>
</map>

With the second axis there is a bit more trouble. We must find it separately using a different pattern.
And the pattern will be different for both the 16 bit and the 8 bit axis.

16 bit:
<?xml version="1.0" encoding="UTF-8"?>
<map xmlns="http://prj-tuning.com/mapdef" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
   xsi:schemaLocation="http://prj-tuning.com/mapdef mapdef.xsd ">
   <id>SRL12GKUW</id>
   <pattern>F6 8E XX XX E6 FC XX XX E6 FD XX XX C2 FE XX50 E6 FC MMXX</pattern>
   <address>
      <dppOffset>4</dppOffset>
   </address>
   <conversion>
      <width>2</width>
      <endianness>LoHi</endianness>
      <factor>0.023438</factor>
   </conversion>
</map>

8 bit:
<?xml version="1.0" encoding="UTF-8"?>
<map xmlns="http://prj-tuning.com/mapdef" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
   xsi:schemaLocation="http://prj-tuning.com/mapdef mapdef.xsd ">
   <id>SRL12GKUB</id>
   <pattern>E6 FC XX XX E6 FD XX XX C2 FE XX XX D7 40 XX XX F2 FF XX XX DA XX XX XX D7 40 XX XX F6 F4 XX XX DB 00</pattern>
   <address>
      <offset>2</offset>
      <dppOffset>6</dppOffset>
   </address>
   <conversion>
      <factor>0.75</factor>
   </conversion>
</map>

If a map is assigned as an axis to another map, and it does not contain a length element, the length is automatically assumed to be the first value at the address, and this is processed appropriately.
However, this is not useful for all maps. For example for LAMFA the lengths are stored in a different place, which is pointed by code.

In this case the length element helps.
<?xml version="1.0" encoding="UTF-8"?>
<map xmlns="http://prj-tuning.com/mapdef" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
   xsi:schemaLocation="http://prj-tuning.com/mapdef mapdef.xsd ">
   <id>LAMFA_COLAXIS</id>
   <pattern>F2 F4 XX XX 88 40 C2 F5 XX XX 88 50 E6 F4 MMXX XX E6 F5</pattern>
   <address>
      <offset>12</offset>
      <dppOffset>16</dppOffset>
   </address>
   <conversion>
      <factor>0.003052</factor>
      <width>2</width>
      <endianness>LoHi</endianness>
   </conversion>
   <length>
      <address>
         <offset>44</offset>
         <dppOffset>40</dppOffset>
      </address>
   </length>
</map>

In this case there is a length element, which contains an address element.
The address element works exactly the same as the main address of the map.

This concludes the second part of the tutorial.


Part 3: XML reference

To create a new XML definition, simply add an XML to the me7xmls folder.
You can keep the program running and re-parse a binary as often as you like. Changes to your XML's will be picked up on the fly.

The mapdef.xsd file outlays the format. Here is a list of all the elements and details on what they do.


Map:
<map> - the root map element.
   <id> - the map ID
   <pattern> - the lookup pattern to match the map. You can specify multiple pattern elements, they get checked from top to bottom.
   <afterRowAxis> - can be used instead of pattern, if the given map is located right after the row axis.
   <afterColAxis> - can be used instead of pattern, if the given map is located right after the column axis.
   <address> - address element, describes the map location
   <conversion> - conversion element, describes the map data.
   <length> - length element. Describes the length of the map if it needs to be loaded.
   <rowAxis> - Lists references to the row axis
   <colAxis> - Lists references to the column axis

Address:
<address> - the address element describes the address offset from the pattern location.
   <offset> - the offset from the matched pattern location/marker. Defaults to 0.
   <dpp> - hard coded dpp. Defaults to 0x204h, if dpp nor dppOfset is specified.
   <dppOffset> - the offset from the matched pattern for the dpp value.

Conversion:
<conversion> - the conversion element describes the map data.
   <factor> - the factor for map data, defaults to 1.0.
   <offset> - the offset for map data, defaults to 0.0.
   <width> - the map width. 1 or 2, defaults to 1.
   <endianness> - the map endianness. LoHi or HiLo, defaults to HiLo.
   <signed> - whether the value is a signed data type, defaults to false.
   <alt> - another Conversion element. This is used if an axis is specified as 16 bit, but is actually 8 bit. Look at SNM16ZUUB for an example.

Length:
<length> - the length element is used to specify the length of a map. For maps with axes this is calculated from axis lengths.
   <hardcoded> - hardcoded length inside the XML, defaults to 1.
   <address> - address element to locate the length in the binary.
   <width> - width element, only valid with address element, defines whether length is 1 or 2 bytes long.

RowAxis/ColAxis:
<rowAxis> - the rowAxis and colAxis elements specify a hierarchical list of axis candidate ID's. 
   <id> - one or more ID's of the axis maps.



Part 4: Plugin reference

If you would to define a plugin, then you will have to implement the LocatorPlugin interface, make a jar, and put the jar into the "lib" folder.
The folder is scanned for plugins on startup.

The LocatorPlugin interface defines a single method:
public interface LocatorPlugin {
  Collection<? extends LocatedMap> locateMaps(byte[] binary);
}

LocatedMap is a bean, which your plugin should fill out as completely as possible.
Each plugin is run in a separate background thread.