from __future__ import print_function
import sys


def eprint(*args, **kwargs):
    print(*args, file=sys.stderr, **kwargs)

import json
from flask import Flask, render_template, send_file, jsonify
import os
from lib import calendar, misc
import settings
from lib.telebot import Telebot


app = Flask(__name__)
tb = Telebot(settings.BOT_TOKEN)

@app.route('/fin_curr')
def show_fin_curr():
    return render_template("fin_curr.html")

@app.route('/news')
def show_news():
    return render_template("news.html")

@app.route('/clock')
def show_clock():
    return render_template("clock.html")

@app.route('/next')
def next_event():
    tb.handle({'text':'/next'})
    return jsonify('OK')


@app.route('/resume')
def resume_event():
    tb.handle({'text':'/resume'})
    return jsonify('OK')


@app.route("/telebot", methods=['GET'])
def get_telebot():
    def next():
        calendars = calendar.connect(settings.CALDAV_USER, settings.CALDAV_PASSWORD, settings.CALDAV_URL)
        data = calendar.get_next_events(calendars)
        return json.dumps(data, cls=misc.DateTimeEncoder)
    def resume():
        calendars = calendar.connect(settings.CALDAV_USER, settings.CALDAV_PASSWORD, settings.CALDAV_URL)
        data = calendar.get_current_events(calendars)
        return json.dumps(data, cls=misc.DateTimeEncoder)
    cmds = {
        'next': next,
        'resume': resume,
    }
    return cmds[tb.get()]()


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


# --- WEB CONTENT ---
@app.route("/")
@app.route("/index")
def test_index():
    return render_template("index.html")

#TODO: move abs_path somewhere else out of each method
@app.route("/js/<script>")
def get_some_js(script):
    abs_path = os.path.join(os.path.dirname(os.path.abspath(__file__)), settings.STATIC_FOLDER)
    return send_file(os.path.join(abs_path, "js", script), mimetype="application/json")


@app.route("/css/<style>")
def get_some_css(style):
    abs_path = os.path.join(os.path.dirname(os.path.abspath(__file__)), settings.STATIC_FOLDER)
    return send_file(os.path.join(abs_path, "css", style), mimetype="text/css")

@app.route("/html/<html>")
def get_some_html(html):
    abs_path = os.path.join(os.path.dirname(os.path.abspath(__file__)), settings.STATIC_FOLDER)
    return send_file(os.path.join(abs_path, "html", html), mimetype="text/html")

@app.route("/js/<dir>/<file>")
def get_nested_js(dir, file):
    img_path = os.path.join(settings.STATIC_FOLDER, "js")
    folder_path = os.path.join(img_path, dir)
    abs_path = os.path.join(os.path.dirname(os.path.abspath(__file__)), folder_path)
    filename = os.path.join(abs_path, file)
    return send_file(filename, mimetype='application/json')


@app.route('/images/<folder>')
def get_image_list(folder):
    img_path = os.path.join(settings.STATIC_FOLDER, settings.IMG_FOLDER)
    folder_path = os.path.join(img_path, folder)
    abs_path = os.path.join(os.path.dirname(os.path.abspath(__file__)), folder_path)
    filelist = [file for file in os.listdir(abs_path) if file.endswith(".png") or file.endswith(".jpg")]
    return json.dumps(["images/"+folder+"/"+file for file in filelist])

@app.route("/images/<folder>/<file>")
def get_image(folder, file):
    img_path = os.path.join(settings.STATIC_FOLDER, settings.IMG_FOLDER)
    folder_path = os.path.join(img_path, folder)
    abs_path = os.path.join(os.path.dirname(os.path.abspath(__file__)), folder_path)
    filename = os.path.join(abs_path, file)
    return send_file(filename, mimetype='image/gif')

# --- WEB CONTENT END ---

if __name__ == '__main__':
    tb.start()
    app.run(threaded=True, host='0.0.0.0')
