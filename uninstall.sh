#!/bin/bash

# Prompt function
confirm() {
    while true; do
        read -rp "$1 [y/N]: " response
        case "$response" in
            [yY][eE][sS]|[yY]) return 0 ;;
            [nN][oO]|[nN]|"") return 1 ;;
            *) echo "❌ Invalid input, please enter y or n." ;;
        esac
    done
}

# Are you sure you want to uninstall the system?
echo "🚀 This script will remove the system containers."
if ! confirm "⚠️ Are you sure you want to proceed?"; then
    echo "✅ Operation canceled."
    exit 0
fi

# Do you want to remove proprietary built images?
if confirm "🛠️ Do you also want to remove proprietary built images?"; then
    REMOVE_PROPRIETARY_IMAGES=YES
else
    REMOVE_PROPRIETARY_IMAGES=NO
fi

# Do you want to remove images downloaded from Docker Hub?
if confirm "📦 Do you also want to remove images downloaded from Docker Hub?"; then
    REMOVE_HUB_IMAGES=YES
else
    REMOVE_HUB_IMAGES=NO
fi

# Do you want to remove created volumes?
if confirm "🗄️ Do you also want to remove created volumes?"; then
    REMOVE_VOLUMES=YES
else
    REMOVE_VOLUMES=NO
fi

# Summary of operations
echo " "
echo "📋 ** SUMMARY OF OPERATIONS **"
echo "- 🗑️ Removing containers: YES"
echo "- 🏗️ Removing proprietary built images: $REMOVE_PROPRIETARY_IMAGES"
echo "- 🔽 Removing images from Docker Hub: $REMOVE_HUB_IMAGES"
echo "- 🗃️ Removing volumes: $REMOVE_VOLUMES"
if ! confirm "✅ Do you confirm to proceed?"; then
    echo "❌ Operation canceled."
    exit 0
fi

# Stopping and removing containers
echo " "
echo "🛑 Stopping and removing containers..."
docker compose down

# Removing proprietary built images if requested
if [ "$REMOVE_PROPRIETARY_IMAGES" == "YES" ]; then
    echo " "
    echo "🚮 Removing proprietary built images..."
    for image in eclipse-mosquitto_sa_fms:latest sensor-simulator_sa_fms:latest nodered_sa_fms:latest influxdb_sa_fms:latest telegraf_sa_fms:latest grafana_sa_fms:latest; do
        echo " "
        echo "🗑️ Removing $image..."
        docker rmi -f "$image"
    done
fi

# Removing images from Docker Hub if requested
if [ "$REMOVE_HUB_IMAGES" == "YES" ]; then
    echo " "
    echo "🌍 Removing images downloaded from Docker Hub..."
    for image in maildev/maildev:latest grafana/grafana-image-renderer:latest; do
        echo " "
        echo "🗑️ Removing $image..."
        docker rmi -f "$image"
    done
fi

# Removing project-specific volumes if requested
if [ "$REMOVE_VOLUMES" == "YES" ]; then
    echo " "
    echo "🗑️ Removing project-specific volumes..."
    for volume in smart-agriculture-field-monitoring-system_grafana_renderer_sa_fms_data smart-agriculture-field-monitoring-system_grafana_sa_fms_data smart-agriculture-field-monitoring-system_influxdb_sa_fms_config smart-agriculture-field-monitoring-system_influxdb_sa_fms_data smart-agriculture-field-monitoring-system_maildev_sa_fms_data smart-agriculture-field-monitoring-system_mosquitto_sa_fms_conf smart-agriculture-field-monitoring-system_mosquitto_sa_fms_data smart-agriculture-field-monitoring-system_mosquitto_sa_fms_logs smart-agriculture-field-monitoring-system_nodered_sa_fms_data smart-agriculture-field-monitoring-system_sensor_simulator_sa_fms_data smart-agriculture-field-monitoring-system_telegraf_sa_fms_data; do
        if docker volume ls -q --filter name="$volume" | grep -q "$volume"; then
            echo " "
            echo "🗃️ Removing volume $volume..."
            docker volume rm -f "$volume"
        fi
    done
fi

echo " "
echo "🎉 Uninstallation completed successfully! 🎉"
