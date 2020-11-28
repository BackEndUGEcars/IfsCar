package fr.uge.webservices;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface INotifications extends Remote {
	void addNotification(Long id, String notif) throws RemoteException;
	List<String> getNotifications(Long id) throws RemoteException;
}
