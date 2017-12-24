[![Snap Status](https://build.snapcraft.io/badge/ahahn94/AudioBookConverter.svg)](https://build.snapcraft.io/user/ahahn94/AudioBookConverter)
# AudioBookConverter
## About
AudioBookConverter Splits M4A-files into MP3-files by embedded chapter marks.  

## Install
### Dependencies
#### Build
- openjdk-8-jdk  

#### Run
- openjdk-8-jre  
- ffmpeg
- libmp3lame

### Installation Instructions
Just run these commands:  
`git clone https://github.com/ahahn94/AudioBookConverter.git`  
`cd AudioBookConverter`  
`./build.sh`  
`./install.sh`  

### Remove Instructions
To remove run `./remove.sh`.

### Generate Java Documentation
`./generateDoc.sh`  

## Usage
This program can be used to split M4A files by the chapter marks they contain.  
It will produce a folder named after the input file and one MP3 file per chapter.  
The file name and title tag will be the same as the chapters name.  

M4B audiobook files can only be used after renaming them to M4A.
M4B audiobooks from iTunes will not work because of the embedded DRM.  

### Syntax  
`abc foo.m4a` will split foo.m4a into multiple mp3-files and put them into a new directory "foo".  
AudioBookConverter supports wildcards (`*`), so `abc *.m4a` will do the above for every m4a-file in the working directory.  

### <a href="https://www.youtube.com/watch?v=Ugpe2cKMvh8">AudioBookConverter in Action</a>
