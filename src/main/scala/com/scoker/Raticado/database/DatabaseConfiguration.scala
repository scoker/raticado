package com.scoker.Raticado.database

import java.util.Properties
import java.sql.DriverManager
import org.squeryl.adapters.PostgreSqlAdapter
import org.squeryl.{Session, SessionFactory}

class DatabaseConfiguration {
  Class.forName("org.postgresql.Driver");



  //TODO co z tymi properties?
  SessionFactory.concreteFactory = Some( () => Session.create(DriverManager.getConnection("...", new Properties), new PostgreSqlAdapter))
}