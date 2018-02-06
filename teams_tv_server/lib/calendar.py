import json
from datetime import datetime
# Here we import local caldav so that we are not dependent on buggy version of the pip installation
import caldav
import pytz


def connect(user, password, url):
    """Connects to the caldav service provider with given credentials, returns list of available calendars
    :param user (str) : username
    :param password (str) : password
    :param url (str) : url without protocol/credentials, e.g. caldav.yandex.ru
    :return ([calendar]): list of available calendars
    """
    full_url = "https://{0}:{1}@{2}".format(user, password, url)
    client = caldav.DAVClient(full_url)
    principal = client.principal()
    calendars = principal.calendars()
    return calendars


def get_events(calendars, time_from, time_to):
    """gets available events from all given calendars
    :param calendars([calendar]):list of available calendars, active
    :param time_from(datetime):time to search from
    :param time_to(datetime): time to search to
    :return ([{event}]): list of obtained events for given period of time
    """
    res = list()
    for calendar in calendars:
        search_result = calendar.date_search(time_from, time_to)
        for event in search_result:
            event.load()
            e = event.instance.vevent
            summary_value = e.summary.value
            summary = summary_value.split(':')[0] if ":" in summary_value else summary_value
            start = e.dtstart.value
            end = e.dtend.value
            try:
                contents = json.loads(e.description.value)
            except Exception as err:
                contents = {"blocks": [], "data": {}}
            blocks = contents["blocks"]
            data = contents["data"]
            uid = e.uid.value
            last_modified = e.last_modified.value
            res.append({
                "sdate": start,
                "edate": end,
                "blocks": blocks,
                "widget": summary,
                "data": data,
                "uid": uid[:-3],  # YANDEX GENERATES UID WITH .RU AND WE CAN'T REALLY use it as tag id in html...
                "last_modified": last_modified
            })
    return res


def get_next_events():
    raise NotImplementedError("This feature in not yet implemented properly.")


def get_now():
    return datetime.now(pytz.utc)


def get_current_events(calendars):
    """Returns
    :param calendars([calendar]):list of available calendars, active
    :return ([{event}]): list of obtained events for current moment
    """
    return get_events(calendars, get_now(), get_now())

