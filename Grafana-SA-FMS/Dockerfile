FROM grafana/grafana:11.4.0

# Crea la directory dei dati se non esiste
RUN mkdir -p /var/lib/grafana

# Copia la cartella "data" locale nella directory di Grafana
COPY ./data /var/lib/grafana

# Trova l'utente e il gruppo predefiniti usati nell'immagine base
USER root
RUN GRAFANA_USER_ID=$(id -u grafana 2>/dev/null || echo 472) && \
    GRAFANA_GROUP_ID=$(id -g grafana 2>/dev/null || echo 472) && \
    chown -R ${GRAFANA_USER_ID}:${GRAFANA_GROUP_ID} /var/lib/grafana

# Cambia utente per eseguire Grafana
USER grafana

# Espone la porta di default di Grafana
EXPOSE 3000

# Comando di avvio
CMD ["/run.sh"]