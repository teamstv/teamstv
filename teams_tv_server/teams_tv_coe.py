import json

from flask import Flask, render_template
import os

from lib import calendar, misc
import settings

app = Flask(__name__)

COE_PHOTOS = os.path.join('static', 'coe_photos')

@app.route('/traffic')
def show_traffic():
    return render_template("traffic.html")


@app.route('/image')
def show_image():
    full_filename = os.path.join(COE_PHOTOS, 'Untitled.png')
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


if __name__ == '__main__':
    app.run()
