#!/bin/bash

PID=$(lsof -ti:1488)

if [ -z "$PID" ]; then
  echo "На порту нет активных процессов."
else
  sudo kill -9 $PID
  echo "Процесс с PID $PID, использующий порт, был успешно убит."
fi