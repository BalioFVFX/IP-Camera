# IP Camera
![Preview](https://github.com/BalioFVFX/IP-Camera/blob/main/media/preview.gif?raw=true)

[Fullscreen](https://youtu.be/NtQ_Al-56Qs)
## How to use
You can either watch this video or follow the steps below.
### How to start live streaming
1. Start the Kotlin server. By default the Kotlin server launches 3 sockets, each acting as a server:
- WebSocket Server (runs on port 1234).
- MJPEG Server (runs on port 4444).
- Camera Server (runs on port 4321).

2. Install the app on your phone.
3. Navigate to app's settings screen and setup your Camera's server IP. For example `192.168.0.101:4321`.
4. Open the stream screen and click the Start streaming button.
5. Now your phone sends video data to your Camera Server.
---
### Watching the stream
The stream can be watched from either your browser, the Web App or apps like VLC media player.

### Browser
Open your favorite web browser and navigate to your MJPEG server's IP address. For example `http://192.168.0.101:4444`

![Preview](https://github.com/BalioFVFX/IP-Camera/blob/main/media/browser.gif?raw=true)

### VLC meida player
Open the VLC media player, File -> Open Network -> Network and write your MJPEG's server IP address. For example `http://192.168.0.101:4444/`

![Preview](https://github.com/BalioFVFX/IP-Camera/blob/main/media/vlc.gif?raw=true)
### The Web App
1. Navigate to the Web app root directory and in your terminal execute `webpack serve`.
2. Open your browser and navigate to `http://localhost:8080/`.
3. Go to settings and enter your WebSocket server ip address. For example `192.168.0.101:1234`.
4. Go to the streaming page `http://localhost:8080/stream.html` and click the connect button.

![Preview](https://github.com/BalioFVFX/IP-Camera/blob/main/media/webapp.gif?raw=true)

### Configuring the Web App's server
Note: This section is required only if you'd like to be able to take screenshots from the Web App.

1. Open the Web App Server project
2. Open index.js and edit the connection object to match your MySQL credentials. 
3. Create the required tables by executing the SQL query located in `user.sql`
4. At the root directory execute `node index.js` in your terminal
5. You may have to update the IP that the Web App connects to. You can edit this IP in Web app's `stream.html` file (`BACKEND_URL` const variable)
6. Create a user through the Web App from `http://localhost:8080/register.html`
7. Take screenshots from `http://localhost:8080/stream.html`
8. View your screenshots at `http://localhost:8080/gallery.html`

![Preview](https://github.com/BalioFVFX/IP-Camera/blob/main/media/webapp_gallery.gif?raw=true)
---
