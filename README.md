CSC3002F Assignment 1

Riyaadh Abrahams (ABRRIY002)
Mikhail Amod (AMDMIK002)
Pavan Singh (SNGPAV002)

For design specs - https://docs.google.com/document/d/1bjk9YrUXEAKs6L-360cTsuxrc02jnjmnAjJ-u2Fgb1A/edit?usp=sharing

10/03/2018 - how it currently works
Broadcasting:
A 'Message' has the following path - ChatAppClient -> ServerThread -> ChatAppServer -> ClientThread -> ChatAppClient(not the same as the first one)
These classes are linked by the users Socket