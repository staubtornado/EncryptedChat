import socket

from GUI import GuiInteractions
from typing import Optional
from datetime import datetime
from InputThread import KeyboardThread


def _receive(s: socket) -> None:
    while True:
        data: bytes = s.recv(1024)
        print(data.decode())


class ChatClient:
    HOST: str
    PORT: int

    def __init__(self, host: str = "127.0.0.1", port: int = 80) -> None:
        self.HOST = host
        self.PORT = port
        gui = GuiInteractions()
        gui.show_gui()

    def _run_and_connect(self) -> None:
        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
            try:
                s.connect((self.HOST, self.PORT))
            except ConnectionRefusedError as e:
                print(e)
                return

            print(f"Successfully connected to: {self.HOST} at {self.PORT}")

            username: str = input("Enter your username:")
            s.sendall(f"%AUTH% {username} {username + str(datetime.now())[21:26]}\n".encode())

            KeyboardThread(send, s)

            data: Optional[bytes] = None
            _receive(s)


def send(msg, s: socket) -> None:
    s.sendall(f"%SAY% {msg}".encode())


c = ChatClient()
c._run_and_connect()
