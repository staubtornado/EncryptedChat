import threading
import socket


class KeyboardThread(threading.Thread):

    def __init__(self, input_cbk, s: socket):
        self.input_cbk = input_cbk
        self.s = s
        super(KeyboardThread, self).__init__(name='keyboard-input-thread')
        self.start()

    def run(self):
        while True:
            self.input_cbk(input(), self.s)  # waits to get input + Return
