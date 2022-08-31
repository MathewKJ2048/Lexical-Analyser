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
'.'  - any character not found in the alphabet
'e'  - null string (epsilon)  
'p'  - empty language (phi)  
'('  - close bracket  
')'  - open bracket
'#'  - end of input
  
If these characters are to be used in the regex, they need to be escaped using the escape character.
'◦' should not be used to explicitly show concatenation; the regex ab is automatically converted to a◦b by the tool.
Thus '◦' should be used only after escaping.
Similarly, '#' should be used only after escaping.
