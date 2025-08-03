const BACKEND_URL = 'http://192.168.178.101:3000';

let username = ''
let password = ''

function redirect(path) {
    const newUrl = window.location.host + "/" + path;
    window.location.replace(newUrl)
    alert("new url: " + newUrl)
}

function getServerIp() {
    const storedIp = localStorage.getItem("server_ip");

    if (storedIp == null) {
        return "192.168.178.101:1234";
    } else {
        return storedIp
    }
}

function setServerIp(ip) {
    localStorage.setItem("server_ip", ip);
}

function getAuthorization() {
    const storedAuth = localStorage.getItem('auth');

    if (storedAuth == null) {
        return ''
    } else {
        return storedAuth
    }
}

function setAuthorization(username, password) {
    const value = 'Basic ' + btoa(username + ":" + password);
    localStorage.setItem('auth', value);
}

function clearAuthorization() {
    localStorage.setItem('auth', '')
}
