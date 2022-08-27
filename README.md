# Lexical-Analyser
An open-source tool which constructs a DFA from the given regular expression

## Operations supprted:
1) concatenation
2) union
3) Kleene closure

## regex syntax:
The following are reserved characters:  
'\*' - Kleene closure  
'|'  - union  
'◦'  - concatenation  
'\'  - escape character  
'.'  - arbitrary character  
'e'  - null string (epsilon)  
'p'  - empty language (phi)  
'('  - close bracket  
')'  - open bracket  
  
If these characters are to be used in the regex, they need to be escaped using the escape character.
'◦' should not be used to explicitly show concatenation; the regex ab is automatically converted to a◦b
Thus '◦' should be used only after escaping.
