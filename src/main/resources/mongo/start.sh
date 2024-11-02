#!/bin/bash
# Lancement de MongoDB en arrière-plan
mongod --fork --logpath /var/log/mongodb.log --bind_ip localhost

# Attente pour que MongoDB soit prêt
sleep 5

# Utilisation de mongoimport pour insérer le fichier JSON dans la base de données et la collection
/usr/bin/mongoimport --db my_database --collection my_collection --file /tmp/mongo-rule.json

# Maintien du conteneur actif
tail -f /dev/null
