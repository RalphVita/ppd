# ppd
Processamento Paralelo e Distribuído

docker-compose up
docker-compose exec ppd bash

docker-compose exec ppd bash -c 'cd /home/ppd/helloworld_rmi; exec bash'
docker-compose exec ppd bash -c 'cd /home/ppd/simplechatlab; exec bash'
docker-compose exec ppd bash -c 'cd /home/ppd/advancedchat; exec bash'

xterm -e bash -c 'cd workspace;ls; exec bash'
