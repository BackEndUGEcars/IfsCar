package fr.uge.webservices;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface ICarDataBase extends Remote {
	
	/*
     * Get all cars in database
     * @param id, the id of the car
     * @return ICar, the ICar from the given id
     * @throws RemoteException
     */
	ICar getCar(Long id) throws RemoteException;

	/*
     * Remove the car from the database
     * @param id, the id of the car
     * @return boolean, true if the car can be removed, false either
     * @throws RemoteException
     */
	boolean removeCar(Long id) throws RemoteException;

	/*
     * Add a car to the database
     * @param t, the car to add
     * @return boolean, true if the car can be added, false either
     * @throws RemoteException
     */
	boolean addCar(ICar t) throws RemoteException;

	/*
     * Get all cars which can be buy
     * @return Map<Long, ICar>, map with id of a car as key and his ICar object as value
     * @throws RemoteException
     */
	Map<Long, ICar> getBuyableCar() throws RemoteException;

	/*
     * Get the JSON representation of the database
     * @return String, the JSON representation of the database
     * @throws RemoteException
     */
	String toJson() throws RemoteException;

	/*
     * Get all cars in database
     * @return Map<Long, ICar>, the map with the car ID as key, and the ICar object as value
     * @throws RemoteException
     */
	Map<Long, ICar> getAllCars() throws RemoteException;

	/*
     * Initialize the database
     * @throws RemoteException
     */
	void init() throws IOException, ParseException, RemoteException;

	/*
     * Rent a car
     * @param carId, the id of the car
     * @param employeeId, the id of the employee which want to rent
     * @return boolean, true if this car can be rent by this employee, false either
     * @throws RemoteException
     */
	boolean rent(Long carId, long employeeId) throws RemoteException;

	/*
     * Unrent a car
     * @param id, the id of the car
     * @return long, the id of the new employee which rent, -1 of there is no one
     * @throws RemoteException
     */
	long unrent(Long id) throws RemoteException;

	/*
     * Get all cars which can be buy by a JSON representation
     * @return String, the JSON representation of all buyables cars
     * @throws RemoteException
     */
	String getBuyableCarsJson() throws RemoteException;

	/*
     * Get the JSON representation of a car
     * @param id, the id of the car
     * @return String, the JSON representation of the car
     * @throws RemoteException
     */
	String getCarJson(long id) throws RemoteException;

	/*
     * Get the price of a car
     * @param id, the id of the car
     * @return float, the price of the car
     * @throws RemoteException
     */
	float getPriceOfCar(long id) throws RemoteException;

}
