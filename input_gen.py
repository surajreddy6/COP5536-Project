import requests
import random

url = "http://svnweb.freebsd.org/csrg/share/dict/words?view=co&content-type=text/plain"

response = requests.get(url)
words = response.content.splitlines()
sub_list = words[:2000] + words[5000:7000] + words[10000:12000] + words[15000:17000] + words[20000:22000]

f = open('input.txt', 'w')

size = 100000

for i in range(0, size):
    if (i+1)%1000 == 0:
        f.write(str(random.randint(1, 20)) + '\n')
    else:
        freq = random.randint(10, 1000)
        keyword = sub_list[random.randint(0, len(sub_list) - 1)].decode('utf-8')
        f.write('$' + keyword + ' ' + str(freq) + '\n')
f.write('stop')
f.close()
