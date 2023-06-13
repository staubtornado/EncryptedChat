import PySimpleGUI as Gui


class GuiInteractions:
    def __init__(self):
        self.name = None

    def show_gui(self) -> None:

        Gui.theme('DarkBlack')

        layout = [
            [Gui.Text("Enter Username:")],
            [Gui.Input("", key='-username-')],
            [Gui.Button('Login')]
        ]

        window = Gui.Window("Encrypted (not yet) Chat", layout)

        while True:
            event, values = window.read()

            if event == Gui.WIN_CLOSED or event == 'Close':
                break

            if event == 'Send':
                if not window['-msg-'].get() == "":
                    print("%SAY%")
                    window['-msg-'].update("")

            if event == 'Disconnect':
                print("%DISCONNECT%")
                break

            if event == 'Login':
                if not window['-username-'].get() == "":
                    self.name = window['-username-'].get()
                    layout = [
                        [Gui.Text("", key='-server-')],
                        [Gui.Input("", key='-msg-')],
                        [Gui.Button('Send'), Gui.Button('Disconnect')]
                    ]
                    window.close()
                    window = Gui.Window("Encrypted (not yet) Chat", layout)

        window.close()


GuiInteractions().show_gui()

