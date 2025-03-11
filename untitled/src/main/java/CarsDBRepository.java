import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CarsDBRepository implements CarRepository{

    private JdbcUtils dbUtils;



    private static final Logger logger= LogManager.getLogger();

    public CarsDBRepository(Properties props) {
        logger.info("Initializing CarsDBRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }

    @Override
    public List<Car> findByManufacturer(String manufacturerN) {
        logger.traceEntry();
        Connection conn = dbUtils.getConnection();
        List<Car> cars = new ArrayList<>();
        try (PreparedStatement preStmt = conn.prepareStatement("select * from cars where manufacturer=?")) {
            preStmt.setString(1, manufacturerN);
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt(1);
                    String manufacturer = result.getString(2);
                    String model = result.getString(3);
                    int year = result.getInt(4);
                    Car car = new Car(manufacturer, model, year);
                    car.setId(id);
                    cars.add(car);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error inserting car" + e);
        }
        logger.traceExit();
        return cars;
    }

    @Override
    public List<Car> findBetweenYears(int min, int max) {
        logger.traceEntry();
        Connection conn = dbUtils.getConnection();
        List<Car> cars = new ArrayList<>();
        try (PreparedStatement preStmt = conn.prepareStatement("select * from cars where year<? and year>?")) {
            preStmt.setInt(1, min);
            preStmt.setInt(2, max);
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt(1);
                    String manufacturer = result.getString(2);
                    String model = result.getString(3);
                    int year = result.getInt(4);
                    Car car = new Car(manufacturer, model, year);
                    car.setId(id);
                    cars.add(car);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error inserting car" + e);
        }
        logger.traceExit();
        return cars;
    }

    @Override
    public void add(Car elem) {
        logger.info("Adding car: {}", elem);
        Connection conn=dbUtils.getConnection();
        try(PreparedStatement preStmt= conn.prepareStatement("insert into cars(manufacturer,model,year) values (?,?,?)")) {
            preStmt.setString(1, elem.getManufacturer());
            preStmt.setString(2, elem.getModel());
            preStmt.setInt(3, elem.getYear());
            int result=preStmt.executeUpdate();
            logger.trace("Saved car: {}", result);
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error inserting car"+e);
        }
        logger.traceExit();
    }

    @Override
    public void update(Integer integer, Car elem) {
        int id= ((Car)((List<?>) findAll()).get(integer)).getId();
        logger.info("Updating car with id {}: {}", id, elem);
        Connection conn = dbUtils.getConnection();

        try (PreparedStatement preStmt = conn.prepareStatement(
                "UPDATE cars SET manufacturer=?, model=?, year=? WHERE id=?")) {
            preStmt.setString(1, elem.getManufacturer());
            preStmt.setString(2, elem.getModel());
            preStmt.setInt(3, elem.getYear());
            preStmt.setInt(4, id);

            int result = preStmt.executeUpdate();
            if (result > 0) {
                logger.trace("Updated car with id {}", id);
            } else {
                logger.warn("No car found with id {}", id);
            }
        } catch (SQLException e) {
            logger.error("Error updating car", e);
            System.err.println("Error updating car: " + e);
        }
        logger.traceExit();
    }

    @Override
    public Iterable<Car> findAll() {
        logger.traceEntry();
        Connection conn = dbUtils.getConnection();
        List<Car> cars = new ArrayList<>();
        try (PreparedStatement preStmt = conn.prepareStatement("select * from cars")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt(1);
                    String manufacturer = result.getString(2);
                    String model = result.getString(3);
                    int year = result.getInt(4);
                    Car car = new Car(manufacturer, model, year);
                    car.setId(id);
                    cars.add(car);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error inserting car" + e);
        }
        logger.traceExit();
        return cars;
    }


    }

