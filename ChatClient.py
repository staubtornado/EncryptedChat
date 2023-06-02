import socket
from InputThread import KeyboardThread
from typing import Optional
from datetime import datetime


class ChatClient:
    HOST: str
    PORT: int

    def __init__(self, host: str = "127.0.0.1", port: int = 80) -> None:
        self.HOST = host
        self.PORT = port

    def _run_and_connect(self) -> None:
        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
            try:
                s.connect((self.HOST, self.PORT))
            except ConnectionRefusedError as e:
                print(e)
                return

            print(f"Successfully connected to: {self.HOST} at {self.PORT}")

            username: str = input("Enter your username:")
            s.sendall(f"username={username}/uID={username + str(datetime.now())[21:26]}\n".encode())

            KeyboardThread(send, s)

            data: Optional[bytes] = None
            while True:
                data: bytes = s.recv(1024)
                print(data.decode())

    def _encrypt(self):
        return

    def _decrypt(self):
        return


def send(msg, s: socket) -> None:
    s.sendall(msg.encode())


c = ChatClient()
c._run_and_connect()
