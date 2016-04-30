#!/bin/bash
line=0
while IFS=';' read type nom keyWord1 keyWord2 keyWord3
do
line=$((line+1))
param="{type : $type , nom: $nom, keyWord1  : $keyWord1 , keyWord2 : $keyWord2, keyWord3: $keyWord3}"
#echo $param
curl -XPUT 'localhost:9200/vetements/external/'$line'?pretty' -d '{"type" : "'"$type"'" , "nom": "'"$nom"'", "keyWord1" : "'"$keyWord1"'" , "keyWord2" : "'"$keyWord2"'", "keyWord3": "'"$keyWord3"'"}'
#echo "$line type $type nom $nom keyWord1 $keyWord1 keyWord2 $keyWord2 keyWord3 $keyWord3"
done < /c/datas/programmes/workspaces/ep/perso/les-castors-allumes-font-du-code/test.csv


while IFS=';' read index nom keyWord1 keyWord2 keyWord3 masterPiece
do curl -XPUT 'localhost:9200/themes/external/'$index'?pretty' -d '{"id"  : "'"$index"'" , "Nom" : "'"$nom"'", "keyWord1": "'"$keyWord1"'", "keyWord2"  : "'"$keyWord2"'" , "keyWord3" : "'"$keyWord3"'", "masterPiece" : "'"$masterPiece"'" }'
done < /c/datas/programmes/workspaces/ep/perso/les-castors-allumes-font-du-code/themes.csv