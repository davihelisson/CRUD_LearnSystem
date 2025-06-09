/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import factory.ConnectionFactory;
import java.sql.Connection;

/**
 *
 * @author Fatec
 */
public class CursoDAO {
    private Connection connection;
    
    public CursoDAO(){
        this.connection = new ConnectionFactory().getConnection();
    }
    
    // Continuar a partir daqui
    
    
}
