## Android app for collecting light sensor data using the MQTT protocol

This app allows users to use their phone as a light sensor that periodically collects and publishes the location/time/sensor data using the MQTT protocol. <br />

<table align=center>
  <tr>
    <td>First Screen Page</td>
     <td>Sensor Screen</td>
  </tr>
  <tr>
     <td><img src="https://github.com/user-attachments/assets/fb6caa64-2160-432d-82ff-b6b3f2b91609" alt="Alt Text" width=270 height=600></td>
   <td><img src="https://github.com/user-attachments/assets/df3cebd0-0bfb-47a8-9faa-866c2b1f7528" alt="Alt Text" width=270 height=600></td>
  </tr>
 </table>


<br />The application requires location permissions to collect the current longitude and latitude of the user. Users can also use simulation mode and select the desired sensor data range, frequency and location in the settings. 
When the sensor is activated in simulation mode it will use the selected simulation settings instead of real data. Users can also send custom event messages under specific topics.  <br />

<table align=center>
  <tr>
    <td>Settings</td>
     <td>Select Location</td>
    <td>Event messages</td>
  </tr>
  <tr>
     <td><img src="https://github.com/user-attachments/assets/bdb704f5-31c8-4fe3-9d4e-2cdd69dd431b" alt="Alt Text" width=270 height=600></td>
   <td><img src="https://github.com/user-attachments/assets/cf083850-b4b7-4650-9387-a2bd2d366992" alt="Alt Text" width=270 height=600></td>
    <td><img src="https://github.com/user-attachments/assets/f8c52cb1-5f9a-42b1-9f45-bb9eed2e4fed" alt="Alt Text" width=270 height=600></td>
  </tr>
 </table>


## MQTT Broker and Database
The EMQX Cloud Broker is used to coordinate messages between different clients. A Python Flask script is connected to the broker and subscribed to the topics sensor/light and event/#. 
Upon recieving sensor data it converts it to a JSON and stores it into a Firebase Realtime Database. 
![mqtt_close](https://github.com/user-attachments/assets/75ecd388-fb13-461f-937c-c5e1c13a0b77)
