<b>Comando para atualizar o container:</b>

sudo docker build -f Dockerfile -t <nome do .jar> .

Exemplo:
sudo docker build -f Dockerfile -t sgc_producer .

<b>Comando para subir um arquivo docker-compose</b>

<diretorio do arquivo> sudo docker-compose up

<b>Comando para alterar o endereço de IP do arquivo de configuração do docker</b>

Exemplo:
echo nameserver 8.8.8.8 | sudo tee /etc/resolv.conf

<b>Instalação do docker via WSL</>

https://educoutinho.com.br/windows/instalando-docker-no-wsl/
