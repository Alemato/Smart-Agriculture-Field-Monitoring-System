FROM eclipse-mosquitto:2.0.20-openssl

# Copy the configuration file inside the container
COPY config/ /mosquitto/config/

# Expose port 1883 (not mandatory if done in docker-compose)
EXPOSE 1883

# Default command
CMD ["mosquitto", "-c", "/mosquitto/config/mosquitto.conf"]