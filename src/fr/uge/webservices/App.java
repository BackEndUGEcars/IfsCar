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
	private long employeeId = -1;
	private final List<Long> myBasket;
	public App() throws MalformedURLException, RemoteException, NotBoundException {
		employees = (IEmployeeDataBase) Naming.lookup("rmi://localhost:7778/EmployeeDataBase");
		cars = (ICarDataBase) Naming.lookup("rmi://localhost:1099/CarDataBase");
		notifs = (INotifications) Naming.lookup("rmi://localhost:7780/Notifications");
		myBasket = new ArrayList<Long>();
	}
	
	public boolean connect(String login, String password) throws RemoteException {
		employeeId = employees.getIDofLogin(login, password);
		if(employeeId != -1) return true;
		return false;
	}
	
	public boolean addToCart(long carId) throws RemoteException {
		if(myBasket.contains(carId)) return false;
		myBasket.add(carId);
		return true;
	}
	
	public boolean removeFromCart(long carId) throws RemoteException {
		if(!myBasket.contains(carId)) return false;
		myBasket.remove(myBasket.indexOf(carId));
		return true;
	}
	
	public boolean isInCart(long carId) {
		if(!myBasket.contains(carId)) return false;
		return true;
	}
	
	public String basketToJSON() throws RemoteException {
		StringJoiner sj = new StringJoiner(", ");
		for (Long id : myBasket) {
			ICar c = cars.getCar(id);
			sj.add(c.toJson(id));
		}
		return "{" +
        "    \"cars\": [" +
        sj.toString() +
        "]}";
	}
	
	public String getAllCars() throws RemoteException {
		return cars.toJson();
	}
	
	public int rent(long carId) throws RemoteException {
		if(employeeId == -1) return -1;
		if(cars.getCar(carId) == null) return -1;
		if(cars.getCar(carId).isRented() == employeeId) return -1;
		employees.getEmployee(employeeId).addRent(carId);
		removeFromCart(carId);
		if(!cars.rent(carId, employeeId)) {
			notifs.addNotification(employeeId, carId, cars.getCar(carId).getImagePath(), "you are is queue for " + cars.getCar(carId).getModel());
			return 0;
		}
		notifs.addNotification(employeeId, carId, cars.getCar(carId).getImagePath(), "you are now renting " + cars.getCar(carId).getModel());
		return 1;
	}
	
	public int unrent(long carId, float note, float cleanlinessNote) throws RemoteException {
		if(employeeId == -1) return 0;
		ICar c = cars.getCar(carId);
		if(c == null) return 0;
		if(c.isRented() != employeeId) return 0;
		c.addNoteCar(note);
		c.addNoteCleanliness(cleanlinessNote);
		cars.unrent(carId);
		employees.getEmployee(employeeId).removeRent(carId);
		Long new_employee_id = cars.getCar(carId).isRented();
		if(new_employee_id != -1) notifs.addNotification(employeeId, carId, cars.getCar(carId).getImagePath(),"you are now renting " + cars.getCar(carId).getModel());
		return 1;
	}
	
	public boolean isCurrentlyRented(Long carId) throws RemoteException {
		if(employeeId == -1) return false;
		IEmployee e = employees.getEmployee(employeeId);
		List<Long> l = e.getCarRented();
		if(l == null) return false;
		for (Long c : l) {
			if(c == carId) return true;
		}
		return false;
	}
	
	public String getMyCars() throws RemoteException {
		if(employeeId == -1) return "{    \"cars\":[]}";
		List<Long> carRented = null;
		try {
			carRented = employees.getEmployee(employeeId).getCarRented();
			StringJoiner sj = new StringJoiner(", ");
			for (Long l : carRented) {
				sj.add(cars.getCar(l).toJson(l));
			}
			return "{" +
	        "    \"car\": [" +
	        sj.toString() +
	        "]}";
		} catch (Exception e) {
			return "{    \"cars\":[]}";
		}
	}
	
	public String getQueue(long carId) throws RemoteException {
		if(employeeId == -1) return "{    \"employees\":[]}";
		Queue<Long> q = cars.getCar(carId).getRentQueue();
		StringJoiner sj = new StringJoiner(", ");
		for (Long l : q) {
			sj.add(employees.getEmployee(l).toJson(l));
		}
		return "{" +
        "    \"employees\": [" +
        sj.toString() +
        "]}";
	}
	
	public boolean removeNotification(Long carId) throws RemoteException {
		if(employeeId == -1) return false;
		if(cars.getCar(carId) == null) return false;
		notifs.removeNotifications(employeeId, carId);
		return true;
	}
	
	public String getNotifications() throws RemoteException{
		if(employeeId == -1) {
			return "{    \"notifications\":[]}";
		}
		List<String> n = null;
		try {
			n = notifs.getNotifications(employeeId);
			if(n.size() == 0) {
				return "{    \"notifications\":[]}";
			}
			StringJoiner sj = new StringJoiner(", ");
			for (String s : n) {
				sj.add(s);
			}
			return "{" +
	        "    \"notifications\": [" +
	        sj.toString() +
	        "]}";
		} catch (Exception e) {
			return "{    \"notifications\":[]}";
		}
	}
}
