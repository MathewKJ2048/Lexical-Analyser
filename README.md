# Lexical-Analyser
An open-source tool which constructs various FSMs from the given regular expression and tests if input strings match.

## Use:
Download and run the .jar file.  
Java version 17 or above is required.

## Operations supprted:
1) concatenation
2) union
3) Kleene closure

## Output:
1) syntax tree of regeular expression
2) epsilon-NFA
3) nullable, firstpos, lastpos and followpos
4) NFA
5) DFA

## regex syntax:

The following are reserved characters:  
'\*' - Kleene closure  
'|'  - union  
'◦'  - concatenation (no explicit use)  
'\'  - escape character  
'.'  - any character not found in the alphabet
'e'  - null string (epsilon)  
'p'  - empty language (phi)  
'('  - close bracket  
')'  - open bracket  
'#'  - end of input (no explicit use)
  
If these characters are to be used in the regex, they need to be escaped using the escape character.

'◦' should not be used to explicitly show concatenation; the regex ab is automatically converted to a◦b by the tool.
Thus '◦' should be used only after escaping.
Similarly, '#' should be used only after escaping.
