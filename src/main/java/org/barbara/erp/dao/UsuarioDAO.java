package org.barbara.erp.dao;

import org.barbara.erp.model.Usuario;

public interface UsuarioDAO {
    Usuario autenticar(String usuario, String senha);
}