package fr.uge.webservices;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface INotifications extends Remote {
	void addNotification(Long employeeID, Long carID, String notif) throws RemoteException;
	void removeNotifications(Long employeeId, Long carId) throws RemoteException;
	List<String> getNotifications(Long id) throws RemoteException;
}
