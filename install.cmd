@echo off
:: Paths to the .env files
set "influx_env=.\InfluxDB-SA-FMS\influx.env"
set "sensorsimulation_env=.\sensor-simulator\sensor-simulator.env"
set "nodered_env=.\Node-RED-SA-FMS\nodered.env"
set "telegraf_env=.\Telegraf-SA-FMS\telegraf.env"
set "grafana_env=.\Grafana-SA-FMS\grafana.env"
set "maildev_env=.\MailDev-SA-FMS\maildev.env"
set "output_env=.\se4iot-SA-FMS.env"

:: Check if the combined .env file already exists
if exist "%output_env%" (
    echo The file %output_env% already exists.
    choice /m "Do you want to overwrite it?"
    if errorlevel 2 (
        echo Operation cancelled.
        exit /b 0
    )
)

:: Verify if the individual .env files exist
if not exist "%influx_env%" (
    echo Error: The file %influx_env% does not exist.
    exit /b 1
)
if not exist "%sensorsimulation_env%" (
    echo Error: The file %sensorsimulation_env% does not exist.
    exit /b 1
)
if not exist "%nodered_env%" (
    echo Error: The file %nodered_env% does not exist.
    exit /b 1
)
if not exist "%telegraf_env%" (
    echo Error: The file %telegraf_env% does not exist.
    exit /b 1
)
if not exist "%grafana_env%" (
    echo Error: The file %grafana_env% does not exist.
    exit /b 1
)
if not exist "%maildev_env%" (
    echo Error: The file %maildev_env% does not exist.
    exit /b 1
)

:: Combine the .env files into one
type "%influx_env%" > "%output_env%"
type "%sensorsimulation_env%" >> "%output_env%"
type "%nodered_env%" >> "%output_env%"
type "%telegraf_env%" >> "%output_env%"
type "%grafana_env%" >> "%output_env%"
type "%maildev_env%" >> "%output_env%"

echo Combined .env files into %output_env%.

:: Execute docker compose build and up
docker compose build --no-cache
docker compose up -d --scale telegraf-SA-FMS=2
