import psutil
import random
import threading

class DisplayCPU(threading.Thread):

    def run(self):

        self.running = True

        currentProcess = psutil.Process()

        while self.running:
            print(currentProcess.cpu_percent(interval=1))
            print(currentProcess.memory_info()[0])

    def stop(self):
        self.running = False

# ----

def i_hate_this():
    tab = []
    for i in range(1000000):
        tab.append(random.randint(1, 10000))
    tab.sort()
    return tab

# ---

display_cpu = DisplayCPU()

display_cpu.start()
try:
    result = i_hate_this()
finally: # stop thread even when I press Ctrl+C
    display_cpu.stop()