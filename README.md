# criptografia-simetrica
Trabalho da matéria de Segurança da Informação sobre Criptografia Simétrica e Centro de Distribuição de Chaves (KDC). O objetivo era desenvolver um programa que implemente o centro de distribuição de chaves (KDC). O programa é composto de duas entidades (Alice e Bob) que desejam conversar utilizando criptografia simétrica.
- Bob e o KDC devem compartilhar uma chave mestre.
- Alice e o KDC devem compartilhar uma chave mestre.
- Bob e Alice devem conversar através de uma chave de sessão.
- A chave de sessão deve ser obtida através de uma comunicação criptografada com o KDC, utilizando a chave mestre.
- O KDC irá validar a solicitação e retornar duas chaves de sessão, uma cifrada na chave de Bob e outra na de Alice.
- Bob irá decifrar sua chave de sessão com chave mestre, armazenar e encaminhar a chave de sessão para Alice.
- Alice irá decifrar sua chave de sessão com chave mestre, armazenar e responder um 'nonce' para Bob.
- Bob irá decifrar o 'nonce' com chave de sessão, comparar executando a função combinada com Alice e encaminhar um novo 'nonce' cifrado.
- Alice irá decifrar o 'nonce' com chave de sessão, comparar executando a função combinada e finalmente validar a autenticação.
