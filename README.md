# smartfilecopy
JavaFx Gui application for file and ebook title to copy, delete and compress. And for managing Calibre ebooks and directories. Find double documents for a title, find real double files for a title in the same format.

Usage
=====

Case Ebook
--------

0. Select premade named config set --> input and output dir values.
1. Or select input directory
2. And output (=target) directory
3. Check "titles"
4. Some of next radio buttons:
5. -- 

Case Copy
---------

Config
======

@HOME/.smartfilecopy/config.json = contains json data, which sets different named directory pairs (input and output directories)

./smartfilecopy.properties = constains 2 variables, which defines normal ebook file extensions and which file extensions are documents/ebooks or are compressed files etc, which directories are not "empty" directories, if that directory do not contains here listed 
file extensions!

@HOME/.smartfilecopy/smartfilecopy.properties = the same as default smartfilecopy.properties file, but this is a user specific file.

Windows has its own HOME environment variable, which is used, when a user is running the application on windows.
