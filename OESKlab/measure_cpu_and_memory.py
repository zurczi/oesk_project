import psutil
import random
import threading

class DisplayCPUAndMemory(threading.Thread):
    measurement = 0

    def __init__(self, measurement):
        super(DisplayCPUAndMemory, self).__init__()
        self.measurement = measurement

    def run(self):

        self.running = True

        current_process = psutil.Process()

        while self.running:
            self.measurement.append_to_cpu(current_process.cpu_percent(interval=1)/psutil.cpu_count())
            self.measurement.append_to_memory(current_process.memory_info()[0])
            print(current_process.cpu_percent(interval=1)/psutil.cpu_count())
            print(current_process.memory_info()[0])

    def stop(self):
        self.running = False



def i_hate_this():
    tab = []
    for i in range(1000000):
        tab.append(random.randint(1, 10000))
    tab.sort()
    return tab

# ---


#display_cpu = DisplayCPUAndMemory()

#display_cpu.start()
#try:
#    result = i_hate_this()
#finally:
#    display_cpu.stop()

