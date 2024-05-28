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

<b>Comando alternativo para buildar o container e criar uma tag</b>

docker build -t {usuario}/{repositorio}:{tag} .
 
=======
<b>Comando para atualizar o container:</b>

sudo docker build -f Dockerfile -t <nome do .jar> .

Exemplo:
sudo docker build -f Dockerfile -t sgc_producer .

<b>Comando para subir um arquivo docker-compose</b>

<diretorio do arquivo> sudo docker-compose up

<b>Comando para subir um arquivo docker-compose em segundo plano</b>

<diretorio do arquivo> sudo docker-compose up -d

<b>Comando para parar um arquivo docker-compose em segundo plano</b>

<diretorio do arquivo> sudo docker-compose stop

<b>Comando para visualizar os logs de um container em execução</b>

<diretorio do arquivo>  sudo docker logs <nome do container>

<b>Comando para alterar o endereço de IP do arquivo de configuração do docker</b>

Exemplo:
echo nameserver 8.8.8.8 | sudo tee /etc/resolv.conf

<b>Instalação do docker via WSL</b>

https://educoutinho.com.br/windows/instalando-docker-no-wsl/

<b>Comando para criar um novo Context no ECS AWS "--from-env": Pega as credenciais do profile AWS</b>

docker context create ecs <TIPO CONTEXTO, PARA INFORMAÇÕES DIGITE --help> <NOME DO CONTEXTO>

Exemplo:
docker context create ecs --from-env sgcecscontext

<b>Subir um docker compose para a AWS</b>

Após as configurações de Login no AWS via prompt comando (aws cli ou Git Bach):

<b>Comandos uteis Linux</b>

Implantar app no docker: 
docker compose up

Remover app no docker: 
docker compose down

Instalar o docker no Ubuntu:

yum-config-manager --enable rhui-REGION-rhel-server-extras

yum -y install docker 

systemctl start docker

systemctl enable docker

docker version

Instalar o Git no Ubuntu:

sudo yum update

sudo yum install git

git --version

Criado um diretorio no Ubuntu

mkdir {nome do diretorio}

Copiando um arquivo de um diretório para outro

cp {diretorio origem}/arquivo {diretorio destino}

Exemplo: cp sgc-backend/deploy/docker-compose.yml sgc

Editando um arquivo pelo Ububtu:

nano {arquivo}

Exemplo:

nano teste.txt

Visualizando logs do congainer

docker logs {ID DO CONTAINER} | head

Exemplo: docker logs e985bcc0ac27 | head

Listando os containers

docker ps -a

<b>Deploy no EC2 via linha de comando</b>

Criando uma configuração de Cluster:

ecs-cli configure --cluster {NOME DO CLUSTER} --default-launch-type {TIPO EC2 ou FARGATE} --region {REGIAO} --config-name {NOME DA CONFIGURAÇÃO} 

Exemplo:

ecs-cli configure --cluster sgc_deploy --default-launch-type EC2 --region sa-east-1 --config-name config_sgc_deploy   

Deploy:

ecs-cli compose --project-name {NOME DO SERVIÇO} --file {NOME DO ARQUIVO .YML} --debug service up  --deployment-max-percent 100 --deployment-min-healthy-percent 0 --region {REGIÃO} --ecs-profile {PERFIL} --cluster-config {NOME DA CONFIG} --timeout 10

Exemplo:

ecs-cli compose --project-name sgc-backend --file docker-compose.yml --debug service up  --deployment-max-percent 100 --deployment-min-healthy-percent 0 --region sa-east-1 --ecs-profile sgc --cluster-config config_sgc_deploy --timeout 10

URLs:

Swagger:

http://{hostname}:{porta do container sgc-backend}/swagger-ui.html

Kafdrop:

http://{hostname}:{porta do container do kafdrop}

