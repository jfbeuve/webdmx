sudo pkill -f webdmx
sleep 1
sudo nohup java -jar webdmx.jar </dev/null >/dev/null 2>&1 &
