FROM mongo:latest

# Copier le fichier JSON et le script shell dans le conteneur
COPY mongo/mongo-rule.json /tmp/
COPY mongo/start.sh /start.sh

# Rend le script exécutable
RUN chmod +x /start.sh

# Exposer le port par défaut de MongoDB
EXPOSE 27017

# Use the entrypoint script to start MongoDB and import data
ENTRYPOINT ["/start.sh"]
