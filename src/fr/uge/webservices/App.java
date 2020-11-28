package fr.uge.webservices;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Queue;

public class App {
	private final IEmployeeDataBase employees;
	private final ICarDataBase cars;
	private final INotifications notifs;
	public App() throws MalformedURLException, RemoteException, NotBoundException {
		employees = (IEmployeeDataBase) Naming.lookup("rmi://localhost:7778/EmployeeDataBase");
		cars = (ICarDataBase) Naming.lookup("rmi://localhost:1099/CarDataBase");
		notifs = (INotifications) Naming.lookup("rmi://localhost:7780/Notifications");
	}
	
	public String getAllCars() throws RemoteException {
		return cars.toJson();
	}
	
	public boolean rent(String login, String password, long car_id) throws RemoteException {
		Long employee_id = employees.getIDofLogin(login, password);
		if(cars.getCar(car_id).isRented() == employee_id) return false;
		employees.getEmployee(employee_id).addRent(car_id);
		return cars.rent(car_id, employee_id);
	}
	
	public boolean unrent(String login, String password, long car_id, float note, float cleanlinessNote) throws RemoteException {
		Long employee_id = employees.getIDofLogin(login, password);
		ICar c = cars.getCar(car_id);
		if(c.isRented() != employee_id) return false;
		c.addNoteCar(note);
		c.addNoteCleanliness(cleanlinessNote);
		cars.unrent(car_id);
		employees.getEmployee(employee_id).removeRent(car_id);
		Long new_employee_id = cars.getCar(car_id).isRented();
		if(new_employee_id != -1) notifs.addNotification(employee_id, "you are now renting " + cars.getCar(car_id).getModel());
		return true;
	}
	
	public String getEmployeeCars(String login, String password) throws RemoteException {
		Long employee_id = employees.getIDofLogin(login, password);
		List<Long> carRented = employees.getEmployee(employee_id).getCarRented();
		StringBuilder sb = new StringBuilder();
		for (Long l : carRented) {
			sb.append(cars.getCar(l).toJson(l) + "\n");
		}
		return sb.toString();
	}
	
	public String getQueue(long car_id) throws RemoteException {
		Queue<Long> q = cars.getCar(car_id).getRentQueue();
		StringBuilder sb = new StringBuilder();
		for (Long l : q) {
			sb.append(employees.getEmployee(l).toJson(l) + "\n");
		}
		return sb.toString();
	}
	
	public void addNotification(String login, String password, String notif)throws RemoteException {
		Long employee_id = employees.getIDofLogin(login, password);
		notifs.addNotification(employee_id, notif);
	}
	
	public String getNotifications(String login, String password) throws RemoteException{
		Long employee_id = employees.getIDofLogin(login, password);
		List<String> n = notifs.getNotifications(employee_id);
		if(n == null) return "";
		StringBuilder sb = new StringBuilder();
		for (String s : n) {
			sb.append(s + "\n");
		}
		return sb.toString();
	}
}
