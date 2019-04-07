# Pretty Paster
A tool to pretty print/decode clipboard content.  
Meaning any ([supported](#Features)) content put on the system clipboard (`ctrl+c` on windows) will be pretty printed/decoded unto it. 
Thus it will be formatted/decoded when you paste it (`ctrl+v` on windows).

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
