package fr.uge.webservices;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface INotifications extends Remote {
	
	/**
     * Add notifications to an employee
     * @param employeeID, the employee id
     * @param carID, the car id
     * @param imagePath, the image path
     * @param notif, the notification
     */
	void addNotification(Long employeeID, Long carID, String imagePath, String notif) throws RemoteException;
	
	/**
     * Remove notification to an employee
     * @param employeeId, the employee id
     * @param carId, the car id
     */
	void removeNotifications(Long employeeId, Long carId) throws RemoteException;
	
	/**
	 * Get notifications of an employee
	 * @param id, the employee id
	 * @return List<String> the notifications list of an employee 
	 */
	List<String> getNotifications(Long id) throws RemoteException;
}
