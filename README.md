# sgc-backend

<b>Comando para startar o Docker no wsl</b>

 sudo dockerd

<b>Comando para subir o arquivo docker.compose</b>

Na pasta do arquivo docker.compose: 
sudo docker-compose up

<b>Comando para atualizar o container:</b>

sudo docker build -f Dockerfile -t <nome do .jar> .

Exemplo:
sudo docker build -f Dockerfile -t sgc_producer .

<b>Comando para alterar o endereço de IP do arquivo de configuração do docker</b>

Exemplo:
echo nameserver 8.8.8.8 | sudo tee /etc/resolv.conf

<b>Comando para listar os containeres com ID do Container</b>

sudo docker container ls -a

<b>Comando para commitar uma imagem no Docker Hub</b>

docker commit "{ID CONTAINER}" "{Usuario}"/"{repositório}":"{tag}"
 
docker commit 2084a2befb9a bpastorelli/sgc_consumer:1.0.0

<b>Comando para enviar para o repositório do Docker Hub</b>

docker push "{usuario}"/"{repositório}":"{tag}"

docker push bpastorelli/sgc_consumer:1.0.0
 
