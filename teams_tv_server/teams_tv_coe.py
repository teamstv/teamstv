import json

from flask import Flask, render_template

from lib import calendar, misc
import settings

app = Flask(__name__)


@app.route('/traffic')
def show_index():
    return render_template("index.html")


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
