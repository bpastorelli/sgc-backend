INSERT INTO `residencia` (`id`, `endereco`, `numero`, `complemento`, `bairro`, `cep`, `cidade`, `uf`, `data_criacao`, `data_atualizacao`, `guide`)
VALUES (NULL,'Rua Antônio Cândido de Oliveira', 5, 'Casa 5','Chácara Tres Marias', '04475492', 'Sorocaba', 'SP', CURRENT_DATE(), CURRENT_DATE(), 'uwiqsalksalskasllsAdAa');

INSERT INTO `morador` (`id`,`nome`, `email`, `cpf`, `rg`, `senha`, `telefone`, `celular`, `perfil`, `data_atualizacao`, `data_criacao`, `posicao`, `associado`, `guide`)
VALUES (NULL, 'Bruno Dias Pastorelli', 'bruno@gmail.com', '35979769056', '342729952', '$2a$10$UftFkustVngaNHOsbNE8h.l/lNsQauk2IUMymJN.cUhQkaJtvnaR.', '1155600310', '11975778998', 'ROLE_ADMIN', CURRENT_DATE(), CURRENT_DATE(), 1, 1, 'djsaksaslsajsaksasjaskasjadasas');

INSERT INTO `vinculo_residencia` (`id`,`morador_id`, `residencia_id`, `data_vinculo`, `guide`)
VALUES (NULL, (SELECT `id` FROM `morador` WHERE `cpf`='35979769056'), (SELECT `id` FROM `residencia` WHERE `id`=1), CURRENT_DATE(), 'djaksakslasjahsakskk1qqowqqw');

INSERT INTO `modulo` (`id`,`descricao`, `path_modulo`, `posicao`) VALUES (NULL,'Administração', '', 1);

INSERT INTO `modulo` (`id`,`descricao`, `path_modulo`, `posicao`) VALUES (NULL,'Contribuições', '', 1);

INSERT INTO `modulo` (`id`,`descricao`, `path_modulo`, `posicao`) VALUES (NULL,'Moradores', '', 1);

INSERT INTO `modulo` (`id`,`descricao`, `path_modulo`, `posicao`) VALUES (NULL,'Residências', '', 1);

INSERT INTO `modulo` (`id`,`descricao`, `path_modulo`, `posicao`) VALUES (NULL,'Veículos', '', 1);

INSERT INTO `modulo` (`id`,`descricao`, `path_modulo`, `posicao`) VALUES (NULL,'Visitas', '', 1);

INSERT INTO `modulo` (`id`,`descricao`, `path_modulo`, `posicao`) VALUES (NULL,'Visitantes', '', 1);

INSERT INTO `funcionalidade` (`id`, `id_modulo`, `descricao`, `funcao`, `path_funcionalidade`, `posicao`) VALUES (NULL, (SELECT `id` FROM `modulo` WHERE `descricao`='Administração'), 'Atribuir Acessos', 'create', 'acessoModulo/create', 1);

INSERT INTO `funcionalidade` (`id`, `id_modulo`, `descricao`, `funcao`, `path_funcionalidade`, `posicao`) VALUES (NULL, (SELECT `id` FROM `modulo` WHERE `descricao`='Administração'), 'Acessos Funcionalidades', 'create', 'acessoFuncionalidade/create', 1);

INSERT INTO `funcionalidade` (`id`, `id_modulo`, `descricao`, `funcao`, `path_funcionalidade`, `posicao`) VALUES (NULL, (SELECT `id` FROM `modulo` WHERE `descricao`='Administração'), 'Consultar Módulos', 'view', 'modulos', 1);

INSERT INTO `funcionalidade` (`id`, `id_modulo`, `descricao`, `funcao`, `path_funcionalidade`, `posicao`) VALUES (NULL, (SELECT `id` FROM `modulo` WHERE `descricao`='Administração'), 'Consultar Funcionalidades', 'view', 'funcionalidades', 1);

INSERT INTO `funcionalidade` (`id`, `id_modulo`, `descricao`, `funcao`, `path_funcionalidade`, `posicao`) VALUES (NULL, (SELECT `id` FROM `modulo` WHERE `descricao`='Administração'), 'Cadastrar Módulo', 'create', 'modulo/create', 1);

INSERT INTO `funcionalidade` (`id`, `id_modulo`, `descricao`, `funcao`, `path_funcionalidade`, `posicao`) VALUES (NULL, (SELECT `id` FROM `modulo` WHERE `descricao`='Administração'), 'Cadastrar Funcionalidade', 'create','funcionalidade/create', 1);

INSERT INTO `funcionalidade` (`id`, `id_modulo`, `descricao`, `funcao`, `path_funcionalidade`, `posicao`) VALUES (NULL, (SELECT `id` FROM `modulo` WHERE `descricao`='Moradores'), 'Cadastrar Morador', 'create', 'morador/create', 1);

INSERT INTO `funcionalidade` (`id`, `id_modulo`, `descricao`, `funcao`, `path_funcionalidade`, `posicao`) VALUES (NULL, (SELECT `id` FROM `modulo` WHERE `descricao`='Moradores'), 'Consultar Moradores', 'view', 'moradores', 1);

INSERT INTO `funcionalidade` (`id`, `id_modulo`, `descricao`, `funcao`, `path_funcionalidade`, `posicao`) VALUES (NULL, (SELECT `id` FROM `modulo` WHERE `descricao`='Residências'), 'Cadastrar Residência', 'create', 'residencia/create', 1);

INSERT INTO `funcionalidade` (`id`, `id_modulo`, `descricao`, `funcao`, `path_funcionalidade`, `posicao`) VALUES (NULL, (SELECT `id` FROM `modulo` WHERE `descricao`='Residências'), 'Consultar Residências', 'view', 'residencias', 1);

INSERT INTO `funcionalidade` (`id`, `id_modulo`, `descricao`, `funcao`, `path_funcionalidade`, `posicao`) VALUES (NULL, (SELECT `id` FROM `modulo` WHERE `descricao`='Contribuições'), 'Importar Contribuições', 'create', 'importar', 1);

INSERT INTO `funcionalidade` (`id`, `id_modulo`, `descricao`, `funcao`, `path_funcionalidade`, `posicao`) VALUES (NULL, (SELECT `id` FROM `modulo` WHERE `descricao`='Contribuições'), 'Consultar Contribuições', 'view', 'contribuicoes', 1);

INSERT INTO `funcionalidade` (`id`, `id_modulo`, `descricao`, `funcao`, `path_funcionalidade`, `posicao`) VALUES (NULL, (SELECT `id` FROM `modulo` WHERE `descricao`='Contribuições'), 'Minhas Contribuições', 'view', 'minhascontribuicoes', 1);

INSERT INTO `funcionalidade` (`id`, `id_modulo`, `descricao`, `funcao`, `path_funcionalidade`, `posicao`) VALUES (NULL, (SELECT `id` FROM `modulo` WHERE `descricao`='Visitas'), 'Consultar Visitas', 'view', 'visitas', 1);

INSERT INTO `funcionalidade` (`id`, `id_modulo`, `descricao`, `funcao`, `path_funcionalidade`, `posicao`) VALUES (NULL, (SELECT `id` FROM `modulo` WHERE `descricao`='Veículos'), 'Cadastar Veículo', 'create', 'veiculo/create', 1);

INSERT INTO `funcionalidade` (`id`, `id_modulo`, `descricao`, `funcao`, `path_funcionalidade`, `posicao`) VALUES (NULL, (SELECT `id` FROM `modulo` WHERE `descricao`='Veículos'), 'Consultar Veículos', 'view', 'veiculos', 1);

INSERT INTO `funcionalidade` (`id`, `id_modulo`, `descricao`, `funcao`, `path_funcionalidade`, `posicao`) VALUES (NULL, (SELECT `id` FROM `modulo` WHERE `descricao`='Visitantes'), 'Cadastar Visitante', 'create', 'visitante/create', 1);

INSERT INTO `funcionalidade` (`id`, `id_modulo`, `descricao`, `funcao`, `path_funcionalidade`, `posicao`) VALUES (NULL, (SELECT `id` FROM `modulo` WHERE `descricao`='Visitantes'), 'Consultar Visitantes', 'view', 'visitantes', 1);

INSERT INTO `acesso_modulo` (`id`, `id_usuario`,`id_modulo`, `acesso`, `data_cadastro`, `posicao`) VALUES (NULL, 1, 1, true, CURRENT_DATE(), 1);

INSERT INTO `acesso_modulo` (`id`, `id_usuario`,`id_modulo`, `acesso`, `data_cadastro`, `posicao`) VALUES (NULL, 1, 2, true, CURRENT_DATE(), 1);

INSERT INTO `acesso_modulo` (`id`, `id_usuario`,`id_modulo`, `acesso`, `data_cadastro`, `posicao`) VALUES (NULL, 1, 3, true, CURRENT_DATE(), 1);

INSERT INTO `acesso_modulo` (`id`, `id_usuario`,`id_modulo`, `acesso`, `data_cadastro`, `posicao`) VALUES (NULL, 1, 4, true, CURRENT_DATE(), 1);

INSERT INTO `acesso_modulo` (`id`, `id_usuario`,`id_modulo`, `acesso`, `data_cadastro`, `posicao`) VALUES (NULL, 1, 5, true, CURRENT_DATE(), 1);

INSERT INTO `acesso_modulo` (`id`, `id_usuario`,`id_modulo`, `acesso`, `data_cadastro`, `posicao`) VALUES (NULL, 1, 6, true, CURRENT_DATE(), 1);

INSERT INTO `acesso_funcionalidade` (`id`, `id_usuario`,`id_modulo`, `id_funcionalidade`, `acesso`, `inclusao`, `alteracao`, `exclusao`, `data_cadastro`, `posicao`) VALUES (NULL, 1, 1, 1, true, true, true, true, CURRENT_DATE(), 1);

INSERT INTO `acesso_funcionalidade` (`id`, `id_usuario`,`id_modulo`, `id_funcionalidade`, `acesso`, `inclusao`, `alteracao`, `exclusao`, `data_cadastro`, `posicao`) VALUES (NULL, 1, 1, 2, true, true, true, true, CURRENT_DATE(), 1);

INSERT INTO `acesso_funcionalidade` (`id`, `id_usuario`,`id_modulo`, `id_funcionalidade`, `acesso`, `inclusao`, `alteracao`, `exclusao`, `data_cadastro`, `posicao`) VALUES (NULL, 1, 1, 3, true, true, true, true, CURRENT_DATE(), 1);

INSERT INTO `acesso_funcionalidade` (`id`, `id_usuario`,`id_modulo`, `id_funcionalidade`, `acesso`, `inclusao`, `alteracao`, `exclusao`, `data_cadastro`, `posicao`) VALUES (NULL, 1, 1, 4, true, true, true, true, CURRENT_DATE(), 1);

INSERT INTO `acesso_funcionalidade` (`id`, `id_usuario`,`id_modulo`, `id_funcionalidade`, `acesso`, `inclusao`, `alteracao`, `exclusao`, `data_cadastro`, `posicao`) VALUES (NULL, 1, 1, 5, true, true, true, true, CURRENT_DATE(), 1);

INSERT INTO `acesso_funcionalidade` (`id`, `id_usuario`,`id_modulo`, `id_funcionalidade`, `acesso`, `inclusao`, `alteracao`, `exclusao`, `data_cadastro`, `posicao`) VALUES (NULL, 1, 1, 6, true, true, true, true, CURRENT_DATE(), 1);

INSERT INTO `acesso_funcionalidade` (`id`, `id_usuario`,`id_modulo`, `id_funcionalidade`, `acesso`, `inclusao`, `alteracao`, `exclusao`, `data_cadastro`, `posicao`) VALUES (NULL, 1, 3, 7, true, true, true, true, CURRENT_DATE(), 1);

INSERT INTO `acesso_funcionalidade` (`id`, `id_usuario`,`id_modulo`, `id_funcionalidade`, `acesso`, `inclusao`, `alteracao`, `exclusao`, `data_cadastro`, `posicao`) VALUES (NULL, 1, 3, 8, true, true, true, true, CURRENT_DATE(), 1);

INSERT INTO `acesso_funcionalidade` (`id`, `id_usuario`,`id_modulo`, `id_funcionalidade`, `acesso`, `inclusao`, `alteracao`, `exclusao`, `data_cadastro`, `posicao`) VALUES (NULL, 1, 2, 9, true, true, true, true, CURRENT_DATE(), 1);

INSERT INTO `acesso_funcionalidade` (`id`, `id_usuario`,`id_modulo`, `id_funcionalidade`, `acesso`, `inclusao`, `alteracao`, `exclusao`, `data_cadastro`, `posicao`) VALUES (NULL, 1, 2, 10, true, true, true, true, CURRENT_DATE(), 1);