
line=0

while read type sexe nom attr1 attr2 attr3
do line=$line+1
do curl -XPUT 'localhost:9200/vetements/external/$line?pretty' -d '{"type"  : "$type" ,"Sexe" : "$sexe", "nom": "$nom", "Couleur"  : "*attr1" , "Theme" :"$attr2", "attribut": "$attr3"}'
done < produits.csv


while read index nom attr1 attr2 attr3 masterPieceF masterPieceM
id=$index+1
do curl -XPUT 'localhost:9200/themes/external/$id?pretty' -d '{"index"  : "$index" , "Nom" : "$nom", "Attr1": "$attr1", "Attr2"  : "$attr2" , "Attr3" : "$attr3", "NomImageMasterPieceFemale": "$masterPieceF" ,  "NomImageMasterPieceMale": "$masterPieceM" }'
done < sujets.csv