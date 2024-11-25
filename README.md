## Android app for collecting light sensor data using the MQTT protocol

This app allows users to use their phone as a light sensor that periodically collects and publishes the location/time/sensor data using the MQTT protocol. <br />
![Screenshot_2024-11-25-12-32-24-989_com example lightsensorapp](https://github.com/user-attachments/assets/fb6caa64-2160-432d-82ff-b6b3f2b91609)
![Screenshot_2024-11-25-12-32-09-873_com example lightsensorapp](https://github.com/user-attachments/assets/df3cebd0-0bfb-47a8-9faa-866c2b1f7528)

<br />The application requires location permissions to collect the current longitude and latitude of the user. Users can also use simulation mode and select the desired sensor data range, frequency and location in the settings. 
When the sensor is activated in simulation mode it will use the selected simulation settings instead of real data. <br />

![Screenshot_2024-11-25-12-31-36-124_com example lightsensorapp](https://github.com/user-attachments/assets/bdb704f5-31c8-4fe3-9d4e-2cdd69dd431b)
![Screenshot_2024-11-25-12-31-19-815_com example lightsensorapp](https://github.com/user-attachments/assets/cf083850-b4b7-4650-9387-a2bd2d366992)

Users can also send custom event messages under specific topics.  

## MQTT Broker and Database
The EMQX Cloud Broker is used to coordinate messages between different clients. A Python Flask script is connected to the broker and subscribed to the topics sensor/light and event/#. 
Upon recieving sensor data it converts it to a JSON and stores it into a Firebase Realtime Database. 
![mqtt_close](https://github.com/user-attachments/assets/75ecd388-fb13-461f-937c-c5e1c13a0b77)
