import psutil
import time
import os
from functools import wraps


class Measurement:
    cpu = []
    memory = []
    time = []


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