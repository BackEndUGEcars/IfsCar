package fr.uge.webservices;

import org.json.simple.parser.ParseException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IEmployee extends Remote {
	
	/**
     * Get the employee first name
     * @return the employee first name
     */
	String getFirstName() throws RemoteException;

	/**
     * Get the employee last name
     * @return the employee last name
     */
	String getLastName() throws RemoteException;

	/**
     * Convert employee to JSON
     * @param id, the id of the employee
     * @return the JSOn representation of the employee
     */
	String toJson(Long id) throws RemoteException;

	/**
     * Get the login of the employee
     * @return the Employee object representation of the employee
     */
	String getLogin() throws RemoteException;

	/**
     * Check if the given password is correct 
     * @param password, the employee password
     * @return true if the password is correct, false either
     */
	boolean isPasswordCorrect(String password) throws RemoteException;

	/**
     * Add a rent to an the employee
     * @param idCar, the id of the car
     * @return true if the car can be rented, false either
     */
	boolean addRent(Long idCar) throws RemoteException;

	 /**
     * Remove the rent from the employee
     * @param carId, the id of the car
     * @return true if the car can be removed, false either
     */
	boolean removeRent(Long carId) throws RemoteException;

	/**
     * Get all the car rented by the employee
     * @return List<Long> which represents all cars rented by the employee
     */
	List<Long> getCarRented() throws RemoteException;

}
