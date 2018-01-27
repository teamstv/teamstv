import json

from flask import Flask, render_template, send_file, jsonify
import os

from lib import calendar, misc
import settings
from lib.telebot import Telebot

app = Flask(__name__)
tb = Telebot()

@app.route('/traffic')
def show_traffic():
    return render_template("traffic.html")

@app.route('/rss')
def show_rss():
    return render_template("rss.html")

@app.route('/image')
def show_image():
    full_filename = os.path.join(settings.IMG_FOLDER, 'Untitled.png')
    return render_template("image.html", user_image=full_filename)


@app.route('/next')
def next_event():
    tb.handle({'text':'/next'})
    return jsonify('OK')

@app.route('/next')
def resume_event():
    tb.handle({'text':'/resume'})
    return jsonify('OK')

@app.route("/telebot", methods=['GET'])
def get_telebot():
    def next():
        print('next')
        calendars = calendar.connect(settings.CALDAV_USER, settings.CALDAV_PASSWORD, settings.CALDAV_URL)
        data = calendar.get_next_events(calendars)
        return json.dumps(data, cls=misc.DateTimeEncoder)
    def resume():
        print('resume')
        calendars = calendar.connect(settings.CALDAV_USER, settings.CALDAV_PASSWORD, settings.CALDAV_URL)
        data = calendar.get_current_events(calendars)
        return json.dumps(data, cls=misc.DateTimeEncoder)
    cmds = {
        'next': next,
        'resume': resume,
    }
    return cmds[tb.get()]()

@app.route("/front_index")
def test_index():
    return render_template("index.html")

@app.route("/map.html")
def get_map():
    return render_template("map.html")

@app.route("/js/<script>")
def get_some_js(script):
    return send_file(os.path.join(settings.STATIC_FOLDER, "js", script), mimetype="application/json")


@app.route("/css/<style>")
def get_some_css(style):
    return send_file(os.path.join(settings.STATIC_FOLDER, "css", style), mimetype="text/css")

@app.route("/js/<dir>/<file>")
def get_nested_js(dir, file):
    img_path = os.path.join(settings.STATIC_FOLDER, "js")
    folder_path = os.path.join(img_path, dir)
    filename = os.path.join(folder_path, file)
    return send_file(filename, mimetype='application/json')

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
    tb.start()
    app.run(threaded=True, host='0.0.0.0')
