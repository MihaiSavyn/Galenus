package ro.sci.gms.dao.db;
//package ro.sci.gms.dao.db;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.sql.Timestamp;
//import java.util.Collection;
//import java.util.Date;
//import java.util.LinkedList;
//import java.util.List;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import ro.sci.gms.dao.AppointmentDAO;
//import ro.sci.gms.domain.Appointment;
//import ro.sci.gms.domain.Gender;
//import ro.sci.gms.domain.User;
//
///**
// * Pure JDBC implementation for {@link EmployeeDAO}.
// * 
// * @author sebi
// *
// */
//public class JDBCAppointmentDao implements AppointmentDAO {
//	private static final Logger LOGGER = LoggerFactory.getLogger(JDBCAppointmentDao.class);
//
//	private String host;
//	private String port;
//	private String dbName;
//	private String userName;
//	private String pass;
//
//	public JDBCAppointmentDao(String host, String port, String dbName, String userName, String pass) {
//		this.host = host;
//		this.userName = userName;
//		this.pass = pass;
//		this.port = port;
//		this.dbName = dbName;
//	}
//
//	@Override
//	public Collection<Appointment> getAll() {
//		Connection connection = newConnection();
//
//		Collection<Appointment> result = new LinkedList<>();
//
//		try (ResultSet rs = connection.createStatement().executeQuery("select * from employee")) {
//
//			while (rs.next()) {
//				result.add(extractAppointment(rs));
//			}
//			connection.commit();
//		} catch (SQLException ex) {
//
//			throw new RuntimeException("Error getting employees.", ex);
//		} finally {
//			try {
//				connection.close();
//			} catch (Exception ex) {
//
//			}
//		}
//
//		return result;
//	}
//
//	@Override
//	public Appointment findById(Long id) {
//		Connection connection = newConnection();
//
//		List<Appointment> result = new LinkedList<>();
//
//		try (ResultSet rs = connection.createStatement().executeQuery("select * from employee where id = " + id)) {
//
//			while (rs.next()) {
//				result.add(extractAppointment(rs));
//			}
//			connection.commit();
//		} catch (SQLException ex) {
//
//			throw new RuntimeException("Error getting employees.", ex);
//		} finally {
//			try {
//				connection.close();
//			} catch (Exception ex) {
//
//			}
//		}
//
//		if (result.size() > 1) {
//			throw new IllegalStateException("Multiple Employees for id: " + id);
//		}
//		return result.isEmpty() ? null : result.get(0);
//	}
//
//	@Override
//	public Appointment update(Appointment model) {
//		Connection connection = newConnection();
//		try {
//			PreparedStatement ps = null;
//			if (model.getId() > 0) {
//				ps = connection.prepareStatement(
//						"update employee set first_name=?, last_name=?, job_title=?, birth_date=?, employment_date=?, gender = ? "
//								+ "where id = ? returning id");
//
//			} else {
//
//				ps = connection.prepareStatement(
//						"insert into employee (first_name, last_name, job_title, birth_date, employment_date, gender) "
//								+ "values (?, ?, ?, ?, ?, ?) returning id");
//
//			}
////			ps.setString(1, model.getFirstName());
////			ps.setString(2, model.getLastName());
////			ps.setString(3, model.getJobTitle());
////			ps.setTimestamp(4, new Timestamp(model.getBirthDate().getTime()));
////			ps.setTimestamp(5, new Timestamp(model.getEmploymentDate().getTime()));
////			ps.setString(6, model.getGender().name());
//
//			if (model.getId() > 0) {
//				ps.setLong(7, model.getId());
//			}
//
//			ResultSet rs = ps.executeQuery();
//			if (rs.next()) {
//				model.setId(rs.getLong(1));
//			}
//			rs.close();
//
//			connection.commit();
//
//		} catch (SQLException ex) {
//
//			throw new RuntimeException("Error getting employees.", ex);
//		} finally {
//			try {
//				connection.close();
//			} catch (Exception ex) {
//
//			}
//		}
//
//		return model;
//	}
//
//	@Override
//	public boolean delete(Appointment model) {
//		boolean result = false;
//		Connection connection = newConnection();
//		try {
//			Statement statement = connection.createStatement();
//			result = statement.execute("delete from employee where id = " + model.getId());
//			connection.commit();
//		} catch (SQLException ex) {
//
//			throw new RuntimeException("Error updating employees.", ex);
//		} finally {
//			try {
//				connection.close();
//			} catch (Exception ex) {
//
//			}
//		}
//		return result;
//
//	}
//
////	@Override
////	public Collection<Appointment> searchByName(String query) {
////		if (query == null) {
////			query = "";
////		} else {
////			query = query.trim();
////		}
////
////		Connection connection = newConnection();
////
////		Collection<Employee> result = new LinkedList<>();
////
////		try (ResultSet rs = connection.createStatement()
////				.executeQuery("select * from employee where lower(first_name || ' ' || last_name) like '%"
////						+ query.toLowerCase() + "%'")) {
////
////			while (rs.next()) {
////				result.add(extractAppointment(rs));
////			}
////			connection.commit();
////		} catch (SQLException ex) {
////
////			throw new RuntimeException("Error getting employees.", ex);
////		}
////
////		return result;
////	}
//
//	protected Connection newConnection() {
//		try {
//			Class.forName("org.postgresql.Driver").newInstance();
//
//			String url = new StringBuilder()//
//					.append("jdbc:")//
//					.append("postgresql")//
//					.append("://")//
//					.append(host)//
//					.append(":")//
//					.append(port)//
//					.append("/")//
//					.append(dbName)//
//					.append("?user=")//
//					.append(userName)//
//					.append("&password=")//
//					.append(pass).toString();
//			Connection result = DriverManager.getConnection(url);
//			result.setAutoCommit(false);
//
//			return result;
//		} catch (Exception ex) {
//			throw new RuntimeException("Error getting DB connection", ex);
//		}
//
//	}
//
//	private Appointment extractAppointment(ResultSet rs) throws SQLException {
//		Appointment appointment = new Appointment();
////		appointment.setId(rs.getLong("id"));
////		appointment.setFirstName(rs.getString("first_name"));
////		appointment.setLastName(rs.getString("last_name"));
////		appointment.setJobTitle(rs.getString("job_title"));
////		appointment.setBirthDate(new Date(rs.getTimestamp("birth_date").getTime()));
////		appointment.setEmploymentDate(new Date(rs.getTimestamp("employment_date").getTime()));
////		appointment.setGender(Gender.valueOf(rs.getString("gender")));
//		return appointment;
//
//	}
//
//	@Override
//	public Collection<Appointment> searchById(String query) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Collection<Appointment> getAll(User user) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Collection<Appointment> search(String query) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//}
