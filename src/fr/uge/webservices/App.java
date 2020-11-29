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

/**
 * Allow to manage Rent car app
 *
 */
public class App {
	private final IEmployeeDataBase employees;
	private final ICarDataBase cars;
	private final INotifications notifs;
	private long employeeId = -1;
	private final List<Long> myBasket;
	
	/**
	 * App constructor
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	public App() throws MalformedURLException, RemoteException, NotBoundException {
		employees = (IEmployeeDataBase) Naming.lookup("rmi://localhost:7778/EmployeeDataBase");
		cars = (ICarDataBase) Naming.lookup("rmi://localhost:1099/CarDataBase");
		notifs = (INotifications) Naming.lookup("rmi://localhost:7780/Notifications");
		myBasket = new ArrayList<Long>();
	}

	/**
	 * Allow to connect to the App
	 * @param login the login
	 * @param password the password
	 * @return true if the connection is established, false either
	 * @throws RemoteException
	 */
	public boolean connect(String login, String password) throws RemoteException {
		employeeId = employees.getIDofLogin(login, password);
		if(employeeId != -1) return true;
		return false;
	}
	
	/**
	 * Check if the user is connected
	 * @return true if the user is connected, false either
	 */
	public boolean myConnection() {
		if(employeeId == -1) return false;
		return true;
	}
	
	/**
	 * Add a car to the cart
	 * @param carId the car id
	 * @return true if the car can be added, false either
	 * @throws RemoteException
	 */
	public boolean addToCart(long carId) throws RemoteException {
		if(myBasket.contains(carId)) return false;
		myBasket.add(carId);
		return true;
	}
	
	/**
	 * Remove a car from the cart
	 * @param carId the car id
	 * @return true if the car can be removed, false either
	 * @throws RemoteException
	 */
	public boolean removeFromCart(long carId) throws RemoteException {
		if(!myBasket.contains(carId)) return false;
		myBasket.remove(myBasket.indexOf(carId));
		return true;
	}
	
	/**
	 * Check if the car is in the cart
	 * @param carId the car id
	 * @return true if the car is in the cart, false eiter
	 * @throws RemoteException
	 */
	public boolean isInCart(long carId) {
		if(!myBasket.contains(carId)) return false;
		return true;
	}
	
	/**
	 * Convert the basket to a JSON
	 * @return JSON representation of the basket
	 * @throws RemoteException
	 */
	public String basketToJSON() throws RemoteException {
		StringJoiner sj = new StringJoiner(", ");
		for (Long id : myBasket) {
			ICar c = cars.getCar(id);
			if(c!=null) {
				sj.add(c.toJson(id));
			}
		}
		return "{" +
        "    \"cars\": [" +
        sj.toString() +
        "]}";
	}
	
	/**
	 * Get JSON representation of all cars
	 * @return JSON representation of all cars
	 * @throws RemoteException
	 */
	public String getAllCars() throws RemoteException {
		return cars.toJson();
	}
	
	/**
	 * Rent the a car
	 * @param carId the car to rent
	 * @return 0 if the car cant be rent, 1 either
	 * @throws RemoteException
	 */
	public int rent(long carId) throws RemoteException {
		if(employeeId == -1) return -1;
		if(cars.getCar(carId) == null) return -1;
		if(cars.getCar(carId).isRented() == employeeId) return -1;
		removeFromCart(carId);
		if(!cars.rent(carId, employeeId)) {
			notifs.addNotification(employeeId, carId, cars.getCar(carId).getImagePath(), "you are in queue for " + cars.getCar(carId).getModel());
			return 0;
		}
		employees.getEmployee(employeeId).addRent(carId);
		notifs.addNotification(employeeId, carId, cars.getCar(carId).getImagePath(), "you are now renting " + cars.getCar(carId).getModel());
		return 1;
	}
	
	/**
	 * Unrent the a car
	 * @param carId the car to rent
	 * @param note the note
	 * @param cleanlinessNote the cleanliness note
	 * @return 0 if the car cant be unrent, 1 either
	 * @throws RemoteException
	 */
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
		if(new_employee_id != -1) {
			notifs.addNotification(new_employee_id, carId, cars.getCar(carId).getImagePath(),"you are now renting " + cars.getCar(carId).getModel());
			employees.getEmployee(new_employee_id).addRent(carId);
		}
		notifs.addNotification(employeeId, carId, cars.getCar(carId).getImagePath(),"you unrent " + cars.getCar(carId).getModel());
		return 1;
	}
	
	/**
	 * Check if the given car is currently rented
	 * @param carId the car id
	 * @return true if the car is currently rented, false either
	 * @throws RemoteException
	 */
	public boolean isCurrentlyRented(Long carId) throws RemoteException {
		if(employeeId == -1) return false;
		IEmployee e = employees.getEmployee(employeeId);
		List<Long> l = e.getCarRented();
		if(l == null) return false;
		for (Long c : l) {
			if(c.equals(carId)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Get all cars rented by employee
	 * @return JSON representation of cars rented by employee
	 * @throws RemoteException
	 */
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
	
	/**
	 * Remove a notification
	 * @param carId the car id of the notification
	 * @return true if the notification can be removed, false either
	 * @throws RemoteException
	 */
	public boolean removeNotification(Long carId) throws RemoteException {
		if(employeeId == -1) return false;
		if(cars.getCar(carId) == null) return false;
		notifs.removeNotifications(employeeId, carId);
		return true;
	}
	
	/**
	 * Get notifications of the employee
	 * @return JSON representation of notifications
	 * @throws RemoteException
	 */
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
