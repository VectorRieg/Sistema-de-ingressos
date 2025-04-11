package com.br.cinema_package.cinema_project;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil 
{
	    private static final SessionFactory sessionFactory;

	    static 
	    {
	        try 
	        {
	        	//Carrega o hibernate.cfg.xml e monta a SessionFactory
	        	sessionFactory = new Configuration().configure().buildSessionFactory();
	        } catch (Throwable ex) //Pega qualquer exceção
	        
	        {
	            System.err.println("Erro na criação do SessionFactory." + ex);
	            //KYS
	            throw new ExceptionInInitializerError(ex);
	        }
	    }

	    public static SessionFactory getSessionFactory() 
	    {
	        return sessionFactory;
	    }
	}

