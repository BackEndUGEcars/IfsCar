package fr.uge.webservices;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.StringJoiner;

public class App {
	private final IEmployeeDataBase employees;
	private final ICarDataBase cars;
	private final INotifications notifs;
	private long employee_id = -1;
	private final List<Long> myBasket;
	public App() throws MalformedURLException, RemoteException, NotBoundException {
		employees = (IEmployeeDataBase) Naming.lookup("rmi://localhost:7778/EmployeeDataBase");
		cars = (ICarDataBase) Naming.lookup("rmi://localhost:1099/CarDataBase");
		notifs = (INotifications) Naming.lookup("rmi://localhost:7780/Notifications");
		myBasket = new ArrayList<Long>();
	}
	
	public boolean connect(String login, String password) throws RemoteException {
		employee_id = employees.getIDofLogin(login, password);
		if(employee_id != -1) return true;
		return false;
	}
	
	public boolean addToCart(long car_id) throws RemoteException {
		if(myBasket.contains(car_id)) return false;
		myBasket.add(car_id);
		return true;
	}
	
	public boolean removeFromCart(long car_id) throws RemoteException {
		if(!myBasket.contains(car_id)) return false;
		myBasket.remove(myBasket.indexOf(car_id));
		return true;
	}
	
	public String basketToJSON() throws RemoteException {
		StringJoiner sj = new StringJoiner(", ");
		for (Long id : myBasket) {
			ICar c = cars.getCar(id);
			sj.add(c.toJson(id));
		}
		return "{" +
        "    'cars': [" +
        sj.toString() +
        "]}";
	}
	
	public String getAllCars() throws RemoteException {
		return cars.toJson();
	}
	
	public boolean rent(long car_id) throws RemoteException {
		if(employee_id == -1) return false;
		if(cars.getCar(car_id).isRented() == employee_id) return false;
		employees.getEmployee(employee_id).addRent(car_id);
		return cars.rent(car_id, employee_id);
	}
	
	public boolean unrent(long car_id, float note, float cleanlinessNote) throws RemoteException {
		if(employee_id == -1) return false;
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
	
	public String getMyCars() throws RemoteException {
		if(employee_id == -1) return "{    'cars':[]}";
		List<Long> carRented = employees.getEmployee(employee_id).getCarRented();
		StringJoiner sj = new StringJoiner(", ");
		for (Long l : carRented) {
			sj.add(cars.getCar(l).toJson(l));
		}
		return "{" +
        "    'cars': [" +
        sj.toString() +
        "]}";
	}
	
	public String getQueue(long car_id) throws RemoteException {
		if(employee_id == -1) return "{    'employees':[]}";
		Queue<Long> q = cars.getCar(car_id).getRentQueue();
		StringJoiner sj = new StringJoiner(", ");
		for (Long l : q) {
			sj.add(employees.getEmployee(l).toJson(l));
		}
		return "{" +
        "    'employees': [" +
        sj.toString() +
        "]}";
	}
	
	public String getNotifications() throws RemoteException{
		if(employee_id == -1) return "{    'notifications':[]}";
		List<String> n = notifs.getNotifications(employee_id);
		if(n == null) return "{    'notifications':[]}";
		StringJoiner sj = new StringJoiner(", ");
		for (String s : n) {
			sj.add(s);
		}
		return "{" +
        "    'notifications': [" +
        sj.toString() +
        "]}";
	}
}
