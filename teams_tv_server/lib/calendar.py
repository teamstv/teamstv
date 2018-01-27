import json
from datetime import datetime, timedelta
from settings import TIME_SHIFT
import caldav


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
            summary = e.summary.value
            if ':' in summary:
                summary = summary.split(':')[0]
            start = e.dtstart.value
            end = e.dtend.value
            try:
                contents = json.loads(e.description.value)
            except Exception:
                contents = ""
            blocks = contents["blocks"]
            data = contents["data"]
            uid = e.uid.value
            last_modified = e.last_modified.value
            res.append({
                "sdate": start,
                "edate": end,
                "blocks": blocks,
                "widget": summary.split(':')[0],
                "data": data,
                "uid": uid[:-3], # YANDEX GENERATES UID WITH .RU AND WE CAN'T REALLY use it as tag id in html...
                "last_modified": last_modified
            })
    print(res)
    return res

def get_now():
    return datetime.now() + timedelta(**TIME_SHIFT)

def get_current_events(calendars):
    """Returns
    :param calendars([calendar]):list of available calendars, active
    :return ([{event}]): list of obtained events for current moment
    """
    return get_events(calendars, get_now(), get_now())

events_times = None

def get_next_event_time(calendars):
    global events_times
    if not events_times:
        events_times = get_events_times(calendars)
    return events_times.pop(0)

def get_next_events(calendars):
    time = get_next_event_time(calendars)
    if time:
        return get_events(calendars, time, time)

def get_events_times(calendars):
    events = get_events(calendars, get_now().date(), get_now().date() + timedelta(days=1))
    events = list({event['sdate'] for event in events})
    return sorted(events)

