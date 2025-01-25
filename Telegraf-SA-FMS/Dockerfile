FROM telegraf:1.33.1

# Copia il file di configurazione nella directory di configurazione di Telegraf
COPY config/telegraf.conf /etc/telegraf/telegraf.conf

# Specifica il comando da eseguire quando il container viene avviato
CMD ["telegraf"]