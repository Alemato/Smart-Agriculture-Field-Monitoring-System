# Use a base image specific to Node-RED with Node.js 18 (for node-red-contrib-telegrambot supporting up to Node 18)
# Specifying a tag reduces the risk of problems with unexpected updates.
FROM nodered/node-red:latest-18

# Switch to root user to perform operations requiring administrator privileges.
USER root

# Installs the additional nodes needed for the Node-RED application.
# - “--no-optional” prevents installation of optional dependencies.
# - “--production” ensures that only packages needed for execution are installed.
# - “--clearing the npm cache reduces the final image size.
RUN npm install --no-optional --production \
    node-red-contrib-telegrambot \
    node-red-contrib-influxdb && \
    npm cache clean --force

# Copy all configuration files from the local “data” directory to the Node-RED data directory.
# - Including everything in a single COPY command optimizes the number of layers.
# - The “settings.js” file contains the encryption key for the “flows_cred.json” file (row 44).
# - “flows_cred.json” includes sensitive credentials such as those of the TelegramBot, which accesses the BOT_TOKEN environment variable.
# - The BOT_TOKEN variable must be declared in the docker-compose or docker run command.
# - The BOT_TOKEN secret is defined in the file as: {env.get(“BOT_TOKEN”)}
# - “flows.json” contains all the configured flows and use their secrets defined in “flows_cred.json”
COPY data/ /data/

# Set the correct permissions for the /data directory.
# - Change the owner of the copied files to “node-red”, the user used for execution
RUN chown -R node-red:node-red /data

# Back to the “node-red” user to ensure that the application runs with restricted privileges.
# - It is a good security practice to run containers as an unprivileged user.
USER node-red

# IMPORTANT: Be sure to set the environment variable BOT_TOKEN.
# - The BOT_TOKEN variable must be declared in the docker-compose or docker run command.
# - Example usage: docker run -e BOT_TOKEN=<value_of_your_token> image_name

# End of dockerfile
# The container will automatically use the CMD command defined in the base image.