import socket
from InputThread import KeyboardThread
from typing import Optional
from datetime import datetime


class ChatClient:
    HOST: str
    PORT: int

    def __init__(self, host: str = "127.0.0.1", port: int = 80):
        self.HOST = host
        self.PORT = port

    def _run_and_connect(self):
        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
            try:
                s.connect((self.HOST, self.PORT))
            except ConnectionRefusedError as e:
                print(e)
                return

            print(f"Successfully connected to: {self.HOST} at {self.PORT}")

            username: str = input("Enter your username:")
            u_id = username + str(datetime.now())[21:26]
            print(f"Username : {username} / UserID: {u_id}")
            s.sendall(f"username={username}/uID={u_id}\n".encode())

            input_thread = KeyboardThread(send)

            data: Optional[bytes] = None
            while True:
                data: bytes = s.recv(1024)
                print(data.decode())

    def _encrypt(self):
        return

    def _decrypt(self):
        return


def send(msg, s: socket):
    s.sendall(msg.encode())


c = ChatClient()
c._run_and_connect()
