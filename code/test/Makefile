all:
	@echo 'Se voce nao possui o gradle instalado, execute o comando "make install" para instala-lo. Logo apos, execute "make build" para construir o projeto e "make run" para executar o jogo. Caso queira remover todas as instalacoes feitas, execute "make clean". Boa diversao!'

build:
	gradle build

run:
	gradle run

install:
	sudo apt-get install gradle -y

clean:
	sudo apt-get remove gradle -y && sudo apt-get autoremove -y
