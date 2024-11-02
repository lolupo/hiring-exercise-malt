# Utiliser l'image officielle de MongoDB
FROM mongo:latest

# Copier le script d'initialisation et le fichier JSON dans l'image Docker
COPY init-mongo.js /docker-entrypoint-initdb.d/
COPY mongo-rule.json /docker-entrypoint-initdb.d/

# Exposer le port par défaut de MongoDB
EXPOSE 27017

# Définir un volume pour persister les données
VOLUME /data/db