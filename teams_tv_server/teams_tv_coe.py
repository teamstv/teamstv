from flask import Flask, render_template, jsonify
from lib.telebot import Telebot
import os

app = Flask(__name__)
tb = Telebot()

COE_PHOTOS = os.path.join('static', 'coe_photos')

@app.route('/traffic')
def show_traffic():
    return render_template("traffic.html")


@app.route('/image')
def show_image():
    full_filename = os.path.join(COE_PHOTOS, 'Untitled.png')
    return render_template("image.html", user_image=full_filename)

@app.route("/telebot", methods=['GET'])
def get_telebot():
    return jsonify(tb.get())

if __name__ == '__main__':
    tb.start()
    app.run()
