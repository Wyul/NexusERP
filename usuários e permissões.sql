-- 1. Criação de usuários
CREATE USER administrador WITH PASSWORD 'senha_admin';
CREATE USER suporte WITH PASSWORD 'senha_suporte';
CREATE USER mecanica WITH PASSWORD 'senha_mecanica';
CREATE USER usuario WITH PASSWORD 'senha_usuario';

-- 2. Criação de roles (grupos)
CREATE ROLE dba;
CREATE ROLE consulta;
CREATE ROLE mecanica_role;

-- 3. Concessão de permissões

-- A. DBA: Acesso total (superusuário) para Administrador e Suporte
GRANT dba TO administrador;
GRANT dba TO suporte;
ALTER ROLE administrador WITH SUPERUSER;
ALTER ROLE suporte WITH SUPERUSER;

-- B. Consulta: Permissão de consulta (somente leitura) para Mecânica e Usuário
GRANT consulta TO mecanica;
GRANT consulta TO usuario;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO consulta;

-- C. Mecânica: Permissões de manipulação de dados (INSERT, UPDATE, DELETE)
GRANT INSERT, UPDATE, DELETE ON agendamento TO mecanica;
GRANT INSERT, UPDATE, DELETE ON venda_nfe TO mecanica;
GRANT INSERT, UPDATE, DELETE ON estoque TO mecanica;
GRANT SELECT ON permissao, usuario, fornecedor, cliente TO mecanica;

-- D. Restrição de criação e alteração de estrutura
GRANT CREATE ON DATABASE nome_do_banco TO dba; -- Somente para DBA

-- 4. Finalizar: Revogar permissões de administração de dados para grupos de consulta
REVOKE ALL ON SCHEMA public FROM consulta;
REVOKE ALL ON SCHEMA public FROM mecanica;
