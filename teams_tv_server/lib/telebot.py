import sys
import logging
from threading import Thread
from queue import Queue, Empty
import telepot
from telepot.loop import MessageLoop

TOKEN = '463871639:AAEMtFe-8bxU2qEob3AvFKNYJUj7bBL52k4'

class Client(object):
    def __init__(self):
        self._qu = Queue()

    def notify(self, msg):
        self._qu.put(msg)

    def wait(self):
        return self._qu.get()


class Telebot(object):
    def _start(self, msg):
        result = ["Hey, %s!" % msg["from"].get("first_name"),
                "\rI can accept only these commands:"]
        for command in self.CMDs:
            result.append(command)
        response = "\n\t".join(result)
        self.bot.sendMessage(msg['chat']['id'], response)

    def _help(self, msg):
        result = ["Hey, %s!" % msg["from"].get("first_name"),
                "\rI can accept only these commands:"]
        for command in self.CMDs:
            result.append(command)
        response = "\n\t".join(result)
        self.bot.sendMessage(msg['chat']['id'], response)

    def _next(self, msg):
        pass

    def _resume(self, msg):
        pass

    def __init__(self):
        self.CMDs = {
            '/start': self._start,
            '/help':  self._help,
            '/next':  self._next,
            '/resume': self._resume,
        }
        self.clients = Queue()

    def start(self):
        self.bot = telepot.Bot(TOKEN)
        MessageLoop(self.bot, self.handle).run_as_thread()
        print('Listening ...')

    def get(self):
        req = Client()
        self.clients.put(req)
        return req.wait()

    def notify(self, msg):
        try:
            while True:
                req = self.clients.get(False)
                req.notify(msg)
        except Empty:
            pass

    def handle(self, msg):
        self.CMDs[msg['text']](msg)


