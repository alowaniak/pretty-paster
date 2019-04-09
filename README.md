# Pretty Paster
A tool to pretty print/decode clipboard content.  
Meaning any ([supported](#Features)) content put on the system clipboard (`ctrl+c` on windows) will be pretty printed/decoded unto it. 
Thus it will be formatted/decoded when you paste it (`ctrl+v` on windows).

## Usage
While you've got PrettyPaster running just copy&paste away! ☺️  
![](./data/usage.gif)

### Installation
Simply download the "[.zip](https://github.com/alowaniak/pretty-paster/releases/download/v0.1/PrettyPaster-0.1.zip)" or "[.tar](https://github.com/alowaniak/pretty-paster/releases/download/v0.1/PrettyPaster-0.1.tar)" file from the [release page](https://github.com/alowaniak/pretty-paster/releases/tag/v0.1) and extract to a location of your choosing.  
Start PrettyPaster by running one of the startscripts in the "bin" folder.  
![](./data/installation.gif)  

## Features

### Base 64
Checks if content is _relatively likely_ to be base64 encoded, and if so decodes it.  
Considers content to be _likely_ base64 if it
 - Has a length of at least 50 characters 
 - Only contains base64 characters (obviously)
 - Is valid/mappable UTF-8 when decoded

### Xml
Checks if content is syntactically correct xml and if so "pretty prints" it.  
Placing linebreaks before nested tags and indenting levels with 4 spaces.
