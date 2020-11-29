package fr.uge.webservices;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.Map;

public interface IEmployeeDataBase extends Remote{
	
	 /**
     * Get an employee form the database
     * @param id, the employee id
     * @return an IEmployee object representation of the employee
     */
	IEmployee getEmployee(Long id) throws RemoteException;
	
	/**
     * Remove an employee form the database
     * @param id, the employee id
     * @return true if employee can be removed, false either
     */
    boolean removeEmployee(Long id) throws RemoteException;
    
    /**
     * Add an employee form the database
     * @param t, the employee 
     * @return true if employee can be added, false either
     */
    boolean addEmployee(IEmployee t) throws RemoteException;
    
    /**
     * Get the employee map
     * @return map representation if the employee data base with id as key 
     * and an IEmployee Object as value
     */
    Map<Long, IEmployee> getEmployeeMap() throws RemoteException;
    
    /**
     * Initialize the database
     */
    void init() throws IOException, ParseException, RemoteException, org.json.simple.parser.ParseException;
    
    /**
     * Convert the database to a JSON representation
     * @return String the JSON database representation
     */
    String toJson() throws RemoteException;
    
    /**
     * Get the id of the specified login/passord
     * @return long the id of the employee which has this login/password
     */
    long getIDofLogin(String login, String password) throws RemoteException;
}
