from flask import Flask
import flask_cors

app = Flask(__name__)
flask_cors.CORS(app)


@app.route('/')
def hello_world():
    return 'Hello, World!'


def runServer():
    app.run()


@app.route('/hello', methods=['POST'])
def hello():
    return {
        "username": "Miguel",
        "theme": "Bora",
        "image": "querias",
    }


if __name__ == "__main__":
    runServer()
