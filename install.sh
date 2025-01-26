# Linux and MacOS
#!/bin/bash

# Paths to the .env files
influx_env="./InfluxDB-SA-FMS/influx.env"
sensorsimulation_env="./sensor-simulator/sensor-simulator.env"
nodered_env="./Node-RED-SA-FMS/nodered.env"
telegraf_env="./Telegraf-SA-FMS/telegraf.env"
grafana_env="./Grafana-SA-FMS/grafana.env"
maildev_env="./MailDev-SA-FMS/maildev.env"
output_env="./se4iot-SA-FMS.env"

# Check if the combined .env file already exists
if [ -f "$output_env" ]; then
    echo "The file $output_env already exists."
    read -p "Do you want to overwrite it? (y/n): " response
    if [[ "$response" != "y" && "$response" != "Y" ]]; then
        echo "Operation cancelled."
        exit 0
    fi
fi

# Verify if the individual .env files exist
if [ ! -f "$influx_env" ]; then
    echo "Error: The file $influx_env does not exist."
    exit 1
fi
if [ ! -f "$sensorsimulation_env" ]; then
    echo "Error: The file $sensorsimulation_env does not exist."
    exit 1
fi
if [ ! -f "$nodered_env" ]; then
    echo "Error: The file $nodered_env does not exist."
    exit 1
fi
if [ ! -f "$telegraf_env" ]; then
    echo "Error: The file $telegraf_env does not exist."
    exit 1
fi
if [ ! -f "grafana_env" ]; then
    echo "Error: The file $grafana_env does not exist."
    exit 1
fi
if [ ! -f "grafana_env" ]; then
    echo "Error: The file $maildev_env does not exist."
    exit 1
fi

# Combine the .env files into one
cat "$influx_env" "$sensorsimulation_env" "$nodered_env" "$telegraf_env" "$grafana_env" "$maildev_env" > "$output_env"

echo "Combined .env files into $output_env"

# Execute docker compose build and up
docker compose build --no-cache
docker compose up -d --scale telegraf-SA-FMS=2
