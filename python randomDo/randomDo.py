import sys
import re
import random

taskList = []

def add_task():
    title = raw_input('Please enter your task: ')
    priority = raw_input('Please enter the priority(high, medium, low): ')
    tuple = (title,priority)
    
    taskList.append(tuple)
    
    again = raw_input('Do you want to add another task(Y/N)? ')
    if again.upper() == 'Y':
      add_task()
    else:
      return 
      

def delete_task():
  taskToDelete = raw_input('Enter name of task to delete: ')
  for x in taskList:
    print x[0]
    if x[0].lower() == taskToDelete.lower():
      taskList.remove(x)
  return

def sortKey(x):
  return x[1]


def main():
  args = sys.argv[1:]
    
  while 1:
    cmd = raw_input('Enter your command: ')
    if cmd == 'add':
      add_task()
    elif cmd == 'delete':
      delete_task()
    elif cmd == 'view':
      i = 1
      random.shuffle(taskList)
      for x in taskList:
        print str(i) + ': ' + x[0] + '    ' + x[1]
        i += 1
    elif cmd == 'start':
      i = 1
      random.shuffle(taskList)
      for x in taskList:
        print str(i) +': ' + x[0]
        raw_input('Press Enter when you have completed this task')
      print 'Congradulations, you have completed all your tasks'
    elif cmd == 'export':
      i = 1
      for x in sorted(taskList,key=sortKey):
        output += str(i) + ': ' + x[0] + '    ' + x[1] + '\n'
        i += 1   
      f = open('tasks', 'w')
      f.write(output)
    elif cmd == 'exit':
      break

if __name__ == '__main__':
  main()