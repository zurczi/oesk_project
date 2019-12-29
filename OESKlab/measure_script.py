import psutil
import time
import os
from functools import wraps
import statistics


class Measurement:
    name = ""
    cpu = []
    memory = []
    time = []

    def __str__(self):
        return "name: {0} cpu: {1} memory: {2} time: {3} ".format(self.name, self.get_mean_cpu(), self.get_max_memory(), self.time)

    def __init__(self, name):
        self.name = name

    def append_to_memory(self, memory_data):
        self.memory.append(memory_data)

    def append_to_cpu(self, cpu_data):
        self.cpu.append(cpu_data)

    def get_mean_cpu(self):
       # for x in self.cpu:
        #    print(x, end=',')
        return statistics.mean(self.cpu)

    def get_max_memory(self):
        return max(self.memory)
   #stat.median , mode, stdev , variance




def measure():
    process = psutil.Process(os.getpid())
    print(process.cpu_percent())
    # gives an object with many fields
    psutil.virtual_memory()
    # you can convert that object to a dictionary
    print(dict(process.virtual_memory()._asdict()))


def fn_timer(function):
    @wraps(function)
    def function_timer(*args, **kwargs):
        t0 = time.time()
        result = function(*args, **kwargs)
        t1 = time.time()
        print("Total time running : %s seconds", str(t1-t0))
        return result
    return function_timer