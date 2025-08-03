const express = require('express')
const mysql = require('mysql')
const bcrypt = require('bcrypt')
const app = express();
const port = 3000;

const fs = require('fs');
const cors = require('cors');

const connection = mysql.createConnection({
    'host': 'localhost',
    'user': 'db_username',
    'password': 'db_password',
    'database': 'db_name'
})

app.use(express.json());
app.use(cors());
app.use(helmet());

const limiter = rateLimit({
    windowMs: 15 * 60 * 1000,
    max: 100
});

app.use(limiter);

app.post('/register', async function (req, res) {
    res.setHeader('Access-Control-Allow-Origin', '*')

    if (req.body.username === undefined || req.body.username === '' || req.body.password === undefined || req.body.password === '') {
        res.sendStatus(400);
        return
    }

    const username = req.body.username;
    const password = req.body.password;

    const hashPassword = await bcrypt.hash(password, 10);

    const sql = "INSERT INTO user (username, password) VALUES (?, ?)";

    connection.query(sql, [username, hashPassword], function (err, sqlRes) {
        if (err == null) {
            res.sendStatus(201);
        } else {
            res.sendStatus(401);
        }
    });
});

app.post('/login', async function (req, res) {

    const auth = await isAuthorized(req);

    if (!auth) {
        res.sendStatus(401);
        return;
    }

    res.sendStatus(200);
});

app.post('/screenshot', async (req, res) => {
    res.setHeader('Access-Control-Allow-Origin', '*')

    const auth = await isAuthorized(req);

    if (!auth) {
        res.sendStatus(401);
        return;
    }

    const username = getUsernameFromAuth(req);

    const sanitizedUsername = username.replace(/[^a-zA-Z0-9]/g, '');

    fs.mkdir(sanitizedUsername, { recursive: true }, function () {
        const stream = fs.createWriteStream(sanitizedUsername + '/' + Date.now().toString() + '.jpeg')
        stream.write(new Uint8Array(req.body))
    });

    res.sendStatus(201)
});

app.get('/gallery', async (req, res) => {
    const auth = await isAuthorized(req);

    if (!auth) {
        res.sendStatus(401);
        return;
    }

    const username = getUsernameFromAuth(req);

    const sanitizedUsername = username.replace(/[^a-zA-Z0-9]/g, '');

    fs.readdir(sanitizedUsername, (err, files) => {
        if (err) {
            res.sendStatus(404);
            return
        }

        const arr = [];

        files.forEach(file => {
         const data = fs.readFileSync(sanitizedUsername + '/' + file)
            arr.push([...data]);
        });

        res.send(arr);
    });
})

app.listen(port, () => {
    console.log('Server started on port: ' + port)
})

async function isAuthorized(req) {
    return new Promise((resolve, reject) => {
        const b64auth = (req.headers.authorization || '').split(' ')[1] || ''
        let [login, password] = Buffer.from(b64auth, 'base64').toString().split(':')

        if (login === '' || password === '') {
            resolve(false);
            return
        }

        login = mysql.escape(login);
        connection.query('select password from user where username = ?', [login], async function (err, res) {
            if (err == null) {
                const dbPassword = res[0].password;

                if (dbPassword === undefined || dbPassword === '') {
                    resolve(false);
                }

                const hashResult = await bcrypt.compare(password, dbPassword);

                resolve(hashResult);

            } else {
                resolve(false);
            }
        });
    });
}

function getUsernameFromAuth(req) {
    const b64auth = (req.headers.authorization || '').split(' ')[1] || ''
    return Buffer.from(b64auth, 'base64').toString().split(':')[0];
}
