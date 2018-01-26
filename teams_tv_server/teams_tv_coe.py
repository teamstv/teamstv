import json

from flask import Flask, render_template, send_file
import os

from lib import calendar, misc
import settings

app = Flask(__name__)

@app.route('/traffic')
def show_traffic():
    return render_template("traffic.html")

@app.route('/rss')
def show_traffic():
    return render_template("rss.html")

@app.route('/image')
def show_image():
    full_filename = os.path.join(settings.IMG_FOLDER, 'Untitled.png')
    return render_template("image.html", user_image=full_filename)


@app.route("/test_json")
def test_json():
    calendars = calendar.connect(settings.CALDAV_USER, settings.CALDAV_PASSWORD, settings.CALDAV_URL)
    data = calendar.get_current_events(calendars)
    return json.dumps(data, cls=misc.DateTimeEncoder)


@app.route("/events/current")
def get_current_events():
    calendars = calendar.connect(settings.CALDAV_USER, settings.CALDAV_PASSWORD, settings.CALDAV_URL)
    data = calendar.get_current_events(calendars)
    return json.dumps(data, cls=misc.DateTimeEncoder)


@app.route('/images/<folder>')
def get_image_list(folder):
    img_path = os.path.join(settings.STATIC_FOLDER, settings.IMG_FOLDER)
    folder_path = os.path.join(img_path, folder)
    filelist = [file for file in os.listdir(folder_path) if file.endswith(".png") or file.endswith(".jpg")]
    return json.dumps(filelist)


@app.route("/images/<folder>/<file>")
def get_image(folder, file):
    img_path = os.path.join(settings.STATIC_FOLDER, settings.IMG_FOLDER)
    folder_path = os.path.join(img_path, folder)
    filename = os.path.join(folder_path, file)
    return send_file(filename, mimetype='image/gif')

if __name__ == '__main__':
    app.run()
