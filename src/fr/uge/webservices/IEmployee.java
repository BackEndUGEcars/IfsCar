package fr.uge.webservices;

import org.json.simple.parser.ParseException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IEmployee extends Remote {
	String getFirstName() throws RemoteException;

	String getLastName() throws RemoteException;

	String toJson(Long id) throws RemoteException;

	String getLogin() throws RemoteException;

	boolean isPasswordCorrect(String password) throws RemoteException;

	boolean addRent(Long idCar) throws RemoteException;

	boolean removeRent(Long carId) throws RemoteException;

	List<Long> getCarRented() throws RemoteException;

}
