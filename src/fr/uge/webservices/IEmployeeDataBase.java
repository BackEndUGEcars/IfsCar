package fr.uge.webservices;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.Map;

public interface IEmployeeDataBase extends Remote{
	IEmployee getEmployee(Long id) throws RemoteException;
    boolean removeEmployee(Long id) throws RemoteException;
    boolean addEmployee(IEmployee t) throws RemoteException;
    Map<Long, IEmployee> getEmployeeMap() throws RemoteException;
    void init() throws IOException, ParseException, RemoteException, org.json.simple.parser.ParseException;
    String toJson() throws RemoteException;
    long getIDofLogin(String login, String password) throws RemoteException;
}
