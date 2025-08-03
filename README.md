# IP Camera

[![Preview](https://github.com/BalioFVFX/IP-Camera/blob/main/media/preview.gif?raw=true)](https://youtu.be/NtQ_Al-56Qs)

## Overview

![Overview](https://github.com/BalioFVFX/IP-Camera/blob/main/media/high_level_overview.png?raw=true)

## How to Use

You can either watch the video above or follow the steps below.

### How to Start Live Streaming

1. Start the Video Server. By default, the Video Server launches 3 sockets, each acting as a server:
   - WebSocket Server (runs on port 1234)
   - MJPEG Server (runs on port 4444)
   - Camera Server (runs on port 4321)
2. Install the app on your phone.
3. Navigate to the app's settings screen and set up your camera server's IP. For example: `192.168.178.101:4321`
4. Open the stream screen and click the "Start streaming" button.
5. Your phone is now sending video data to your Camera Server.

---

### Watching the Stream

The stream can be watched from either your browser, the Web App, or apps like VLC Media Player.

### Browser

Open your favorite web browser and navigate to your MJPEG Server's IP address. For example:  
`http://192.168.178.101:4444`

[![Browser Preview](https://github.com/BalioFVFX/IP-Camera/blob/main/media/browser.gif?raw=true)](https://youtu.be/NtQ_Al-56Qs)

### VLC Media Player

Open VLC Media Player. Go to **File → Open Network → Network** and enter your MJPEG Server's IP address. For example:  
`http://192.168.178.101:4444/`

[![VLC Preview](https://github.com/BalioFVFX/IP-Camera/blob/main/media/vlc.gif?raw=true)](https://youtu.be/NtQ_Al-56Qs)

### The Web App

1. Navigate to the Web App's root directory and execute `webpack serve` in your terminal.
2. Open your browser and go to `http://localhost:8080/`.
3. Go to the settings page and enter your WebSocket Server's IP address. For example: `192.168.178.101:1234`
4. Navigate to the streaming page at `http://localhost:8080/stream.html` and click the "Connect" button.

[![Web App Preview](https://github.com/BalioFVFX/IP-Camera/blob/main/media/webapp.gif?raw=true)](https://youtu.be/NtQ_Al-56Qs)

### Configuring the Web App's Server

> **Note:** This section is only required if you'd like to take screenshots from the Web App.

1. Open the Web App's server project.
2. Open `index.js` and edit the connection object to match your MySQL credentials.
3. Create the required tables by executing the SQL query located in `user.sql`.
4. From the root directory, run `node index.js` in your terminal.
5. You may have to update the IP that the Web App connects to. You can edit this IP in the Web App's `stream.html` file (see the `BACKEND_URL` constant).
6. Create a user through the Web App at `http://localhost:8080/register.html`.
7. Take screenshots from `http://localhost:8080/stream.html`.
8. View your screenshots at `http://localhost:8080/gallery.html`.

[![Web App Gallery Preview](https://github.com/BalioFVFX/IP-Camera/blob/main/media/webapp_gallery.gif?raw=true)](https://youtu.be/NtQ_Al-56Qs)

