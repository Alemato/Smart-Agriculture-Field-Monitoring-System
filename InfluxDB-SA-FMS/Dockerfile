FROM influxdb:2.7.4

# Create the configuration folder (if it does not exist)
# and copies the configuration file inside the container.
RUN mkdir -p /etc/influxdb2/configs
COPY config/influx-configs /etc/influxdb2/configs/influx-configs

# Expose port 8086 (not mandatory if done in docker-compose)
EXPOSE 8086

# Default command (optional,
# InfluxDB 2.x already starts with ‘influxd’ by default)
CMD ["influxd"]