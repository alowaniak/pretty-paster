# ![Pretty Paster logo](./data/pretty-paster-logo.png) [![Build Status](https://travis-ci.com/alowaniak/pretty-paster.svg?branch=master)](https://travis-ci.com/alowaniak/pretty-paster)
A tool to pretty print/decode clipboard content.

## ToC
- [Usage](#usage)
- [Features](#features)
  - [Base 64](#base-64)
  - [Xml](#xml)
- [Installation](#installation)

## Usage
While you've got PrettyPaster running just copy&paste away! ☺️  
![Demo showing the automatic formatting/decoding when copy-pasting.](./data/usage.gif)

## Features
### Base 64
Decodes content that's _relatively likely_ to be base64 encoded.  
Considers content to be _likely_ base64 if it
 - Has a length of at least 50 characters 
 - Only contains base64 characters (obviously)
 - Is valid/mappable UTF-8 when decoded

### Xml
Pretty-prints xml (~~if, God forbid, you have to use it~~) by placing linebreaks after tags and indenting with 2 spaces per level.

### Json
Pretty-prints json by placing linebreaks after elements and indenting with 2 spaces per level.

## Installation
Simply download the "[.zip](https://github.com/alowaniak/pretty-paster/releases/download/v0.2/PrettyPaster-0.2.zip)" or "[.tar](https://github.com/alowaniak/pretty-paster/releases/download/v0.2/PrettyPaster-0.2.tar)" file from the [release page](https://github.com/alowaniak/pretty-paster//releases/latest) and extract to a location of your choosing.  
Start PrettyPaster by running one of the startscripts in the "bin" folder.  
![Gif showing the aforementioned installation process.](./data/installation.gif)  

### Requirements
At least Java 8 is required for running.  
For building (with running tests) at least java 11 is required.
