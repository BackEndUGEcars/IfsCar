package fr.uge.webservices;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Queue;

public interface ICar extends Remote {
	
	/*
     * Add a cleanliness note to the car
     * @param note, the cleanliness note
     * @throws RemoteException
     */
	float addNoteCleanliness(float note) throws RemoteException;

	/*
     * Add a note to the car
     * @param note, the note
     * @throws RemoteException
     */
	float addNoteCar(float note) throws RemoteException;

	/*
     * Get the car note
     * @throws RemoteException
     */
	float getNoteCar() throws RemoteException;

	/*
     * Get the car cleanliness note
     * @throws RemoteException
     */
	float getNoteCarCleanliness() throws RemoteException;

	/*
     * Rent the car
     * @param id, the location id
     * @return boolean, true if the car can be rented, false either
     * @throws RemoteException
     */
	boolean rent(long id) throws RemoteException;

	/*
     * Unrent the car
     * @return long, -1 if there is no other location, either, the id of the new location
     * @throws RemoteException
     */
	long unrent() throws RemoteException;

	/*
     * Get the rent price
     * @return float, the rent price
     * @throws RemoteException
     */
	float getRentPrice() throws RemoteException;

	/*
     * Get the sell price
     * @return float, the sell price
     * @throws RemoteException
     */
	float getSellPrice() throws RemoteException;

	/*
     * Check if the car is sellable
     * @return boolean, true if the car is sellable, false either
     * @throws RemoteException
     */
	boolean isSellable() throws RemoteException;

	/*
     * Check if the car is rented
     * @return long, the id of the actual location
     * @throws RemoteException
     */
	long isRented() throws RemoteException;

	/*
     * Get the car model
     * @return String, the car model
     * @throws RemoteException
     */
    public 
	String getModel() throws RemoteException;

    /*
     * Get the image path
     * @return String, the image path
     * @throws RemoteException
     */
	String getImagePath() throws RemoteException;

	/*
     * Get the JSON representation of the car
     * @param id, the car id
     * @return String, the car JSON representation
     * @throws RemoteException
     */
	String toJson(Long id) throws RemoteException;

    /*
     * Get the rent queue
     * @return Queue<Long>, the rent queue
     * @throws RemoteException
     */
	Queue<Long> getRentQueue() throws RemoteException;

	/*
     * Add an employee to the location queue
     * @param idEmployee, the id of the employee
     * @return boolean, true if the employee can be added, false either
     * @throws RemoteException
     */
	boolean addEmployeeQueue(Long idEmployee) throws RemoteException;

	/*
     * Remove an employee from the location queue
     * @return long, true if the employee can be added, false either
     * @throws RemoteException
     */
	long removeEmployeeQueue() throws RemoteException;

}
