@echo off

:: Are you sure you want to uninstall the system?
echo This script will remove the system containers.
choice /m "Are you sure you want to proceed?"
if errorlevel 2 (
    echo Operation canceled.
    exit /b 0
)

:: Do you want to remove proprietary built images?
choice /m "Do you also want to remove proprietary built images?"
if errorlevel 2 (
    set REMOVE_PROPRIETARY_IMAGES=NO
) else (
    set REMOVE_PROPRIETARY_IMAGES=YES
)

:: Do you want to remove images downloaded from Docker Hub?
choice /m "Do you also want to remove images downloaded from Docker Hub?"
if errorlevel 2 (
    set REMOVE_HUB_IMAGES=NO
) else (
    set REMOVE_HUB_IMAGES=YES
)

:: Do you want to remove created volumes?
choice /m "Do you also want to remove created volumes?"
if errorlevel 2 (
    set REMOVE_VOLUMES=NO
) else (
    set REMOVE_VOLUMES=YES
)

:: Summary of operations
echo.
echo ** SUMMARY OF OPERATIONS **
echo - Removing containers: YES
echo - Removing proprietary built images: %REMOVE_PROPRIETARY_IMAGES%
echo - Removing images from Docker Hub: %REMOVE_HUB_IMAGES%
echo - Removing volumes: %REMOVE_VOLUMES%
choice /m "Do you confirm to proceed?"
if errorlevel 2 (
    echo Operation canceled.
    exit /b 0
)

:: Stopping and removing containers
docker compose down

:: Removing proprietary built images if requested
if "%REMOVE_PROPRIETARY_IMAGES%"=="YES" (
    echo Removing proprietary built images...
    for %%i in (eclipse-mosquitto_sa_fms:latest sensor-simulator_sa_fms:latest nodered_sa_fms:latest influxdb_sa_fms:latest telegraf_sa_fms:latest grafana_sa_fms:latest) do (
        echo Removing %%i...
        docker rmi -f %%i
    )
)

:: Removing images from Docker Hub if requested
if "%REMOVE_HUB_IMAGES%"=="YES" (
    echo Removing images downloaded from Docker Hub...
    for %%i in (maildev/maildev:latest grafana/grafana-image-renderer:latest) do (
        echo Removing %%i...
        docker rmi -f %%i
    )
)

:: Removing project-specific volumes if requested
if "%REMOVE_VOLUMES%"=="YES" (
    echo Removing project-specific volumes...
    for %%v in (smart-agriculture-field-monitoring-system_grafana_renderer_sa_fms_data smart-agriculture-field-monitoring-system_grafana_sa_fms_data smart-agriculture-field-monitoring-system_influxdb_sa_fms_config smart-agriculture-field-monitoring-system_influxdb_sa_fms_data smart-agriculture-field-monitoring-system_maildev_sa_fms_data smart-agriculture-field-monitoring-system_mosquitto_sa_fms_conf smart-agriculture-field-monitoring-system_mosquitto_sa_fms_data smart-agriculture-field-monitoring-system_mosquitto_sa_fms_logs smart-agriculture-field-monitoring-system_nodered_sa_fms_data smart-agriculture-field-monitoring-system_sensor_simulator_sa_fms_data smart-agriculture-field-monitoring-system_telegraf_sa_fms_data) do (
        docker volume ls -q --filter name=%%v >nul && echo Removing volume %%v... && docker volume rm -f %%v
    )
)

echo.
echo Uninstallation completed successfully!
exit /b 0